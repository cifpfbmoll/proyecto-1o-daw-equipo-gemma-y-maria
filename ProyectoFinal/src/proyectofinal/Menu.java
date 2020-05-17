package proyectofinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase para el menú y desarrollo principal de la aplicación con método main.
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
//TODO decidir qué tratamiento damos a las excepciones
public class Menu {

    protected static Scanner lector = new Scanner(System.in);
    protected static Connection con;
    //Conexión es algo que querremos usar en prácticamente todos los métodos, lo que es la definición de una variable global

    /**
     * Método main: inicia la conexión con la base de datos y llama al menú
     * principal
     *
     * @param args para main
     */
    public static void main(String[] args) {
        //La conexión tiene autoclose: con y con2 son el mismo objeto desde dos puntos diferentes.
        //Si se quiere con como variable estática no se puede iniciar en el try().
        try (Connection con2 = obtenerConexion()) {
            con = con2;
            String usuario = "";
            do {
                usuario = menuLogIn();
            } while (usuario.equals(""));
            if (comprobarEntrenador(usuario) == true) {
                menuEntrenador(usuario);
            } else {
                menuAlumno(usuario);
            }
            //TODO: si el usuario es instance of entrenador, menuEntrenador. Else menuAlumno

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Pide usuario y contraseña y los confirma.
     *
     * @return identificador del usuario
     */
    public static String menuLogIn() {
        String usuario = "";
        do {
            System.out.println("Introduce usuario: ");
            usuario = lector.nextLine();
        } while (!comprobarValidez(usuario));
        System.out.println("Introduce contraseña: ");
        String contrasena = lector.nextLine();
        if (!comprobarValidez(contrasena)) {
            System.out.println("La contraseña no es válida para este usuario.");
            return "";
        }
        return usuario;
    }

    /**
     * Comprueba qué tipo de usuario es.
     *
     * @param id identificador de usuario
     * @return true si es entrenador, false si es alumno
     */
    public static boolean comprobarEntrenador(String id) throws SQLException {
        boolean esEntrenador = false;
        String query = "SELECT discriminador FROM usuario WHERE ID = ?;";
        PreparedStatement prepStat = con.prepareStatement(query);
        prepStat.setString(1, id);
        ResultSet queryResult = prepStat.executeQuery();
        if (queryResult.next()) {               //Comprobando que no me lo devuelva vacío.
            if (queryResult.getString("discriminador").equals("entrenador")) {
                esEntrenador = true;
            }
        }
        queryResult.close();
        prepStat.close();
        return esEntrenador;
    }

    /**
     * Opciones de menú específicas para entrenadores.
     *
     * @param id identificador de usuario
     */
    public static void menuEntrenador(String id) throws SQLException{
        System.out.println("Bienvenido, entrenador.\n¿Qué quieres hacer?");
        System.out.println("  1- crear programa de entrenamiento");
        System.out.println("  2- consultar un programa de entrenamiento existente");
        System.out.println("  3- imprimir un programa de entrenamiento existente");
        System.out.println("  4- como alumno, consultar un programa de entrenamiento existente");
        System.out.println("  5- como alumno, solicitar un nuevo programa de entrenamiento");
        System.out.println("  6- como alumno, imprimir un programa de entrenamiento personal existente");
        System.out.println("  7- registrar a un nuevo alumno");
        System.out.println("Introduce el número de tu selección:");
        String opcion = lector.nextLine();

        switch (opcion) {
            case "1":
                Entrenamiento.crearEntrenamiento(id);
                break;
            case "2":
                Entrenamiento.consultarEntrenamientoPorEntrenador(id);
                break;
            case "3":
                //TODO
                break;
            case "4":
                Entrenamiento.consultarEntrenamientoPorAlumno(id);
                break;
            case "5":
                //TODO
                break;
            case "6":
                //TODO
                break;
            case "7":
                //TODO
                break;
            default:
                System.out.println("La opción seleccionada no existe.");
        }
    }

    /**
     * Opciones de menú específicas para alumnos; un entrenador puede ser alumno
     * de otros entrenadores..
     *
     * @param id identificador de usuario
     */
    public static void menuAlumno(String id) throws SQLException{
        System.out.println("Bienvenido, alumno.\n¿Qué quieres hacer?");
        System.out.println("  1- consultar un programa de entrenamiento existente");
        System.out.println("  2- solicitar un nuevo programa de entrenamiento");
        System.out.println("  3- imprimir un programa de entrenamiento personal existente");
        System.out.println("Introduce el número de tu selección:");
        String opcion = lector.nextLine();

        switch (opcion) {
            case "1":
                Entrenamiento.consultarEntrenamientoPorAlumno(id);
                break;
            case "2":
                //TODO
                break;
            case "3":
                //TODO
                break;
            default:
                System.out.println("La opción seleccionada no existe.");
        }
    }

    /**
     * Comprueba si el valor que se pasa existe en tablas.
     *
     * @param texto cuya validez y/o existencia en tablas se desea comprobar
     * @return verdadero si está en tablas
     */
    public static boolean comprobarValidez(String texto) {
        boolean textoValido = false;
        //TODO comparar con bbdd
        return textoValido;
    }

    /**
     * Obtiene la conexión con la base de datos.
     *
     * @return tipo String con la información de conexión a la base de datos
     * @throws SQLException
     */
    public static Connection obtenerConexion() throws SQLException {
        //TODO completar; el puerto default para MySQL es 3306
        String url = "jdbc:mysql://localhost:3306/programacion";
        String password = "alualualu";
        return DriverManager.getConnection(url, "root", password);
    }
}
