package proyecto.controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import proyecto.modelo.Ejercicio;
import proyecto.modelo.TipoEjercicio;
import proyecto.vista.MenuPrincipal;

public class ControladorEjercicio {
    /**
     * Genera un objeto de tipo Ejercicio desde datos obtenidos en la tabla.
     * @param codigo identificador del ejercicio para buscar en tabla
     * @return objeto Ejercicio
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static Ejercicio generarEjercicioDesdeTabla(String codigo) throws SQLException{
        Ejercicio ej = new Ejercicio();
        String queryEjercicio = "SELECT * FROM ejercicio WHERE ex_code = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(queryEjercicio);
        prepStat.setString(1, codigo);
        ResultSet results = prepStat.executeQuery();
        results.next();
        ej.setCodigo(codigo);
        ej.setNombre(results.getString("nombre"));
        ej.setDescripcion(results.getString("descripcion"));
        results.close();
        prepStat.close();
        String queryTipo = "SELECT * FROM tipo_ejercicio WHERE ej_code = ?;";
        PreparedStatement prepStatTipo = MenuPrincipal.con.prepareStatement(queryTipo);
        prepStatTipo.setString(1, codigo);
        ResultSet resultsTipo = prepStatTipo.executeQuery();
        ArrayList<TipoEjercicio> listaTipos = new ArrayList<>();
        while (resultsTipo.next()) {
            TipoEjercicio tipoEj = TipoEjercicio.valueOf(resultsTipo.getString("tipo"));
            listaTipos.add(tipoEj);
        }
        ej.setTipo(listaTipos);
        resultsTipo.close();
        prepStatTipo.close();
        return ej;
    }
    
    /**
     * Crea objeto ejercicio y lo rellena con datos pedidos por consola.
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static void crearNuevoEjercicio() throws SQLException{
        Ejercicio nuevoEjercicio = new Ejercicio();
        imprimirCodigosExistentes(null);
        String nuevoCodigo;
        boolean codigoYaExiste = true;
        do {
            System.out.println("Introduce el código del nuevo ejercicio:");
            nuevoCodigo = MenuPrincipal.lector.nextLine().toUpperCase();
            if (nuevoCodigo.length() != 2) {
                System.out.println("Error: el código del ejercicio debe tener exactamente dos caracteres.");
            }
            codigoYaExiste = comprobarCodigoExistente(nuevoCodigo, null);
            if (codigoYaExiste) {
                System.out.println("Error: el código introducido ya está en uso.");
            }
        } while ((codigoYaExiste) && (nuevoCodigo.length() != 2));
        nuevoEjercicio.setCodigo(nuevoCodigo);
        System.out.println("Introduce el título del nuevo ejercicio:");
        nuevoEjercicio.setNombre(MenuPrincipal.lector.nextLine());
        System.out.println("Introduce la descripción del nuevo ejercicio:");
        nuevoEjercicio.setDescripcion(MenuPrincipal.lector.nextLine());
        ArrayList<TipoEjercicio> listaTipos = generarListaTipos();
        nuevoEjercicio.setTipo(listaTipos);
        guardarEjercicioEnTabla(nuevoEjercicio);
    }
    
    /**
     * Toma un objeto Ejercicio y lo guarda en la tabla
     * @param ejercicio de clase Ejercicio
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static void guardarEjercicioEnTabla(Ejercicio ejercicio) throws SQLException{
        boolean estadoAC = MenuPrincipal.con.getAutoCommit();
        try {
            MenuPrincipal.con.setAutoCommit(false);
            String query = "INSERT INTO ejercicio (ex_code, nombre, descripcion) VALUES (?, ?, ?);";
            PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setString(1, ejercicio.getCodigo());
            prepStat.setString(2, ejercicio.getNombre());
            prepStat.setString(3, ejercicio.getDescripcion());
            prepStat.execute();
            MenuPrincipal.con.commit();
            prepStat.close();
            //Tengo que commitear el ejercicio antes de meter tipos, de lo contrario me falla por PK
            for (int i = 0; i < ejercicio.getTipo().size(); i++) {
                String queryTipo = "INSERT INTO tipo_ejercicio (ej_code, tipo) VALUES (?, ?);";
                PreparedStatement prepStatTipo = MenuPrincipal.con.prepareStatement(queryTipo);
                prepStatTipo.setString(1, ejercicio.getCodigo());
                prepStatTipo.setString(2, ejercicio.getTipo().get(i).name());
                prepStatTipo.execute();
                prepStatTipo.close();
            }
            MenuPrincipal.con.commit();
            System.out.println("Ejercicio creado con éxito. Detalles:");
            ejercicio.mostrarDatosEjercicio();
        } catch (SQLException sqle) {
            String tituloError = "Error en la introducción del ejercicio.";
            Utilidades.logErrores(tituloError, Arrays.toString(sqle.getStackTrace()));
            System.out.println(tituloError);
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
        }
    }
    
    /**
     * Opera con los tipos de ejercicios.
     * @return lista en ArrayList de enum TipoEjercicio
     */
    public static ArrayList<TipoEjercicio> generarListaTipos() {
        System.out.println("Los tipos de ejercicio existentes son:");
        TipoEjercicio.imprimirTipo();
        System.out.println("¿En cuántos de estos tipos se puede clasificar el nuevo ejercicio?\nIntroduce número:");
        int numTipos;
        do {
            numTipos = Utilidades.recibirNumero();
            if (numTipos <= 0) {
                System.out.println("Error: tiene que haber al menos un ejercicio.");
            } else if (numTipos > 50) {
                System.out.println("Error: un programa no debiera tener más de 10 ejercicios.");
            }
        } while ((numTipos < 1) || (numTipos > 10));
        if (numTipos > TipoEjercicio.values().length + 1) {
            System.out.println(numTipos + " es mayor a la cantidad de tipos posibles (" + TipoEjercicio.values().length + 1 + ")");
            System.out.println("Introduce nuevo número de tipos:");
            numTipos = Utilidades.recibirNumero();
        }
        ArrayList<TipoEjercicio> lista = new ArrayList<>();
        for (int i = 0; i < numTipos; i++) {
            boolean tipoExistente = false;
            while (!tipoExistente) {
                System.out.println("Introduce las siglas del código del tipo de ejercicio:");
                String tipo = MenuPrincipal.lector.nextLine().toUpperCase().trim();
                tipoExistente = TipoEjercicio.comprobarTipo(tipo);
                if (!tipoExistente) {
                    System.out.println("Error. Código inexistente.\nLos tipos de ejercicio existentes son:");
                    TipoEjercicio.imprimirTipo();
                } else {
                    lista.add(TipoEjercicio.valueOf(tipo));
                }
            }    
        }
        return lista;
    }
    
