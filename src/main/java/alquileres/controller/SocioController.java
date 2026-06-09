package alquileres.controller;

import alquileres.dao.SocioDAO;
import alquileres.modelo.Socio;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/socios")
@CrossOrigin(origins = "*")
public class SocioController {

    private SocioDAO dao = new SocioDAO();

    @GetMapping
    public List<Socio> listarTodos() throws SQLException {
        return dao.listarSocios();
    }

    @GetMapping("/{id}")
    public Socio buscarPorId(@PathVariable int id) throws SQLException {
        return dao.buscarSocioPorId(id);
    }

    @PostMapping
    public boolean insertar(@RequestBody Socio s) throws SQLException {
        return dao.insertarSocio(s);
    }

    @PutMapping("/{id}/desactivar")
    public boolean desactivar(@PathVariable int id) throws SQLException {
        return dao.desactivarSocio(id);
    }

}