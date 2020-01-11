package com.example.ofertasuv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button init,registrar;
    private EditText user,pass;
    private FirebaseAuth mAuth;
    Usuario usu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        init = findViewById(R.id.iniciarSesion);
        registrar = findViewById(R.id.registrar);
        user = findViewById(R.id.mail);
        pass = findViewById(R.id.password);
        init.setOnClickListener(this);
        registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        Intent intent;
        switch(i){
            case R.id.iniciarSesion:
                ObtenerDatos(1);
                break;
            case R.id.registrar:
                ObtenerDatos(2);
                break;
        }
    }

    private void ObtenerDatos(int proceso) {
        String nombre,contra;
        nombre = user.getText().toString();
        contra = pass.getText().toString();
        if(nombre.length()>0 && contra.length()>0){
            usu = new Usuario(nombre,contra);
            if(proceso == 1){
                ValidarDatos(usu);
            }else{
                CrearUsuario(usu);
                }
        }else{
            Toast("Verifique que los datos esten completos");
        }
    }

    private void CrearUsuario(Usuario usu) {
        mAuth.createUserWithEmailAndPassword(usu.getCorreo(),usu.getContrasena())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            IniciarSesion();
                        }else{
                            Toast("El usuario que intenta crear ya existe");
                        }
                    }
                });
    }

    private void IniciarSesion() {
        if(usu.getCorreo().equals("RobertoGarcia@hotmail.com")){
            Intent intent = new Intent(this,MainAdministrador.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_WEEK);
            intent.putExtra("seleccion",day);
            startActivity(intent);
        }


    }

    private void ValidarDatos(Usuario usu) {
        mAuth.signInWithEmailAndPassword(usu.getCorreo(),usu.getContrasena())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            IniciarSesion();
                        }else{
                            Toast("Verifique que los datos sean correctos");
                        }
                    }
                });
    }

    public void Toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
