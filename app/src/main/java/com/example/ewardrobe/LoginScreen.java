package com.example.ewardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    EditText usuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        usuario = findViewById(R.id.user);
        contrasena = findViewById(R.id.contrasena);
    }

    protected void login(View v){
        if(usuario.getText().toString().equals("Pablo") && contrasena.getText().toString().equals("Pablo")){
            Intent intent = new Intent(LoginScreen.this, MainScreen.class);
            intent.putExtra("DATO_STRING", usuario.getText().toString());
            intent.putExtra("DATO_STRING", contrasena.getText().toString());
            startActivity(intent);
        }else{
            if(usuario.getText().toString().equals("Pablo")) {
                Toast.makeText(LoginScreen.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                contrasena.getText().clear();
            }else{
                Toast.makeText(LoginScreen.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                contrasena.getText().clear();
                usuario.getText().clear();
            }

        }
    }

    protected void register(View v){
        Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
        startActivity(intent);
    }
}