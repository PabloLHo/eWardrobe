package com.example.ewardrobe.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewardrobe.Adapters.PrendaAdapter;
import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.BBDD.Usuario;
import com.example.ewardrobe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

enum TipoRopa{
    superior,
    inferior,
    medio,
    accesorios
}

public class CreaOutfitScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    RecyclerView recycler;

    String email;
    Usuario user;
    DatabaseReference reference;
    FirebaseDatabase database;

    ImageView img, img2,img3;

    ImageButton superior,piernas,pies;

    PrendaAdapter prendaAdapter;
    List<Prenda> prendas;
    List<Prenda> visibles;

    TipoRopa visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crea_outfit_screen);
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
        obtenerUsuario();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.botonMas);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recycler.setHasFixedSize(true);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                boolean dentro = false;
                if (itemID == R.id.bottom_top) {
                    visible = TipoRopa.superior;
                    dentro = true;
                } else if (itemID == R.id.bottom_bottom) {
                    visible = TipoRopa.inferior;
                    dentro = true;
                } else if (itemID == R.id.bottom_middle) {
                    visible = TipoRopa.medio;
                    dentro = true;
                } else if (itemID == R.id.bottom_accs) {
                    visible = TipoRopa.accesorios;
                    dentro = true;
                }
                if (dentro){
                    actualizaRopa();
                    return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img = findViewById(R.id.superior);
        img2 = findViewById(R.id.piernas);
        img3 = findViewById(R.id.pies);

        superior = findViewById(R.id.limpiaSuperior);
        superior.setVisibility(View.INVISIBLE);
        superior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(android.R.color.transparent);
                superior.setVisibility(View.INVISIBLE);
            }
        });

        piernas = findViewById(R.id.limpiaPiernas);
        piernas.setVisibility(View.INVISIBLE);
        piernas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2.setImageResource(android.R.color.transparent);
                piernas.setVisibility(View.INVISIBLE);
            }
        });

        pies = findViewById(R.id.limpiaPies);
        pies.setVisibility(View.INVISIBLE);
        pies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3.setImageResource(android.R.color.transparent);
                pies.setVisibility(View.INVISIBLE);
            }
        });

        prendas = new ArrayList<>();
        visibles = new ArrayList<>();

        obtenerPrendas();


    }

    private void obtenerPrendas(){

        reference = database.getReference();
        Query query = reference.child("usuarios").child(user.getId()).child("prendas");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String foto = dataSnapshot.child("fotoURL").getValue(String.class);
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String tipo = dataSnapshot.child("tipo").getValue(String.class);
                    String marca = dataSnapshot.child("marca").getValue(String.class);
                    Boolean destacada = dataSnapshot.child("destacada").getValue(Boolean.class);
                    ArrayList<String> colores = new ArrayList<>();
                    ArrayList<String> caracteristicas = new ArrayList<>();
                    for (DataSnapshot snapshotColores: dataSnapshot.child("colores").getChildren()) {
                        colores.add(snapshotColores.getValue(String.class));
                    }
                    for (DataSnapshot snapshotCarac: dataSnapshot.child("caracteristicas").getChildren()) {
                        caracteristicas.add(snapshotCarac.getValue(String.class));
                    }
                    Prenda prenda = new Prenda(colores, caracteristicas, tipo, foto, nombre, marca, destacada);

                    prendas.add(prenda);
                }
                visible = TipoRopa.superior;
                actualizaRopa();
                prendaAdapter = new PrendaAdapter(visibles, getApplicationContext());
                recycler.setAdapter(prendaAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void obtenerUsuario(){
        email = getIntent().getStringExtra("email");
        reference = database.getReference();
        Query query = reference.child("usuarios").orderByChild("mail").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    TextView usuario = findViewById(R.id.nombre_header);
                    usuario.setText(userSnapshot.child("nombre").getValue(String.class));
                    TextView mail = findViewById(R.id.email_header);
                    mail.setText(userSnapshot.child("mail").getValue(String.class));

                    user = new Usuario(usuario.getText().toString(), mail.getText().toString(), userSnapshot.child("pass").getValue(String.class));
                    user.setId(userSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreaOutfitScreen.this, error.toException().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.nav_outfit){
            Intent intent = new Intent(CreaOutfitScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "OutfitFragment");
            startActivity(intent);
        }else if(itemID == R.id.nav_wardrobe){
            Intent intent = new Intent(CreaOutfitScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "WardrobeFragment");
            startActivity(intent);
        }else if(itemID == R.id.nav_clothes){
            Intent intent = new Intent(CreaOutfitScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "PrendasFragment");
            startActivity(intent);
        }else if(itemID == R.id.nav_home){
            Intent intent = new Intent(CreaOutfitScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "HomeFragment");
            startActivity(intent);
        }else if(itemID == R.id.nav_profile){
            Intent intent = new Intent(CreaOutfitScreen.this, ProfileScreen.class);
            startActivity(intent);
        }else if(itemID == R.id.nav_logout){
            SharedPreferences.Editor aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();
            aux.clear();
            aux.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginScreen.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    public void actualizaRopa(){
        visibles.clear();
        ArrayList<String> opciones = new ArrayList<>();
        switch (visible){
            case superior:
                opciones.add("Camiseta");
                opciones.add("Camisa");
                opciones.add("Sudadera");
                opciones.add("Chaqueta");
                opciones.add("Vestido");
                opciones.add("Abrigo");
                opciones.add("Traje");
                opciones.add("Pijama");
                break;
            case medio:
                opciones.add("Pantalón");
                opciones.add("Falda");
                opciones.add("Bañador");
                break;
            case inferior:
                opciones.add("Zapatos");
                opciones.add("Zapatillas");
                break;
            case accesorios:
                opciones.add("Accesorio");
                break;
        }
        for (int i = 0; i < prendas.size(); i++) {
            if(opciones.contains(prendas.get(i).getTipo()))
                visibles.add(prendas.get(i));
        }
    }
}