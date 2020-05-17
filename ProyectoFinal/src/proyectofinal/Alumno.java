package proyectofinal;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public Alumno(TipoEjercicio tipoEjercicio, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono, String direccion) {
        super(password, nombre, apellido1, apellido2, dni, email, telefono, direccion);
        this.tipoEjercicio = tipoEjercicio;
    }

    //Métodos:
    /**
     * Crea un objeto de tipo alumno a partir de su dni
     *
     * @param dni identificador del usuario tipo alumno
     * @return Alumno objeto tipo alumno
     */
    public static Alumno crearObjetoAlumno(String dni) throws SQLException {
        Alumno alumno = new Alumno();
        ResultSet lineaAlumno = Usuario.buscarUsuarioPorDni(dni);
        lineaAlumno.next();
        alumno.setDni(dni);
        alumno.setNombre(lineaAlumno.getString("nombre"));
        alumno.setApellido1(lineaAlumno.getString("apellido1"));
        alumno.setApellido2(lineaAlumno.getString("apellido2"));
        alumno.setEmail(lineaAlumno.getString("email"));
        alumno.setTelefono(lineaAlumno.getInt("telefono"));
        alumno.setDireccion(lineaAlumno.getString("direccion"));
        lineaAlumno.close();
        return alumno;
    }
    //Getters y setters:

    public TipoEjercicio getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(TipoEjercicio tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }

}
