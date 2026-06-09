package alquileres.modelo;

public class Inquilino {
    private Integer id;
    private String nombre;
    private String telefono;
    private String email;
    private String documento;
    private Boolean activo;

//CONSTRUCTOR VACIO

public Inquilino(){
}

public Inquilino(Integer id , String nombre, String telefono , String email, String documento , Boolean activo){
    this.id=id;
    this.nombre=nombre;
    this.telefono=telefono;
    this.email=email;
    this.documento=documento;
    this.activo=activo;

}

            // SETTERS Y GETTERS

public void setId(Integer id){
    this.id=id;
}

public int getId(){
    return id;
}

public void setNombre(String nombre){
    this.nombre=nombre;
}

public String getNombre(){
    return nombre;
}

public void setTelefono (String telefono){
    this.telefono=telefono;
}

public String getTelefono(){
    return telefono;
}

public void setEmail(String email){
    this.email=email;
}

public String getEmail(){
    return email;
}

public void setDocumento (String documento){
    this.documento=documento;
}

public String getDocumento(){
    return documento;
}


public void setActivo(Boolean activo){
    this.activo=activo;
}

public boolean isActivo(){
    return activo;
}

}


