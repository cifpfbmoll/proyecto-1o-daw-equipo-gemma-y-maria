package proyectofinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase para el menú y desarrollo principal de la aplicación con método main.
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Menu {
    
    public static Scanner lector = new Scanner(System.in);
    
    public static void main(String[] args) {
        // TODO completar
        //Estre try tiene autoclose de manera que la conexión se cierra sola
        try (Connection con = obtenerConexion()) {
        
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
    }
    
    
    /**
     * Obtiene la conexión con la base de datos
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
