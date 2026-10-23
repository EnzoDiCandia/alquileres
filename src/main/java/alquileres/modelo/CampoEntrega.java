package alquileres.modelo;

import java.time.LocalDate;

/** Entrega (pago parcial o total) recibida para un período del campo (anio + mes 3/6/9/12). */
public class CampoEntrega {

    private Integer id;
    private Integer anio;
    private Integer mes;
    private LocalDate fecha;
    private Double monto;
    private String observacion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
