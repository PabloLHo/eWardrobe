package com.example.ewardrobe.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ewardrobe.Adapters.FiltroAdapter;
import com.example.ewardrobe.Adapters.FiltroColorAdapter;
import com.example.ewardrobe.BBDD.Prenda;
import com.example.ewardrobe.Adapters.PrendaAdapter;
import com.example.ewardrobe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OutfitFragment extends Fragment {

    RecyclerView recycler, recycler_filtros, recycler_colores;
    PrendaAdapter prendaAdapter;
    List<Prenda> prendas;

    FiltroAdapter filtroAdapter;

    FiltroColorAdapter filtroColorAdapter;
    ImageButton button;
    private ArrayList<Prenda> searchList;
    Boolean filtrosVisibles;

    ArrayList<String> filtros;
    ArrayList<String> colores;

    DatabaseReference reference;
    FirebaseDatabase database;

    String userID;

    ImageView img, img2,img3;

    ImageButton superior,piernas,pies;

    String[] filtradoColaborativo;

    public OutfitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_outfit, container, false);

        filtradoColaborativo = new String[2];
        for (int i = 0; i < 2; i++) {
            filtradoColaborativo[i] = "";
        }

        setRecycler(view);
        userID = getArguments().getString("userID");
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
        filtrosVisibles = false;
        prendas = new ArrayList<>();

        img = view.findViewById(R.id.superior);
        img2 = view.findViewById(R.id.piernas);
        img3 = view.findViewById(R.id.pies);

        prendaAdapter = new PrendaAdapter(searchList, getContext());
        recycler.setAdapter(prendaAdapter);

        filtroAdapter = new FiltroAdapter(filtros,getContext(), this, "tipo");
        recycler_filtros.setAdapter(filtroAdapter);

        filtroColorAdapter = new FiltroColorAdapter(colores,getContext(), this);
        recycler_colores.setAdapter(filtroColorAdapter);

        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filtrosVisibles){
                    recycler_filtros.setVisibility(View.GONE);
                    recycler_colores.setVisibility(View.GONE);
                }else{
                    recycler_filtros.setVisibility(View.VISIBLE);
                    recycler_colores.setVisibility(View.VISIBLE);
                }
                filtrosVisibles = !filtrosVisibles;
            }
        });

        superior = view.findViewById(R.id.limpiaSuperior);
        superior.setVisibility(View.INVISIBLE);
        superior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(android.R.color.transparent);
                superior.setVisibility(View.INVISIBLE);
            }
        });

        piernas = view.findViewById(R.id.limpiaPiernas);
        piernas.setVisibility(View.INVISIBLE);
        piernas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2.setImageResource(android.R.color.transparent);
                piernas.setVisibility(View.INVISIBLE);
            }
        });

        pies = view.findViewById(R.id.limpiaPies);
        pies.setVisibility(View.INVISIBLE);
        pies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3.setImageResource(android.R.color.transparent);
                pies.setVisibility(View.INVISIBLE);
            }
        });

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
        searchList.addAll(prendas);
        obtenerFiltros();
        obtenerColores();

        prendaAdapter = new PrendaAdapter(searchList, getContext());
        recycler.setAdapter(prendaAdapter);

        prendaAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prenda prenda = prendas.get(recycler.getChildAdapterPosition(view));

                switch (prenda.getTipo()){
                    case "Camiseta":
                        Glide.with(getContext()).load(prenda.getFotoURL()).into(img);
                        superior.setVisibility(View.VISIBLE);
                        break;
                    case "Zapatos":
                    case "Zapatillas":
                        Glide.with(getContext()).load(prenda.getFotoURL()).into(img3);
                        pies.setVisibility(View.VISIBLE);
                        break;
                    case "Pantalones":
                        Glide.with(getContext()).load(prenda.getFotoURL()).into(img2);
                        piernas.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        filtroAdapter = new FiltroAdapter(filtros,getContext(), this, "tipo");
        recycler_filtros.setAdapter(filtroAdapter);


        filtroColorAdapter = new FiltroColorAdapter(colores,getContext(), this);
        recycler_colores.setAdapter(filtroColorAdapter);
    }


    public void obtenerFiltros(){
        filtros = new ArrayList<>();
        for (Prenda data : prendas) {
            if(!filtros.contains(data.getTipo())){
                filtros.add(data.getTipo());
            }
        }
    }

    public void obtenerColores(){
        colores = new ArrayList<>();
        for (Prenda data : prendas) {
            for (String color : data.getColores()) {
                String aux = color.replace(" ", "");
                if(!colores.contains(aux)){
                    colores.add(aux);
                }
            }
        }
    }

    public void filtradoBotones(String nombre, String tipo, String get){

        if(tipo.equals("filtrar")){
            switch (get){
                case "color":
                    filtradoColaborativo[1]+= " " + nombre;
                    break;
                case "tipo":
                    filtradoColaborativo[0]+= " " + nombre;
                    break;
            }
            filtrar();
            recycler.getAdapter().notifyDataSetChanged();
        }else{
            switch (get){
                case "color":
                    filtradoColaborativo[1] = filtradoColaborativo[1].replace(" " + nombre, "");
                    break;
                case "tipo":
                    filtradoColaborativo[0] = filtradoColaborativo[0].replace(" " + nombre, "");
                    break;
            }
            filtrar();
            recycler.getAdapter().notifyDataSetChanged();
        }

    }

    public void setRecycler(View view){
        recycler = view.findViewById(R.id.recyclerView);
        recycler_filtros = view.findViewById(R.id.recyclerBotones);
        recycler_colores = view.findViewById(R.id.recyclerColores);
        button = view.findViewById(R.id.button);
        recycler_filtros.setVisibility(View.INVISIBLE);
        recycler_filtros.setVisibility(View.GONE);
        recycler_colores.setVisibility(View.INVISIBLE);
        recycler_colores.setVisibility(View.GONE);
        recycler_filtros.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_colores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recycler.setHasFixedSize(true);
    }

    private void filtrar() {
        searchList.clear();
        for (Prenda prenda : prendas) {
            if (filtradoColaborativo[0].equals("") || filtradoColaborativo[0].contains(prenda.getTipo())) {
                boolean color_esta = false;
                if (filtradoColaborativo[1].equals(""))
                    color_esta = true;
                else {
                    for (String color : prenda.getColores()) {
                        String aux = color.replace(" ", "");
                        if (filtradoColaborativo[1].contains(aux))
                            color_esta = true;
                    }
                }
                if (color_esta) {
                    searchList.add(prenda);
                }
            }
        }
    }
}