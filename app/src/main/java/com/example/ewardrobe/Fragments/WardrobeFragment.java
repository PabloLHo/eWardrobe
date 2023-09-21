package com.example.ewardrobe.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class WardrobeFragment extends Fragment {


    private boolean armario = false;
    private String usuario;

    DatabaseReference reference;
    FirebaseDatabase database;

    public WardrobeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        usuario = getArguments().getString("userID");
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
        obtenerArmario();
        View view;
        if(!armario) {
            view = inflater.inflate(R.layout.no_wardrobe, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_wardrobe, container, false);
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void obtenerArmario(){
        reference = database.getReference();
        Query query = reference.child("usuarios").child(usuario).child("armario");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    armario = true;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String foto = dataSnapshot.child("fotoURL").getValue(String.class);
                        String nombre = dataSnapshot.child("nombre").getValue(String.class);
                        String tipo = dataSnapshot.child("tipo").getValue(String.class);
                        String marca = dataSnapshot.child("marca").getValue(String.class);
                        ArrayList<String> colores = new ArrayList<>();
                        ArrayList<String> caracteristicas = new ArrayList<>();
                        for (DataSnapshot snapshotColores : dataSnapshot.child("colores").getChildren()) {
                            colores.add(snapshotColores.getValue(String.class));
                        }
                        for (DataSnapshot snapshotCarac : dataSnapshot.child("caracteristicas").getChildren()) {
                            caracteristicas.add(snapshotCarac.getValue(String.class));
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}