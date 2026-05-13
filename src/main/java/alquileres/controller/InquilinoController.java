package alquileres.controller;
import alquileres.dao.InquilinoDAO;
import alquileres.modelo.Inquilino;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/inquilinos")
@CrossOrigin(origins = "*")
public class InquilinoController {


    private InquilinoDAO dao = new InquilinoDAO();

    @GetMapping
    public List<Inquilino> listarTodos() throws SQLException {
        return dao.listarInquilinos();
    }

    @GetMapping("/{id}")
    public Inquilino buscarPorId(@PathVariable int id) throws SQLException {
        return dao.buscarInqporID(id);
    }

    @GetMapping("/documento/{documento}")
    public Inquilino buscarPorDocumento(@PathVariable String documento) throws SQLException {
        return dao.buscarInqporDocumento(documento);
    }

    @GetMapping("/email/{email}")
    public Inquilino buscarPorEmail(@PathVariable String email) throws SQLException {
        return dao.buscarInqporEmail(email);
    }

    @GetMapping("/telefono/{telefono}")
    public Inquilino buscarPorTelefono(@PathVariable String telefono) throws SQLException {
        return dao.buscarInqporTel(telefono);
    }
    @GetMapping("/nombre/{nombre}")
    public List<Inquilino> buscarPorNombre(@PathVariable String nombre) throws SQLException {
        return dao.listarInquilinosPorNombre(nombre);
    }

    @GetMapping("/inactivos")
    public List<Inquilino> listarInactivos() throws SQLException {
        return dao.listarInquilinosInactivos();
    }

    @PostMapping
    public boolean insertar(@RequestBody Inquilino i) throws SQLException {
        return dao.insertarInquilino(i);
    }

    @PutMapping("/{id}/desactivar")
    public boolean desactivar(@PathVariable int id) throws SQLException {
        return dao.desactivarInquilino(id);
    }
}