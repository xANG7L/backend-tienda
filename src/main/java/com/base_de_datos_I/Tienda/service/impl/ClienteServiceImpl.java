package com.base_de_datos_I.Tienda.service.impl;

import com.base_de_datos_I.Tienda.models.Cliente;
import com.base_de_datos_I.Tienda.repository.ClienteRepository;
import com.base_de_datos_I.Tienda.service.ClienteService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente crearCliente(Cliente cliente) {
        clienteRepository.crearCliente(cliente);
        return clienteRepository.findByCodigo(cliente.getCodigo()).orElseThrow();
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.listarClientes();
    }

    @Override
    public boolean existeDuiEnLaBD(String dui) {
        return clienteRepository.existePorDui(dui);
    }

    @Override
    public String generarCodigo() {
        int numeroCodigo = clienteRepository.cantidadClientes() + 1;
        String numeroFormateado = String.format("%07d", numeroCodigo);
        return "CL-" + numeroFormateado;
    }

    @Override
    public Optional<Cliente> buscarClientePorCodigo(String codigo) {
        return clienteRepository.findByCodigo(codigo);
    }

    @Override
    public Optional<Cliente> actualizarCliente(Cliente cliente) {
        Optional<Cliente> optionalClienteDb = clienteRepository.findByCodigo(cliente.getCodigo());
        if (optionalClienteDb.isPresent()) {
            Cliente clienteDb = optionalClienteDb.orElseThrow();
            clienteDb.setNombre(cliente.getNombre().trim());
            clienteDb.setApellido(cliente.getApellido().trim());
            clienteDb.setCalle(cliente.getCalle().trim());
            clienteDb.setCasa(cliente.getCasa().trim());
            clienteDb.setCiudad(cliente.getCiudad().trim());
            clienteDb.setTelefono(cliente.getTelefono().trim());

            clienteRepository.actualizarCliente(cliente);
            return Optional.of(clienteDb);
        }
        return Optional.empty();
    }

    @Override
    public Integer eliminarCliente(String codigo) {
        return clienteRepository.eliminarClientePorId(codigo);
    }

    @Override
    public List<Cliente> listarClientesPorNombreCompleto(String filtro) {
        return clienteRepository.listarClientesPorNombreCompleto("%" + filtro + "%");
    }

}
