package alquileres.controller;

import alquileres.dao.MovimientoDAO;
import alquileres.dao.RetiroDAO;
import alquileres.modelo.Movimiento;
import alquileres.modelo.ResumenMensual;
import alquileres.dao.RetiroDAO;
import alquileres.modelo.ResumenMensual;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {

    private MovimientoDAO dao = new MovimientoDAO();

    @GetMapping
    public List<Movimiento> listarTodos() throws SQLException {
        return dao.listarMovimientos();
    }

    @GetMapping("/{id}")
    public Movimiento buscarPorId(@PathVariable int id) throws SQLException {
        return dao.buscarMovimientoPorId(id);
    }

    @GetMapping("/contrato/{idContrato}")
    public List<Movimiento> buscarPorContrato(@PathVariable int idContrato) throws SQLException {
        return dao.buscarMovimientosPorContrato(idContrato);
    }

    @PostMapping
    public boolean insertar(@RequestBody Movimiento m) throws SQLException {
        return dao.insertarMovimiento(m);
    }

    @GetMapping("/total/{anio}/{mes}")
    public double totalPorMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.totalIngresosPorMes(anio, mes);
    }

    @GetMapping("/resumen/{anio}/{mes}")
    public ResumenMensual resumenMensual(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        double ingresos = dao.totalIngresosPorMes(anio, mes);
        RetiroDAO retiroDAO = new RetiroDAO();
        double retiros = retiroDAO.totalRetirosPorMes(anio, mes);
        return new ResumenMensual(anio, mes, ingresos, retiros);
    }
}