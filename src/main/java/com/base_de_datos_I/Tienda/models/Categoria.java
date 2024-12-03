package com.base_de_datos_I.Tienda.models;

public class Categoria {

    private int id;

    private String nombre;

    private String descripcion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + "}";
    }

    
    
}
