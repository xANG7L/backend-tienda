package com.base_de_datos_I.Tienda.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base_de_datos_I.Tienda.models.Categoria;
import com.base_de_datos_I.Tienda.models.Producto;
import com.base_de_datos_I.Tienda.repository.ProductoRepository;
import com.base_de_datos_I.Tienda.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodasLasCategorias() {
        return productoRepository.listarCategorias();
    }

    @Override
    @Transactional
    public Producto guardarProducto(Producto producto) {
        productoRepository.crearProducto(producto);
        return productoRepository.buscarProductoPorCodigo(producto.getCodigo()).orElseThrow();
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        productoRepository.actualizarProducto(producto);
        return productoRepository.buscarProductoPorCodigo(producto.getCodigo()).orElseThrow();
    }

    @Override
    public Optional<Producto> buscarProductoPorCodigo(String codigo) {
        return productoRepository.buscarProductoPorCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepository.listarProductos();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProductoPorCodigo(String codigo) {
        return productoRepository.existeProductoPorCategoria(codigo) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductosPorNombreYCategoria(String filtro, Long coidgoCategoria) {
        return productoRepository.filtrarProductosPorNombreYCategoria("%" + filtro + "%", coidgoCategoria);
    }

}
