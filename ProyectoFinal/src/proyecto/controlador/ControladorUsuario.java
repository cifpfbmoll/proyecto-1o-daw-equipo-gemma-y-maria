package proyecto.controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import proyecto.vista.MenuPrincipal;
import proyecto.modelo.*;

/**
 * Incluye los métodos que trabajan con objetos y procesos relacionados con la clase modelo.Usuario
 */
public class ControladorUsuario {
    /**
     * Permite encontrar un usuario en base a su DNI.
     *
     * @param dni
     * @return queryResult (tipoResultSet)
     * @throws SQLException
     */
    public static ResultSet buscarUsuarioPorDni(String dni) throws SQLException {
        String query = "SELECT * FROM usuario WHERE DNI = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        prepStat.setString(1, dni);
        ResultSet queryResult = prepStat.executeQuery();
        //Nota: queryResult no se debe cerrar aquí sino en el método que lo llama
        prepStat.close();
        return queryResult;
    }

    /**
     * Comprueba si el valor que se pasa existe en tablas.
     *
     * @param id cuya validez y/o existencia en tablas se desea comprobar
     * @return verdadero si está en tablas
     */
    public static boolean comprobarUsuario(String id) throws SQLException {
        boolean usuarioExistente = false;
        String query = "SELECT count(*) as cuenta FROM USUARIO WHERE DNI = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        prepStat.setString(1, id);
        ResultSet queryResult = prepStat.executeQuery();
        queryResult.next();
        if (queryResult.getInt("cuenta") > 0) {
            usuarioExistente = true;
        }
        queryResult.close();
        prepStat.close();
        return usuarioExistente;
    }
    
    /**
     * Comprueba si la contraseña es la de el usuario que se pasa.
     *
     * @param id cuya validez y/o existencia en tablas se desea comprobar
     * @param pw contraseña propuesta para el usuario
     * @return verdadero si está en tablas
     */
    public static boolean comprobarContrasena(String id, String pw) throws SQLException {
        boolean pwUsuario = false;
        String query = "SELECT count(*) as cuenta FROM USUARIO WHERE DNI = ? AND password = SHA2(?, 256);";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        prepStat.setString(1, id);
        prepStat.setString(2, pw);
        ResultSet queryResult = prepStat.executeQuery();
        queryResult.next();
        if (queryResult.getInt("cuenta") > 0) {
            pwUsuario = true;
        }
        queryResult.close();
        prepStat.close();
        return pwUsuario;
    }
    
    /**
     * Comprueba qué tipo de usuario es, y devuelve true si es entrenador.
     *
     * @param id identificador de usuario
     * @return true si es entrenador, false si es alumno
     */
    public static boolean comprobarTipoUsuario(String id) throws SQLException {
        String query = "SELECT discriminador FROM USUARIO WHERE DNI = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        prepStat.setString(1, id);
        ResultSet queryResult = prepStat.executeQuery();
        if (queryResult.next()) {               //Comprobando que no me lo devuelva vacío.
            if (queryResult.getString("discriminador").equals("entrenador")) {
                return true;
            }
        }
        queryResult.close();
        prepStat.close();
        return false;
    }
    
    /**
     * Pide los datos del nuevo usuario por consola y los setea en el objeto que devuelve.
     */
    public static void crearNuevoUsuario(Usuario user) {
        String userDni = "";
        do {
            System.out.println("  -dni:");
            userDni = MenuPrincipal.lector.nextLine().toUpperCase().trim();
        } while (!validarDni(userDni));
        user.setDni(userDni);
        System.out.println("  -contraseña:");
        String userPw = MenuPrincipal.lector.nextLine().toLowerCase().trim();
        user.setPassword(userPw);
        System.out.println("  -nombre:");
        String userNombre = MenuPrincipal.lector.nextLine();
        user.setNombre(userNombre);
        System.out.println("  -primer apellido:");
        String userApellido1 = MenuPrincipal.lector.nextLine();
        user.setApellido1(userApellido1);
        System.out.println("  -segundo apellido:");
        String userApellido2 = MenuPrincipal.lector.nextLine();
        user.setApellido2(userApellido2);
        System.out.println("  -e-mail:");
        String userEmail = MenuPrincipal.lector.nextLine();
        user.setEmail(userEmail);
        System.out.println("  -telefono:");
        int userPhone = Integer.parseInt(MenuPrincipal.lector.nextLine());
        user.setTelefono(userPhone);
        System.out.println("  -direccion:");
        String userAdress = MenuPrincipal.lector.nextLine();
        user.setDireccion(userAdress);
    }

    /**
     * Es una validación básica, no buscamos que la letra coincida porque los datos preintroducidos son inventados.<br>
     * Dos comprobaciones: que los ocho primeros caracteres sean números (ascii entre 48 y 57) y que el último sea letra.
     * Sólo consideramos DNI, no NIE ni pasaportes.
     * @param dni
     * @return true si cumple los requisitos de validez
     */
    public static boolean validarDni(String dni){
        boolean dniValido = true;
        if (dni.length() != 9 || !Character.isLetter(dni.charAt(8))) {
            dniValido = false;
        } else {
            for (int i = 0; i < dni.length() - 1; i++) {
                int numAscii = dni.codePointAt(i);
                boolean numValido = (numAscii > 47 && numAscii < 58);
                if (!numValido) {
                    dniValido = false;
                }
            }
        }
        if (!dniValido) {
            System.out.println("Este DNI no cumple los requisitos.");
        }
        return dniValido;
    }
    
    /**
     * Obtiene el tipo de programa que ha solicitado un alumno concreto
     * 
     * @param idAlumno identificador del alumno
     * @return tipo de programa solicitado (objeto TipoEjercicio)
     * @throws SQLException 
     */ 
    public static TipoEjercicio obtenerSolicitudUsuario(String idAlumno) throws SQLException {
        String query = "SELECT tipo_prog_solicitado FROM usuario WHERE DNI = ?;";      //sé que el último que he creado es el mayor, por serial
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
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
    
    /**
     * Para un usuario, comprueba si ha creado o tiene solicitado algun ejercicio.
     * @param idAlumno
     * @param idEntrenador
     * @return false si no tiene ningún ejercicio guardado.
     */
    public static boolean comprobarNumEjUsuario(String idAlumno, String idEntrenador) throws SQLException{
        boolean existenciaEjercicios = false;
        String query = "SELECT count(*) as cuenta FROM ENTRENAMIENTO ";
        PreparedStatement prepStat = null;
        if (idAlumno == null) {
            query += "WHERE dni_entrenador = ?;";
            prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setString(1, idEntrenador);
        } else if (idEntrenador == null) {
            query += "WHERE dni_alumno = ?;";
            prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setString(1, idAlumno);
        }
        ResultSet queryResult = prepStat.executeQuery();
        queryResult.next();
        if (queryResult.getInt("cuenta") > 0) {
            existenciaEjercicios = true;
        }
        queryResult.close();
        prepStat.close();
        return existenciaEjercicios;
    }
    
}
