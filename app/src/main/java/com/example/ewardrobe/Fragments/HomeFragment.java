package com.example.ewardrobe.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ewardrobe.Screens.LoginScreen;
import com.example.ewardrobe.Screens.ProfileScreen;
import com.example.ewardrobe.R;
import com.example.ewardrobe.Screens.SettingsScreen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    ArrayList<SlideModel> imagenes;
    FragmentManager fragmentManager;

    String userID;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userID = getArguments().getString("userID");
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ImageSlider slider = view.findViewById(R.id.slider_image);

        crearBotones(view);
        imagenes = new ArrayList<>();


        slider.setImageList(imagenes);

        return view;
    }

    public void crearBotones(View v){
        CardView perfil = v.findViewById(R.id.PerfilCard);
        CardView prendas = v.findViewById(R.id.PrendasCard);
        CardView armario = v.findViewById(R.id.WardrobeCard);
        CardView outfit = v.findViewById(R.id.OutfitCard);
        CardView setting = v.findViewById(R.id.SettingCard);
        CardView logout = v.findViewById(R.id.LogoutCard);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor aux = getActivity().getSharedPreferences("com.example.ewardrobe.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit();
                aux.clear();
                aux.apply();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        prendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("userID", userID);
                fragmentManager = getActivity().getSupportFragmentManager();
                PrendasFragment prendas = new PrendasFragment();
                prendas.setArguments(args);
                openFragment(prendas);
            }
        });
        armario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("userID", userID);
                fragmentManager = getActivity().getSupportFragmentManager();
                WardrobeFragment wardrobeFragment = new WardrobeFragment();
                wardrobeFragment.setArguments(args);
                openFragment(wardrobeFragment);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        outfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("userID", userID);
                OutfitFragment outfit = new OutfitFragment();
                outfit.setArguments(args);
                fragmentManager = getActivity().getSupportFragmentManager();
                openFragment(outfit);
            }
        });
    }

    private void openFragment(Fragment frag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, frag);
        transaction.commit();
    }
}