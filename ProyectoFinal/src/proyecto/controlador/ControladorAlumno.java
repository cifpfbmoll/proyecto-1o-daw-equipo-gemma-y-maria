package proyecto.controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import proyecto.modelo.Alumno;
import proyecto.modelo.Entrenamiento;
import proyecto.modelo.TipoEjercicio;
import proyecto.vista.MenuPrincipal;

public class ControladorAlumno extends ControladorUsuario{
    
    /**
     * Solicita datos para crear un nuevo alumno.
     * @throws SQLException 
     */
    public static void crearNuevoAlumno() throws SQLException{
        Alumno alu = new Alumno();
        System.out.println("Introduce los siguientes datos del nuevo alumno:");
        crearNuevoUsuario(alu);
        String userIban = "";
        do {
            System.out.println("  -IBAN para domiciliacion:");
            userIban = MenuPrincipal.lector.nextLine().toUpperCase().trim();
        } while (!validarIban(userIban));
        alu.setIban(userIban);
        introducirNuevoAlumno(alu);
        System.out.println("¿Deseas establecer un tipo de entrenamiento solicitado para " + alu.getNombre()+ "?");
        System.out.println("  1- sí");
        System.out.println("  2- no");
        String opcionUserPrograma = MenuPrincipal.lector.nextLine().trim();
        if (opcionUserPrograma.equals("1")) {
            ControladorEntrenamiento.solicitarEntrenamiento(alu.getDni());
        }
        System.out.println("Alumno creado con éxito.");
    }
    
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
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(queryUsuario);
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
        return user;
    }

    //Al ser un insert, por transacción. Sólo hago sin transacción las meras consultas.
    /**
     * Pasa un objeto Alumno a la tabla
     * @throws SQLException 
     */
    public static void introducirNuevoAlumno(Alumno alu) throws SQLException {
        boolean estadoAC = MenuPrincipal.con.getAutoCommit();
        try {
            MenuPrincipal.con.setAutoCommit(false);
            String query = "INSERT INTO usuario (DNI, password, discriminador, nombre, apellido1, apellido2, email, telefono, direccion, IBAN) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setString(1, alu.getDni());
            prepStat.setString(2, alu.getPassword());
            prepStat.setString(3, "alumno");
            prepStat.setString(4, alu.getNombre());
            prepStat.setString(5, alu.getApellido1());
            prepStat.setString(6, alu.getApellido2());
            prepStat.setString(7, alu.getEmail());
            prepStat.setInt(8, alu.getTelefono());
            prepStat.setString(9, alu.getDireccion());
            prepStat.setString(10, alu.getIban());
            prepStat.execute();
            MenuPrincipal.con.commit();
            System.out.println("Nuevo alumno (" + alu.getNombre() + " " + alu.getApellido1() + ") introducido correctamente.");
            prepStat.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error en la introducción del alumno.");
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
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
        ResultSet lineaAlumno = buscarUsuarioPorDni(dni);
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
}
