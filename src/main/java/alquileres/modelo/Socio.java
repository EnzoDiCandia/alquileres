package alquileres.modelo;

public class Socio {
    private int id;
    private String nombre;
    private String email;
    private boolean activo;
    private String password;

    // CONSTRUCTOR VACÍO
    public Socio() {
    }

    // CONSTRUCTOR COMPLETO
    public Socio(int id, String nombre, String email, boolean activo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.password = password;

    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}