package proyectofinal;

/**
 * Subclase de Usuario para los usuarios tipo 'alumno'
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Alumno extends Usuario {

    //Atributos:
    private TipoEjercicio tipoEjercicio;

    //Constructores:
    public Alumno() {
    }

    public Alumno(TipoEjercicio tipoEjercicio, int id, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono, String direccion) {
        super(id, password, nombre, apellido1, apellido2, dni, email, telefono, direccion);
        this.tipoEjercicio = tipoEjercicio;
    }

    //Métodos:
    //Getters y setters:

    public TipoEjercicio getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(TipoEjercicio tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }
    
}
