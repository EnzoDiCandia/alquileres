package alquileres.modelo;

import java.time.LocalDateTime;

public class Retiro {
    private int id;
    private int idSocio;
    private LocalDateTime fecha;
    private double monto;
    private String observacion;

    // CONSTRUCTOR VACÍO
    public Retiro() {
    }

    // CONSTRUCTOR COMPLETO
    public Retiro(int id, int idSocio, LocalDateTime fecha, double monto, String observacion) {
        this.id = id;
        this.idSocio = idSocio;
        this.fecha = fecha;
        this.monto = monto;
        this.observacion = observacion;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}