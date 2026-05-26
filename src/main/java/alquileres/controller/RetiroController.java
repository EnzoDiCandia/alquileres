package alquileres.controller;

import alquileres.dao.RetiroDAO;
import alquileres.modelo.Retiro;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/retiros")
@CrossOrigin(origins = "*")
public class RetiroController {

    private RetiroDAO dao = new RetiroDAO();

    @GetMapping
    public List<Retiro> listarTodos() throws SQLException {
        return dao.listarRetiros();
    }

    @GetMapping("/{id}")
    public Retiro buscarPorId(@PathVariable int id) throws SQLException {
        return dao.buscarRetiroPorId(id);
    }

    @GetMapping("/socio/{idSocio}")
    public List<Retiro> buscarPorSocio(@PathVariable int idSocio) throws SQLException {
        return dao.buscarRetirosPorSocio(idSocio);
    }

    @PostMapping
    public boolean insertar(@RequestBody Retiro r) throws SQLException {
        return dao.insertarRetiro(r);
    }

    @GetMapping("/total/{anio}/{mes}")
    public double totalPorMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.totalRetirosPorMes(anio, mes);
    }

    @GetMapping("/socio/{idSocio}/{anio}/{mes}")
    public List<Retiro> retirosPorSocioYMes(
            @PathVariable int idSocio,
            @PathVariable int anio,
            @PathVariable int mes) throws SQLException {
        return dao.retirosPorSocioYMes(idSocio, anio, mes);
    }

    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable int id) throws SQLException {
        return dao.eliminarRetiro(id);
    }

    @GetMapping("/mes/{anio}/{mes}")
    public List<Retiro> retirosPorMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.retirosPorMes(anio, mes);
    }

    @GetMapping("/anio/{anio}")
    public List<Retiro> retirosPorAnio(@PathVariable int anio) throws SQLException {
        return dao.retirosPorAnio(anio);
    }
}