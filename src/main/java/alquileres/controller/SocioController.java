package alquileres.controller;

import alquileres.dao.SocioDAO;
import alquileres.modelo.Socio;
import alquileres.utils.Validar;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioController {

    private SocioDAO dao = new SocioDAO();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
        Validar.requerido(s.getNombre(), "nombre");
        Validar.requerido(s.getUsuario(), "usuario");
        Validar.requerido(s.getPassword(), "contraseña");
        if (s.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        if (dao.buscarPorUsuario(s.getUsuario()) != null) {
            throw new IllegalArgumentException("Ya existe un socio con ese usuario");
        }
        s.setPassword(encoder.encode(s.getPassword()));   // se guarda encriptada, nunca en texto plano
        return dao.insertarSocio(s);
    }

    @PutMapping("/{id}/desactivar")
    public boolean desactivar(@PathVariable int id) throws SQLException {
        return dao.desactivarSocio(id);
    }
}