package alquileres.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Map;

/**
 * Convierte los errores de los controllers en respuestas JSON {ok:false, mensaje}
 * que el frontend muestra al usuario. Los errores de base se registran en el log
 * pero no se envía el detalle técnico al navegador.
 */
@RestControllerAdvice
public class ManejoErrores {

    // Datos inválidos (validaciones de Validar, números mal escritos, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> datosInvalidos(IllegalArgumentException e) {
        return Map.of("ok", false, "mensaje", String.valueOf(e.getMessage()));
    }

    // JSON con formato incorrecto (por ejemplo una fecha mal escrita)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> jsonInvalido(HttpMessageNotReadableException e) {
        return Map.of("ok", false, "mensaje", "Datos inválidos: revisá los campos del formulario");
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> errorBase(SQLException e) {
        e.printStackTrace();
        return Map.of("ok", false, "mensaje", "Error al acceder a la base de datos");
    }
}
