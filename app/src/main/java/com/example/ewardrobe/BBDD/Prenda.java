package com.example.ewardrobe.BBDD;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Prenda implements Parcelable {
    private ArrayList<String> colores;

    private ArrayList<String> caracteristicas;
    private String tipo;
    private String fotoURL;
    private String nombre;
    private String marca;




    public Prenda(ArrayList<String> colores, ArrayList<String> caracteristicas, String tipo, String foto, String nombre, String marca) {
        this.colores = colores;
        this.caracteristicas = caracteristicas;
        this.tipo = tipo;
        this.fotoURL = foto;
        this.nombre = nombre;
        this.marca = marca;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public ArrayList<String> getColores() {
        return colores;
    }

    public void setColores(ArrayList<String> colores) {
        this.colores = colores;
    }

    public ArrayList<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(ArrayList<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Prenda{" +
                "colores=" + colores +
                ", caracteristicas=" + caracteristicas +
                ", tipo='" + tipo + '\'' +
                ", foto=" + fotoURL +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(marca);
        parcel.writeString(nombre);
        parcel.writeString(tipo);
        parcel.writeString(fotoURL);
        parcel.writeStringList(colores);
        parcel.writeStringList(caracteristicas);
    }


    protected Prenda(Parcel in) {
        marca = in.readString();
        nombre = in.readString();
        tipo = in.readString();
        fotoURL = in.readString();
        colores = in.createStringArrayList();
        caracteristicas = in.createStringArrayList();
    }

    public static final Creator<Prenda> CREATOR = new Creator<Prenda>() {
        @Override
        public Prenda createFromParcel(Parcel in) {
            return new Prenda(in);
        }

        @Override
        public Prenda[] newArray(int size) {
            return new Prenda[size];
        }
    };

}
