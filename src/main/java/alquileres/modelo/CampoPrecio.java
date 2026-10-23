package alquileres.modelo;

import java.time.LocalDate;

/** Precio de pizarra de soja de un día, en $ por tonelada. */
public class CampoPrecio {

    private Integer id;
    private LocalDate fecha;
    private Double precio;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
