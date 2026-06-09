package alquileres.modelo;
import java.time.LocalDate;

public class Contrato {

    private Integer id;
    private Integer idPropiedad;
    private Integer idInquilino;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double montoBase;
    private double deposito;
    private String archivoPdfRuta;
    private Boolean activo;
    private Integer periodicidadMeses;
    private java.time.LocalDate ultimoAjuste;

    // CONSTRUCTOR VACÍO
    public Contrato() {
    }

    public Contrato(Integer id, Integer idPropiedad , Integer idInquilino , LocalDate fechaInicio , LocalDate fechaFin , double montoBase){
        
        this.id=id;
        this.idPropiedad=idPropiedad;
        this.idInquilino=idInquilino;
        this.fechaInicio=fechaInicio;
        this.fechaFin=fechaFin;
        this.montoBase=montoBase;
    }

    // GETTERS Y SETTERS
    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getIdPropiedad() { return idPropiedad; }

    public void setIdPropiedad(int idPropiedad) { 
        this.idPropiedad = idPropiedad; 
    }

    public Integer getIdInquilino() { 
        return idInquilino; 
    }

    public void setIdInquilino(int idInquilino) { 
        this.idInquilino = idInquilino; 
    }

    public LocalDate getFechaInicio() { 
        return fechaInicio; 
    }

    public void setFechaInicio(LocalDate fechaInicio) 
    { this.fechaInicio = fechaInicio; 

    }

    public LocalDate getFechaFin() 
    { return fechaFin; 

    }

    public void setFechaFin(LocalDate fechaFin) { 
        this.fechaFin = fechaFin; 
    }

    public double getMontoBase() { 
        return montoBase; 
    }

    public void setMontoBase(double montoBase) { 
        this.montoBase = montoBase; 
    }

    public double getDeposito() { 
        return deposito; 
    }

    public void setDeposito(double deposito) { this.deposito = deposito; }

    public String getArchivoPdfRuta() { 
        return archivoPdfRuta; 
    }

    public void setArchivoPdfRuta(String archivoPdfRuta) { 
        this.archivoPdfRuta = archivoPdfRuta; 
    }

    public Boolean isActivo() { 
        return activo; 
    }

    public void setActivo(Boolean activo) { 
        this.activo = activo; 
    }

    public Integer getPeriodicidadMeses() {
        return periodicidadMeses;
    }

    public void setPeriodicidadMeses(Integer periodicidadMeses) {
        this.periodicidadMeses = periodicidadMeses;
    }

    public java.time.LocalDate getUltimoAjuste() {
        return ultimoAjuste;
    }

    public void setUltimoAjuste(java.time.LocalDate ultimoAjuste) {
        this.ultimoAjuste = ultimoAjuste;
    }

    // ── Método de utilidad: calcula la próxima fecha de ajuste ──
    // Devuelve null si el contrato no tiene periodicidad configurada

    public java.time.LocalDate getProximoAjuste() {
        if (periodicidadMeses == null || ultimoAjuste == null) return null;
        return ultimoAjuste.plusMonths(periodicidadMeses);
    }

    // ── Método de utilidad: días que faltan para el próximo ajuste ──
    // Devuelve null si no está configurado, negativo si ya venció

    public Long getDiasParaAjuste() {
        java.time.LocalDate proximo = getProximoAjuste();
        if (proximo == null) return null;
        return java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), proximo);
    }
    
}
