package com.example.ewardrobe.Fragments;

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
import com.example.ewardrobe.Screens.MainScreen;
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
import java.util.Locale;


public class PrendasFragment extends Fragment {

    RecyclerView recycler, recycler_filtros, recycler_colores, recycler_marcas;
    PrendaAdapter prendaAdapter;
    List<Prenda> prendas;

    FiltroAdapter filtroAdapter, marcaAdapter;

    FiltroColorAdapter filtroColorAdapter;
    SearchView searchView;
    ImageButton button;
    private ArrayList<Prenda> searchList;
    Boolean filtrosVisibles;

    ArrayList<String> filtros;
    ArrayList<String> colores;
    ArrayList<String> marcas;

    String userID;

    DatabaseReference reference;
    FirebaseDatabase database;

    String[] filtradoColaborativo;



    public PrendasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_prendas, container, false);
        filtradoColaborativo = new String[4];
        for (int i = 0; i < 4; i++) {
            filtradoColaborativo[i] = "";
        }
        setRecycler(view);
        userID = getArguments().getString("userID");
        filtrosVisibles = false;
        prendas = new ArrayList<>();
        searchList = new ArrayList<>();
        colores = new ArrayList<>();
        marcas = new ArrayList<>();
        filtros = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");



        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.clear();
                String searchText = newText.toLowerCase(Locale.getDefault());
                if (!searchText.isEmpty()) {
                    filtradoColaborativo[0] = searchText;
                    filtrar();
                    recycler.getAdapter().notifyDataSetChanged();
                } else {
                    filtradoColaborativo[0] = searchText;
                    filtrar();
                    recycler.getAdapter().notifyDataSetChanged();
                }
                return false;
            }
        });

        obtenerPrendas();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filtrosVisibles){
                    recycler_filtros.setVisibility(View.GONE);
                    recycler_marcas.setVisibility(View.GONE);
                    recycler_colores.setVisibility(View.GONE);
                }else{
                    recycler_filtros.setVisibility(View.VISIBLE);
                    recycler_marcas.setVisibility(View.VISIBLE);
                    recycler_colores.setVisibility(View.VISIBLE);
                }
                filtrosVisibles = !filtrosVisibles;
            }
        });

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
        obtenerMarcas();

        prendaAdapter = new PrendaAdapter(searchList, getContext());
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

        filtroAdapter = new FiltroAdapter(filtros,getContext(), this, "tipo");
        recycler_filtros.setAdapter(filtroAdapter);

        marcaAdapter = new FiltroAdapter(marcas,getContext(), this, "marca");
        recycler_marcas.setAdapter(marcaAdapter);

        filtroColorAdapter = new FiltroColorAdapter(colores,getContext(), this);
        recycler_colores.setAdapter(filtroColorAdapter);
    }

    public void filtradoBotones(String nombre, String tipo, String get){

        if(tipo.equals("filtrar")){
            switch (get){
                case "color":
                    filtradoColaborativo[3]+= " " + nombre;
                    break;
                case "tipo":
                    filtradoColaborativo[1]+= " " + nombre;
                    break;
                case "marca":
                    filtradoColaborativo[2]+= " " + nombre;
                    break;
            }
            filtrar();
            recycler.getAdapter().notifyDataSetChanged();
        }else{
            switch (get){
                case "color":
                    filtradoColaborativo[3] = filtradoColaborativo[3].replace(" " + nombre, "");
                    break;
                case "tipo":
                    filtradoColaborativo[1] = filtradoColaborativo[1].replace(" " + nombre, "");
                    break;
                case "marca":
                    filtradoColaborativo[2] = filtradoColaborativo[2].replace(" " + nombre, "");
                    break;
            }
            filtrar();
            recycler.getAdapter().notifyDataSetChanged();
        }

    }

    public void obtenerFiltros(){
        for (Prenda data : prendas) {
            if(!filtros.contains(data.getTipo())){
                filtros.add(data.getTipo());
            }
        }
    }

    public void obtenerMarcas(){
        for (Prenda data : prendas) {
            if(!marcas.contains(data.getMarca())){
                marcas.add(data.getMarca());
            }
        }
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
        recycler_filtros = view.findViewById(R.id.recyclerBotones);
        recycler_marcas = view.findViewById(R.id.recyclerMarcas);
        recycler_colores = view.findViewById(R.id.recyclerColores);
        searchView = view.findViewById(R.id.search);
        button = view.findViewById(R.id.button);
        recycler_filtros.setVisibility(View.INVISIBLE);
        recycler_filtros.setVisibility(View.GONE);
        recycler_marcas.setVisibility(View.INVISIBLE);
        recycler_marcas.setVisibility(View.GONE);
        recycler_colores.setVisibility(View.INVISIBLE);
        recycler_colores.setVisibility(View.GONE);
        recycler_filtros.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_marcas.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_colores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycler.setHasFixedSize(true);
    }

    private void filtrar(){
        searchList.clear();
        for(Prenda prenda: prendas){
            if(filtradoColaborativo[0].equals("") || prenda.getNombre().contains(filtradoColaborativo[0])){
                if(filtradoColaborativo[1].equals("") || filtradoColaborativo[1].contains(prenda.getTipo())){
                    if(filtradoColaborativo[2].equals("") || filtradoColaborativo[2].contains(prenda.getMarca())){
                        boolean color_esta = false;
                        if(filtradoColaborativo[3].equals(""))
                            color_esta = true;
                        else {
                            for (String color : prenda.getColores()) {
                                String aux = color.replace(" ", "");
                                if (filtradoColaborativo[3].contains(aux))
                                    color_esta = true;
                            }
                        }
                        if(color_esta){
                            searchList.add(prenda);
                        }
                    }
                }
            }
        }
    }
}