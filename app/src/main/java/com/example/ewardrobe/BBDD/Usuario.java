package com.example.ewardrobe.BBDD;

public class Usuario {

    private String nombre;
    private String mail;
    private String pass;

    private String id;

    private String tlf;
    private String ubi;

    public Usuario(String nombre, String mail, String pass) {
        this.nombre = nombre;
        this.mail = mail;
        this.pass = pass;
        this.ubi = "N/A";
        this.tlf = "N/A";
    }

    public Usuario(String nombre, String mail, String pass, String ubi, String tlf) {
        this.nombre = nombre;
        this.mail = mail;
        this.pass = pass;
        this.ubi = ubi;
        this.tlf = tlf;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getUbi() {
        return ubi;
    }

    public void setUbi(String ubi) {
        this.ubi = ubi;
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
