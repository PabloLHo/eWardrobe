package com.example.ewardrobe.Screens;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ewardrobe.BBDD.Usuario;
import com.example.ewardrobe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginScreen extends AppCompatActivity {

    private EditText usuario, contrasena, signupname, signuppass, signupmail;

    private FirebaseAuth auth;

    private FirebaseDatabase database;
    private DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        usuario = findViewById(R.id.user);
        contrasena = findViewById(R.id.pass);

        auth = FirebaseAuth.getInstance();

        compruebaSesion();

    }

    private void compruebaSesion(){
        SharedPreferences aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        String email = aux.getString("email", null);
        String pass = aux.getString("contraseña", null);

        if(email != null && pass != null){
            Intent intent = new Intent(LoginScreen.this, MainScreen.class);
            intent.putExtra("fragmentToOpen", "HomeFragment");
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }

    public void login(View v){
        if(!usuario.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(usuario.getText().toString()).matches()){
            if(!contrasena.getText().toString().isEmpty()) {
                auth.signInWithEmailAndPassword(usuario.getText().toString(), contrasena.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        SharedPreferences.Editor aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();
                        aux.putString("email", usuario.getText().toString());
                        aux.putString("contraseña", contrasena.getText().toString());
                        aux.apply();
                        Toast.makeText(LoginScreen.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                        intent.putExtra("email", usuario.getText().toString());
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginScreen.this, "Inicio de sesión incorrecto", Toast.LENGTH_SHORT).show();
                        contrasena.getText().clear();
                        usuario.getText().clear();
                    }
                });
            }else{
                Toast.makeText(LoginScreen.this, "Falta contraseña", Toast.LENGTH_SHORT).show();
            }
        }else if(usuario.getText().toString().isEmpty()){
                Toast.makeText(LoginScreen.this, "Falta usuario", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LoginScreen.this, "Formato de email incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View v){
        if(!signupname.getText().toString().isEmpty() && !signuppass.getText().toString().isEmpty() && !signupmail.getText().toString().isEmpty()){
            auth.createUserWithEmailAndPassword(signupmail.getText().toString(), signuppass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
                        reference = database.getReference();
                        Usuario user = new Usuario(signupname.getText().toString(), signupmail.getText().toString(), signuppass.getText().toString());
                        String id = generateUserIdFromEmail(signupmail.getText().toString());
                        reference.child("usuarios").child(id).setValue(user);

                        user.setId(id);
                        SharedPreferences.Editor aux = getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();
                        aux.putString("email", signupmail.getText().toString());
                        aux.putString("pass", signuppass.getText().toString());
                        aux.apply();
                        Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                        intent.putExtra("email", signupmail.getText().toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginScreen.this, "Fallo en el registro" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else{
            if(signupmail.getText().toString().isEmpty()) {
                Toast.makeText(LoginScreen.this, "Falta email", Toast.LENGTH_SHORT).show();
            }else if(signuppass.getText().toString().isEmpty()){
                Toast.makeText(LoginScreen.this, "Falta contraseña", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginScreen.this, "Falta un nombre de usuario", Toast.LENGTH_SHORT).show();
            }

        }
    }



    public void change_register(View v){

        setContentView(R.layout.signin_screen);
        signupname = findViewById(R.id.signup_name);
        signupmail = findViewById(R.id.signup_email);
        signuppass = findViewById(R.id.signup_password);

    }

    public void change_login(View v){
        setContentView(R.layout.login_screen);
    }

    public static String generateUserIdFromEmail(String email) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(email.getBytes());
            StringBuilder hashStringBuilder = new StringBuilder();

            for (byte hashByte : hashBytes) {
                // Convert each byte to hexadecimal representation
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hashStringBuilder.append('0');
                }
                hashStringBuilder.append(hex);
            }

            return hashStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}