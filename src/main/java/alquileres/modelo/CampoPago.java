package alquileres.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pago trimestral del campo (marzo, junio, septiembre, diciembre).
 * Valor = (kg soja/ha por año × hectáreas ÷ 4) × promedio de pizarra ($/t) ÷ 1000
 */
public class CampoPago {

    private Integer id;
    private Integer anio;
    private Integer mes;            // 3, 6, 9 o 12
    private Double kgHaAnual;       // ej: 1300
    private Double hectareas;       // ej: 57
    private Boolean cobrado;
    private List<CampoPrecio> precios = new ArrayList<>();   // hasta 10 días de pizarra

    // ── Valores calculados (se devuelven en el JSON, no se guardan) ──

    public double getKgACobrar() {
        if (kgHaAnual == null || hectareas == null) return 0;
        return kgHaAnual * hectareas / 4;
    }

    public double getPrecioPromedio() {
        if (precios == null || precios.isEmpty()) return 0;
        double suma = 0;
        for (CampoPrecio p : precios) suma += p.getPrecio();
        return Math.round(suma / precios.size() * 100) / 100.0;
    }

    public double getMonto() {
        // el precio de pizarra está en $ por tonelada → se divide por 1000 para pasarlo a $/kg
        return Math.round(getKgACobrar() * getPrecioPromedio() / 1000 * 100) / 100.0;
    }

    // ── Getters y setters ──

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }

    public Double getKgHaAnual() { return kgHaAnual; }
    public void setKgHaAnual(Double kgHaAnual) { this.kgHaAnual = kgHaAnual; }

    public Double getHectareas() { return hectareas; }
    public void setHectareas(Double hectareas) { this.hectareas = hectareas; }

    public Boolean getCobrado() { return cobrado; }
    public void setCobrado(Boolean cobrado) { this.cobrado = cobrado; }

    public List<CampoPrecio> getPrecios() { return precios; }
    public void setPrecios(List<CampoPrecio> precios) { this.precios = precios; }
}
