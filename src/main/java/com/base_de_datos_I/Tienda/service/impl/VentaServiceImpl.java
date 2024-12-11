package com.base_de_datos_I.Tienda.service.impl;

import com.base_de_datos_I.Tienda.dto.VentaDto;
import com.base_de_datos_I.Tienda.models.Venta;
import com.base_de_datos_I.Tienda.repository.ClienteRepository;
import com.base_de_datos_I.Tienda.repository.ProductoRepository;
import com.base_de_datos_I.Tienda.repository.VentaRepository;
import com.base_de_datos_I.Tienda.service.VentaService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.repo.InputStreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    @Override
    public Map<String,Object> generarCorrelativo() {
        Map<String,Object> map = new HashMap<>();
        int siguienteCorrelativo = ventaRepository.retornarCorrelativoActual() + 1;
        map.put("correlativo", String.format("%05d", siguienteCorrelativo));
        map.put("codigo",siguienteCorrelativo);
        return map;
    }

    @Transactional
    @Override
    public String generarVenta(Venta venta) {
        ventaRepository.crearVenta(venta);
        ventaRepository.insertarDetalleDeVenta(venta.getDetalleVenta(), venta.getCodigoVenta());
        return "Venta facturada";
    }

    @Override
    public Map<String, Object> generarJsonParaDashboard() {
        Map<String, Object> map = new HashMap<>();
        List<VentaDto> ventas = ventaRepository.obtenerVentas();
        int cantidadVentas = ventaRepository.cantidadVentas();
        int cantidadClientes = clienteRepository.cantidadClientes();
        int cantidadProductos = productoRepository.cantidadProductos();

        map.put("ventas", ventas);
        map.put("cantidadVentas", cantidadVentas);
        map.put("cantidadClientes", cantidadClientes);
        map.put("cantidadProductos", cantidadProductos);
        return map;
    }

    @Override
    public String anularVenta(int codigoVenta) {
        ventaRepository.aularVenta(codigoVenta);
        return "Venta anulada!!";
    }

    @Override
    public Optional<Venta> obtenerVenta(int codigoVenta) {
        Optional<Venta> optionalVenta = ventaRepository.obtenerEncabezado(codigoVenta);
        if (optionalVenta.isPresent()) {
            Venta venta = optionalVenta.get();
            venta.setDetalleVenta(ventaRepository.obtenerDetalleVenta(codigoVenta));
            return Optional.of(venta);
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<?> generarFactura(Venta venta) {
        try {
            //archivo donde esta ubicado la factura
            final InputStream archivoJasper = getClass().getResourceAsStream("/reports/factura_tienda.jasper");
            // creamos una instancia del jasper report
            final JasperReport comprobante = (JasperReport) JRLoader.loadObject(archivoJasper);
            // parametros del jasper report
            final Map<String, Object> parametros = new HashMap<>();
            parametros.put("correlativo", venta.getCorrelativo());
            parametros.put("codigoCliente", venta.getCliente().getCodigo());
            parametros.put("nombre", venta.getCliente().getNombre() + " " + venta.getCliente().getApellido());
            parametros.put("duiCliente",venta.getCliente().getDui());
            parametros.put("telefonoCliente",venta.getCliente().getTelefono());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            parametros.put("fechaVentaStr", venta.getFechaVenta().format(dtf));
            parametros.put("calle", venta.getCliente().getCalle());
            parametros.put("casa", venta.getCliente().getCasa());
            parametros.put("ciudad", venta.getCliente().getCiudad());
            parametros.put("total",venta.getTotal());
            // ds
//
//            System.out.println(venta.getDetalleVenta().size());

            JRBeanArrayDataSource dsDetalleVenta = new JRBeanArrayDataSource(venta.getDetalleVenta().toArray());
            parametros.put("dsDetalleVenta", dsDetalleVenta);

            JasperPrint jprint = JasperFillManager.fillReport(comprobante, parametros, new JREmptyDataSource());

            byte[] reporte = JasperExportManager.exportReportToPdf(jprint);

            //InputStreamResource inputStreamResource = new InputStreamResource();

            StringBuilder sb = new StringBuilder();
            sb.append("Factura_").append(venta.getCorrelativo());
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(sb.append(".pdf").toString()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            return ResponseEntity.ok().contentLength((long) reporte.length).contentType(MediaType.APPLICATION_PDF)
                    .headers(headers).body(new ByteArrayResource(reporte));


        } catch (JRException e) {
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Collections.singletonMap("error", "Error al generar el comprobante"));
    }


}
