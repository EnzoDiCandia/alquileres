package alquileres.modelo;

import java.time.LocalDate;

/** Gasto del campo, asignado a un período de pago (anio + mes 3/6/9/12). */
public class CampoGasto {

    private Integer id;
    private Integer anio;
    private Integer mes;
    private LocalDate fecha;
    private String concepto;
    private Double monto;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
