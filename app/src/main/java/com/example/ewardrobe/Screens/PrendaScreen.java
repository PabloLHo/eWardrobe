package com.example.ewardrobe.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ewardrobe.BBDD.Usuario;
import com.example.ewardrobe.Adapters.CaracAdapter;
import com.example.ewardrobe.Adapters.ColorAdapter;
import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PrendaScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Prenda prenda;

    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private RecyclerView recycler, recycler_colores;
    private CaracAdapter caracAdapter;
    private ColorAdapter colorAdapter;

    private ImageView imagen;
    private TextView t1,t2,t3;

    String email;
    Usuario user;

    DatabaseReference reference;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prenda_screen);
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
        obtenerUsuario();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        Bundle args = getIntent().getExtras();
        if (args != null) {
            prenda = args.getParcelable("prenda");
        }
        recycler = findViewById(R.id.recyclerView);
        recycler_colores = findViewById(R.id.infoColores);

        recycler_colores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_colores.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setHasFixedSize(true);


        caracAdapter = new CaracAdapter(prenda.getCaracteristicas(), this);
        colorAdapter = new ColorAdapter(prenda.getColores(), this);
        recycler_colores.setAdapter(colorAdapter);
        recycler.setAdapter(caracAdapter);

        imagen = findViewById(R.id.fotoPrincipal);
        Glide.with(this).load(prenda.getFotoURL()).into(imagen);

        t1 = findViewById(R.id.nombrePrenda);
        t1.setText(prenda.getNombre());
        t2 = findViewById(R.id.marcaPrenda);
        t2.setText(prenda.getMarca());
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
                Toast.makeText(PrendaScreen.this, error.toException().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.nav_outfit){
            Intent intent = new Intent(PrendaScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "OutfitFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_wardrobe){
            Intent intent = new Intent(PrendaScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "WardrobeFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_clothes){
            Intent intent = new Intent(PrendaScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "PrendasFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_home){
            Intent intent = new Intent(PrendaScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "HomeFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_outfitcreator){
            Intent intent = new Intent(this, CreaOutfitScreen.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_profile){
            Intent intent = new Intent(this, ProfileScreen.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(itemID == R.id.nav_logout){
            SharedPreferences.Editor aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();
            aux.clear();
            aux.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PrendaScreen.this, LoginScreen.class);
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
}