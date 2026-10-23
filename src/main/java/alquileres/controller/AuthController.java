package alquileres.controller;

import alquileres.dao.SocioDAO;
import alquileres.modelo.Socio;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private SocioDAO dao = new SocioDAO();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body,
            HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String usuario = body.get("usuario");   // <-- antes era "nombre"
        String password = body.get("password");

        Socio s = usuario == null ? null : dao.buscarPorUsuario(usuario); // <-- antes era buscarPorNombre

        if (s == null || !s.isActivo() || password == null || s.getPassword() == null
                || !encoder.matches(password, s.getPassword())) {
            return Map.of("ok", false, "mensaje", "Usuario o contraseña incorrectos");
        }

        // Login correcto: se guarda la autenticación en la sesión del servidor.
        // Si ya había una sesión se le cambia el id (evita fijación de sesión).
        if (request.getSession(false) != null) {
            request.changeSessionId();
        }
        UsernamePasswordAuthenticationToken auth =
                UsernamePasswordAuthenticationToken.authenticated(usuario, null, List.of());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        contextRepository.saveContext(context, request, response);

        return Map.of("ok", true, "id", s.getId(), "nombre", s.getNombre());
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return Map.of("ok", true);
    }

}
