package com.base_de_datos_I.Tienda.service.impl;

import com.base_de_datos_I.Tienda.repository.VentaRepository;
import com.base_de_datos_I.Tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Transactional(readOnly = true)
    @Override
    public String generarCorrelativo() {
        int siguienteCorrelativo = ventaRepository.retornarCorrelativoActual() + 1;
        return String.format("%05d", siguienteCorrelativo);
    }

}
