package com.example.ofertasuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class MainAdministrador extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager vp;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);
        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabAdmin);
        vp = findViewById(R.id.viewPagerAdmin);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new Fragment_Aprobados(),"Aprobados");
        adapter.AddFragment(new Fragment_Solicitud(),"Solicitudes");
        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_administrador,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.subirAdmin){
            finish();
            Intent intent = new Intent(this,MapaCoordenadasRestaurante.class);
            intent.putExtra("Usuario",1);
            startActivity(intent);
        }
        return true;
    }
}
