package com.base_de_datos_I.Tienda.models;

public class DetalleVenta {

    private Long idDetalleVenta;

    private Producto producto;

    private double precioUnitario;

    private double cantidad;

    private double descuento;

    private double montoTotal;

    public DetalleVenta() {
        this.producto = new Producto();
    }

    public Long getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Long idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMonto(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    //getters para factura
    public String getCodigoProducto() {
        return producto != null ? producto.getCodigo(): "-----";
    }

    public String getNombreProducto() {
        return producto != null ? producto.getNombre(): "-----";
    }

  /*  public Double getCantidad() {
        return producto != null ? producto.getCodigo(): "-----";
    }*/


}
