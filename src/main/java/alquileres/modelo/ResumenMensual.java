package alquileres.modelo;

public class ResumenMensual {
    private int anio;
    private int mes;
    private double totalIngresos;
    private double totalRetiros;
    private double saldo;

    public ResumenMensual(int anio, int mes, double totalIngresos, double totalRetiros) {
        this.anio = anio;
        this.mes = mes;
        this.totalIngresos = totalIngresos;
        this.totalRetiros = totalRetiros;
        this.saldo = totalIngresos - totalRetiros;
    }

    public int getAnio() {
        return anio;
    }

    public int getMes() {
        return mes;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public double getTotalRetiros() {
        return totalRetiros;
    }

    public double getSaldo() {
        return saldo;
    }
}