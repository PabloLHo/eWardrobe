package com.example.ewardrobe.BBDD;

public class Usuario {

    private String nombre;
    private String mail;
    private String pass;

    private String id;

    public Usuario(String nombre, String mail, String pass) {
        this.nombre = nombre;
        this.mail = mail;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
