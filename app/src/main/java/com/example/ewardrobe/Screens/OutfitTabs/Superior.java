package com.example.ewardrobe.Screens.OutfitTabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.ewardrobe.Adapters.FiltroAdapter;
import com.example.ewardrobe.Adapters.FiltroColorAdapter;
import com.example.ewardrobe.Adapters.PrendaAdapter;
import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.R;
import com.example.ewardrobe.Screens.MainScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Superior extends Fragment {

    RecyclerView recycler, recycler_colores;
    PrendaAdapter prendaAdapter;
    List<Prenda> prendas;


    FiltroColorAdapter filtroColorAdapter;

    ArrayList<String> colores;

    String userID;

    DatabaseReference reference;
    FirebaseDatabase database;


    public Superior() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_superior, container, false);

        setRecycler(view);
        userID = getArguments().getString("userID");

        prendas = new ArrayList<>();
        colores = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");


        obtenerPrendas();

        return view;
    }

    private void obtenerPrendas(){

        reference = database.getReference();
        Query query = reference.child("usuarios").child(userID).child("prendas");
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
                continuacion();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void continuacion(){
        obtenerColores();

        prendaAdapter = new PrendaAdapter(prendas, getContext());
        recycler.setAdapter(prendaAdapter);

        prendaAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prenda prenda = prendas.get(recycler.getChildAdapterPosition(view));
                Bundle args = new Bundle();
                args.putParcelable("prenda", prenda);
                MainScreen screen = (MainScreen) requireActivity();
                screen.irAActivity(args);
            }
        });

        filtroColorAdapter = new FiltroColorAdapter(colores,getContext(), this);
        recycler_colores.setAdapter(filtroColorAdapter);
    }

    public void obtenerColores(){
        for (Prenda data : prendas) {
            for (String color : data.getColores()) {
                String aux = color.replace(" ", "");
                if(!colores.contains(aux)){
                    colores.add(aux);
                }
            }
        }
    }

    public void setRecycler(View view){
        recycler = view.findViewById(R.id.recyclerView);
        recycler_colores = view.findViewById(R.id.recyclerColores);
        recycler_colores.setVisibility(View.INVISIBLE);
        recycler_colores.setVisibility(View.GONE);
        recycler_colores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycler.setHasFixedSize(true);
    }
}