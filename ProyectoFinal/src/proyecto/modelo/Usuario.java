package proyecto.modelo;

import proyecto.controlador.Utilidades;

/**
 * Superclase abstracta con los atributos básicos de cualquier usuario (sea
 * entrenador o alumno) que se conecta a la aplicación.
 *
 * @author Gemma Díez Cabeza y María Rabanales González
 * @version 20.05.10.am
 */
public abstract class Usuario {
    //Atributos:
    private String dni;
    private String password;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private int telefono;
    private String direccion;
    private TipoEjercicio tipoEjercicio;

    //Constructores:
    public Usuario() {
    }

    public Usuario(String dni, String password, String nombre, String apellido1, String apellido2, String email, int telefono, String direccion, TipoEjercicio tipoEjercicio) {
        this.password = password;
        this.setNombre(nombre);
        this.setApellido1(apellido1);
        this.setApellido2(apellido2);
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipoEjercicio = tipoEjercicio;
    }
    
    //Getters y setters:
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
        this.nombre = Utilidades.adaptarStringMayusMinus(nombre);
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = Utilidades.adaptarStringMayusMinus(apellido1);
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = Utilidades.adaptarStringMayusMinus(apellido2);
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoEjercicio getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(TipoEjercicio tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }

}