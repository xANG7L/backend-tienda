package com.base_de_datos_I.Tienda.service;

import java.util.List;
import java.util.Optional;

import com.base_de_datos_I.Tienda.models.Cliente;

public interface ClienteService {

    Cliente crearCliente(Cliente cliente);

    List<Cliente> listarClientes();

    List<Cliente> listarClientesPorNombreCompleto(String filtro);

    boolean existeDuiEnLaBD(String dui);

    String generarCodigo();

    Optional<Cliente> buscarClientePorCodigo(String codigo);

    Optional<Cliente> actualizarCliente(Cliente cliente);

    Integer eliminarCliente(String codigo);

}
