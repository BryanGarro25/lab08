package com.example.lab08.LogicaNegocio;
import android.graphics.Bitmap;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String correo;
    private String contraseña;
    private String nombre;
    private String cedula;
    private Bitmap foto;
    String telefono;
    public Usuario() {
        this.correo = "none";
        this.contraseña = "none";
        this.nombre = "none";
        this.cedula = "none";
        this.foto = null;
    }
    public Usuario(String contraseña, String nombre,String correo,String cedula, Bitmap foto, String telefono) {
        this.correo = correo;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.cedula = cedula;
        this.foto = foto;
        this.telefono = telefono;

    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombre() {return nombre; }

    public String getCedula() { return cedula; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setCedula(String cedula) { this.cedula = cedula; }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return cedula;
        /*
        return "Curso{" +
                "nombre='" + nombre + ''' +
                ", codigo='" + codigo + ''' +
                ", creditos=" + creditos +
                ", horas=" + horas +
                '}';
                */
    }
}
