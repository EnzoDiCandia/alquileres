package alquileres.controller;

import alquileres.dao.MovimientoDAO;
import alquileres.dao.RetiroDAO;
import alquileres.modelo.Movimiento;
import alquileres.modelo.ResumenMensual;
import alquileres.dao.RetiroDAO;
import alquileres.modelo.ResumenMensual;
import alquileres.utils.Validar;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
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
        Validar.requerido(m.getIdContrato(), "contrato");
        Validar.requerido(m.getFecha(), "fecha");
        Validar.positivo(m.getMonto(), "El monto");
        Validar.requerido(m.isEsIngreso(), "tipo de movimiento");
        Validar.mesYAnio(m.getMesReferencia(), m.getAnioReferencia());
        return dao.insertarMovimiento(m);
    }

    @GetMapping("/total/{anio}/{mes}")
    public double totalPorMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.totalIngresosPorMes(anio, mes);
    }

    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable int id) throws SQLException {
        return dao.eliminarMovimiento(id);
    }

    @PostMapping("/gasto")
    public boolean registrarGasto(@RequestBody Movimiento m) throws SQLException {
        Validar.requerido(m.getConcepto(), "concepto");
        Validar.requerido(m.getFecha(), "fecha");
        Validar.positivo(m.getMonto(), "El monto");
        Validar.mesYAnio(m.getMesReferencia(), m.getAnioReferencia());
        return dao.insertarGasto(m);
    }

    @GetMapping("/resumen/{anio}/{mes}")
    public ResumenMensual resumenMensual(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        double ingresos = dao.totalIngresosPorMes(anio, mes);
        double gastos = dao.totalGastosPorMes(anio, mes);
        RetiroDAO retiroDAO = new RetiroDAO();
        double retiros = retiroDAO.totalRetirosPorMes(anio, mes);
        return new ResumenMensual(anio, mes, ingresos, retiros, gastos);
    }

    @GetMapping("/gastos/{anio}/{mes}")
    public List<Movimiento> gastosPorMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.gastosPorMes(anio, mes);
    }

    @GetMapping("/contrato/{idContrato}/{anio}/{mes}")
    public List<Movimiento> pagosPorContratoYMes(@PathVariable int idContrato, @PathVariable int anio,
            @PathVariable int mes) throws SQLException {
        return dao.pagosPorContratoYMes(idContrato, anio, mes);
    }

    @GetMapping("/ingresos/{anio}/{mes}")
    public List<Movimiento> ingresosPorMes(@PathVariable int anio, @PathVariable int mes) throws SQLException {
        return dao.ingresosPorMes(anio, mes);
    }

    @GetMapping("/ingresos/{anio}")
    public List<Movimiento> ingresosPorAnio(@PathVariable int anio) throws SQLException {
        return dao.ingresosPorAnio(anio);
    }

    @GetMapping("/gastos/{anio}")
    public List<Movimiento> gastosPorAnio(@PathVariable int anio) throws SQLException {
        return dao.gastosPorAnio(anio);
    }
}