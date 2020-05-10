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

    public Alumno(TipoEjercicio tipoEjercicio, String id, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono) {
        super(id, password, nombre, apellido1, apellido2, dni, email, telefono);
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
