package com.base_de_datos_I.Tienda.repository;

import com.base_de_datos_I.Tienda.models.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClienteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public int crearCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (ClienteID, dui,nombreC,apellidoC, telefono, casa, calle, ciudad) VALUES (?,?,?,?,?,?,?,?)";
        // KeyHolder keyHolder = new GeneratedKeyHolder(); PARA OBTENER EL ID GENERADO
        // EN TABLAS CON CAMPOS IDENTITYS
        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getCodigo());
            ps.setString(2, cliente.getDui());
            ps.setString(3, cliente.getNombre());
            ps.setString(4, cliente.getApellido());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getCasa());
            ps.setString(7, cliente.getCalle());
            ps.setString(8, cliente.getCiudad());
            return ps;
        });

    }

    @Transactional
    public int actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombreC = ?, apellidoC = ?, telefono = ?, casa = ?, calle = ?, ciudad = ? WHERE ClienteID = ?";
        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, 0);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCasa());
            ps.setString(5, cliente.getCalle());
            ps.setString(6, cliente.getCiudad());
            ps.setString(7, cliente.getCodigo());
            return ps;
        });
    }

    @Transactional
    public int eliminarClientePorId(String codigo) {
        String sql = "DELETE FROM clientes WHERE ClienteID = ?";
        int filasAfectadas = jdbcTemplate.update(sql, codigo);
        return filasAfectadas > 0 ? 1 : -1; // 1 si se eliminó, -1 si no se encontró.
    }

    @Transactional
    public Optional<Cliente> findByCodigo(String codigo) {
        try {
            String sql = "SELECT ClienteID, dui, nombrec, apellidoc, telefono, casa, calle, ciudad FROM clientes WHERE ClienteID = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, new ClienteRowMapper(), codigo));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        String sql = "SELECT ClienteID, dui, nombrec, apellidoc, telefono, casa, calle, ciudad FROM clientes";
        return jdbcTemplate.query(sql, new ClienteRowMapper());
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientesPorNombreCompleto(String filtro) {
        String sql = "SELECT ClienteID, dui, nombrec, apellidoc, telefono, casa, calle, ciudad " +
             "FROM clientes " +
             "WHERE CONCAT(nombrec, ' ', ApellidoC) LIKE ?";
        return jdbcTemplate.query(sql, new ClienteRowMapper(), filtro);
    }

    @Transactional(readOnly = true)
    public boolean existePorDui(String dui) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE dui = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dui);
        return count != null && count > 0;
    }

    @Transactional(readOnly = true)
    public int cantidadClientes() {
        String sql = "SELECT COUNT(*) FROM clientes";
        Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class);
        return cantidad != null ? cantidad : 0;
    }

    private static class ClienteRowMapper implements RowMapper<Cliente> {

        @Override
        public Cliente mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
            Cliente cliente = new Cliente();
            cliente.setCodigo(rs.getString("ClienteID"));
            cliente.setDui(rs.getString("dui"));
            cliente.setNombre(rs.getString("nombrec"));
            cliente.setApellido(rs.getString("apellidoc"));
            cliente.setCalle(rs.getString("calle"));
            cliente.setCiudad(rs.getString("ciudad"));
            cliente.setTelefono(rs.getString("telefono"));
            cliente.setCasa(rs.getString("casa"));
            return cliente;
        }

    }

}
