package com.base_de_datos_I.Tienda.service;

import com.base_de_datos_I.Tienda.models.Venta;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface VentaService {

    Map<String,Object> generarCorrelativo();

    String generarVenta(Venta venta);

    Map<String, Object> generarJsonParaDashboard();

    String anularVenta(int codigoVenta);

    Optional<Venta> obtenerVenta(int codigoVenta);

    ResponseEntity<?> generarFactura(Venta venta);
}
