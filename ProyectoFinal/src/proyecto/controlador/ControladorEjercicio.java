package proyecto.controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import proyecto.modelo.Ejercicio;
import proyecto.modelo.TipoEjercicio;
import proyecto.vista.MenuPrincipal;

public class ControladorEjercicio {
    /**
     * Genera un objeto de tipo Ejercicio desde datos obtenidos en la tabla.
     * 
     * @param codigo identificador del ejercicio para buscar en tabla
     * @return objeto Ejercicio
     * @throws SQLException 
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
     * @throws SQLException 
     */
    public static void crearNuevoEjercicio() throws SQLException{
        Ejercicio nuevoEjercicio = new Ejercicio();
        imprimirCodigosExistentes();
        String nuevoCodigo;
        do {
            System.out.println("Introduce el código del nuevo ejercicio:");
            nuevoCodigo = MenuPrincipal.lector.nextLine().toUpperCase();
            //TODO: mejora validar que el código sólo tenga dos letras
            //todo añadir mensaje si error System.out.println("Error. El código introducido ya está en uso.");
        } while (comprobarCodigoExistente(nuevoCodigo));
        nuevoEjercicio.setCodigo(nuevoCodigo);
        System.out.println("Introduce el título del nuevo ejercicio:");
        nuevoEjercicio.setNombre(MenuPrincipal.lector.nextLine());
        System.out.println("Introduce la descripción del nuevo ejercicio:");
        nuevoEjercicio.setDescripcion(MenuPrincipal.lector.nextLine());
        ArrayList<TipoEjercicio> listaTipos = generarListaTipos();
        nuevoEjercicio.setTipo(listaTipos);
        guardarEjercicioEnTabla(nuevoEjercicio);
    }
    
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
            sqle.printStackTrace();
            System.out.println("Error en la introducción del ejercicio.");
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
        }
    }
    
    public static ArrayList<TipoEjercicio> generarListaTipos() {
        System.out.println("Los tipos de ejercicio existentes son:");
        TipoEjercicio.imprimirTipo();
        System.out.println("¿En cuántos de estos tipos se puede clasificar el nuevo ejercicio?\nIntroduce número:");
        int numTipos = Integer.parseInt(MenuPrincipal.lector.nextLine());
        //TODO convertir if en bucle
        if (numTipos > TipoEjercicio.values().length + 1) {
            System.out.println(numTipos + " es mayor a la cantidad de tipos posibles (" + TipoEjercicio.values().length + 1 + ")");
            System.out.println("Introduce nuevo número de tipos:");
            numTipos = Integer.parseInt(MenuPrincipal.lector.nextLine());
        }
        ArrayList<TipoEjercicio> lista = new ArrayList<>();
        for (int i = 0; i < numTipos; i++) {
            boolean tipoExistente = false;
            while (!tipoExistente) {
                System.out.println("Introduce las siglas del código del tipo de ejercicio:");
                String tipo = MenuPrincipal.lector.nextLine();
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
    
    public static boolean comprobarCodigoExistente(String codigo) throws SQLException{
        ArrayList<String> listaCodigos = obtenerCodigosExistentes();
        if (listaCodigos.contains(codigo)) {
            return true;
        }
        return false;
    }
    
    public static void imprimirCodigosExistentes() throws SQLException{
        System.out.println("En estos momentos están en uso los siguientes códigos:");
        ArrayList<String> listaCodigos = obtenerCodigosExistentes();
        for (int i = 0; i < listaCodigos.size(); i++) {
            if (i != listaCodigos.size()-1) {
                System.out.print(listaCodigos.get(i) + " - ");
            } else {
                System.out.println(listaCodigos.get(i));
            }
        }
    }
    
    public static ArrayList<String> obtenerCodigosExistentes() throws SQLException{
        ArrayList<String> lista = new ArrayList<>();
        String query = "SELECT ex_code FROM ejercicio;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        ResultSet results = prepStat.executeQuery();
        while (results.next()) {
            lista.add(results.getString("ex_code"));
        }
        results.close();
        prepStat.close();
        return lista;
    }
}