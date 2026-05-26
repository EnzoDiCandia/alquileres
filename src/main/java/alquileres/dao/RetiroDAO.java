package alquileres.dao;

import alquileres.modelo.Retiro;
import alquileres.utils.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RetiroDAO {

    // LISTAR TODOS LOS RETIROS
    public List<Retiro> listarRetiros() throws SQLException {
        List<Retiro> retiros = new ArrayList<>();
        String sql = "SELECT * FROM retiro";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Retiro r = new Retiro();
                r.setId(rs.getInt("id"));
                r.setIdSocio(rs.getInt("id_socio"));
                r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                r.setMonto(rs.getDouble("monto"));
                r.setObservacion(rs.getString("observacion"));
                retiros.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return retiros;
    }

    // BUSCAR RETIRO POR ID
    public Retiro buscarRetiroPorId(int id) throws SQLException {
        Retiro r = null;
        String sql = "SELECT * FROM retiro WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = new Retiro();
                r.setId(rs.getInt("id"));
                r.setIdSocio(rs.getInt("id_socio"));
                r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                r.setMonto(rs.getDouble("monto"));
                r.setObservacion(rs.getString("observacion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return r;
    }

    // BUSCAR RETIROS POR SOCIO
    public List<Retiro> buscarRetirosPorSocio(int idSocio) throws SQLException {
        List<Retiro> retiros = new ArrayList<>();
        String sql = "SELECT * FROM retiro WHERE id_socio=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Retiro r = new Retiro();
                r.setId(rs.getInt("id"));
                r.setIdSocio(rs.getInt("id_socio"));
                r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                r.setMonto(rs.getDouble("monto"));
                r.setObservacion(rs.getString("observacion"));
                retiros.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return retiros;
    }

    // INSERTAR RETIRO
    public boolean insertarRetiro(Retiro r) throws SQLException {
        String sql = "INSERT INTO retiro (id_socio, monto, observacion, mes_referencia, anio_referencia) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getIdSocio());
            ps.setDouble(2, r.getMonto());
            ps.setString(3, r.getObservacion());
            ps.setObject(4, r.getMesReferencia());
            ps.setObject(5, r.getAnioReferencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public double totalRetirosPorMes(int anio, int mes) throws SQLException {
        String sql = "SELECT SUM(monto) FROM retiro WHERE anio_referencia=? AND mes_referencia=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }

    // RETIROS DEL MES POR SOCIO

    public List<Retiro> retirosPorSocioYMes(int idSocio, int anio, int mes) throws SQLException {
        List<Retiro> retiros = new ArrayList<>();
        String sql = "SELECT * FROM retiro WHERE id_socio=? AND anio_referencia=? AND mes_referencia=? ORDER BY fecha";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            ps.setInt(2, anio);
            ps.setInt(3, mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Retiro r = new Retiro();
                r.setId(rs.getInt("id"));
                r.setIdSocio(rs.getInt("id_socio"));
                r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                r.setMonto(rs.getDouble("monto"));
                r.setObservacion(rs.getString("observacion"));
                r.setMesReferencia(rs.getObject("mes_referencia", Integer.class));
                r.setAnioReferencia(rs.getObject("anio_referencia", Integer.class));
                retiros.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return retiros;
    }

    // ELIMINA LOS RETIROS REGISTRADOS
    public boolean eliminarRetiro(int id) throws SQLException {
        String sql = "DELETE FROM retiro WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Retiro> retirosPorMes(int anio, int mes) throws SQLException {
        List<Retiro> retiros = new ArrayList<>();
        String sql = "SELECT * FROM retiro WHERE anio_referencia=? AND mes_referencia=? ORDER BY fecha";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Retiro r = new Retiro();
                r.setId(rs.getInt("id"));
                r.setIdSocio(rs.getInt("id_socio"));
                r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                r.setMonto(rs.getDouble("monto"));
                r.setObservacion(rs.getString("observacion"));
                r.setMesReferencia(rs.getObject("mes_referencia", Integer.class));
                r.setAnioReferencia(rs.getObject("anio_referencia", Integer.class));
                retiros.add(r);
            }
        }
        return retiros;
    }

    public List<Retiro> retirosPorAnio(int anio) throws SQLException {
    List<Retiro> retiros = new ArrayList<>();
    String sql = "SELECT * FROM retiro WHERE anio_referencia=? ORDER BY fecha";
    try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, anio);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Retiro r = new Retiro();
            r.setId(rs.getInt("id"));
            r.setIdSocio(rs.getInt("id_socio"));
            r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
            r.setMonto(rs.getDouble("monto"));
            r.setObservacion(rs.getString("observacion"));
            r.setMesReferencia(rs.getObject("mes_referencia", Integer.class));
            r.setAnioReferencia(rs.getObject("anio_referencia", Integer.class));
            retiros.add(r);
        }
    }
    return retiros;
}
}