package alquileres.modelo;

import java.time.LocalDate;

public class Movimiento {
    private Integer id;
    private Integer idContrato;
    private LocalDate fecha;
    private String concepto;
    private Double monto;
    private Boolean esIngreso;
    private Integer mesReferencia;
    private Integer anioReferencia;

    // CONSTRUCTOR VACÍO
    public Movimiento() {
    }

    // CONSTRUCTOR COMPLETO
    public Movimiento(Integer id, Integer idContrato, LocalDate fecha, String concepto, Double monto,
            Boolean esIngreso) {
        this.id = id;
        this.idContrato = idContrato;
        this.fecha = fecha;
        this.concepto = concepto;
        this.monto = monto;
        this.esIngreso = esIngreso;
    }

    // GETTERS Y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Boolean isEsIngreso() {
        return esIngreso;
    }

    public void setEsIngreso(Boolean esIngreso) {
        this.esIngreso = esIngreso;
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