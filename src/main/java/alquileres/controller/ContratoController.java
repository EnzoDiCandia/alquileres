package alquileres.controller;

import alquileres.dao.ContratoDAO;
import alquileres.modelo.Contrato;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contratos")
@CrossOrigin(origins = "*")
public class ContratoController {

    private ContratoDAO dao = new ContratoDAO();

    @GetMapping
    public List<Contrato> listarTodos() throws SQLException {
        return dao.listarContratos();
    }

    @GetMapping("/{id}")
    public Contrato buscarPorId(@PathVariable int id) throws SQLException {
        return dao.buscarContratoPorId(id);
    }

    @GetMapping("/inquilino/{idInquilino}")
    public List<Contrato> buscarPorInquilino(@PathVariable int idInquilino) throws SQLException {
        return dao.buscarContratosPorInquilino(idInquilino);
    }

    @GetMapping("/propiedad/{idPropiedad}")
    public List<Contrato> buscarPorPropiedad(@PathVariable int idPropiedad) throws SQLException {
        return dao.buscarContratosPorPropiedad(idPropiedad);
    }

    @PostMapping
    public boolean insertar(@RequestBody Contrato c) throws SQLException {
        return dao.insertarContrato(c);
    }

    @PutMapping("/{id}/desactivar")
    public boolean desactivar(@PathVariable int id) throws SQLException {
        return dao.desactivarContrato(id);
    }

    // ─── CONTRATOS PRÓXIMOS A AJUSTE ─────────────────────────────────────────────
    // GET /contratos/proximos-ajuste         → usa 30 días por defecto
    // GET /contratos/proximos-ajuste?dias=15 → configurable

    @GetMapping("/proximos-ajuste")
    public List<Contrato> proximosAjuste(
            @RequestParam(defaultValue = "30") int dias) throws SQLException {
        return dao.contratosProximosAjuste(dias);
    }

    // ─── REGISTRAR AJUSTE DE MONTO ───────────────────────────────────────────────
    // PUT /contratos/{id}/ajuste
    // Body: { "nuevoMonto": 150000, "fechaAjuste": "2025-06-01" }

    @PutMapping("/{id}/ajuste")
    public boolean registrarAjuste(
            @PathVariable int id,
            @RequestBody Map<String, String> body) throws SQLException {
        double nuevoMonto = Double.parseDouble(body.get("nuevoMonto"));
        LocalDate fechaAjuste = body.containsKey("fechaAjuste")
                ? LocalDate.parse(body.get("fechaAjuste"))
                : LocalDate.now();
        return dao.registrarAjuste(id, nuevoMonto, fechaAjuste);
    }

    // ─── CONFIGURAR PERIODICIDAD EN CONTRATO EXISTENTE ───────────────────────────
    // PUT /contratos/{id}/periodicidad
    // Body: { "periodicidadMeses": 3, "ultimoAjuste": "2025-03-01" }

    @PutMapping("/{id}/periodicidad")
    public boolean actualizarPeriodicidad(
            @PathVariable int id,
            @RequestBody Map<String, String> body) throws SQLException {
        int periodicidadMeses = Integer.parseInt(body.get("periodicidadMeses"));
        LocalDate ultimoAjuste = body.containsKey("ultimoAjuste")
                ? LocalDate.parse(body.get("ultimoAjuste"))
                : LocalDate.now();
        return dao.actualizarPeriodicidad(id, periodicidadMeses, ultimoAjuste);
    }
}
