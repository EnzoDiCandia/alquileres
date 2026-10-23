package alquileres.controller;

import alquileres.dao.CampoDAO;
import alquileres.modelo.CampoGasto;
import alquileres.modelo.CampoPago;
import alquileres.modelo.CampoPrecio;
import alquileres.utils.Validar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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

    /** Crea o actualiza el pago del período (anio + mes). */
    @PostMapping("/pagos")
    public CampoPago guardarPago(@RequestBody CampoPago p) throws SQLException {
        validarPeriodo(p.getAnio(), p.getMes());
        Validar.positivo(p.getKgHaAnual(), "Los kg de soja por hectárea");
        Validar.positivo(p.getHectareas(), "Las hectáreas");
        if (p.getPrecios() == null || p.getPrecios().isEmpty()) {
            throw new IllegalArgumentException("Cargá al menos un precio de pizarra");
        }
        if (p.getPrecios().size() > 10) {
            throw new IllegalArgumentException("Se pueden cargar hasta 10 días de pizarra");
        }
        for (CampoPrecio pr : p.getPrecios()) {
            Validar.requerido(pr.getFecha(), "fecha de cada precio");
            Validar.positivo(pr.getPrecio(), "Cada precio de pizarra");
        }
        dao.guardarPago(p);
        return dao.buscarPago(p.getAnio(), p.getMes());
    }

    @PutMapping("/pagos/{id}/cobrado")
    public boolean marcarCobrado(@PathVariable int id, @RequestBody Map<String, Boolean> body) throws SQLException {
        return dao.marcarCobrado(id, Boolean.TRUE.equals(body.get("cobrado")));
    }

    @DeleteMapping("/pagos/{id}")
    public boolean eliminarPago(@PathVariable int id) throws SQLException {
        return dao.eliminarPago(id);
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
