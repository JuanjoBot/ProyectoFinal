package com.example.tiendaapp;

public class Producto {
    public String id;
    public String nombre;
    public String descripcion;
    public String precio;
    public String calificacion;

    public Producto() {
    }

    public Producto(String id, String nombre, String descripción, String precio, String calificación) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripción;
        this.precio = precio;
        this.calificacion = calificación;
    }

    public String toString(){
        return this.nombre+" - $"+this.precio;
    }
}
