package com.example.ofertasuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MapaCoordenadasRestaurante extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private int user;
    private int enfoque = 0;
    private Marker marker;
    private int candado = 0;
    private LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_coordenadas_restaurante);
        Toolbar toolbar = findViewById(R.id.toolbarMapa);
        setSupportActionBar(toolbar);
        ObtenerDatos();
        PermisosUbicacion();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (enfoque == 0) {
            Localizarme();
        }
    }

    private void PermisosUbicacion() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                try {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } catch (Exception e) {
                    Log.i("|---|", "|----|");
                }
            }
            Localizarme();
        }
    }

    private void Localizarme() {
        LocationManager locatManger = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (candado == 0) {
                    position = new LatLng(location.getLatitude(), location.getLongitude());
                    if (marker != null) {
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title("Posicion actual"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(position,16);
                    mMap.animateCamera(ubicacion);
                    candado = 1;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        try {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    try {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }catch (Exception e) {
                        Log.i("|---|", "|----|");
                    }
                }
                return;
            }
            locatManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener, Looper.getMainLooper());
            locatManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener, Looper.getMainLooper());
        }catch (Exception e){
            Localizarme();
        }
        enfoque = 1;
    }

    private void ObtenerDatos(){
        Bundle extras = getIntent().getExtras();
        user = extras.getInt("Usuario");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.backMapa:
                finish();
                if(user == 2){
                    Calendar now = Calendar.getInstance();
                    int day = now.get(Calendar.DAY_OF_WEEK);
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.putExtra("seleccion",day);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this,MainAdministrador.class);
                    startActivity(intent);
                }
            break;
            case R.id.nextTo:
                finish();
                Intent intent2 = new Intent(this,Form_Solicitud_Local.class);
                intent2.putExtra("Usuario",user);
                intent2.putExtra("Latitude",position.latitude);
                intent2.putExtra("Longitude",position.longitude);
                startActivity(intent2);
            break;
            case R.id.ubicarme:
                finish();
                Intent intent = new Intent(this,MapaCoordenadasRestaurante.class);
                intent.putExtra("Usuario",user);
                startActivity(intent);
            break;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker!=null){
                    marker.remove();
                }
                position = latLng;
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Posicion elegida"));
            }
        });
        Localizarme();
    }

}
