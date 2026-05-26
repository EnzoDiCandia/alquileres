package alquileres.controller;

import alquileres.dao.PropiedadDAO;
import alquileres.modelo.Propiedad;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/propiedades")
@CrossOrigin(origins = "*")
public class PropiedadController {

    private PropiedadDAO dao = new PropiedadDAO();

    @GetMapping
    public List<Propiedad> listarTodas() throws SQLException {
        return dao.listarTodas();
    }

    @GetMapping("/{id}")
    public Propiedad buscarPorId(@PathVariable int id) throws SQLException {
        return dao.buscarPropPorId(id);
    }

    @PostMapping
    public boolean insertar(@RequestBody Propiedad p) throws SQLException {
        return dao.insertarPropiedad(p);
    }

    @PutMapping("/{id}/desactivar")
    public boolean desactivar(@PathVariable int id) throws SQLException {
        return dao.desactivarPropiedad(id);
    }

    @PutMapping("/{id}/activar")
    public boolean activar(@PathVariable int id) throws SQLException {
        return dao.activarPropiedad(id);
    }
}