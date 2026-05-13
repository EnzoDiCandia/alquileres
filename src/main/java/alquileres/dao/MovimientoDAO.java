package alquileres.dao;

import alquileres.modelo.Movimiento;
import alquileres.utils.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    // LISTAR TODOS LOS MOVIMIENTOS
    public List<Movimiento> listarMovimientos() throws SQLException {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimiento";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setIdContrato(rs.getInt("id_contrato"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setConcepto(rs.getString("concepto"));
                m.setMonto(rs.getDouble("monto"));
                m.setEsIngreso(rs.getBoolean("es_ingreso"));
                movimientos.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return movimientos;
    }

    // BUSCAR MOVIMIENTO POR ID
    public Movimiento buscarMovimientoPorId(int id) throws SQLException {
        Movimiento m = null;
        String sql = "SELECT * FROM movimiento WHERE id=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setIdContrato(rs.getInt("id_contrato"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setConcepto(rs.getString("concepto"));
                m.setMonto(rs.getDouble("monto"));
                m.setEsIngreso(rs.getBoolean("es_ingreso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return m;
    }

    // BUSCAR MOVIMIENTOS POR CONTRATO
    public List<Movimiento> buscarMovimientosPorContrato(int idContrato) throws SQLException {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimiento WHERE id_contrato=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setIdContrato(rs.getInt("id_contrato"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setConcepto(rs.getString("concepto"));
                m.setMonto(rs.getDouble("monto"));
                m.setEsIngreso(rs.getBoolean("es_ingreso"));
                movimientos.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return movimientos;
    }

    // INSERTAR MOVIMIENTO
    public boolean insertarMovimiento(Movimiento m) throws SQLException {
        String sql = "INSERT INTO movimiento (id_contrato, fecha, concepto, monto, es_ingreso) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getIdContrato());
            ps.setDate(2, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(3, m.getConcepto());
            ps.setDouble(4, m.getMonto());
            ps.setBoolean(5, m.isEsIngreso());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

                    // TOTAL INGRESOS POR MES

                    
    public double totalIngresosPorMes(int anio, int mes) throws SQLException {
        String sql = "SELECT SUM(monto) FROM movimiento WHERE es_ingreso = true AND YEAR(fecha)=? AND MONTH(fecha)=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }

}