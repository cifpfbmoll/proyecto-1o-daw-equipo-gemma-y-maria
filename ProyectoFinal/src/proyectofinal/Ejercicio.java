package proyectofinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que refleja cada ejercicio posible.
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Ejercicio {
    
    //Atributos:
    private String codigo;
    private String nombre;
    private String descripcion;
    private ArrayList<TipoEjercicio> tipo;
    
    //Constructores:
    public Ejercicio() {
    }

    public Ejercicio(String codigo, String nombre, String descripcion, ArrayList<TipoEjercicio> tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }
    
    //Métodos:
    /**
     * Imprime los datos del ejercicio por consola.
     */
    public void mostrarDatosEjercicio() {
        System.out.println("  " + this.getNombre() + " (código " + this.getCodigo() + "):");
        System.out.println("  " + this.getDescripcion());
        System.out.println("  Tipos:");
        for (int i = 0; i < this.getTipo().size(); i++){
            System.out.println("      -" + this.getTipo().get(i).getTextoTipoEjercicio() + " (código " + this.getTipo().get(i) + ")");
        }
    }
    
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
        PreparedStatement prepStat = Menu.con.prepareStatement(queryEjercicio);
        prepStat.setString(1, codigo);
        ResultSet results = prepStat.executeQuery();
        results.next();
        ej.setCodigo(codigo);
        ej.setNombre(results.getString("nombre"));
        ej.setDescripcion(results.getString("descripcion"));
        results.close();
        prepStat.close();
        String queryTipo = "SELECT * FROM tipo_ejercicio WHERE ej_code = ?;";
        PreparedStatement prepStatTipo = Menu.con.prepareStatement(queryTipo);
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
        imprimirCodigosExistentens();
        String nuevoCodigo;
        do {
            System.out.println("Introduce el código del nuevo ejercicio:");
            nuevoCodigo = Menu.lector.nextLine().toUpperCase();
            //TODO: mejora validar que el código sólo tenga dos letras
            //todo añadir mensaje si error System.out.println("Error. El código introducido ya está en uso.");
        } while (comprobarCodigoExistente(nuevoCodigo));
        nuevoEjercicio.setCodigo(nuevoCodigo);
        System.out.println("Introduce el título del nuevo ejercicio:");
        nuevoEjercicio.setNombre(Menu.lector.nextLine());
        System.out.println("Introduce la descripción del nuevo ejercicio:");
        nuevoEjercicio.setDescripcion(Menu.lector.nextLine());
        ArrayList<TipoEjercicio> listaTipos = generarListaTipos();
        nuevoEjercicio.setTipo(listaTipos);
        System.out.println("Ejercicio creado con éxito. Detalles:");
        nuevoEjercicio.mostrarDatosEjercicio();
    }
    
    public static ArrayList<TipoEjercicio> generarListaTipos() {
        System.out.println("Los tipos de ejercicio existentes son:");
        TipoEjercicio.imprimirTipo();
        System.out.println("¿En cuántos de estos tipos se puede clasificar el nuevo ejercicio?\nIntroduce número:");
        int numTipos = Integer.parseInt(Menu.lector.nextLine());
        //TODO convertir if en bucle
        if (numTipos > TipoEjercicio.values().length + 1) {
            System.out.println(numTipos + " es mayor a la cantidad de tipos posibles (" + TipoEjercicio.values().length + 1 + ")");
            System.out.println("Introduce nuevo número de tipos:");
            numTipos = Integer.parseInt(Menu.lector.nextLine());
        }
        ArrayList<TipoEjercicio> lista = new ArrayList<>();
        for (int i = 0; i < numTipos; i++) {
            boolean tipoExistente = false;
            while (!tipoExistente) {
                System.out.println("Introduce las siglas del código del tipo de ejercicio:");
                String tipo = Menu.lector.nextLine();
                tipoExistente = TipoEjercicio.comprobarTipo(tipo);
                if (!tipoExistente) {
                    System.out.println("Error. Código inexistente.\nLos tipos de ejercicio existentes son:");
                    TipoEjercicio.imprimirTipo();
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
    
    public static void imprimirCodigosExistentens() throws SQLException{
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
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        ResultSet results = prepStat.executeQuery();
        while (results.next()) {
            lista.add(results.getString("ex_code"));
        }
        results.close();
        prepStat.close();
        return lista;
    }
    
    //Getters y setters:
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<TipoEjercicio> getTipo() {
        return tipo;
    }

    public void setTipo(ArrayList<TipoEjercicio> tipo) {
        this.tipo = tipo;
    }
    
    
}
