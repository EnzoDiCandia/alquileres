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
    

                    // LISTA LOS CONTRATOS ACTIVOS

        public List<Contrato> listarContratos() throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE activo = TRUE";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
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
                contratos.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
    }

                 // BUSCAR CONTRATO POR ID

    public Contrato buscarContratoPorId(int id) throws SQLException {
        Contrato c = null;
        String sql = "SELECT * FROM contrato WHERE id=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new Contrato();
                c.setId(rs.getInt("id"));
                c.setIdInquilino(rs.getInt("id_inquilino"));
                c.setIdPropiedad(rs.getInt("id_propiedad"));
                c.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                c.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                c.setMontoBase(rs.getDouble("monto_base"));
                c.setDeposito(rs.getDouble("deposito"));
                c.setArchivoPdfRuta(rs.getString("archivo_pdf_ruta"));
                c.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return c;
    }

                    // BUSCAR CONTRATOS POR INQUILINO


    public List<Contrato> buscarContratosPorInquilino(int idInquilino) throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE id_inquilino=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idInquilino);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                contratos.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
    }

                // BUSCAR CONTRATOS POR PROPIEDAD


    public List<Contrato> buscarContratosPorPropiedad(int idPropiedad) throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE id_propiedad=?";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                contratos.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return contratos;
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



}
