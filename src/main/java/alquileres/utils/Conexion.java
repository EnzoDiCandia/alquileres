package alquileres.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Conexion {

    // Pool de conexiones: se crea una sola vez y las conexiones se reutilizan.
    // Los DAO siguen usando getConexion() igual que antes; al cerrar la conexión
    // (try-with-resources) vuelve al pool en lugar de cerrarse de verdad.
    private static HikariDataSource pool;

    public static Connection getConexion() throws SQLException {
        return getPool().getConnection();
    }

    private static synchronized HikariDataSource getPool() {
        if (pool == null) {
            String url = System.getenv("SPRING_DATASOURCE_URL");
            String user = System.getenv("SPRING_DATASOURCE_USERNAME");
            String password = System.getenv("SPRING_DATASOURCE_PASSWORD");

            if (url == null) url = System.getProperty("SPRING_DATASOURCE_URL");
            if (user == null) user = System.getProperty("SPRING_DATASOURCE_USERNAME");
            if (password == null) password = System.getProperty("SPRING_DATASOURCE_PASSWORD");

            if (url == null) url = "jdbc:mysql://localhost:3306/alquileres_db?trustServerCertificate=true";
            if (user == null) user = "root";
            if (password == null) password = "root";

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setPoolName("alquileres");
            config.setMaximumPoolSize(5);   // suficiente para pocos usuarios y respeta el límite de la base
            config.setMinimumIdle(1);
            pool = new HikariDataSource(config);
        }
        return pool;
    }
}
