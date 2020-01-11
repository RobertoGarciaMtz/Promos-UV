package com.example.ofertasuv;

import android.app.ProgressDialog;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Base_de_datos {
    private DatabaseReference mRootReference;
    private StorageReference mStorage;
    private String img;

    public Base_de_datos(){
        this.mRootReference = FirebaseDatabase.getInstance().getReference();
    }

    public void EscribirDatos(String reference, Promociones_Restaurantes promo){
        Map<String,Object> listMap = new HashMap<>();
        listMap.put("promocion",promo.getPromocion());
        listMap.put("nombre_restaurante",promo.getNombre_restaurante());
        listMap.put("horario",promo.getHorario());
        listMap.put("dia",promo.getDia());
        listMap.put("direccion",promo.getDireccion());
        listMap.put("imagenNet",promo.getImagenNet());
        listMap.put("telefono",promo.getTelefono());
        listMap.put("imagen",promo.getImagen());
        listMap.put("imagenNet",promo.getImagenNet());
        listMap.put("posX",promo.getPosX());
        listMap.put("posY",promo.getPosY());
        mRootReference.child(reference).push().setValue(listMap);
    }

    public void EliminarDatos(String hijo, Promociones_Restaurantes promo){
        mRootReference.child(hijo).child(promo.getKey()).removeValue();
    }

    public void SubirImagen(Uri dato, final ProgressDialog progressDialog){
        progressDialog.show();
        mStorage = FirebaseStorage.getInstance().getReference().child("Fotos");
        final StorageReference filepath = mStorage.child("Image"+dato.getLastPathSegment());
        filepath.putFile(dato).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        img = String.valueOf(uri);
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    public String getImg(){
        return img;
    }
}
