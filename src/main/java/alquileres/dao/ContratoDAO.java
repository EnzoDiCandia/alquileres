package alquileres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alquileres.modelo.Contrato;
import alquileres.utils.Conexion;

public class ContratoDAO {

    // ─── MAPEO COMPLETO DE UN ROW A CONTRATO ────────────────────────────────────

    private Contrato mapear(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        c.setId(rs.getInt("id"));
        c.setIdInquilino(rs.getInt("id_inquilino"));
        c.setIdPropiedad(rs.getInt("id_propiedad"));
        c.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        c.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        c.setMontoBase(rs.getDouble("monto_base"));
        c.setDeposito(rs.getDouble("deposito"));
        c.setArchivoPdfRuta(rs.getString("archivo_pdf_ruta"));
        c.setActivo(rs.getBoolean("activo"));
        c.setPeriodicidadMeses(rs.getObject("periodicidad_meses") != null ? rs.getInt("periodicidad_meses") : null);
        java.sql.Date ultimoAjuste = rs.getDate("ultimo_ajuste");
        c.setUltimoAjuste(ultimoAjuste != null ? ultimoAjuste.toLocalDate() : null);
        return c;
    }

    // ─── LISTA LOS CONTRATOS ACTIVOS ─────────────────────────────────────────────

    public List<Contrato> listarContratos() throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE activo = TRUE";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                contratos.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
    }

    // ─── BUSCAR CONTRATO POR ID
    // ───────────────────────────────────────────────────

    public Contrato buscarContratoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM contrato WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapear(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    // ─── BUSCAR CONTRATOS POR INQUILINO ──────────────────────────────────────────

    public List<Contrato> buscarContratosPorInquilino(int idInquilino) throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE id_inquilino=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idInquilino);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                contratos.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
    }

    // ─── BUSCAR CONTRATOS POR PROPIEDAD ──────────────────────────────────────────

    public List<Contrato> buscarContratosPorPropiedad(int idPropiedad) throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE id_propiedad=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                contratos.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
    }

    // ─── INSERTAR CONTRATO ───────────────────────────────────────────────────────
    // Ahora incluye periodicidad_meses y ultimo_ajuste

    public boolean insertarContrato(Contrato c) throws SQLException {
        String sql = "INSERT INTO contrato (id_propiedad, id_inquilino, fecha_inicio, fecha_fin, " +
                "monto_base, deposito, archivo_pdf_ruta, periodicidad_meses, ultimo_ajuste) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdPropiedad());
            ps.setInt(2, c.getIdInquilino());
            ps.setDate(3, java.sql.Date.valueOf(c.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(c.getFechaFin()));
            ps.setDouble(5, c.getMontoBase());
            ps.setDouble(6, c.getDeposito());
            ps.setString(7, c.getArchivoPdfRuta());
            if (c.getPeriodicidadMeses() != null) {
                ps.setInt(8, c.getPeriodicidadMeses());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            // Si no se indica último ajuste, se usa la fecha de inicio del contrato
            java.time.LocalDate ajuste = c.getUltimoAjuste() != null ? c.getUltimoAjuste() : c.getFechaInicio();
            ps.setDate(9, java.sql.Date.valueOf(ajuste));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ─── DESACTIVAR CONTRATO ─────────────────────────────────────────────────────

    public boolean desactivarContrato(int id) throws SQLException {
        String sql = "UPDATE contrato SET activo=false WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ─── REGISTRAR AJUSTE DE MONTO ───────────────────────────────────────────────
    // Actualiza el monto_base y registra la fecha del ajuste

    public boolean registrarAjuste(int id, double nuevoMonto, java.time.LocalDate fechaAjuste) throws SQLException {
        String sql = "UPDATE contrato SET monto_base=?, ultimo_ajuste=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoMonto);
            ps.setDate(2, java.sql.Date.valueOf(fechaAjuste));
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ─── CONTRATOS PRÓXIMOS A AJUSTE ─────────────────────────────────────────────
    // Devuelve contratos activos donde la próxima fecha de ajuste cae
    // dentro de los próximos 'diasAviso' días (por defecto 30)
    //
    // Lógica: proximo_ajuste = ultimo_ajuste + periodicidad_meses
    // si (proximo_ajuste - hoy) <= diasAviso → incluir en la lista

    public List<Contrato> contratosProximosAjuste(int diasAviso) throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato " +
                "WHERE activo = TRUE " +
                "  AND periodicidad_meses IS NOT NULL " +
                "  AND ultimo_ajuste IS NOT NULL " +
                "  AND DATE_ADD(ultimo_ajuste, INTERVAL periodicidad_meses MONTH) " +
                "      <= DATE_ADD(CURDATE(), INTERVAL ? DAY) " +
                "  AND DATE_ADD(ultimo_ajuste, INTERVAL periodicidad_meses MONTH) >= CURDATE() " +
                "ORDER BY DATE_ADD(ultimo_ajuste, INTERVAL periodicidad_meses MONTH) ASC";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diasAviso);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                contratos.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
    }

    // ─── ACTUALIZAR PERIODICIDAD DE UN CONTRATO EXISTENTE ────────────────────────

    public boolean actualizarPeriodicidad(int id, int periodicidadMeses, java.time.LocalDate ultimoAjuste)
            throws SQLException {
        String sql = "UPDATE contrato SET periodicidad_meses=?, ultimo_ajuste=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, periodicidadMeses);
            ps.setDate(2, java.sql.Date.valueOf(ultimoAjuste));
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean actualizarRutaPdf(int id, String ruta) throws SQLException {
        String sql = "UPDATE contrato SET archivo_pdf_ruta=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruta);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean activarContrato(int id) throws SQLException {
        String sql = "UPDATE contrato SET activo=true WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarContrato(Contrato c) throws SQLException {
        String sql = "UPDATE contrato SET id_propiedad=?, id_inquilino=?, fecha_inicio=?, fecha_fin=?, monto_base=?, deposito=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdPropiedad());
            ps.setInt(2, c.getIdInquilino());
            ps.setDate(3, java.sql.Date.valueOf(c.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(c.getFechaFin()));
            ps.setDouble(5, c.getMontoBase());
            ps.setDouble(6, c.getDeposito());
            ps.setInt(7, c.getId());
            return ps.executeUpdate() > 0;
        }
    }
}
