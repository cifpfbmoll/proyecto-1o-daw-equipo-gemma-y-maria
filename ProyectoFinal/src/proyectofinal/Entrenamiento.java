package proyectofinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Recoge cada entrenamiento elaborado por un entrenador para un alumno.<br>
 * La lista de ejercicios se tomará de la tabla linea_entrenamiento en base al
 * código del entrenamiento.
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Entrenamiento {

    //Atributos:
    private int codigo;
    private Entrenador entrenador;
    private Alumno alumno;
    private String fecha;
    private ArrayList<Ejercicio> listaEjercicios;

    //Constructores:
    /**
     * Constructor vacío
     */
    public Entrenamiento() {
    }
    //TODO demás constructores

    //Métodos:
    /**
     * Menú de creación de entrenamientos.
     *
     * @param id identificador del entrenador que crea el programa
     */
    public static void crearEntrenamiento(String id) {
        System.out.println("¿Qué tipo de entrenamiento quieres crear?");
        System.out.println("  1- crear programa nuevo");
        System.out.println("  2- copiar programa existente");
        String opcionCrear = Menu.lector.nextLine();

        switch (opcionCrear) {
            case "1":
                Entrenamiento.crearEntrenamiento(id);
                break;
            case "2":
                //TODO
                break;
            default:
                System.out.println("La opción seleccionada no existe.");
        }
    }

    //El método de consultas tiene dos parámetros; paso como null, valor imposible en id, el que NO quiero usar como base
    //Para facilitar la lectura y las llamadas es más apropiado separarlo así.
    public static void consultarEntrenamientoPorEntrenador(String id) throws SQLException {
        Entrenamiento.consultarEntrenamiento(id, null);
    }

    public static void consultarEntrenamientoPorAlumno(String id) throws SQLException {
        Entrenamiento.consultarEntrenamiento(null, id);
    }

    public static void consultarEntrenamiento(String idEntrenador, String idAlumno) throws SQLException {
        //TODO: cambiar el select para que de también el nombre de alumno a través de su id
        String query = "SELECT train_code, fecha_creacion FROM ENTRENAMIENTO ";
        PreparedStatement prepStat = null ;
        if (idAlumno == null) {
            query += "WHERE dni_entrenador = ?;";
             prepStat = Menu.con.prepareStatement(query);
            prepStat.setString(1, idEntrenador);
        } else if (idEntrenador == null) {
            query += "WHERE dni_alumno = ?;";
            prepStat = Menu.con.prepareStatement(query);
            prepStat.setString(1, idAlumno);
        }
        ResultSet queryResult = prepStat.executeQuery();
        ArrayList<Integer> codigosEncontrados = new ArrayList<>();
        System.out.println("Entrenamientos encontrados:");
        while (queryResult.next()) {
            codigosEncontrados.add(queryResult.getInt("train_code"));
            System.out.println("  código " + queryResult.getInt("train_code") + "; fecha de creación: " + queryResult.getDate("fecha_creacion"));
            //TODO añadir la conversion a fecha            
        }
        if (queryResult != null) {
            queryResult.close();
        }
        prepStat.close();
        System.out.println("Introduce el código del programa de entrenamiento que quieres consultar:");
        int opcionCodigo = Menu.lector.nextInt();
        if (codigosEncontrados.contains(opcionCodigo)) {
            Entrenamiento programa = crearObjetoEntrenamiento(opcionCodigo);
            programa.mostrarEntrenamiento();
        } else {
            System.out.println("La opción introducida no existe.");
        }
        //TODO: replantear lo anterior para convertirlo en un bucle    
    }

    public static Entrenamiento crearObjetoEntrenamiento(int codigo) throws SQLException {
        Entrenamiento programa = new Entrenamiento();
        //falta fecha
        String query = "SELECT * FROM ENTRENAMIENTO WHERE train_code = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        prepStat.setInt(1, codigo);
        ResultSet queryResult = prepStat.executeQuery();
        queryResult.next();
        programa.setCodigo(codigo);
        programa.setEntrenador(Entrenador.crearObjetoEntrenador(queryResult.getString("entrenador")));
        //TODO comletar

        if (queryResult != null) {
            queryResult.close();
        }
        prepStat.close();
        return programa;
    }

    public void mostrarEntrenamiento() {
        System.out.println("Información del programa de entrenamiento:");
        System.out.println("  -código: " + this.getCodigo());
        System.out.println("  -entrenador: ");
        System.out.println("  -alumno: ");
        System.out.println("  -fecha de creación: " + this.getFecha());
    }

    //Getters y setters:
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Ejercicio> getListaEjercicios() {
        return listaEjercicios;
    }

    public void setListaEjercicios(ArrayList<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

}
