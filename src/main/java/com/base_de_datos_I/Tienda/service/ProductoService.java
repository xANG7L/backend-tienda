package com.base_de_datos_I.Tienda.service;

import java.util.List;
import java.util.Optional;

import com.base_de_datos_I.Tienda.models.Categoria;
import com.base_de_datos_I.Tienda.models.Producto;

public interface ProductoService {

    List<Categoria> listarTodasLasCategorias();

    Producto guardarProducto(Producto producto);

    Producto actualizarProducto(Producto producto);

    Optional<Producto> buscarProductoPorCodigo(String codigo);

    List<Producto> listarProductos();

    List<Producto> listarProductosPorNombreYCategoria(String filtro, Long coidgoCategoria);

    boolean existeProductoPorCodigo(String codigo);

}
