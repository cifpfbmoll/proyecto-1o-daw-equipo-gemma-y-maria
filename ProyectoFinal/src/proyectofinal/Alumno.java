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
    private String iban;

    //Constructores:
    public Alumno() {
    }

    public Alumno(String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono, String direccion, TipoEjercicio tipoEjercicio, String iban) {
        super(password, nombre, apellido1, apellido2, dni, email, telefono, direccion, tipoEjercicio);
        this.iban = iban;
    }

    //Métodos:
    /**
     * Crea un objeto tipo Alumno desde valores obtenidos en tablas.<br> Se
     * utiliza para varias funciones.
     *
     * @param id DNI del alumno
     * @return objeto tipo Alumno
     * @throws SQLException
     */
    public static Alumno generarAlumnoDesdeTabla(String id) throws SQLException {
        Alumno user = new Alumno();
        String queryUsuario = "SELECT * FROM usuario WHERE DNI = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(queryUsuario);
        prepStat.setString(1, id);
        ResultSet results = prepStat.executeQuery();
        results.next();
        user.setDni(id);
        user.setNombre(results.getString("nombre"));
        user.setPassword(results.getString("password"));
        user.setApellido1(results.getString("apellido1"));
        user.setApellido2(results.getString("apellido2"));
        user.setEmail(results.getString("email"));
        user.setTelefono(results.getInt("telefono"));
        user.setDireccion(results.getString("direccion"));
        String tipoDeEjercicio = results.getString("tipo_prog_solicitado");
        if (tipoDeEjercicio != null) {
            user.setTipoEjercicio(TipoEjercicio.valueOf(tipoDeEjercicio));
        }
        results.close();
        prepStat.close();
        //Nota: casi todo lo anterior se podría generalizar en usuario. Mejorar si hay tiempo.
        return user;
    }

    /**
     * Solicita datos para crear un nuevo alumno.
     * @throws SQLException 
     */
    public static void crearNuevoAlumno() throws SQLException{
        Alumno alu = new Alumno();
        System.out.println("Introduce los siguientes datos del nuevo alumno:");
        alu.crearNuevoUsuario();
        System.out.println("  -IBAN para domiciliacion:");
        String userIban = Menu.lector.nextLine();
        alu.setIban(userIban);
        alu.introducirNuevoAlumno();
        System.out.println("¿Deseas establecer un tipo de entrenamiento solicitado para " + alu.getNombre()+ "?");
        System.out.println("  1- sí");
        System.out.println("  2- no");
        String opcionUserPrograma = Menu.lector.nextLine().trim();
        if (opcionUserPrograma.equals("1")) {
            Menu.solicitarEntrenamiento(alu.getDni());
        }
        System.out.println("Alumno creado con éxito.");
    }

    //Al ser un insert, por transacción. Sólo hago sin transacción las meras consultas.
    /**
     * Pasa un objeto Alumno a la tabla
     * @throws SQLException 
     */
    public void introducirNuevoAlumno() throws SQLException {
        boolean estadoAC = Menu.con.getAutoCommit();
        try {
            Menu.con.setAutoCommit(false);
            String query = "INSERT INTO usuario (DNI, password, discriminador, nombre, apellido1, apellido2, email, telefono, direccion, IBAN) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepStat = Menu.con.prepareStatement(query);
            prepStat.setString(1, this.getDni());
            prepStat.setString(2, this.getPassword());
            prepStat.setString(3, "alumno");
            prepStat.setString(4, this.getNombre());
            prepStat.setString(5, this.getApellido1());
            prepStat.setString(6, this.getApellido2());
            prepStat.setString(7, this.getEmail());
            prepStat.setInt(8, this.getTelefono());
            prepStat.setString(9, this.getDireccion());
            prepStat.setString(10, this.getIban());
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
        alumno.setIban("IBAN");
        lineaAlumno.close();
        return alumno;
    }

    public static TipoEjercicio obtenerSolicitudAlumno(String idAlumno) throws SQLException {
        String query = "SELECT tipo_prog_solicitado FROM usuario WHERE DNI = ?;";      //sé que el último que he creado es el mayor, por serial
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        prepStat.setString(1, idAlumno);
        ResultSet results = prepStat.executeQuery();
        results.next();
        String tipoString = results.getString(1);
        results.close();
        prepStat.close();
        if (tipoString != null) {
            return TipoEjercicio.valueOf(tipoString);
        }
        return null;
    }
    
    //Getters y setters:
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }   
}
