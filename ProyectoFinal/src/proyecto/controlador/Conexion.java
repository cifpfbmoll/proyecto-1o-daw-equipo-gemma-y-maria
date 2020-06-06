package proyecto.controlador;

import java.sql.*;

public class Conexion {
    
    public Conexion(){
    }
    
    /**
     * Obtiene la conexión con la base de datos.
     *
     * @return tipo String con la información de conexión a la base de datos
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/programacion";
        String password = "alualualu";
        return DriverManager.getConnection(url, "root", password);
    }
    
}
