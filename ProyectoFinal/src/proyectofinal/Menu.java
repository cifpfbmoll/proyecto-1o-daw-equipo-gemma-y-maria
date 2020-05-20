package proyectofinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para el menú y desarrollo principal de la aplicación con método main.
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.18.am
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
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public static String menuLogIn() throws SQLException {
        String usuario = "";
        String contrasena = "";
        do {
            System.out.println("Introduce DNI del usuario: ");
            usuario = lector.nextLine();
            contrasena = comprobarUsuario(usuario);
            if (contrasena.equals("error")) {
                System.out.println("Este usuario no existe.");
            }
        } while (contrasena.equals("error"));
        System.out.println("Introduce contraseña: ");
        String password = lector.nextLine();
        if (!password.equals(contrasena)) {
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
        String query = "SELECT discriminador FROM USUARIO WHERE DNI = ?;";
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
    public static void menuEntrenador(String id) throws SQLException {
        boolean salir = false;
        while (salir == false) {
            System.out.println("Bienvenido, entrenador.\n¿Qué quieres hacer?");
            System.out.println("  1- crear programa de entrenamiento");
            System.out.println("  2- consultar un programa de entrenamiento existente");
            System.out.println("  3- imprimir un programa de entrenamiento existente");
            System.out.println("  4- como alumno, consultar un programa de entrenamiento existente");
            System.out.println("  5- como alumno, solicitar un nuevo programa de entrenamiento");
            System.out.println("  6- como alumno, imprimir un programa de entrenamiento personal existente");
            System.out.println("  7- registrar a un nuevo alumno");
            //TODO mejora final: admin q cree entrenadores tambien
            System.out.println("  0- salir");
            System.out.println("Introduce el número de tu selección:");
            String opcion = lector.nextLine();

            switch (opcion) {
                case "1":
                    Entrenamiento.menuCrearEntrenamiento(id);
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
                    solicitarEntrenamiento(id);
                    break;
                case "6":
                    //TODO
                    break;
                case "7":
                    Alumno alu = new Alumno();
                    alu.crearNuevoAlumno();
                    alu.introducirNuevoAlumno();
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
     * Opciones de menú específicas para alumnos.
     *
     * @param id identificador de usuario
     */
    public static void menuAlumno(String id) throws SQLException {
        boolean salir = false;
        while (salir == false) {
            System.out.println("Bienvenido, alumno.\n¿Qué quieres hacer?");
            System.out.println("  1- consultar un programa de entrenamiento existente");
            System.out.println("  2- solicitar un nuevo programa de entrenamiento");
            System.out.println("  3- imprimir un programa de entrenamiento personal existente");
            System.out.println("  0- salir");
            System.out.println("Introduce el número de tu selección:");
            String opcion = lector.nextLine();

            switch (opcion) {
                case "1":
                    Entrenamiento.consultarEntrenamientoPorAlumno(id);
                    break;
                case "2":
                    solicitarEntrenamiento(id);
                    break;
                case "3":
                    //TODO
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
     * Comprueba si el valor que se pasa existe en tablas.
     *
     * @param texto cuya validez y/o existencia en tablas se desea comprobar
     * @return verdadero si está en tablas
     */
    public static String comprobarUsuario(String texto) throws SQLException {
        String password = "error";
        String query = "SELECT password FROM USUARIO WHERE DNI = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        prepStat.setString(1, texto);
        ResultSet queryResult = prepStat.executeQuery();
        //Como no tenemos método isEmpty, la solución es:
        if (queryResult.next() != false) {
            password = queryResult.getString("password");
        }
        //lógica de esto: si existe un resultado de esta búsqueda, entonces existe la entrada: es válida
        if (queryResult != null) {
            queryResult.close();
        }
        prepStat.close();
        //aprovecho para obtener la contraseña y así no tengo que hacer la búsqueda mil veces
        return password;
    }

    //TODO plantear si es mejor pasar esto a usuario
    public static void solicitarEntrenamiento(String id) throws SQLException {
        System.out.println("¿Qué tipo de entrenamiento quieres solicitar?");
        for (int i = 0; i < TipoEjercicio.values().length; i++) {
            EnumSet.allOf(TipoEjercicio.class)
                    .forEach(tipo -> System.out.println("  -" + tipo + ":" + tipo.getTextoTipoEjercicio()));
        }
        System.out.println("Introduce el código de la opción elegida:");
        String opcionTipo = lector.nextLine();
        while (!TipoEjercicio.comprobarTipo(opcionTipo)) {
            System.out.println("Error en la selección.");
            System.out.println("Introduce de nuevo el código de la opción elegida:");
            opcionTipo = lector.nextLine();
        }
        guardarSolicitudEntrenamiento(id, opcionTipo);
    }

    public static void guardarSolicitudEntrenamiento(String id, String tipo) throws SQLException {
        boolean estadoAC = Menu.con.getAutoCommit();
        //TODO confirmar q no ha solicitado un programa anteriormente
        try {
            Menu.con.setAutoCommit(false);
            String query = "UPDATE usuario SET tipo_prog_solicitado = ? where DNI = ?;";
            PreparedStatement prepStat = Menu.con.prepareStatement(query);
            prepStat.setString(1, tipo);
            prepStat.setString(2, id);
            prepStat.execute();
            Menu.con.commit();
            System.out.println("Solicitud de programa de entrenamiento realizada.");
            prepStat.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error en la solicitud.");
            Menu.con.rollback();
        } finally {
            Menu.con.setAutoCommit(estadoAC);
        }
    }

    public static void verSolicitudesEntrenamiento() throws SQLException {
        //realmente para esto no hace falta que sea preparedstatement porque no le meto valores... TODO cambiar por statement normal
        String query = "SELECT DNI, nombre, apellido1, apellido2, tipo_prog_solicitado FROM usuario WHERE tipo_prog_solicitado is not NULL;";
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        ResultSet queryResult = prepStat.executeQuery();
        System.out.println("Entrenamientos solicitados actualmente:");
        while (queryResult.next()) {
            //TODO asociar el tipo con el enum
            System.out.println("  -tipo " + queryResult.getString("tipo_prog_solicitado") + " para alumno " + queryResult.getString("nombre") + " " 
                    + queryResult.getString("apellido1") + " " + queryResult.getString("apellido2") + " (con DNI " + queryResult.getString("DNI") + ")");           
        }
        queryResult.close();
        prepStat.close();
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
