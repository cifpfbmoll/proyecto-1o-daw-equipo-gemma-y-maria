package proyecto.vista;

import proyecto.controlador.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Clase para el menú y desarrollo principal de la aplicación con método main.
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.18.am
 */
public class MenuPrincipal {

    public static Scanner lector = new Scanner(System.in);
    public static Connection con;
    //Conexión es algo que querremos usar en prácticamente todos los métodos, lo que es la definición de una variable global

    /**
     * Método main: inicia la conexión con la base de datos y llama al menú
     * principal
     *
     * @param args para main
     */
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //La conexión tiene autoclose: con y con2 son el mismo objeto desde dos puntos diferentes.
        //Si se quiere con como variable estática no se puede iniciar en el try().
        try (Connection con2 = Conexion.obtenerConexion()) {
            con = con2;
            String usuario = "";
            do {
                usuario = menuLogIn();
                if (usuario.equals("")) {
                    System.out.println("Usuario inexistente.");
                } else if (usuario.equals("error")) {
                    System.out.println("Contraseña incorrecta.");
                }
            } while ((usuario.equals("")) || (usuario.equals("error")));
            if (ControladorUsuario.comprobarTipoUsuario(usuario) == true) {
                System.out.println("Bienvenido, entrenador. ");
                menuEntrenador(usuario);
            } else {
                System.out.println("Bienvenido, alumno. ");
                menuAlumno(usuario);
            }
        } catch (SQLException sqle) {
            String tituloError = "Error en el menú principal.";
            Utilidades.logErrores(tituloError, Arrays.toString(sqle.getStackTrace()));
            System.out.println(tituloError);
        }
    }

    /**
     * Pide usuario y contraseña y los confirma.
     *
     * @return identificador del usuario
     * @throws java.sql.SQLException
     */
    public static String menuLogIn() throws SQLException {
        String usuario;
        String contrasena;
        System.out.println("Introduce DNI del usuario: ");
        usuario = lector.nextLine();
        if (!ControladorUsuario.comprobarUsuario(usuario)) {
            return "";
        }
        System.out.println("Introduce contraseña: ");
        contrasena = lector.nextLine();
        if (!ControladorUsuario.comprobarContrasena(usuario, contrasena)) {
            return "error";
        }
        return usuario;
    }

    /**
     * Opciones de menú específicas para entrenadores.<br>
     * Los entrenadores pueden elegir ciertas opciones de alumno (gratuitamente, como pago por sus servicios).
     *
     * @param id identificador de usuario
     * @throws java.sql.SQLException
     */
    public static void menuEntrenador(String id) throws SQLException {
        boolean salir = false;
        while (salir == false) {
            System.out.println("");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("  1- crear programa de entrenamiento");
            System.out.println("  2- consultar un programa de entrenamiento existente");
            System.out.println("  3- imprimir un programa de entrenamiento existente");
            System.out.println("  4- como alumno, consultar un programa de entrenamiento existente");
            System.out.println("  5- como alumno, solicitar un nuevo programa de entrenamiento");
            System.out.println("  6- como alumno, imprimir un programa de entrenamiento personal existente");
            System.out.println("  7- registrar a un nuevo alumno");
            System.out.println("  8- registrar un nuevo ejercicio");
            System.out.println("  0- salir");
            System.out.println("Introduce el número de tu selección:");
            String opcion = lector.nextLine();

            switch (opcion) {
                case "1":
                    menuCrearEntrenamiento(id);
                    break;
                case "2":
                    ControladorEntrenamiento.consultarEntrenamientoPorEntrenador(id);
                    break;
                case "3":
                    ControladorEntrenamiento.imprimirEntrenamientoDesdeTablaPorEntrenador(id);
                    break;
                case "4":
                    ControladorEntrenamiento.consultarEntrenamientoPorAlumno(id);
                    break;
                case "5":
                    ControladorEntrenamiento.solicitarEntrenamiento(id);
                    break;
                case "6":
                    ControladorEntrenamiento.imprimirEntrenamientoDesdeTablaPorAlumno(id);
                    break;
                case "7":
                    ControladorAlumno.crearNuevoAlumno();
                    break;
                case "8":
                    ControladorEjercicio.crearNuevoEjercicio();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Adiós.");
                    break;
                default:
                    System.out.println("La opción seleccionada no existe.");
            }
        }
    }

    /**
     * Menú de creación de entrenamientos.
     *
     * @param id identificador del entrenador que crea el programa
     * @throws java.sql.SQLException
     */
    public static void menuCrearEntrenamiento(String id) throws SQLException {
        System.out.println("");
        System.out.println("¿Qué tipo de entrenamiento quieres crear?");
        System.out.println("  1- crear programa nuevo");
        System.out.println("  2- copiar programa existente");
        String opcionCrear = MenuPrincipal.lector.nextLine();

        switch (opcionCrear) {
            case "1":
                ControladorEntrenamiento.crearNuevoEntrenamiento(id);
                break;
            case "2":
                ControladorEntrenamiento.copiarEntrenamiento(id);
                break;
            default:
                System.out.println("La opción seleccionada no existe.");
        }
    }
    
    /**
     * Opciones de menú específicas para alumnos.
     *
     * @param id identificador de usuario
     * @throws java.sql.SQLException
     */
    public static void menuAlumno(String id) throws SQLException {
        boolean salir = false;
        while (salir == false) {
            System.out.println("");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("  1- consultar un programa de entrenamiento existente");
            System.out.println("  2- solicitar un nuevo programa de entrenamiento");
            System.out.println("  3- imprimir un programa de entrenamiento personal existente");
            System.out.println("  0- salir");
            System.out.println("Introduce el número de tu selección:");
            String opcion = lector.nextLine();

            switch (opcion) {
                case "1":
                    ControladorEntrenamiento.consultarEntrenamientoPorAlumno(id);
                    break;
                case "2":
                    ControladorEntrenamiento.solicitarEntrenamiento(id);
                    break;
                case "3":
                    ControladorEntrenamiento.imprimirEntrenamientoDesdeTablaPorAlumno(id);
                    break;
                case "0":
                    salir = true;
                    System.out.println("Adiós.");
                    break;
                default:
                    System.out.println("La opción seleccionada no existe.");
            }
        }
    }
    
}
