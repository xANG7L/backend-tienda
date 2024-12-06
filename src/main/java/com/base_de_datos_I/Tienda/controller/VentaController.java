package com.base_de_datos_I.Tienda.controller;

import com.base_de_datos_I.Tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@CrossOrigin(originPatterns = { "*" })
@RestController
@RequestMapping("api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/generar-correlativo")
    public Map<String,String> obtenerCorrelativo(){
        return Collections.singletonMap("correlativo",ventaService.generarCorrelativo() ) ;
    }
}
