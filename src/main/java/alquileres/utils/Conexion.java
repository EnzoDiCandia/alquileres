package alquileres.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection getConexion() throws SQLException {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        String user = System.getenv("SPRING_DATASOURCE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");

        if (url == null) url = System.getProperty("SPRING_DATASOURCE_URL");
        if (user == null) user = System.getProperty("SPRING_DATASOURCE_USERNAME");
        if (password == null) password = System.getProperty("SPRING_DATASOURCE_PASSWORD");

        if (url == null) url = "jdbc:mysql://localhost:3306/alquileres_db?trustServerCertificate=true";
        if (user == null) user = "root";
        if (password == null) password = "root";

        return DriverManager.getConnection(url, user, password);
    }
}