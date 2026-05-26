package alquileres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alquileres.modelo.Contrato;
import alquileres.modelo.Inquilino;
import alquileres.utils.Conexion;

public class InquilinoDAO {

    // CONSTRUCTOR VACIO

    public InquilinoDAO() {

    }

    // LISTAR INQUILINOS ACTIVOS

    public List<Inquilino> listarInquilinos() throws SQLException {
        List<Inquilino> inquilinos = new ArrayList<>();
        String sql = "SELECT * FROM inquilino WHERE activo = TRUE";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inquilino i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
                inquilinos.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return inquilinos;
    }

    // LISTAR INQUILINOS INACTIVOS

    public List<Inquilino> listarInquilinosInactivos() throws SQLException {
        List<Inquilino> inquilinos = new ArrayList<>();
        String sql = "SELECT * FROM inquilino WHERE activo = False";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inquilino i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
                inquilinos.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return inquilinos;
    }

    // LISTAR INQUILINOS POR NOMBRE

    public List<Inquilino> listarInquilinosPorNombre(String nombre) throws SQLException {
        List<Inquilino> inquilinos = new ArrayList<>();
        String sql = "SELECT * FROM inquilino WHERE nombre =?";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Inquilino i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
                inquilinos.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return inquilinos;
    }

    // BUSCA INQUILINO POR ID

    public Inquilino buscarInqporID(int id) throws SQLException {
        Inquilino i = null;
        String sql = "SELECT * FROM inquilino WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return i;
    }

    // BUSCA INQUILINO POR DOCUMENTO

    public Inquilino buscarInqporDocumento(String documento) throws SQLException {
        Inquilino i = null;
        String sql = "SELECT * FROM inquilino WHERE documento=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return i;
    }

    // BUSCA INQUILINO POR EMAIL

    public Inquilino buscarInqporEmail(String email) throws SQLException {
        Inquilino i = null;
        String sql = "SELECT * FROM inquilino WHERE email=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return i;
    }

    // BUSCA INQUILINO POR TELEFONO

    public Inquilino buscarInqporTel(String telefono) throws SQLException {
        Inquilino i = null;
        String sql = "SELECT * FROM inquilino WHERE telefono=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, telefono);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return i;
    }

    // INSERTAR INQUILINO EN LA BASE DE DATOS

    public boolean insertarInquilino(Inquilino i) throws SQLException {
        String sql = "INSERT INTO inquilino (nombre,documento,telefono,email) VALUES (?,?,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNombre());
            ps.setString(2, i.getDocumento());
            ps.setString(3, i.getTelefono());
            ps.setString(4, i.getEmail());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean desactivarInquilino(int id) throws SQLException {
        String sql = "UPDATE inquilino SET activo=false WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // INSERTAR CONTRATO
    public boolean insertarContrato(Contrato c) throws SQLException {
        String sql = "INSERT INTO contrato (id_propiedad, id_inquilino, fecha_inicio, fecha_fin, monto_base, deposito, archivo_pdf_ruta) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdPropiedad());
            ps.setInt(2, c.getIdInquilino());
            ps.setDate(3, java.sql.Date.valueOf(c.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(c.getFechaFin()));
            ps.setDouble(5, c.getMontoBase());
            ps.setDouble(6, c.getDeposito());
            ps.setString(7, c.getArchivoPdfRuta());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DESACTIVAR CONTRATO
    public boolean desactivarContrato(int id) throws SQLException {
        String sql = "UPDATE contrato SET activo=false WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean activarInquilino(int id) throws SQLException {
        String sql = "UPDATE inquilino SET activo=1 WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //LISTA TODOS LOS INQUILINOS

    public List<Inquilino> listarTodosInquilinos() throws SQLException {
        List<Inquilino> inquilinos = new ArrayList<>();
        String sql = "SELECT * FROM inquilino";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inquilino i = new Inquilino();
                i.setId(rs.getInt("id"));
                i.setNombre(rs.getString("nombre"));
                i.setDocumento(rs.getString("documento"));
                i.setTelefono(rs.getString("telefono"));
                i.setEmail(rs.getString("email"));
                i.setActivo(rs.getBoolean("activo"));
                inquilinos.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return inquilinos;
    }
}