    /**
     * Comprueba si un código concreto existe ya.
     * @param codigo tipo de ejercicio String para comprobar
     * @param tipoEj tipo de programa para crear la lista en que comprobar
     * @return true si existe, false si no existe
     * @throws SQLException  excepción SQL por la conexión a la base de datos
     */
    public static boolean comprobarCodigoExistente(String codigo, String tipoEj) throws SQLException{
        ArrayList<String> listaCodigos = obtenerCodigosExistentes(tipoEj);
        if (listaCodigos.contains(codigo)) {
            return true;
        }
        return false;
    }
    
    /**
     * Muestra por pantalla los códigos existentes para un tipo de ejercicio concreto.
     * @param tipoEj String para comprobar si existe.
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static void imprimirCodigosExistentes(String tipoEj) throws SQLException{
        System.out.println("En estos momentos están en uso los siguientes códigos de ejercicio:");
        ArrayList<String> listaCodigos = obtenerCodigosExistentes(tipoEj);
        for (int i = 0; i < listaCodigos.size(); i++) {
            String codigoEj = listaCodigos.get(i);
            System.out.print("  " + codigoEj + " - " + imprimirNombreDesdeCodigo(codigoEj) + "\n");
        }
    }
    
    /**
     * Muestra por pantalla el nombre de un ejercicio a partir de su código.
     * @param codigoEj String con el código del ejercicio
     * @return nombre String con el nombre del ejercicio
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static String imprimirNombreDesdeCodigo(String codigoEj) throws SQLException {
        String nombre = "";
        String query = "SELECT nombre FROM ejercicio WHERE ex_code = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        prepStat.setString(1, codigoEj);
        ResultSet results = prepStat.executeQuery();
        results.next();
        nombre = results.getString("nombre");
        results.close();
        prepStat.close();
        return nombre;
    }
    
    /**
     * Obtiene desde tablas una lista con todos los códigos existentes para un tipo de ejercicio concreto.
     * @param tipoEj String con el tipo de ejercicio
     * @return lista tipo ArrayList
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static ArrayList<String> obtenerCodigosExistentes(String tipoEj) throws SQLException{
        ArrayList<String> lista = new ArrayList<>();
        String query = "SELECT DISTINCT ej_code FROM tipo_ejercicio";
        if (tipoEj != null) {
            query += " WHERE tipo = ?;";
        } else {
            query += ";";
        }
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        if (tipoEj != null) {
            prepStat.setString(1, tipoEj);
        }
        ResultSet results = prepStat.executeQuery();
        while (results.next()) {
            lista.add(results.getString("ej_code"));
        }
        results.close();
        prepStat.close();
        return lista;
    }
}
