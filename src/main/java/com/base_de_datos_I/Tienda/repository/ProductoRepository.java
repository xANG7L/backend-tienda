package com.base_de_datos_I.Tienda.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.base_de_datos_I.Tienda.models.Categoria;
import com.base_de_datos_I.Tienda.models.Producto;

@Repository
public class ProductoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void crearProducto(Producto producto) {
        String sql = "INSERT INTO productos (CodProducto, NombreProducto,Precio,CodCategoria) VALUES (?,?,?,?)";
        // System.out.println(producto);
        jdbcTemplate.update(sql, producto.getCodigo(), producto.getNombre(), producto.getPrecio(),
                producto.getCategoria().getId());
    }

    public void actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET NombreProducto=?, Precio=?, CodCategoria=? WHERE CodProducto=?";
        // System.out.println(producto);
        jdbcTemplate.update(sql, producto.getNombre(), producto.getPrecio(),
                producto.getCategoria().getId(), producto.getCodigo());
    }

    public Optional<Producto> buscarProductoPorCodigo(String codigo) {
        try {
            String sql = "select p.CodProducto, p.NombreProducto, p.Precio, c.CodCategoria,c.NombreCategoria, c.Descripcion from Productos as p \n"
                    + "inner join Categorias AS c ON p.CodCategoria = c.CodCategoria where p.CodProducto = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, new ProductoRowMapper(), codigo));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Producto> listarProductos() {
        String sql = "select p.CodProducto, p.NombreProducto, p.Precio, c.CodCategoria,c.NombreCategoria, c.Descripcion from Productos as p \n"
                + "inner join Categorias AS c ON p.CodCategoria = c.CodCategoria ORDER BY c.CodCategoria";
        return jdbcTemplate.query(sql, new ProductoRowMapper());
    }

    public List<Producto> filtrarProductosPorNombreYCategoria(String filtro, Long codigoCategoria) {
        String sql = "select p.CodProducto, p.NombreProducto, p.Precio, c.CodCategoria,c.NombreCategoria, c.Descripcion from Productos as p \n"
                + "inner join Categorias AS c ON p.CodCategoria = c.CodCategoria WHERE p.NombreProducto like ? and c.CodCategoria = ?";
        return jdbcTemplate.query(sql, new ProductoRowMapper(), filtro, codigoCategoria);
    }

    public List<Categoria> listarCategorias() {
        String sql = "select CodCategoria,NombreCategoria, Descripcion from Categorias";
        return jdbcTemplate.query(sql, new CategoriasRowMapper());
    }

    public int cantidadProductos() {
        String sql = "SELECT COUNT(*) FROM Productos";
        Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class);
        return cantidad != null ? cantidad : 0;
    }

    public int existeProductoPorCategoria(String codigo) {
        String sql = "SELECT COUNT(*) FROM Productos where CodProducto = ?";
        Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class, codigo);
        // System.out.println(cantidad);
        return cantidad != null ? cantidad : 0;
    }

    private static class ProductoRowMapper implements RowMapper<Producto> {

        @Override
        public Producto mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
            Producto producto = new Producto();
            producto.setCodigo(rs.getString("CodProducto"));
            producto.setNombre(rs.getString("NombreProducto"));
            producto.setPrecio(rs.getDouble("Precio"));
            producto.getCategoria().setId(rs.getInt("CodCategoria"));
            producto.getCategoria().setNombre(rs.getString("NombreCategoria"));
            producto.getCategoria().setDescripcion(rs.getString("Descripcion"));
            return producto;
        }

    }

    private static class CategoriasRowMapper implements RowMapper<Categoria> {

        @Override
        @Nullable
        public Categoria mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("CodCategoria"));
            categoria.setNombre(rs.getString("NombreCategoria"));
            categoria.setDescripcion(rs.getString("Descripcion"));
            return categoria;
        }

    }

}
