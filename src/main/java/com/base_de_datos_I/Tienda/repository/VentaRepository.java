package com.base_de_datos_I.Tienda.repository;

import com.base_de_datos_I.Tienda.dto.VentaDto;
import com.base_de_datos_I.Tienda.models.DetalleVenta;
import com.base_de_datos_I.Tienda.models.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class VentaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int crearVenta(Venta venta) {
        String sql = "INSERT INTO ventas (correlativo, FechaVenta, hora, ClienteID, SubTotal, Iva, Total, estado, Cod_venta)\n" +
                "\tVALUES (?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, venta.getCorrelativo());
           // Date fechaVenta = Date.valueOf(venta.getFechaVenta());
            ps.setDate(2,Date.valueOf(venta.getFechaVenta()));
            ps.setTime(3, Time.valueOf(venta.getHoraVenta()));
            ps.setString(4,venta.getCliente().getCodigo());
            ps.setDouble(5,venta.getSubTotal());
            ps.setDouble(6,venta.getIva());
            ps.setDouble(7,venta.getTotal());
            ps.setBoolean(8,true);
            ps.setInt(9,venta.getCodigoVenta());
            return ps;
        });
    }

    public void insertarDetalleDeVenta(List<DetalleVenta> listaProductos, int idVenta) {
        for (DetalleVenta venta : listaProductos) {
            String sql = "INSERT INTO Detalle_ventas(Cod_Venta,CodProducto,PrecioUnitario,Cantidad,Monto)\n" +
                    "\tVALUES (?,?,?,?,?)";
            jdbcTemplate.update(sql, idVenta,venta.getProducto().getCodigo(),venta.getProducto().getPrecio(),venta.getCantidad(),venta.getMontoTotal());
        }
    }

    public void aularVenta(int codigoVenta){
        String sql = "UPDATE ventas set estado=0 where Cod_Venta=?";
        jdbcTemplate.update(sql,codigoVenta);
    }

    public Optional<Venta> obtenerEncabezado(int idVenta) {
        try{
            String sql = "SELECT v.Cod_Venta, v.correlativo, v.FechaVenta, v.hora, v.SubTotal,v.IVA,v.Total,c.ClienteID ,c.dui, c.NombreC, c.ApellidoC, c.Telefono, c.Calle, c.Casa, c.Ciudad\n" +
                    "\tFROM Ventas as v INNER JOIN clientes as c on v.ClienteID = c.ClienteID WHERE v.Cod_Venta = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, new VentaRowMapperHead(),idVenta));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public List<DetalleVenta> obtenerDetalleVenta(int idVenta) {
        String sql = "  SELECT dv.VentaID, dv.CodProducto, p.NombreProducto, p.Precio, p.CodCategoria, c.NombreCategoria, c.Descripcion, dv.PrecioUnitario, dv.Cantidad, dv.monto\n" +
                "  from Detalle_ventas as dv inner join Productos as p on dv.CodProducto = p.CodProducto inner join Categorias as c on p.CodCategoria = c.CodCategoria\n" +
                "  WHERE dv.Cod_Venta = ?";
        return jdbcTemplate.query(sql, new DetalleVentaRowMapper(),idVenta);
    }


    public int retornarCorrelativoActual() {
        String sql = "SELECT COUNT(*) FROM ventas";
        Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class);
        return cantidad != null ? cantidad : 0;
    }

    public List<VentaDto> obtenerVentas() {
        String sql = "  SELECT top(10) v.Cod_Venta, v.correlativo, v.FechaVenta, c.dui, c.NombreC, c.ApellidoC, v.Total from Ventas as v \n" +
                "\tinner join Clientes as C on v.ClienteID = c.ClienteID WHERE v.estado = 1 order by v.correlativo DESC";
        return jdbcTemplate.query(sql,new VentaRowMapper());
    }

    public int cantidadVentas() {
        String sql = "SELECT COUNT(*) FROM Ventas";
        Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class);
        return cantidad != null ? cantidad : 0;
    }

    public static class VentaRowMapper implements RowMapper<VentaDto> {
        @Override
        public VentaDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            VentaDto venta = new VentaDto();
            venta.setCodigoVenta(rs.getInt("Cod_Venta"));
            venta.setCorrelativo(rs.getString("correlativo"));
            venta.setFechaVenta(rs.getDate("FechaVenta").toLocalDate());
            venta.setTotal(rs.getDouble("Total"));
            //venta.getCliente().setCodigo(rs.getString("ClienteID"));
            venta.getCliente().setNombre(rs.getString("NombreC"));
            venta.getCliente().setDui(rs.getString("dui"));
            venta.getCliente().setApellido(rs.getString("apellidoc"));
            return venta;
        }
    }

    public static class VentaRowMapperHead implements RowMapper<Venta> {

        @Override
        public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
            Venta venta = new Venta();
            venta.setCodigoVenta(rs.getInt("Cod_Venta"));
            venta.setCorrelativo(rs.getString("correlativo"));
            venta.setFechaVenta(rs.getDate("FechaVenta").toLocalDate());
            venta.setSubTotal(rs.getDouble("SubTotal"));
            venta.setIva(rs.getDouble("IVA"));
            venta.setTotal(rs.getDouble("Total"));
            venta.getCliente().setCodigo(rs.getString("ClienteID"));
            venta.getCliente().setNombre(rs.getString("NombreC"));
            venta.getCliente().setDui(rs.getString("dui"));
            venta.getCliente().setApellido(rs.getString("apellidoc"));
            venta.getCliente().setTelefono(rs.getString("Telefono"));
            venta.getCliente().setCalle(rs.getString("Calle"));
            venta.getCliente().setCasa(rs.getString("Casa"));
            venta.getCliente().setCiudad(rs.getString("Ciudad"));
            return venta;
        }
    }

   public static class DetalleVentaRowMapper implements RowMapper<DetalleVenta> {
        @Override
        public DetalleVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setIdDetalleVenta(rs.getLong("VentaID"));
            detalleVenta.getProducto().setCodigo(rs.getString("CodProducto"));
            detalleVenta.getProducto().setNombre(rs.getString("NombreProducto"));
            detalleVenta.getProducto().setPrecio(rs.getDouble("Precio"));
            detalleVenta.getProducto().getCategoria().setId(rs.getInt("CodCategoria"));
            detalleVenta.getProducto().getCategoria().setNombre(rs.getString("NombreCategoria"));
            detalleVenta.getProducto().getCategoria().setDescripcion(rs.getString("Descripcion"));
            detalleVenta.setCantidad(rs.getDouble("Cantidad"));
            detalleVenta.setMonto(rs.getDouble("Monto"));
            detalleVenta.setPrecioUnitario(rs.getDouble("PrecioUnitario"));
            return detalleVenta;
        }
    }

}
