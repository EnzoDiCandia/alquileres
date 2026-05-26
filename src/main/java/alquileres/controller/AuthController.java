package alquileres.controller;

import alquileres.dao.SocioDAO;
import alquileres.modelo.Socio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private SocioDAO dao = new SocioDAO();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) throws SQLException {
        String usuario = body.get("usuario");   // <-- antes era "nombre"
        String password = body.get("password");

        Socio s = dao.buscarPorUsuario(usuario); // <-- antes era buscarPorNombre

        if (s == null || !encoder.matches(password, s.getPassword())) {
            return Map.of("ok", false, "mensaje", "Usuario o contraseña incorrectos");
        }

        return Map.of("ok", true, "id", s.getId(), "nombre", s.getNombre());
    }

}
