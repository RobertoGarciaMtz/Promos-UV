package com.example.ofertasuv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class VistaCompleta_Promocion extends AppCompatActivity {
    Promociones_Restaurantes promos;
    TextView promo,res,horario,dia,direccion,telefono;
    int tipoUser;
    int frag;
    Button btnAprobar;
    Switch eliminar;
    ImageView image;
    SensorManager sensorManager;
    Sensor sensor;
    int movimientos=0;
    SensorEventListener sensorEventListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_informacion_platillo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.back:
                RegresarMain();
                break;
            case R.id.UbicarMapa:
                String direccion = "google.navigation:q="+promos.getPosY()+","+promos.getPosX() +"";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(direccion));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_completa__promocion);
        ObtenerDatos();
        if(tipoUser == 1){
            if(frag == 2){
                btnAprobar = findViewById(R.id.BtnAprobar);
                btnAprobar.setVisibility(View.VISIBLE);
                btnAprobar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CrearAlerta("¿Esta seguro que desea aprobar este registro?",3,"Aprobar registro");
                    }
                });
            }
            eliminar = findViewById(R.id.Eliminar);
            eliminar.setVisibility(View.VISIBLE);
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CrearAlerta("¿Esta seguro que desea eliminar este registro?",frag,"Eliminar registro");
                }
            });
        }
        Toolbar toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);
        promo = findViewById(R.id.promocioncompleta);
        res = findViewById(R.id.Restaurantecompleta);
        horario = findViewById(R.id.horariocompleta);
        dia = findViewById(R.id.diacompleta );
        direccion = findViewById(R.id.direccioncompleta);
        telefono = findViewById(R.id.telefonocompleta);
        image = findViewById(R.id.imagencompleta);
        promo.setText(promos.getPromocion());
        res.setText(promos.getNombre_restaurante());
        horario.setText(promos.getHorario());
        dia.setText(promos.getDia());
        direccion.setText(promos.getDireccion());
        telefono.setText(promos.getTelefono());
        Picasso.with(this)
                .load(promos.getImagenNet())
                .fit()
                .centerCrop()
                .into(image);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor==null){
            Toast.makeText(this,"No cuenta con el sensor",Toast.LENGTH_LONG).show();
        }else{
            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float x = sensorEvent.values[0];
                    if(x<-5 && movimientos==0){
                        movimientos++;
                    }
                    if(x>5 && movimientos==1){
                        movimientos++;
                    }
                    if(movimientos == 2){
                        if(tipoUser == 1){
                            CrearAlerta("¿Esta seguro que desea eliminar este registro?",frag,"Eliminar registro");
                        }else{
                            msgToast("Saludos, esperamos encuentres lo que busques");
                        }
                        movimientos = 0;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            start();
        }
    }

    private void start(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stop();
        super.onPause();
    }

    private void CrearAlerta(String msg, final int caso, String motivo) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (caso == 3){
                            AprobarRegistro();
                            EliminarRegistro("Solicitud");
                            dialogInterface.cancel();
                        }else{
                            String hijo = "";
                            if(caso == 1){
                                hijo = "Aprobado";
                            }else{
                                hijo = "Solicitud";
                            }
                            EliminarRegistro(hijo);
                            dialogInterface.cancel();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        if(caso!=3){
                            eliminar.setChecked(false);
                        }
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle(motivo);
        titulo.show();
    }

    private void EliminarRegistro(String caso) {
        Base_de_datos bd = new Base_de_datos();
        bd.EliminarDatos(caso,promos);
        RegresarMain();
    }

    private void AprobarRegistro() {
        Base_de_datos bd = new Base_de_datos();
        bd.EscribirDatos("Aprobado",promos);
        //RegresarMain();
    }

    private void RegresarMain() {
        finish();
        if(tipoUser == 1){
            Intent intent = new Intent(this,MainAdministrador.class);
            startActivity(intent);
        }else{
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_WEEK);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("seleccion",day);
            startActivity(intent);
        }
    }

    private void ObtenerDatos() {
        promos = new Promociones_Restaurantes();
        Bundle extras = getIntent().getExtras();
        promos.setPromocion(extras.getString("Promocion"));
        promos.setNombre_restaurante(extras.getString("Restaurante"));
        promos.setHorario(extras.getString("horario"));
        promos.setDia(extras.getString("dia"));
        promos.setKey(extras.getString("key"));
        promos.setDireccion(extras.getString("direccion"));
        promos.setTelefono(extras.getString("telefono"));
        promos.setImagen(extras.getInt("imagen"));
        promos.setImagenNet(extras.getString("imagenNet"));
        promos.setPosX(extras.getDouble("posx"));
        promos.setPosY(extras.getDouble("posy"));
        tipoUser = extras.getInt("Usuario");
        frag = extras.getInt("fragment");
    }

    private void msgToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
