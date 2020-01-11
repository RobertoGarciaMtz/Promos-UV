package com.example.ofertasuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.onNoteListener {

    int touch=0;
    int eleccion=0;
    private int LOCAT = 30;
    String nombre_dia = "";
    List<Promociones_Restaurantes> promos;
    DatabaseReference mRootReference;
    private RecyclerViewAdapter recyclerViewAdapter;
    AutoCompleteTextView filtro;
    String titulo = "OfertasUV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecibirPeticionDia();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        asignarTitulo(toolbar);
        mRootReference = FirebaseDatabase.getInstance().getReference();
        crearPromos();
        RecyclerView recyclerView = findViewById(R.id.recycler_rest);
        recyclerViewAdapter = new RecyclerViewAdapter(this,promos,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void asignarTitulo(Toolbar toolbar) {
        toolbar.setTitle(titulo);
    }

    private void NotificarCambios() {
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void RecibirPeticionDia() {
        Bundle extras = getIntent().getExtras();
        if (extras==null){
            return;
        }
        eleccion = extras.getInt("seleccion");
        switch (eleccion){
            case 2:
                nombre_dia = "Lunes";
                break;
            case 3:
                nombre_dia = "Martes";
                break;
            case 4:
                nombre_dia = "Miercoles";
                break;
            case 5:
                nombre_dia = "Jueves";
                break;
            case 6:
                nombre_dia = "Viernes";
                break;
            case 7:
                nombre_dia = "Sabado";
                break;
            case 1:
                nombre_dia = "Domingo";
                break;
                default:
                    nombre_dia = "";
                    break;
        }
        titulo = titulo +"-"+nombre_dia;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.solicitar){
            finish();
            Intent intent = new Intent(this, MapaCoordenadasRestaurante.class);
            intent.putExtra("Usuario", 2);
            startActivity(intent);
        }else{
            if(id == R.id.buscar){
                if (touch == 0){
                    String [] dias = getResources().getStringArray(R.array.dias);
                    filtro = findViewById(R.id.buscador);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dias);
                    filtro.setAdapter(adapter);
                    filtro.setVisibility(View.VISIBLE);
                    touch = 1;
                }else{
                    String name = filtro.getText().toString();
                    switch (name){
                        case "Lunes":
                            RecargarActivity(2);
                            MostrarToast("Lunes");
                            break;
                        case "Martes":
                            RecargarActivity(3);
                            MostrarToast("Martes");
                            break;
                        case "Miercoles":
                            RecargarActivity(4);
                            MostrarToast("Miercoles");
                            break;
                        case "Jueves":
                            RecargarActivity(5);
                            MostrarToast("Jueves");
                            break;
                        case "Viernes":
                            RecargarActivity(6);
                            MostrarToast("Viernes");
                            break;
                        case "Sabado":
                            RecargarActivity(7);
                            MostrarToast("Sabado");
                            break;
                        case "Domingo":
                            RecargarActivity(1);
                            MostrarToast("Domingo");
                            break;
                        default:
                            MostrarToast("No selecciono");
                             break;
                    }
                    filtro.setVisibility(View.INVISIBLE);
                    touch = 0;
                }
            }
        }
        return true;
    }

    private void crearPromos() {
        promos = new ArrayList<>();
        mRootReference.child("Aprobado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    String key = snap.getKey();
                    Promociones_Restaurantes promo = snap.getValue(Promociones_Restaurantes.class);
                    promo.setKey(key);
                    if(promo.getDia().compareTo(nombre_dia)==0){
                        promos.add(promo);
                    }
                }
                NotificarCambios();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        finish();
        Intent intent = new Intent(this,VistaCompleta_Promocion.class);
        intent.putExtra("Promocion",promos.get(position).getPromocion());
        intent.putExtra("Restaurante",promos.get(position).getNombre_restaurante());
        intent.putExtra("horario",promos.get(position).getHorario());
        intent.putExtra("dia",promos.get(position).getDia());
        intent.putExtra("key",promos.get(position).getKey());
        intent.putExtra("direccion",promos.get(position).getDireccion());
        intent.putExtra("telefono",promos.get(position).getTelefono());
        intent.putExtra("imagen",promos.get(position).getImagen());
        intent.putExtra("imagenNet",promos.get(position).getImagenNet());
        intent.putExtra("posx",promos.get(position).getPosX());
        intent.putExtra("posy",promos.get(position).getPosY());
        intent.putExtra("Usuario",2);
        intent.putExtra("fragment",1);
        startActivity(intent);
    }

    public void MostrarToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }

    public void RecargarActivity(int dia){
        finish();
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("seleccion",dia);
        startActivity(i);
    }
}
