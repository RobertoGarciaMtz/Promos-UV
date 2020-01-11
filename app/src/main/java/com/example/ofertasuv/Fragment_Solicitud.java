package com.example.ofertasuv;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Solicitud extends Fragment implements RecyclerViewAdapter.onNoteListener{
    View v;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Promociones_Restaurantes> promos;
    DatabaseReference mRootReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.call_fragment,container,false);
        recyclerView = v.findViewById(R.id.RecyclerAdmin);
        mRootReference = FirebaseDatabase.getInstance().getReference();
        crearPromos();
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),promos,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    private void crearPromos() {
        promos = new ArrayList<>();
        mRootReference.child("Solicitud").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    String key = snap.getKey();
                    Promociones_Restaurantes promo = snap.getValue(Promociones_Restaurantes.class);
                    promo.setKey(key);
                    promos.add(promo);
                }
                NotificarCambios();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void NotificarCambios() {
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {
        getActivity().finish();
        Intent intent = new Intent(getActivity(),VistaCompleta_Promocion.class);
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
        intent.putExtra("Usuario",1);
        intent.putExtra("fragment",2);
        startActivity(intent);
    }
}
