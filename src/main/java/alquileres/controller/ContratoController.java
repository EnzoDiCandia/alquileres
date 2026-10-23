package alquileres.controller;

import alquileres.dao.ContratoDAO;
import alquileres.modelo.Contrato;
import alquileres.service.StorageService;
import alquileres.utils.Validar;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private ContratoDAO dao = new ContratoDAO();
    private final StorageService storageService;

    // Spring inyecta el StorageService (con la ruta configurada). Ya no se usa "new StorageService()".
    public ContratoController(StorageService storageService) {
        this.storageService = storageService;
    }

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
        validarContrato(c);
        return dao.insertarContrato(c);
    }

    @PutMapping("/{id}/desactivar")
    public boolean desactivar(@PathVariable int id) throws SQLException {
        return dao.desactivarContrato(id);
    }

    @GetMapping("/proximos-ajuste")
    public List<Contrato> proximosAjuste(
            @RequestParam(defaultValue = "30") int dias) throws SQLException {
        return dao.contratosProximosAjuste(dias);
    }

    // Monto que correspondía cobrar en un mes (solo contratos ajustados después de ese mes)
    @GetMapping("/montos/{anio}/{mes}")
    public Map<Integer, Double> montosDelMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.montosDelMes(anio, mes);
    }

    @PutMapping("/{id}/ajuste")
    public boolean registrarAjuste(
            @PathVariable int id,
            @RequestBody Map<String, String> body) throws SQLException {
        Validar.requerido(body.get("nuevoMonto"), "nuevo monto");
        double nuevoMonto = Double.parseDouble(body.get("nuevoMonto"));
        Validar.positivo(nuevoMonto, "El nuevo monto");
        LocalDate fechaAjuste = body.containsKey("fechaAjuste")
                ? LocalDate.parse(body.get("fechaAjuste"))
                : LocalDate.now();
        return dao.registrarAjuste(id, nuevoMonto, fechaAjuste);
    }

    @PutMapping("/{id}/periodicidad")
    public boolean actualizarPeriodicidad(
            @PathVariable int id,
            @RequestBody Map<String, String> body) throws SQLException {
        Validar.requerido(body.get("periodicidadMeses"), "periodicidad");
        int periodicidadMeses = Integer.parseInt(body.get("periodicidadMeses"));
        Validar.positivo(periodicidadMeses, "La periodicidad");
        LocalDate ultimoAjuste = body.containsKey("ultimoAjuste")
                ? LocalDate.parse(body.get("ultimoAjuste"))
                : LocalDate.now();
        return dao.actualizarPeriodicidad(id, periodicidadMeses, ultimoAjuste);
    }

    // ─── SUBIR ARCHIVO ───────────────────────────────────────────────────────────
    // Se guarda en el volume y en la base queda solo el nombre del archivo.

    @PostMapping("/{id}/upload")
    public Map<String, Object> subirArchivo(
            @PathVariable int id,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            String nombre = storageService.subirArchivo(archivo, id);
            dao.actualizarRutaPdf(id, nombre);
            return Map.of("ok", true, "url", "/contratos/" + id + "/archivo");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "mensaje", String.valueOf(e.getMessage()));
        }
    }

    // ─── VER / DESCARGAR ARCHIVO ─────────────────────────────────────────────────

    @GetMapping("/{id}/archivo")
    public ResponseEntity<Resource> verArchivo(@PathVariable int id) throws SQLException {
        Contrato c = dao.buscarContratoPorId(id);
        if (c == null || c.getArchivoPdfRuta() == null) {
            return ResponseEntity.notFound().build();
        }
        Path ruta = storageService.rutaDe(c.getArchivoPdfRuta());
        if (!Files.exists(ruta)) {
            return ResponseEntity.notFound().build();
        }
        String nombre = ruta.getFileName().toString();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(storageService.tipoContenido(nombre)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombre + "\"")
                .body(new FileSystemResource(ruta));
    }

    // ─── ELIMINAR ARCHIVO ────────────────────────────────────────────────────────

    @DeleteMapping("/{id}/archivo")
    public Map<String, Object> eliminarArchivo(@PathVariable int id) {
        try {
            Contrato c = dao.buscarContratoPorId(id);
            if (c == null || c.getArchivoPdfRuta() == null) {
                return Map.of("ok", false, "mensaje", "No hay archivo para este contrato");
            }
            storageService.eliminarArchivo(c.getArchivoPdfRuta());
            dao.actualizarRutaPdf(id, null);
            return Map.of("ok", true);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "mensaje", String.valueOf(e.getMessage()));
        }
    }

    @PutMapping("/{id}/activar")
    public boolean activar(@PathVariable int id) throws SQLException {
        return dao.activarContrato(id);
    }

    @PutMapping("/{id}")
    public boolean actualizar(@PathVariable int id, @RequestBody Contrato c) throws SQLException {
        c.setId(id);
        validarContrato(c);
        return dao.actualizarContrato(c);
    }

    private void validarContrato(Contrato c) {
        Validar.requerido(c.getIdPropiedad(), "propiedad");
        Validar.requerido(c.getIdInquilino(), "inquilino");
        Validar.requerido(c.getFechaInicio(), "fecha de inicio");
        Validar.requerido(c.getFechaFin(), "fecha de fin");
        if (!c.getFechaFin().isAfter(c.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        Validar.positivo(c.getMontoBase(), "El monto");
        if (c.getDeposito() < 0) {
            throw new IllegalArgumentException("El depósito no puede ser negativo");
        }
    }
}
