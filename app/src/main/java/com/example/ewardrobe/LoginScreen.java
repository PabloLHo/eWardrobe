package com.example.ewardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    EditText usuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        usuario = findViewById(R.id.user);
        contrasena = findViewById(R.id.pass);

    }

    public void login(View v){
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

    public void register(View v){
        Intent intent = new Intent(LoginScreen.this, MainScreen.class);
        startActivity(intent);
    }

    public void change_login(View v){
        setContentView(R.layout.login_screen);
    }

    public void change_register(View v){
        setContentView(R.layout.signin_screen);
    }

}