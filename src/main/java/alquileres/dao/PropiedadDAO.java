package alquileres.dao;

import alquileres.modelo.Propiedad;
import alquileres.utils.Conexion;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PropiedadDAO {

    public PropiedadDAO() {
    }

    // LISTAR PROPIEDADES ACTIVAS

    public List<Propiedad> listarTodas() throws SQLException {
        List<Propiedad> propiedades = new ArrayList<>();
        String sql = "SELECT * FROM propiedad WHERE activo = true";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Propiedad p = new Propiedad();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipo(rs.getString("tipo"));
                p.setActivo(rs.getBoolean("activo"));
                propiedades.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return propiedades;
    }

    // BUSCA PROPIEDADES POR ID
    public Propiedad buscarPropPorId(int id) throws SQLException {
        Propiedad p = null;
        String sql = "SELECT * FROM propiedad where id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Propiedad();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipo(rs.getString("tipo"));
                p.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return p;
    }

    // BUSCA PROPIEDADES POR NOMBRE
    public List<Propiedad> buscarPorNombre(String nombre) throws SQLException {
        List<Propiedad> propiedades = new ArrayList<>();
        String sql = "SELECT * FROM propiedad where nombre=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Propiedad p = new Propiedad();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipo(rs.getString("tipo"));
                p.setActivo(rs.getBoolean("activo"));
                propiedades.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return propiedades;
    }

    // BUSCA PROPIEDADES POR DIRECCION
    public List<Propiedad> buscarPorDireccion(String direccion) throws SQLException {
        List<Propiedad> propiedades = new ArrayList<>();
        String sql = "SELECT * FROM propiedad where direccion=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, direccion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Propiedad p = new Propiedad();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipo(rs.getString("tipo"));
                p.setActivo(rs.getBoolean("activo"));
                propiedades.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return propiedades;
    }

    // BUSCA PROPIEDADES POR TIPO
    public List<Propiedad> buscarPorTipo(String tipo) throws SQLException {
        List<Propiedad> propiedades = new ArrayList<>();
        String sql = "SELECT * FROM Propiedad where tipo=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Propiedad p = new Propiedad();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipo(rs.getString("tipo"));
                p.setActivo(rs.getBoolean("activo"));
                propiedades.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return propiedades;
    }

    // BUSCA PROPIEDADES POR ACTIVIDAD

    public List<Propiedad> buscarPorActividad(boolean activo) throws SQLException {
        List<Propiedad> propiedades = new ArrayList<>();
        String sql = "SELECT * FROM Propiedad where activo=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Propiedad p = new Propiedad();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipo(rs.getString("tipo"));
                p.setActivo(rs.getBoolean("activo"));
                propiedades.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return propiedades;
    }

    // INSERTAR PROPIEDADES EN LA BASE DE DATOS

    public boolean insertarPropiedad(Propiedad p) throws SQLException {
        String sql = "INSERT into propiedad (nombre,direccion,tipo) values (?,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDireccion());
            ps.setString(3, p.getTipo());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ELIMINAR PROPIEDADES EN LA BASE DE DATOS
    public boolean desactivarPropiedad(int id) throws SQLException {
        String sql = "UPDATE propiedad SET activo=0 WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(0, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean activarPropiedad(int id) throws SQLException {
        String sql = "UPDATE propiedad SET activo=1 WHERE id=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
