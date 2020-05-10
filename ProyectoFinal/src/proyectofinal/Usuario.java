package proyectofinal;

/**
 * Superclase abstracta con los atributos básicos de cualquier usuario (sea entrenador o alumno= que se conecta a la aplicación.
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public abstract class Usuario {
    
    //Atributos:
    private String id;
    private String password;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String dni;     //TODO preparar validación para num/letra
    private String email;
    private int telefono;
    
    //Constructores:
    public Usuario() {
    }

    public Usuario(String id, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono) {
        this.id = id;
        this.password = password;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
    }
    
    //Métodos:
    //Getters y setters:
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    
    
    
    
}
