package com.example.ewardrobe.Prendas;

public class Prenda {
    private String color;
    private String tipo;
    private int id;
    private String nombre;


    public Prenda(String color, String tipo, int id, String nombre) {
        this.color = color;
        this.tipo = tipo;
        this.id = id;
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public String getTipo() {
        return tipo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Prenda{" +
                "color='" + color + '\'' +
                ", tipo='" + tipo + '\'' +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
