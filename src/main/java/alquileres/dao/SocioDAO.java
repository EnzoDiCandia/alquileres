package alquileres.dao;

import alquileres.modelo.Socio;
import alquileres.utils.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

            // BUSCAR SOCIO POR NOMBRE PARA LOGIN

    public Socio buscarPorNombre(String nombre) throws SQLException {
        Socio s = null;
        String sql = "SELECT * FROM socio WHERE nombre=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s = new Socio();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setEmail(rs.getString("email"));
                s.setPassword(rs.getString("password"));
                s.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return s;
    }

            // LISTAR SOCIOS ACTIVOS

    public List<Socio> listarSocios() throws SQLException {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socio WHERE activo = TRUE";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Socio s = new Socio();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setEmail(rs.getString("email"));
                s.setActivo(rs.getBoolean("activo"));
                socios.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return socios;
    }

            // BUSCAR SOCIO POR ID
    public Socio buscarSocioPorId(int id) throws SQLException {
        Socio s = null;
        String sql = "SELECT * FROM socio WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s = new Socio();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setEmail(rs.getString("email"));
                s.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return s;
    }

            // INSERTAR SOCIO

    public boolean insertarSocio(Socio s) throws SQLException {
        String sql = "INSERT INTO socio (nombre, email) VALUES (?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getEmail());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

            // DESACTIVAR SOCIO

            
    public boolean desactivarSocio(int id) throws SQLException {
        String sql = "UPDATE socio SET activo=false WHERE id=?";
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
}