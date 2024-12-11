package com.base_de_datos_I.Tienda.controller;

import com.base_de_datos_I.Tienda.dto.VentaDto;
import com.base_de_datos_I.Tienda.models.Venta;
import com.base_de_datos_I.Tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(originPatterns = { "*" })
@RestController
@RequestMapping("api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/generar-correlativo")
    public Map<String,Object> obtenerCorrelativo(){
        return ventaService.generarCorrelativo();
    }

    @PostMapping("/facturar")
    public ResponseEntity<?> generarVenta(@RequestBody Venta venta){
        return ResponseEntity.ok().body(Collections.singletonMap("mensaje",ventaService.generarVenta(venta)));
    }

    @GetMapping("/datos")
    public Map<String,Object> obtenerDatos(){
        return ventaService.generarJsonParaDashboard();
    }

    @GetMapping("/factura/{codigoVenta}")
    public ResponseEntity<?> obtenerFactura(@PathVariable int codigoVenta){
        Optional<Venta> optionalVenta = ventaService.obtenerVenta(codigoVenta);
        if(optionalVenta.isPresent()){
            return ResponseEntity.ok().body(optionalVenta.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje","Venta no registrada en nuestro sistema, error!"));
    }

    @GetMapping("/descargar-factura/{codigoVenta}")
    public ResponseEntity<?> descargarFactura(@PathVariable int codigoVenta){
        Optional<Venta> optionalVenta = ventaService.obtenerVenta(codigoVenta);
        if(optionalVenta.isPresent()){
            return ventaService.generarFactura(optionalVenta.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje","Venta no registrada en nuestro sistema, error!"));
    }

    @PutMapping("/anular/{codigoVenta}")
    public ResponseEntity<?> anularVenta(@PathVariable int codigoVenta){
        return ResponseEntity.ok().body(Collections.singletonMap("mensaje",ventaService.anularVenta(codigoVenta)));
    }

}
