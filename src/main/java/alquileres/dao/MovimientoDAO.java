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
        String sql = "INSERT INTO movimiento (id_contrato, fecha, concepto, monto, es_ingreso, mes_referencia, anio_referencia) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getIdContrato());
            ps.setDate(2, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(3, m.getConcepto());
            ps.setDouble(4, m.getMonto());
            ps.setBoolean(5, m.isEsIngreso());
            ps.setObject(6, m.getMesReferencia());
            ps.setObject(7, m.getAnioReferencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean eliminarMovimiento(int id) throws SQLException {
        String sql = "DELETE FROM movimiento WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // INSERTA GASTOS

    public boolean insertarGasto(Movimiento m) throws SQLException {
        String sql = "INSERT INTO movimiento (id_contrato, fecha, concepto, monto, es_ingreso, mes_referencia, anio_referencia) VALUES (NULL,?,?,?,false,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(2, m.getConcepto());
            ps.setDouble(3, m.getMonto());
            ps.setObject(4, m.getMesReferencia());
            ps.setObject(5, m.getAnioReferencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // TOTAL INGRESOS POR MES (por mes_referencia)
    public double totalIngresosPorMes(int anio, int mes) throws SQLException {
        String sql = "SELECT SUM(monto) FROM movimiento WHERE es_ingreso = true AND anio_referencia=? AND mes_referencia=?";
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

    // TOTAL GASTOS POR MES
    public double totalGastosPorMes(int anio, int mes) throws SQLException {
        String sql = "SELECT SUM(monto) FROM movimiento WHERE es_ingreso = false AND anio_referencia=? AND mes_referencia=?";
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

    // GASTOS DEL MES
    public List<Movimiento> gastosPorMes(int anio, int mes) throws SQLException {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimiento WHERE es_ingreso = false AND anio_referencia=? AND mes_referencia=? ORDER BY fecha";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setConcepto(rs.getString("concepto"));
                m.setMonto(rs.getDouble("monto"));
                m.setEsIngreso(false);
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return lista;
    }

    // PAGOS POR CONTRATO Y MES
    public List<Movimiento> pagosPorContratoYMes(int idContrato, int anio, int mes) throws SQLException {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimiento WHERE es_ingreso = true AND id_contrato=? AND anio_referencia=? AND mes_referencia=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            ps.setInt(2, anio);
            ps.setInt(3, mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setIdContrato(rs.getInt("id_contrato"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setConcepto(rs.getString("concepto"));
                m.setMonto(rs.getDouble("monto"));
                m.setMesReferencia(rs.getInt("mes_referencia"));
                m.setAnioReferencia(rs.getInt("anio_referencia"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return lista;
    }

    public List<Movimiento> ingresosPorMes(int anio, int mes) throws SQLException {
    List<Movimiento> lista = new ArrayList<>();
    String sql = "SELECT * FROM movimiento WHERE es_ingreso = true AND anio_referencia=? AND mes_referencia=? ORDER BY fecha";
    try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, anio);
        ps.setInt(2, mes);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Movimiento m = new Movimiento();
            m.setId(rs.getInt("id"));
            m.setIdContrato(rs.getInt("id_contrato"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setConcepto(rs.getString("concepto"));
            m.setMonto(rs.getDouble("monto"));
            m.setMesReferencia(rs.getInt("mes_referencia"));
            m.setAnioReferencia(rs.getInt("anio_referencia"));
            lista.add(m);
        }
    }
    return lista;
}
public List<Movimiento> ingresosPorAnio(int anio) throws SQLException {
    List<Movimiento> lista = new ArrayList<>();
    String sql = "SELECT * FROM movimiento WHERE es_ingreso = true AND anio_referencia=? ORDER BY fecha";
    try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, anio);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Movimiento m = new Movimiento();
            m.setId(rs.getInt("id"));
            m.setIdContrato(rs.getInt("id_contrato"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setConcepto(rs.getString("concepto"));
            m.setMonto(rs.getDouble("monto"));
            m.setMesReferencia(rs.getInt("mes_referencia"));
            m.setAnioReferencia(rs.getInt("anio_referencia"));
            lista.add(m);
        }
    }
    return lista;
}

public List<Movimiento> gastosPorAnio(int anio) throws SQLException {
    List<Movimiento> lista = new ArrayList<>();
    String sql = "SELECT * FROM movimiento WHERE es_ingreso = false AND anio_referencia=? ORDER BY fecha";
    try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, anio);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Movimiento m = new Movimiento();
            m.setId(rs.getInt("id"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setConcepto(rs.getString("concepto"));
            m.setMonto(rs.getDouble("monto"));
            m.setEsIngreso(false);
            lista.add(m);
        }
    }
    return lista;
}
}