package proyectofinal;

import java.sql.PreparedStatement;
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
    public void crearNuevoAlumno() {
        System.out.println("Introduce los siguientes datos del nuevo alumno:");
        super.crearNuevoUsuario();
        //TODO completar con el tipo de ejercicio cuando cambien en la bbdd
    }

    //Al ser un insert, por transacción. Sólo hago sin transacción las meras consultas.
    public void introducirNuevoAlumno() throws SQLException {
        boolean estadoAC = Menu.con.getAutoCommit();
        try {
            Menu.con.setAutoCommit(false);
            String query = "INSERT INTO usuario (DNI, password, discriminador, nombre, apellido1, apellido2, email) VALUES (?, ?, ?, ?, ?, ?, ?);";
            //TODO completar con el resto de campos
            PreparedStatement prepStat = Menu.con.prepareStatement(query);
            prepStat.setString(1, this.getDni());
            prepStat.setString(2, this.getPassword());
            prepStat.setString(3, "alumno");
            prepStat.setString(4, this.getNombre());
            prepStat.setString(5, this.getApellido1());
            prepStat.setString(6, this.getApellido2());
            prepStat.setString(7, this.getEmail());
            //TODO acabar de completar los valores q faltan
            prepStat.execute();
            Menu.con.commit();
            System.out.println("Nuevo alumno (" + this.getNombre() + " " + this.getApellido1() + ") introducido correctamente.");
            prepStat.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error en la introducción del alumno.");
            Menu.con.rollback();
        } finally {
            Menu.con.setAutoCommit(estadoAC);
        }
    }

    /**
     * Crea un objeto de tipo alumno a partir de su dni (tomando datos de la BD)
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
