package alquileres.dao;

import alquileres.modelo.CampoEntrega;
import alquileres.modelo.CampoGasto;
import alquileres.modelo.CampoPago;
import alquileres.modelo.CampoPrecio;
import alquileres.utils.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampoDAO {

    // ─── TABLAS (se crean solas la primera vez) ──────────────────────────────────

    private static boolean tablasCreadas = false;

    private static synchronized void crearTablasSiNoExisten() throws SQLException {
        if (tablasCreadas) return;
        String[] sqls = {
            "CREATE TABLE IF NOT EXISTS campo_pago (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "anio INT NOT NULL, " +
                "mes INT NOT NULL, " +
                "kg_ha_anual DECIMAL(12,2) NOT NULL, " +
                "hectareas DECIMAL(12,2) NOT NULL, " +
                "cobrado BOOLEAN NOT NULL DEFAULT FALSE, " +
                "UNIQUE KEY uk_campo_pago_periodo (anio, mes))",
            "CREATE TABLE IF NOT EXISTS campo_precio (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "id_pago INT NOT NULL, " +
                "fecha DATE NOT NULL, " +
                "precio DECIMAL(14,2) NOT NULL, " +      // $ por tonelada
                "INDEX idx_campo_precio_pago (id_pago))",
            "CREATE TABLE IF NOT EXISTS campo_gasto (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "anio INT NOT NULL, " +
                "mes INT NOT NULL, " +
                "fecha DATE NOT NULL, " +
                "concepto VARCHAR(255) NOT NULL, " +
                "monto DECIMAL(14,2) NOT NULL, " +
                "INDEX idx_campo_gasto_periodo (anio, mes))",
            "CREATE TABLE IF NOT EXISTS campo_entrega (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "anio INT NOT NULL, " +
                "mes INT NOT NULL, " +
                "fecha DATE NOT NULL, " +
                "monto DECIMAL(14,2) NOT NULL, " +
                "observacion VARCHAR(255), " +
                "INDEX idx_campo_entrega_periodo (anio, mes))"
        };
        try (Connection con = Conexion.getConexion();
                Statement st = con.createStatement()) {
            for (String sql : sqls) st.executeUpdate(sql);
        }
        tablasCreadas = true;
    }

    // ─── PAGOS ───────────────────────────────────────────────────────────────────

    private CampoPago mapearPago(ResultSet rs) throws SQLException {
        CampoPago p = new CampoPago();
        p.setId(rs.getInt("id"));
        p.setAnio(rs.getInt("anio"));
        p.setMes(rs.getInt("mes"));
        p.setKgHaAnual(rs.getDouble("kg_ha_anual"));
        p.setHectareas(rs.getDouble("hectareas"));
        p.setCobrado(rs.getBoolean("cobrado"));
        return p;
    }

    /** Todos los pagos, del más reciente al más viejo, con sus precios y lo entregado. */
    public List<CampoPago> listarPagos() throws SQLException {
        crearTablasSiNoExisten();
        List<CampoPago> pagos = new ArrayList<>();
        Map<Integer, CampoPago> porId = new HashMap<>();
        Map<String, CampoPago> porPeriodo = new HashMap<>();
        try (Connection con = Conexion.getConexion()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM campo_pago ORDER BY anio DESC, mes DESC");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CampoPago p = mapearPago(rs);
                    pagos.add(p);
                    porId.put(p.getId(), p);
                    porPeriodo.put(p.getAnio() + "-" + p.getMes(), p);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM campo_precio ORDER BY fecha, id");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CampoPago p = porId.get(rs.getInt("id_pago"));
                    if (p != null) p.getPrecios().add(mapearPrecio(rs));
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT anio, mes, SUM(monto) AS total FROM campo_entrega GROUP BY anio, mes");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CampoPago p = porPeriodo.get(rs.getInt("anio") + "-" + rs.getInt("mes"));
                    if (p != null) p.setEntregado(rs.getDouble("total"));
                }
            }
        }
        return pagos;
    }

    /** El pago de un período, o null si todavía no se cargó. */
    public CampoPago buscarPago(int anio, int mes) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM campo_pago WHERE anio=? AND mes=?")) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            CampoPago p = mapearPago(rs);
            p.setPrecios(listarPrecios(con, p.getId()));
            p.setEntregado(totalEntregado(con, anio, mes));
            return p;
        }
    }

    /** Devuelve el id del pago del período; si no existe lo crea con los kg/ha y hectáreas indicados. */
    private int asegurarPago(Connection con, int anio, int mes, double kgHaAnual, double hectareas) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT id FROM campo_pago WHERE anio=? AND mes=?")) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO campo_pago (anio, mes, kg_ha_anual, hectareas) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ps.setDouble(3, kgHaAnual);
            ps.setDouble(4, hectareas);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    /** Guarda los kg/ha y las hectáreas del período (crea el pago si no existe). No toca los precios. */
    public void guardarDatosPago(CampoPago p) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion()) {
            int id = asegurarPago(con, p.getAnio(), p.getMes(), p.getKgHaAnual(), p.getHectareas());
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE campo_pago SET kg_ha_anual=?, hectareas=? WHERE id=?")) {
                ps.setDouble(1, p.getKgHaAnual());
                ps.setDouble(2, p.getHectareas());
                ps.setInt(3, id);
                ps.executeUpdate();
            }
        }
    }

    public boolean eliminarPago(int id) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psPrecios = con.prepareStatement("DELETE FROM campo_precio WHERE id_pago=?");
                    PreparedStatement psPago = con.prepareStatement("DELETE FROM campo_pago WHERE id=?")) {
                psPrecios.setInt(1, id);
                psPrecios.executeUpdate();
                psPago.setInt(1, id);
                boolean ok = psPago.executeUpdate() > 0;
                con.commit();
                return ok;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // ─── PRECIOS DE PIZARRA (uno por uno) ────────────────────────────────────────

    private CampoPrecio mapearPrecio(ResultSet rs) throws SQLException {
        CampoPrecio pr = new CampoPrecio();
        pr.setId(rs.getInt("id"));
        pr.setFecha(rs.getDate("fecha").toLocalDate());
        pr.setPrecio(rs.getDouble("precio"));
        return pr;
    }

    private List<CampoPrecio> listarPrecios(Connection con, int idPago) throws SQLException {
        List<CampoPrecio> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM campo_precio WHERE id_pago=? ORDER BY fecha, id")) {
            ps.setInt(1, idPago);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearPrecio(rs));
        }
        return lista;
    }

    /** Agrega un precio al período. Si el pago del período no existe, lo crea con los kg/ha y hectáreas dados. */
    public void agregarPrecio(int anio, int mes, double kgHaAnual, double hectareas, CampoPrecio precio) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion()) {
            int idPago = asegurarPago(con, anio, mes, kgHaAnual, hectareas);
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO campo_precio (id_pago, fecha, precio) VALUES (?,?,?)")) {
                ps.setInt(1, idPago);
                ps.setDate(2, Date.valueOf(precio.getFecha()));
                ps.setDouble(3, precio.getPrecio());
                ps.executeUpdate();
            }
        }
    }

    public boolean actualizarPrecio(int id, CampoPrecio precio) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement("UPDATE campo_precio SET fecha=?, precio=? WHERE id=?")) {
            ps.setDate(1, Date.valueOf(precio.getFecha()));
            ps.setDouble(2, precio.getPrecio());
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarPrecio(int id) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement("DELETE FROM campo_precio WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ─── ENTREGAS ────────────────────────────────────────────────────────────────

    private double totalEntregado(Connection con, int anio, int mes) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(monto), 0) FROM campo_entrega WHERE anio=? AND mes=?")) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getDouble(1);
        }
    }

    public List<CampoEntrega> listarEntregas(int anio, int mes) throws SQLException {
        crearTablasSiNoExisten();
        List<CampoEntrega> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM campo_entrega WHERE anio=? AND mes=? ORDER BY fecha, id")) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CampoEntrega e = new CampoEntrega();
                e.setId(rs.getInt("id"));
                e.setAnio(rs.getInt("anio"));
                e.setMes(rs.getInt("mes"));
                e.setFecha(rs.getDate("fecha").toLocalDate());
                e.setMonto(rs.getDouble("monto"));
                e.setObservacion(rs.getString("observacion"));
                lista.add(e);
            }
        }
        return lista;
    }

    public boolean insertarEntrega(CampoEntrega e) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO campo_entrega (anio, mes, fecha, monto, observacion) VALUES (?,?,?,?,?)")) {
            ps.setInt(1, e.getAnio());
            ps.setInt(2, e.getMes());
            ps.setDate(3, Date.valueOf(e.getFecha()));
            ps.setDouble(4, e.getMonto());
            ps.setString(5, e.getObservacion());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarEntrega(int id) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement("DELETE FROM campo_entrega WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ─── GASTOS ──────────────────────────────────────────────────────────────────

    public List<CampoGasto> listarGastos(int anio, int mes) throws SQLException {
        crearTablasSiNoExisten();
        List<CampoGasto> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM campo_gasto WHERE anio=? AND mes=? ORDER BY fecha, id")) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CampoGasto g = new CampoGasto();
                g.setId(rs.getInt("id"));
                g.setAnio(rs.getInt("anio"));
                g.setMes(rs.getInt("mes"));
                g.setFecha(rs.getDate("fecha").toLocalDate());
                g.setConcepto(rs.getString("concepto"));
                g.setMonto(rs.getDouble("monto"));
                lista.add(g);
            }
        }
        return lista;
    }

    public boolean insertarGasto(CampoGasto g) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO campo_gasto (anio, mes, fecha, concepto, monto) VALUES (?,?,?,?,?)")) {
            ps.setInt(1, g.getAnio());
            ps.setInt(2, g.getMes());
            ps.setDate(3, Date.valueOf(g.getFecha()));
            ps.setString(4, g.getConcepto());
            ps.setDouble(5, g.getMonto());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarGasto(int id) throws SQLException {
        crearTablasSiNoExisten();
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement("DELETE FROM campo_gasto WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
