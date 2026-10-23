package alquileres.controller;

import alquileres.dao.CampoDAO;
import alquileres.modelo.CampoEntrega;
import alquileres.modelo.CampoGasto;
import alquileres.modelo.CampoPago;
import alquileres.modelo.CampoPrecio;
import alquileres.utils.Validar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/campo")
public class CampoController {

    // Meses en que vence el pago del campo
    private static final Set<Integer> MESES_PAGO = Set.of(3, 6, 9, 12);

    private CampoDAO dao = new CampoDAO();

    // ─── PAGOS ───────────────────────────────────────────────────────────────────

    @GetMapping("/pagos")
    public List<CampoPago> listarPagos() throws SQLException {
        return dao.listarPagos();
    }

    /** Pago de un período; 204 (sin contenido) si todavía no se cargó. */
    @GetMapping("/pagos/{anio}/{mes}")
    public ResponseEntity<CampoPago> buscarPago(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        CampoPago p = dao.buscarPago(anio, mes);
        return p == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(p);
    }

    /** Guarda los kg/ha y las hectáreas del período. Los precios se guardan aparte, de a uno. */
    @PostMapping("/pagos")
    public CampoPago guardarPago(@RequestBody CampoPago p) throws SQLException {
        validarPeriodo(p.getAnio(), p.getMes());
        Validar.positivo(p.getKgHaAnual(), "Los kg de soja por hectárea");
        Validar.positivo(p.getHectareas(), "Las hectáreas");
        dao.guardarDatosPago(p);
        return dao.buscarPago(p.getAnio(), p.getMes());
    }

    @DeleteMapping("/pagos/{id}")
    public boolean eliminarPago(@PathVariable int id) throws SQLException {
        return dao.eliminarPago(id);
    }

    // ─── PRECIOS DE PIZARRA ──────────────────────────────────────────────────────

    /** Datos para agregar un precio: el período y los kg/ha y hectáreas por si el pago todavía no existe. */
    public static class PrecioNuevo {
        public Integer anio;
        public Integer mes;
        public Double kgHaAnual;
        public Double hectareas;
        public LocalDate fecha;
        public Double precio;
    }

    @PostMapping("/precios")
    public boolean agregarPrecio(@RequestBody PrecioNuevo n) throws SQLException {
        validarPeriodo(n.anio, n.mes);
        Validar.positivo(n.kgHaAnual, "Los kg de soja por hectárea");
        Validar.positivo(n.hectareas, "Las hectáreas");
        Validar.requerido(n.fecha, "fecha");
        Validar.positivo(n.precio, "El precio");

        CampoPago existente = dao.buscarPago(n.anio, n.mes);
        if (existente != null) {
            if (existente.getPrecios().size() >= 10) {
                throw new IllegalArgumentException("Ya hay 10 días de pizarra cargados en este período");
            }
            for (CampoPrecio pr : existente.getPrecios()) {
                if (pr.getFecha().equals(n.fecha)) {
                    throw new IllegalArgumentException("Ya hay un precio cargado para esa fecha");
                }
            }
        }
        CampoPrecio precio = new CampoPrecio();
        precio.setFecha(n.fecha);
        precio.setPrecio(n.precio);
        dao.agregarPrecio(n.anio, n.mes, n.kgHaAnual, n.hectareas, precio);
        return true;
    }

    @PutMapping("/precios/{id}")
    public boolean actualizarPrecio(@PathVariable int id, @RequestBody CampoPrecio precio) throws SQLException {
        Validar.requerido(precio.getFecha(), "fecha");
        Validar.positivo(precio.getPrecio(), "El precio");
        return dao.actualizarPrecio(id, precio);
    }

    @DeleteMapping("/precios/{id}")
    public boolean eliminarPrecio(@PathVariable int id) throws SQLException {
        return dao.eliminarPrecio(id);
    }

    // ─── ENTREGAS (pagos parciales recibidos) ────────────────────────────────────

    @GetMapping("/entregas/{anio}/{mes}")
    public List<CampoEntrega> listarEntregas(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.listarEntregas(anio, mes);
    }

    @PostMapping("/entregas")
    public boolean insertarEntrega(@RequestBody CampoEntrega e) throws SQLException {
        validarPeriodo(e.getAnio(), e.getMes());
        Validar.requerido(e.getFecha(), "fecha");
        Validar.positivo(e.getMonto(), "El monto");
        return dao.insertarEntrega(e);
    }

    @DeleteMapping("/entregas/{id}")
    public boolean eliminarEntrega(@PathVariable int id) throws SQLException {
        return dao.eliminarEntrega(id);
    }

    // ─── GASTOS ──────────────────────────────────────────────────────────────────

    @GetMapping("/gastos/{anio}/{mes}")
    public List<CampoGasto> listarGastos(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.listarGastos(anio, mes);
    }

    @PostMapping("/gastos")
    public boolean insertarGasto(@RequestBody CampoGasto g) throws SQLException {
        validarPeriodo(g.getAnio(), g.getMes());
        Validar.requerido(g.getConcepto(), "concepto");
        Validar.requerido(g.getFecha(), "fecha");
        Validar.positivo(g.getMonto(), "El monto");
        return dao.insertarGasto(g);
    }

    @DeleteMapping("/gastos/{id}")
    public boolean eliminarGasto(@PathVariable int id) throws SQLException {
        return dao.eliminarGasto(id);
    }

    private void validarPeriodo(Integer anio, Integer mes) {
        Validar.requerido(anio, "año");
        Validar.requerido(mes, "mes");
        if (!MESES_PAGO.contains(mes)) {
            throw new IllegalArgumentException("El período debe ser marzo, junio, septiembre o diciembre");
        }
    }
}
