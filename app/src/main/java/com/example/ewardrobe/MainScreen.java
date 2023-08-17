package com.example.ewardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ewardrobe.Fragments.HomeFragment;
import com.example.ewardrobe.Fragments.OutfitFragment;
import com.example.ewardrobe.Fragments.PerfilFragment;
import com.example.ewardrobe.Fragments.PrendasFragment;
import com.example.ewardrobe.Fragments.WardrobeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String[] prendas = {"Pantalón", "Falda", "Camiseta", "Camisa", "Zapatos", "Accesorio", "Sudadera", "Bañador"};
    String[] colores = {"Rojo", "Azul", "Amarillo", "Naranja", "Marron", "Rosa"};
    String[] caracteristicas = {"Deporte", "Corto", "Largo", "Abrigado"};

    TextView textCarac, textColor, textPrenda;
    ImageView img;
    int selectedItem = -1;

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        fab = findViewById(R.id.botonMas);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.bottom_home){
                    openFragment(new HomeFragment());
                    return true;
                }else if(itemID == R.id.bottom_outfit){
                    openFragment(new OutfitFragment());
                    return true;
                }else if(itemID == R.id.bottom_clothes){
                    openFragment(new PrendasFragment());
                    return true;
                }else if(itemID == R.id.bottom_wardrobe){
                    openFragment(new WardrobeFragment());
                    return true;
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainScreen.this);

                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.dialog_prenda, null);


                Button btn = v.findViewById(R.id.button);
                img = v.findViewById(R.id.imagen_tomada);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tomarFoto();
                    }
                });
                alerta.setTitle("Añadir Prenda");
                alerta.setView(v);
                alerta.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LayoutInflater inflater = getLayoutInflater();
                        View v = inflater.inflate(R.layout.dialog_prenda, null);
                        EditText e1 = v.findViewById(R.id.editTextText);
                        EditText e2 = v.findViewById(R.id.editTextText2);
                        TextView t1 = v.findViewById(R.id.textViewTipo);
                        TextView t2 = v.findViewById(R.id.textViewColores);
                        if(e1.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir un nombre", Toast.LENGTH_SHORT).show();
                        }else if(e2.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir una marca", Toast.LENGTH_SHORT).show();
                        }else if(t1.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir un tipo", Toast.LENGTH_SHORT).show();
                        }else if(t2.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir un color", Toast.LENGTH_SHORT).show();
                        }else if(v.findViewById(R.id.imagen_tomada).getTag() == "estandar"){
                            Toast.makeText(MainScreen.this, "Falta introducir una foto", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainScreen.this, "Prenda guardada", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }

                    }
                });
                alerta.setCancelable(true);

                AlertDialog dialog = alerta.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
            }
        });

    }

    public void tomarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(imageBitmap);
            img.setTag("no estandar");
        }
    }

    public void colores(View v){
        textColor = v.findViewById(R.id.textViewColores);
        boolean[] selectedLanguage;
        ArrayList<Integer> langList = new ArrayList<>();

        selectedLanguage = new boolean[colores.length];
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);

        // set title
        builder.setTitle("Select Language");
        // set dialog non cancelable
        builder.setCancelable(false);
        builder.setMultiChoiceItems(colores, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    langList.add(i);
                    // Sort array list
                    Collections.sort(langList);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < langList.size(); j++) {
                    // concat array value
                    stringBuilder.append(colores[langList.get(j)]);
                    // check condition
                    if (j != langList.size() - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ");
                    }
                }
                // set text on textView
                textColor.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selectedLanguage.length; j++) {
                    // remove all selection
                    selectedLanguage[j] = false;
                    // clear language list
                    langList.clear();
                    // clear text view value
                    textColor.setText("");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.blue));
    }

    public void prendas(View v){
        textPrenda = v.findViewById(R.id.textViewTipo);
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una opción")
                .setSingleChoiceItems(prendas, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        selectedItem = position;
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedItem != -1) {
                            textPrenda.setText(prendas[selectedItem]);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));

    }

    public void caracteristicas(View v){
        boolean[] selectedLanguage;
        ArrayList<Integer> langList = new ArrayList<>();
        textCarac = v.findViewById(R.id.textViewCaracteristicas);

        selectedLanguage = new boolean[caracteristicas.length];
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);

        // set title
        builder.setTitle("Select Language");

        // set dialog non cancelable
        builder.setCancelable(false);
        builder.setMultiChoiceItems(caracteristicas, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    langList.add(i);
                    // Sort array list
                    Collections.sort(langList);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < langList.size(); j++) {
                    // concat array value
                    stringBuilder.append(caracteristicas[langList.get(j)]);
                    // check condition
                    if (j != langList.size() - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ");
                    }
                }
                // set text on textView
                textCarac.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selectedLanguage.length; j++) {
                    // remove all selection
                    selectedLanguage[j] = false;
                    // clear language list
                    langList.clear();
                    // clear text view value
                    textCarac.setText("");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.blue));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.nav_home){
            openFragment(new HomeFragment());
        }else if(itemID == R.id.nav_clothes){
            openFragment(new PrendasFragment());
        }else if(itemID == R.id.nav_wardrobe) {
            openFragment(new WardrobeFragment());
        }else if(itemID == R.id.nav_outfit){
                openFragment(new OutfitFragment());
        }else if(itemID == R.id.nav_logout){
            Intent intent = new Intent(MainScreen.this, LoginScreen.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
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