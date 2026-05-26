package alquileres.controller;

import alquileres.dao.ContratoDAO;
import alquileres.modelo.Contrato;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

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
}