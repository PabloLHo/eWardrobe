package com.example.ewardrobe.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewardrobe.Adapters.PrendaAdapter;
import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.BBDD.Usuario;
import com.example.ewardrobe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FragmentManager fragmentManager;

    String email;
    Usuario user;

    ArrayList<Prenda> prendas;
    ArrayList<String> armarios;
    ArrayList<String> outfits;

    DatabaseReference reference;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");

        prendas = new ArrayList<>();
        armarios = new ArrayList<>();
        outfits = new ArrayList<>();

        obtenerUsuario();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
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
                obtenerPrendas();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileScreen.this, error.toException().toString(), Toast.LENGTH_LONG).show();
            }
        });
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

                imprimirResultados();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.nav_outfit){
            Intent intent = new Intent(ProfileScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "OutfitFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_wardrobe){
            Intent intent = new Intent(ProfileScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "WardrobeFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_clothes){
            Intent intent = new Intent(ProfileScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "PrendasFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_home){
            Intent intent = new Intent(ProfileScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "HomeFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_outfitcreator){
            Intent intent = new Intent(ProfileScreen.this, CreaOutfitScreen.class);
            intent.putExtra("email", email);
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

    private void imprimirResultados(){
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(user.getNombre());

        TextView correo = findViewById(R.id.correo);
        correo.setText(user.getMail());

        TextView tlf = findViewById(R.id.tlf);
        tlf.setText(user.getTlf());

        TextView ubi = findViewById(R.id.ubi);
        ubi.setText(user.getUbi());

        TextView nPrendas = findViewById(R.id.nPrendas);
        nPrendas.setText(Integer.toString(this.prendas.size()));

        TextView nArmarios = findViewById(R.id.nArmarios);
        nArmarios.setText(Integer.toString(this.armarios.size()));

        TextView nOutfits = findViewById(R.id.nOutfits);
        nOutfits.setText(Integer.toString(this.outfits.size()));

        Map<String,Integer> _prenda = new HashMap<>();
        Map<String,Integer> _marca = new HashMap<>();
        Map<String,Integer> _color = new HashMap<>();
        for (int i = 0; i < this.prendas.size(); i++) {
            if(!_prenda.containsKey(this.prendas.get(i).getTipo())) {
                _prenda.put(this.prendas.get(i).getTipo(), 1);
            }else{
                _prenda.put(this.prendas.get(i).getTipo(), _prenda.get(this.prendas.get(i).getTipo()) + 1);
            }
            if(!_marca.containsKey(this.prendas.get(i).getMarca())) {
                _marca.put(this.prendas.get(i).getMarca(), 1);
            }else{
                _marca.put(this.prendas.get(i).getMarca(), _marca.get(this.prendas.get(i).getMarca()) + 1);
            }
            for (int j = 0; j < this.prendas.get(i).getColores().size(); j++) {
                if(!_color.containsKey(this.prendas.get(i).getColores().get(j))) {
                    _color.put(this.prendas.get(i).getColores().get(j), 1);
                }else{
                    _color.put(this.prendas.get(i).getColores().get(j), _color.get(this.prendas.get(i).getColores().get(j)) + 1);
                }
            }
        }

        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : _prenda.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        TextView prenda = findViewById(R.id.prendamas);
        prenda.setText(maxEntry.getKey());

        maxEntry = null;
        for (Map.Entry<String, Integer> entry : _color.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }

        TextView color = findViewById(R.id.colormas);
        color.setText(maxEntry.getKey());

        maxEntry = null;
        for (Map.Entry<String, Integer> entry : _marca.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }

        TextView marca = findViewById(R.id.marcamas);
        marca.setText(maxEntry.getKey());

    }

}