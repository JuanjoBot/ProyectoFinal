package com.example.tiendaapp;

import java.util.HashMap;

public class Cliente {
    public String id;
    public String codigo;
    public String nombre;
    public String usuario;
    public String nivel;
    public String puntos;
    public String restantes;
    public HashMap<String, Boolean> carrito;

    public Cliente() {

    }

    public Cliente(String id, String codigo, String nombre,String usuario, String nivel, String puntos, String restantes)  {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.usuario = usuario;
        this.nivel = nivel;
        this.puntos = puntos;
        this.restantes  = restantes;
        this.carrito = new HashMap<>();
    }
}
