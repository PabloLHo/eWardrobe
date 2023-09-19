package com.example.ewardrobe.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.BBDD.Usuario;
import com.example.ewardrobe.Fragments.HomeFragment;
import com.example.ewardrobe.Fragments.OutfitFragment;
import com.example.ewardrobe.Fragments.PrendasFragment;
import com.example.ewardrobe.Fragments.WardrobeFragment;
import com.example.ewardrobe.R;
import com.example.ewardrobe.Screens.OutfitTabs.Accesorios;
import com.example.ewardrobe.Screens.OutfitTabs.Inferior;
import com.example.ewardrobe.Screens.OutfitTabs.Medio;
import com.example.ewardrobe.Screens.OutfitTabs.Superior;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;

public class CreaOutfitScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    String email;
    Usuario user;
    DatabaseReference reference;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crea_outfit_screen);
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

        bottomNavigationView = findViewById(R.id.bottom_menu_outfit);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                Bundle args = new Bundle();
                args.putString("userID", user.getId());
                if(itemID == R.id.bottom_outfit){
                    OutfitFragment outfitFragment = new OutfitFragment();
                    outfitFragment.setArguments(args);
                    openFragment(outfitFragment);
                    return true;
                }else if(itemID == R.id.bottom_wardrobe){
                    WardrobeFragment wardrobeFragment = new WardrobeFragment();
                    wardrobeFragment.setArguments(args);
                    openFragment(wardrobeFragment);
                    return true;
                }else if(itemID == R.id.bottom_clothes){
                    PrendasFragment prendas = new PrendasFragment();
                    prendas.setArguments(args);
                    openFragment(prendas);
                    return true;
                }else if(itemID == R.id.bottom_home){
                    HomeFragment home = new HomeFragment();
                    home.setArguments(args);
                    openFragment(home);
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
        }
        drawer.closeDrawer(GravityCompat.START);
        finish();
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

    private void openFragment(Fragment frag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, frag);
        transaction.commit();
    }
}