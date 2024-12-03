package com.base_de_datos_I.Tienda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.base_de_datos_I.Tienda.models.Categoria;
import com.base_de_datos_I.Tienda.models.Producto;
import com.base_de_datos_I.Tienda.service.ProductoService;
import com.base_de_datos_I.Tienda.validations.ProductoValidation;

import jakarta.validation.Valid;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@CrossOrigin(originPatterns = { "*" })
@RestController
@RequestMapping("api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoValidation productoValidation;

    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto, BindingResult result) {
        productoValidation.validate(producto, result);
        if (result.hasFieldErrors()) {
            return mensajeDeError(result);
        }
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @PutMapping("/actualizar/{codigo}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        Optional<Producto> productoOptional = productoService.buscarProductoPorCodigo(codigo);
        if (productoOptional.isPresent()) {
            return ResponseEntity.ok(productoService.actualizarProducto(producto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Producto no existente en la base de datos"));
    }

    @GetMapping("/listar")
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/filtro/{codigoCategoria}/{filtro}")
    public List<Producto> listarProductosFiltradosPorNombreYCategoria(@PathVariable Long codigoCategoria,
            @PathVariable String filtro) {
        return productoService.listarProductosPorNombreYCategoria(filtro, codigoCategoria);
    }

    @GetMapping("/ver/{codigo}")
    public ResponseEntity<?> verProductoPorCodigo(@PathVariable String codigo) {
        Optional<Producto> optionalProducto = productoService.buscarProductoPorCodigo(codigo);
        if (optionalProducto.isPresent()) {
            return ResponseEntity.ok(optionalProducto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Producto no existente en la base de datos"));
    }

    @GetMapping("/categorias/listar")
    public List<Categoria> getCategorias() {
        return productoService.listarTodasLasCategorias();
    }

    private ResponseEntity<?> mensajeDeError(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);

    }
}
