package com.example.ofertasuv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Form_Solicitud_Local extends AppCompatActivity implements View.OnClickListener {

    private EditText promo,rest,hor,dia,direc,tel;
    private String pro,re,ho,di,dire,tele,imgString;
    private Uri uriPhoto;
    private Button addPhoto,openCamera;
    private int OPEN_STORAGE = 10;
    private ProgressDialog progres;
    private Base_de_datos bd;
    int photo_cod = 101;
    int user;
    double lat,longi;
    ImageView imagen;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    int movimientos =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form__solicitud__local);
        Toolbar toolbar = findViewById(R.id.toolbarForm);
        setSupportActionBar(toolbar);
        ObtenerDatos();
        progres = new ProgressDialog(this);
        progres.setMessage("Subiendo la imagen, aguarde");
        bd = new Base_de_datos();
        promo = findViewById(R.id.form_s_promo);
        rest = findViewById(R.id.form_s_rest);
        hor = findViewById(R.id.form_s_horario);
        dia = findViewById(R.id.form_s_dia);
        direc = findViewById(R.id.form_s_direccion);
        tel = findViewById(R.id.form_s_telefono);
        addPhoto = findViewById(R.id.buttonGallery);
        addPhoto.setOnClickListener(this);
        openCamera = findViewById(R.id.buttonCamara);
        openCamera.setOnClickListener(this);
        imagen = findViewById(R.id.imgForm);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor==null){
            MsgToast("No cuenta con el sensor acelerometro para poder utilizar todas las funciones");
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
                        GuardarRegistro();
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

    private void ObtenerDatos() {
        Bundle extras = getIntent().getExtras();
        user = extras.getInt("Usuario");
        lat = extras.getDouble("Latitude");
        longi = extras.getDouble("Longitude");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.volver:
                DecidirRetorno();
                break;
            case R.id.guardar:
                GuardarRegistro();
                break;
            default:
                break;
        }
        return true;
    }

    private void DecidirRetorno() {
        if(user ==1){
            VolverMainAdmin();
        }else{
            VolverMain();
        }
    }

    private void GuardarRegistro() {
        Promociones_Restaurantes pr = ValidarDatos();
        if (pr != null){
            pr.setPosX(longi);
            pr.setPosY(lat);
            Base_de_datos bd = new Base_de_datos();
            if(user==1){
                bd.EscribirDatos("Aprobado",pr);
            }else{
                bd.EscribirDatos("Solicitud",pr);
            }
            DecidirRetorno();
            MsgToast("Registro creado correctamente");
        }
    }

    private void VolverMainAdmin() {
        finish();
        Intent intent = new Intent(this,MainAdministrador.class);
        startActivity(intent);
    }

    private Promociones_Restaurantes ValidarDatos() {
        Promociones_Restaurantes nuevo = null;
        pro = promo.getText().toString();
        re = rest.getText().toString();
        ho = hor.getText().toString();
        di = dia.getText().toString();
        dire = direc.getText().toString();
        tele = tel.getText().toString();
        imgString = bd.getImg();
        if(pro.length()>0 && re.length()>0 && ho.length()>0 && di.length()>0 && dire.length()>0 && tele.length()>0 && imgString.length()>0){
            if(di.compareTo("Lunes")== 0 || di.compareTo("lunes")==0 || di.compareTo("Martes")==0 || di.compareTo("martes")==0 ||
                    di.compareTo("Miercoles")==0 || di.compareTo("miercoles")==0 || di.compareTo("Jueves")==0 || di.compareTo("jueves")==0 ||
            di.compareTo("Viernes")==0 || di.compareTo("viernes")==0 || di.compareTo("Sabado")==0 || di.compareTo("sabado")==0 ||
                    di.compareTo("Domingo")==0 || di.compareTo("domingo")==0){
                String diaCorrecto;
                switch(di){
                    case "lunes":
                        diaCorrecto = "Lunes";
                        break;
                    case "martes":
                        diaCorrecto = "Martes";
                        break;
                    case "miercoles":
                        diaCorrecto = "Miercoles";
                        break;
                    case "jueves":
                        diaCorrecto = "Jueves";
                        break;
                    case "viernes":
                        diaCorrecto = "Viernes";
                        break;
                    case "sabado":
                        diaCorrecto = "Sabado";
                        break;
                    case "domingo":
                        diaCorrecto = "Domingo";
                        break;
                        default:
                            diaCorrecto = di;
                            break;
                }
                nuevo = new Promociones_Restaurantes(pro,re,ho,diaCorrecto,"qwerty",dire,tele,R.drawable.maxresdefault);
                nuevo.setImagenNet(imgString);
            }else{
                MsgToast("Favor de insertar el dia bien");
            }
        }else{
            MsgToast("Favor de ingresar todos los datos");
        }
        return nuevo;
    }

    private void MsgToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    private void VolverMain() {
        finish();
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("seleccion",day);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.buttonGallery){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},OPEN_STORAGE);
            }else{
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,OPEN_STORAGE);
            }
        }
        if (i == R.id.buttonCamara){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA);
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1002);
            }else{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Form_Solicitud_Local.this.startActivityForResult(intent,photo_cod);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == OPEN_STORAGE){
                uriPhoto = data.getData();
                bd.SubirImagen(uriPhoto,progres);
                AparecerImagen(uriPhoto);
            }
            if(requestCode == photo_cod){
                Bundle extras = data.getExtras();
                if(extras!=null){
                    Bitmap bit = (Bitmap) extras.get("data");
                    GuardarImagen(bit);
                }
            }
        }
    }

    private void AparecerImagen(Uri uri) {
        imagen.setImageURI(uri);
    }

    private void GuardarImagen(Bitmap imagen) {
        String carpeta = "/MisOfertas";
        String nomImagen = "Oferta";
        String abs;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + carpeta;
        String fecha = ObtenerFecha();
        File directorio = new File(path);
        if(!directorio.mkdirs()){
            directorio.mkdirs();
        }
        String ruta = path+"/"+nomImagen+fecha+".jpg";
        File a = new File(ruta);
        File file = new File(directorio,nomImagen+fecha+".jpg");
        try{
            FileOutputStream fout = new FileOutputStream(file);
            imagen.compress(Bitmap.CompressFormat.JPEG,100,fout);
            fout.flush();
            fout.close();
            MakeSureFileWasCreatedThenMakeAvaible(file);
            AbleToSave(ruta);
        }catch (FileNotFoundException e){
        UnableToSave();
    }
        catch (IOException e){
        UnableToSave();
    }
    }

    private void AbleToSave(String ruta) {
        Toast.makeText(this,"Se pudo guardar con exito la foto, seleccionela desde la opcion de cargar imagen",Toast.LENGTH_LONG).show();
    }

    private void UnableToSave(){
        Toast.makeText(this,"No se ha podido guardar",Toast.LENGTH_LONG).show();

    }

    private void MakeSureFileWasCreatedThenMakeAvaible(File file) {
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null
                , new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });
    }

    private String ObtenerFecha() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String format = df.format(c.getTime());
        return format;
    }

}
