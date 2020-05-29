package proyecto.modelo;

/**
 * Subclase de Usuario para los usuarios tipo 'alumno'
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Alumno extends Usuario {

    //Atributos:
    private String iban;

    //Constructores:
    public Alumno() {
    }

    public Alumno(String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono, String direccion, TipoEjercicio tipoEjercicio, String iban) {
        super(password, nombre, apellido1, apellido2, dni, email, telefono, direccion, tipoEjercicio);
        this.iban = iban;
    }

    //Getters y setters:
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }   
}
