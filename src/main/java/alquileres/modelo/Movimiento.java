package alquileres.modelo;

import java.time.LocalDate;

public class Movimiento {
    private int id;
    private int idContrato;
    private LocalDate fecha;
    private String concepto;
    private double monto;
    private boolean esIngreso;

    // CONSTRUCTOR VACÍO
    public Movimiento() {
    }

    // CONSTRUCTOR COMPLETO
    public Movimiento(int id, int idContrato, LocalDate fecha, String concepto, double monto, boolean esIngreso) {
        this.id = id;
        this.idContrato = idContrato;
        this.fecha = fecha;
        this.concepto = concepto;
        this.monto = monto;
        this.esIngreso = esIngreso;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public boolean isEsIngreso() {
        return esIngreso;
    }

    public void setEsIngreso(boolean esIngreso) {
        this.esIngreso = esIngreso;
    }
}