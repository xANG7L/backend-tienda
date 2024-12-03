package com.base_de_datos_I.Tienda.controller;

import com.base_de_datos_I.Tienda.models.Cliente;
import com.base_de_datos_I.Tienda.service.impl.ClienteServiceImpl;
import com.base_de_datos_I.Tienda.validations.ClienteValidation;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@CrossOrigin(originPatterns = { "*" })
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl clienteService;

    @Autowired
    private ClienteValidation clienteValidation;

    @GetMapping
    public String saludo() {
        return "Hola Kevin GOD";
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
        clienteValidation.validate(cliente, result);
        if (result.hasFieldErrors()) {
            return mensajeDeError(result);
        }
        return ResponseEntity.ok().body(clienteService.crearCliente(cliente));
    }

    @PutMapping("/actualizar/{codigo}")
    public ResponseEntity<?> actualizarCliente(@PathVariable String codigo,@RequestBody Cliente cliente) {
        Optional<Cliente> optionalCliente = clienteService.actualizarCliente(cliente);
        if (optionalCliente.isPresent()) {
            return ResponseEntity.ok().body(optionalCliente.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error","El cliente no esta registrado en la base de datos"));
    }

    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String codigo){
        if (clienteService.eliminarCliente(codigo) > 0) {
            return ResponseEntity.ok().body(Collections.singletonMap("mensaje","Cliente eliminado exitosamente de nuestro sistema"));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","No se pudo eliminar el cliente de la base de datos."));
    }

    @GetMapping("/listar")
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/filtrar/nombre-completo/{filtro}")
    public List<Cliente> listadoFiltradoDeClientes(@PathVariable String filtro) {
        return clienteService.listarClientesPorNombreCompleto(filtro);
    }
    

    @GetMapping("/generar-codigo")
    public Map<String, String> generarCodigo() {
        return Collections.singletonMap("codigo", clienteService.generarCodigo());
    }

    @GetMapping("/cliente/{codigo}")
    public ResponseEntity<?> buscarClientePorCodigo(@PathVariable String codigo) {
        Optional<Cliente> optionalCliente = clienteService.buscarClientePorCodigo(codigo);
        if (optionalCliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalCliente.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error","Cliente no registrado en nuestro sistema"));
    }

    private ResponseEntity<?> mensajeDeError(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);

    }

}