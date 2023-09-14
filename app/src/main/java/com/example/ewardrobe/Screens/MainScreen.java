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
import androidx.fragment.app.FragmentTransaction;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewardrobe.BBDD.Usuario;
import com.example.ewardrobe.Fragments.HomeFragment;
import com.example.ewardrobe.Fragments.OutfitFragment;
import com.example.ewardrobe.Fragments.PrendasFragment;
import com.example.ewardrobe.Fragments.WardrobeFragment;
import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;


public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private ImageView img;
    private TextView textColor, textCarac, textPrenda;
    private int selectedItem = -1;
    String[] prendas = {"Pantalón", "Falda", "Camiseta", "Camisa", "Zapatos", "Zapatillas", "Sudadera", "Chaqueta", "Vestido", "Traje", "Bañador", "Abrigo" ,"Accesorio", "Pijama", };
    String[] colores = {"Rojo", "Azul", "Amarillo", "Naranja", "Marron", "Rosa", "Morado", "Lila", "Blanco", "Negro", "Gris" , "Beige", "Turquesa", "Violeta"};
    String[] caracteristicas = {"Deportivo", "Elegante", "Casual", "Versatil", "Corto", "Largo", "Abrigado", "Fresco", "Holgado", "Ajustado" , "Comodo", "Formal" , "Informal", "Vintage", "Estampado", "Suave", "Aspero", "Térmica"};

    String email;
    Usuario user;

    private Uri imageUri;
    DatabaseReference reference;
    FirebaseDatabase database;

    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
        storageReference = FirebaseStorage.getInstance().getReference();


        String fragmentAbrir = getIntent().getStringExtra("fragmentToOpen");
        obtenerUsuario(fragmentAbrir);
        fragmentManager = getSupportFragmentManager();

        fab = findViewById(R.id.botonMas);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

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
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainScreen.this);

                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.dialog_prenda, null);

                EditText e1 = v.findViewById(R.id.editTextText);
                EditText e2 = v.findViewById(R.id.editTextText2);
                TextView t1 = v.findViewById(R.id.textViewTipo);
                TextView t2 = v.findViewById(R.id.textViewColores);
                TextView t3 = v.findViewById(R.id.textViewCaracteristicas);
                Button btn = v.findViewById(R.id.button_foto);
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

                        if(e1.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir un nombre", Toast.LENGTH_SHORT).show();
                        }else if(e2.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir una marca", Toast.LENGTH_SHORT).show();
                        }else if(t1.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir un tipo", Toast.LENGTH_SHORT).show();
                        }else if(t2.getText().toString().equals("")){
                            Toast.makeText(MainScreen.this, "Falta introducir un color", Toast.LENGTH_SHORT).show();
                        }else if(img.getTag() == "estandar"){
                            Toast.makeText(MainScreen.this, "Falta introducir una foto", Toast.LENGTH_SHORT).show();
                        }else{
                            String[] _colores, _caracteristicas;
                            ArrayList<String> colores = new ArrayList<>();
                            ArrayList<String> caracteristicas = new ArrayList<>();
                            _colores = t2.getText().toString().split(",");
                            _caracteristicas = t3.getText().toString().split(",");
                            Collections.addAll(colores, _colores);
                            Collections.addAll(caracteristicas, _caracteristicas);
                            StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Prenda aux = new Prenda(colores, caracteristicas, t1.getText().toString(), uri.toString() , e1.getText().toString(), e2.getText().toString());
                                            Toast.makeText(MainScreen.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                                            reference = database.getReference();
                                            reference.child("usuarios").child(user.getId()).child("prendas").child(aux.getNombre()).setValue(aux);
                                        }
                                    });

                                }
                            });

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

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    private void primerFragment(String fragmentAbrir){
        if(fragmentAbrir != null) {
            Bundle args = new Bundle();
            args.putString("userID", user.getId());
            switch (fragmentAbrir) {
                case "HomeFragment":
                    HomeFragment home = new HomeFragment();
                    home.setArguments(args);
                    openFragment(home);
                    break;
                case "OutfitFragment":
                    OutfitFragment outfitFragment = new OutfitFragment();
                    outfitFragment.setArguments(args);
                    openFragment(outfitFragment);
                    break;
                case "PrendasFragment":
                    PrendasFragment prendas = new PrendasFragment();
                    prendas.setArguments(args);
                    openFragment(prendas);
                    break;
                case "WardrobeFragmnet":
                    WardrobeFragment wardrobeFragment = new WardrobeFragment();
                    wardrobeFragment.setArguments(args);
                    openFragment(wardrobeFragment);
                    break;
            }
        }else{
            String email = getIntent().getStringExtra("email");
            String pass = getIntent().getStringExtra("pass");
            SharedPreferences.Editor aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();

            aux.putString("email", email);
            aux.putString("pass", pass);
            aux.apply();
            Bundle args = new Bundle();
            args.putString("userID", user.getId());
            HomeFragment home = new HomeFragment();
            home.setArguments(args);
            openFragment(home);
        }
    }

    private void obtenerUsuario(String fragmentAbrir){
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
                primerFragment(fragmentAbrir);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainScreen.this, error.toException().toString(), Toast.LENGTH_LONG).show();
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
            imageUri = getImageUri(getApplicationContext(), imageBitmap);
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        Bundle args = new Bundle();
        args.putString("userID", user.getId());
        if(itemID == R.id.nav_outfit){
            OutfitFragment outfitFragment = new OutfitFragment();
            outfitFragment.setArguments(args);
            openFragment(outfitFragment);
        }else if(itemID == R.id.nav_wardrobe){
            WardrobeFragment wardrobeFragment = new WardrobeFragment();
            wardrobeFragment.setArguments(args);
            openFragment(wardrobeFragment);
        }else if(itemID == R.id.nav_clothes){
            PrendasFragment prendas = new PrendasFragment();
            prendas.setArguments(args);
            openFragment(prendas);
        }else if(itemID == R.id.nav_home){
            HomeFragment home = new HomeFragment();
            home.setArguments(args);
            openFragment(home);
        }else if(itemID == R.id.nav_logout){

            SharedPreferences.Editor aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();
            aux.clear();
            aux.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainScreen.this, LoginScreen.class);
            startActivity(intent);
            finish();
        }else if(itemID == R.id.nav_setting){
            Intent intent = new Intent(this, SettingsScreen.class);
            startActivity(intent);
            finish();
        }else if(itemID == R.id.nav_profile){
            Intent intent = new Intent(this, ProfileScreen.class);
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

    private void openFragment(Fragment frag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, frag);
        transaction.commit();
    }

    public void prendas(View v){
        textPrenda = v.findViewById(R.id.textViewTipo);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Selecciona una opción")
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

        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
    }

    public void caracteristicas(View v){

        textCarac = v.findViewById(R.id.textViewCaracteristicas);
        boolean[] selectedLanguage = new boolean[caracteristicas.length];
        ArrayList<Integer> langList = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
        builder.setTitle("Añadir prenda");
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

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.blue));
    }

    public void colores(View v){
        textColor = v.findViewById(R.id.textViewColores);
        boolean[] selectedLanguage = new boolean[colores.length];
        ArrayList<Integer> langList = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
        builder.setTitle("Añadir prenda");
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

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.blue));
    }

    public void irAActivity(Bundle bundle){
        Intent intent = new Intent(this, PrendaScreen.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}