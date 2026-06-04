package alquileres.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection getConexion() throws SQLException {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        String user = System.getenv("SPRING_DATASOURCE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");

        if (url == null) url = "jdbc:mysql://localhost:3306/alquileres_db?sslMode=REQUIRED&trustServerCertificate=true";
        if (user == null) user = "root";
        if (password == null) password = "root";

        return DriverManager.getConnection(url, user, password);
    }
}