package com.base_de_datos_I.Tienda.models;

public class Producto {

    private String codigo;

    private String nombre;

    private double precio;

    private Categoria categoria;

    public Producto(){
        categoria = new Categoria();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "{codigo=" + codigo + ", nombre=" + nombre + ", precio=" + precio + ", categoria=" + categoria
                + "}";
    }

}
