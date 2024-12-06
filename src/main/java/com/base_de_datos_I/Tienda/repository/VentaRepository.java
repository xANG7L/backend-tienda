package com.base_de_datos_I.Tienda.repository;

import com.base_de_datos_I.Tienda.models.DetalleVenta;
import com.base_de_datos_I.Tienda.models.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class VentaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int retornarCorrelativoActual() {
        String sql = "SELECT COUNT(*) FROM ventas";
        Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class);
        return cantidad != null ? cantidad : 0;
    }

    public static class VentaRowMapper implements RowMapper<Venta> {
        @Override
        public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }

    public static class DetalleVentaRowMapper implements RowMapper<DetalleVenta> {
        @Override
        public DetalleVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }

}
