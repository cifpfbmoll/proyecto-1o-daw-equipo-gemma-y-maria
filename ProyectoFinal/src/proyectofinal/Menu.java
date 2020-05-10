package proyectofinal;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public static Scanner lector = new Scanner(System.in);

    /**
     * Método main: inicia la conexión con la base de datos y llama al menú
     * principal
     *
     * @param args para main
     */
    public static void main(String[] args) {
        // TODO completar
        //Estre try tiene autoclose de manera que la conexión se cierra sola
        try (Connection con = obtenerConexion()) {
            boolean usuarioValido = false;
            do {
                usuarioValido = menuLogIn();
            } while (usuarioValido == false);
            //TODO: si el usuario es instance of entrenador, menuEntrenador. Else menuAlumno
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Pide usuario y contraseña y los confirma.
     */
    public static boolean menuLogIn() {
        String usuario = "";
        do {
            System.out.println("Introduce usuario: ");
            usuario = lector.nextLine();
        } while (!comprobarValidez(usuario));
        System.out.println("Introduce contraseña: ");
        String contrasena = lector.nextLine();
        if (!comprobarValidez(contrasena)) {
            System.out.println("La contraseña no es válida para este usuario.");
            return false;
        }
        return true;
    }
    
    /**
     * Comprueba si el valor que se pasa existe en tablas.
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
     * @return tipo String con la información de conexión a la base de datos
     * @throws SQLException
     */
    public static Connection obtenerConexion() throws SQLException {
        //TODO completar; el puerto default para MySQL es 3306
        String url = "jdbc:mysql://localhost:3306/NOMBREDENUESTRABD";
        String password = "loquesea";
        return DriverManager.getConnection(url, "root", password);
    }
}
