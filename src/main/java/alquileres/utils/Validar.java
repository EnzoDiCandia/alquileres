package alquileres.utils;

/**
 * Validaciones simples de datos que llegan del frontend.
 * Si algo no es válido lanza IllegalArgumentException con un mensaje claro,
 * que ManejoErrores devuelve como 400 {ok:false, mensaje}.
 */
public class Validar {

    public static void requerido(Object valor, String campo) {
        if (valor == null || (valor instanceof String s && s.isBlank())) {
            throw new IllegalArgumentException("Falta completar: " + campo);
        }
    }

    public static void positivo(Number valor, String campo) {
        requerido(valor, campo);
        if (valor.doubleValue() <= 0) {
            throw new IllegalArgumentException(campo + " debe ser mayor a cero");
        }
    }

    public static void mesYAnio(Integer mes, Integer anio) {
        requerido(mes, "mes de referencia");
        requerido(anio, "año de referencia");
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes de referencia debe estar entre 1 y 12");
        }
    }
}
