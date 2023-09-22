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

    RecyclerView recycler, recycler_acc, recycler_colores;
    PrendaAdapter prendaAdapter;
    List<Prenda> prendas;
    List<String> outfits;
    FiltroColorAdapter filtroColorAdapter;
    ArrayList<String> filtros;
    ArrayList<String> colores;
    DatabaseReference reference;
    FirebaseDatabase database;
    String userID;
    ImageView img, img2,img3, img4;



    public OutfitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_outfit, container, false);


        setRecycler(view);
        userID = getArguments().getString("userID");
        database = FirebaseDatabase.getInstance("https://ewardrobe-dcf0c-default-rtdb.europe-west1.firebasedatabase.app/");
        prendas = new ArrayList<>();

        img = view.findViewById(R.id.superior);
        img2 = view.findViewById(R.id.medio);
        img3 = view.findViewById(R.id.inferior);

        prendaAdapter = new PrendaAdapter(prendas, getContext());
        recycler.setAdapter(prendaAdapter);

        filtroColorAdapter = new FiltroColorAdapter(colores,getContext(), this);
        recycler_colores.setAdapter(filtroColorAdapter);

        obtenerPrendas();

        return view;
    }

    private void obtenerPrendas(){

        reference = database.getReference();
        Query query = reference.child("usuarios").child(userID);
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
        obtenerFiltros();
        obtenerColores();

        prendaAdapter = new PrendaAdapter(prendas, getContext());
        recycler.setAdapter(prendaAdapter);

        prendaAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prenda prenda = prendas.get(recycler.getChildAdapterPosition(view));
            }
        });

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

    public void setRecycler(View view){
        recycler = view.findViewById(R.id.recyclerView);
        recycler_colores = view.findViewById(R.id.recyclerColores);
        recycler_colores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recycler.setHasFixedSize(true);
    }

//    private void filtrar() {
//        searchList.clear();
//        for (Prenda prenda : prendas) {
//            if (filtradoColaborativo[0].equals("") || filtradoColaborativo[0].contains(prenda.getTipo())) {
//                boolean color_esta = false;
//                if (filtradoColaborativo[1].equals(""))
//                    color_esta = true;
//                else {
//                    for (String color : prenda.getColores()) {
//                        String aux = color.replace(" ", "");
//                        if (filtradoColaborativo[1].contains(aux))
//                            color_esta = true;
//                    }
//                }
//                if (color_esta) {
//                    searchList.add(prenda);
//                }
//            }
//        }
//    }
}