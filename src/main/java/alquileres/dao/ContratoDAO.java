package alquileres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alquileres.modelo.Contrato;
import alquileres.utils.Conexion;

public class ContratoDAO {

    // ─── MAPEO COMPLETO DE UN ROW A CONTRATO ────────────────────────────────────

    private Contrato mapear(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        c.setId(rs.getInt("id"));
        c.setIdInquilino(rs.getInt("id_inquilino"));
        c.setIdPropiedad(rs.getInt("id_propiedad"));
        java.sql.Date inicio = rs.getDate("fecha_inicio");
        java.sql.Date fin = rs.getDate("fecha_fin");
        c.setFechaInicio(inicio != null ? inicio.toLocalDate() : null);
        c.setFechaFin(fin != null ? fin.toLocalDate() : null);
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

    // Antes de cambiar el monto se guarda el valor anterior en el historial,
    // así los meses previos al ajuste siguen calculándose con el monto viejo.
    public boolean registrarAjuste(int id, double nuevoMonto, java.time.LocalDate fechaAjuste) throws SQLException {
        crearTablaHistorialSiNoExiste();
        String sqlHistorial = "INSERT INTO contrato_monto_historial (id_contrato, monto, vigente_hasta) " +
                "SELECT id, monto_base, ? FROM contrato WHERE id=?";
        String sql = "UPDATE contrato SET monto_base=?, ultimo_ajuste=? WHERE id=?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);   // las dos operaciones se guardan juntas o ninguna
            try (PreparedStatement psHist = con.prepareStatement(sqlHistorial);
                    PreparedStatement ps = con.prepareStatement(sql)) {
                psHist.setDate(1, java.sql.Date.valueOf(fechaAjuste));
                psHist.setInt(2, id);
                psHist.executeUpdate();

                ps.setDouble(1, nuevoMonto);
                ps.setDate(2, java.sql.Date.valueOf(fechaAjuste));
                ps.setInt(3, id);
                boolean ok = ps.executeUpdate() > 0;
                con.commit();
                return ok;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ─── MONTOS VIGENTES EN UN MES ───────────────────────────────────────────────
    // Devuelve, para los contratos que tuvieron un ajuste POSTERIOR al mes pedido,
    // el monto que se cobraba en ese mes. Los contratos que no aparecen usan su
    // monto_base actual. Un ajuste con fecha en un mes rige desde ese mismo mes.

    public Map<Integer, Double> montosDelMes(int anio, int mes) throws SQLException {
        crearTablaHistorialSiNoExiste();
        Map<Integer, Double> montos = new HashMap<>();
        String sql = "SELECT id_contrato, monto FROM contrato_monto_historial " +
                "WHERE YEAR(vigente_hasta) * 100 + MONTH(vigente_hasta) > ? " +
                "ORDER BY vigente_hasta ASC, id ASC";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio * 100 + mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // el primer ajuste posterior al mes indica el monto que regía en ese mes
                montos.putIfAbsent(rs.getInt("id_contrato"), rs.getDouble("monto"));
            }
        }
        return montos;
    }

    private static boolean tablaHistorialCreada = false;

    private static synchronized void crearTablaHistorialSiNoExiste() throws SQLException {
        if (tablaHistorialCreada) return;
        String sql = "CREATE TABLE IF NOT EXISTS contrato_monto_historial (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "id_contrato INT NOT NULL, " +
                "monto DECIMAL(14,2) NOT NULL, " +       // monto que regía ANTES del ajuste
                "vigente_hasta DATE NOT NULL, " +        // fecha del ajuste que lo reemplazó
                "INDEX idx_historial_contrato (id_contrato))";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        tablaHistorialCreada = true;
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
