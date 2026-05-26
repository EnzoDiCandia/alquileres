package alquileres.modelo;

import java.time.LocalDateTime;

public class Retiro {
    private Integer id;
    private Integer idSocio;
    private LocalDateTime fecha;
    private Double monto;
    private String observacion;
    private Integer mesReferencia;
    private Integer anioReferencia;

    // CONSTRUCTOR VACÍO
    public Retiro() {
    }

    // CONSTRUCTOR COMPLETO
    public Retiro(Integer id, Integer idSocio, LocalDateTime fecha, Double monto, String observacion) {
        this.id = id;
        this.idSocio = idSocio;
        this.fecha = fecha;
        this.monto = monto;
        this.observacion = observacion;
    }

    // GETTERS Y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public Integer getAnioReferencia() {
        return anioReferencia;
    }

    public void setAnioReferencia(Integer anioReferencia) {
        this.anioReferencia = anioReferencia;
    }
}