package alquileres.modelo;

public class Propiedad {
    private int id;
    private String nombre;
    private String direccion;
    private String tipo;
    private boolean activo;




    //CONSTRUCTOR VACIO
    public Propiedad (){
    }

    // CONSTRUCTOR SIN ID PARA CUANDO SE GENERA AUTOMATICO EN BD
    public Propiedad (String nombre, String direccion, String tipo,Boolean activo){
        this.nombre=nombre;
        this.direccion=direccion;
        this.tipo=tipo;
        this.activo=activo;
    }

    // CONSTRUCTOR COMPLETO PARA RECUPERAR DE BD
    public Propiedad (int id,String nombre, String direccion, String tipo,Boolean activo ){
        this.id=id;
        this.nombre=nombre;
        this.direccion=direccion;
        this.tipo=tipo;
        this.activo=activo;
    }


                                                            //GETTERS Y SETTERS

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
// PARA Boolean SE USA IS EN VEZ DE GET

    public boolean isActivo() {  
        return activo;
    }

    public void setActivo(boolean activo){
        this.activo=activo;
    }


    // TOSTRING PARA MOSTRAR LA PROPIEDAD
    @Override
    public String toString(){
        return "ID: " + id + " | " + nombre + " | " + direccion + " | " + tipo + " | Activo: " + activo;
    }
}



