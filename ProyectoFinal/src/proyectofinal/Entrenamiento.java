package proyectofinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    private ArrayList<LineaEntrenamiento> listaEjercicios;

    //Constructores:
    /**
     * Constructor vacío
     */
    public Entrenamiento() {
    }

    public Entrenamiento(int codigo, Entrenador entrenador, Alumno alumno, String fecha, ArrayList<LineaEntrenamiento> listaEjercicios) {
        this.codigo = codigo;
        this.entrenador = entrenador;
        this.alumno = alumno;
        this.fecha = fecha;
        this.listaEjercicios = listaEjercicios;
    }

    //Métodos:
    /**
     * Menú de creación de entrenamientos.
     *
     * @param id identificador del entrenador que crea el programa
     */
    public static void menuCrearEntrenamiento(String id) throws SQLException {
        Menu.verSolicitudesEntrenamiento();
        System.out.println("Introduce el DNI del alumno para el que quieres preparar un entrenamiento:");
        String opcionDNI = Menu.lector.nextLine();
        //TODO: comprobar que este dni coincide con los de la base de datos y que necesita entrenamiento
        System.out.println("");
        System.out.println("¿Qué tipo de entrenamiento quieres crear?");
        System.out.println("  1- crear programa nuevo");
        System.out.println("  2- copiar programa existente");
        String opcionCrear = Menu.lector.nextLine();

        switch (opcionCrear) {
            case "1":
                //Conectar
                break;
            case "2":
                //TODO
                break;
            default:
                System.out.println("La opción seleccionada no existe.");
        }
    }

    public void crearNuevoEntrenamiento() throws SQLException {
        boolean estadoAC = Menu.con.getAutoCommit();
        try {
            Menu.con.setAutoCommit(false);
            String query = "INSERT INTO ENTRENAMIENTO (train_code, dni_entrenador, dni_alumno, fecha_creacion) VALUES (?, ?, ?, ?);";
            PreparedStatement prepStat = Menu.con.prepareStatement(query);
            prepStat.setInt(1, this.getCodigo());
            prepStat.setString(2, this.getEntrenador().getDni());
            prepStat.setString(3, this.getAlumno().getDni());
            prepStat.setString(4, this.getFecha());
            prepStat.execute();
            Menu.con.commit();
            System.out.println("Nuevo entrenamiento (" + this.getCodigo() + " " + this.getEntrenador().getDni() + " " + this.getAlumno().getDni() + " " + this.getFecha() + " introducido correctamente.");
            prepStat.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error en la introducción del entrenamiento.");
            Menu.con.rollback();
        } finally {
            Menu.con.setAutoCommit(estadoAC);
        }
        //TODO: tiempos, repeticiones
    }

    public static void copiarEntrenamiento() throws SQLException {
        //TODO maria. usar generarObjetoEntrenamientoDesdeTabla
    }

    //El método de consultas tiene dos parámetros; paso como null, valor imposible en id, el que NO quiero usar como base
    //Para facilitar la lectura y las llamadas es más apropiado separarlo así.
    public static void consultarEntrenamientoPorEntrenador(String id) throws SQLException {
        mostrarEntrenamientoDesdeTabla(id, null);
        //TODO rehacer
    }

    public static void consultarEntrenamientoPorAlumno(String id) throws SQLException {
        mostrarEntrenamientoDesdeTabla(null, id);
        //TODO como arriba
    }

    public static int consultarCodigoEntrenamiento(String idEntrenador, String idAlumno) throws SQLException {
        int codigo = 0;             //TODO: si hago el bucle nunca devolverá esto
        String query = "SELECT train_code, fecha_creacion FROM ENTRENAMIENTO ";
        PreparedStatement prepStat = null;
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
            System.out.println("  código " + queryResult.getInt("train_code") + "; fecha de creación: " + queryResult.getString("fecha_creacion"));
        }
        if (queryResult != null) {
            queryResult.close();
        }
        prepStat.close();
        System.out.println("Introduce el código del programa de entrenamiento que quieres consultar:");
        int opcionCodigo = Integer.parseInt(Menu.lector.nextLine());
        if (codigosEncontrados.contains(opcionCodigo)) {
            return opcionCodigo;
        } else {
            System.out.println("La opción introducida no existe.");
        }
        //TODO: replantear lo anterior para convertirlo en un bucle   
        return codigo;
    }

    /**
     * Toma un entrenamiento de tablas y lo muestra por consola.
     * @param idEntrenador DNI del entrenador
     * @param idAlumno DNI del alumno
     */
    public static void mostrarEntrenamientoDesdeTabla(String idEntrenador, String idAlumno) throws SQLException {
        int codigo = consultarCodigoEntrenamiento(idEntrenador, idAlumno);
        Entrenamiento entreno = generarEntrenamientoDesdeTabla(codigo);
        System.out.println("CONSULTA DE ENTRENAMIENTO:");
        System.out.println("Fecha de creación: " + entreno.getFecha());
        System.out.println("Preparado por el entrenador " + entreno.getEntrenador().getNombre() + " " + entreno.getEntrenador().getApellido1() + 
                " " + entreno.getEntrenador().getApellido2());
        System.out.println("Para el alumno " + entreno.getAlumno().getNombre() + " " + entreno.getAlumno().getApellido1() + " " + 
                entreno.getAlumno().getApellido2());
        System.out.println("Tabla de ejercicios:");
        for (int i = 0; i < entreno.getListaEjercicios().size(); i++) {
            System.out.println(i + 1 + ":");
            System.out.println("   " + entreno.getListaEjercicios().get(i).getEjercicio().getNombre() + " (código " + 
                    entreno.getListaEjercicios().get(i).getEjercicio().getCodigo() + ")\n");
            System.out.println("   " + entreno.getListaEjercicios().get(i).getEjercicio().getDescripcion() + "\n");
            if (entreno.getListaEjercicios().get(i).getRepeticiones() != 0) {           //El tipo primitivo nunca puede ser nulo, es 0 por defecto
                System.out.println("   Repeticiones: " + entreno.getListaEjercicios().get(i).getRepeticiones() + "\n");
            }
            if (entreno.getListaEjercicios().get(i).getMinMinutos() != 0) {
                System.out.println("   Tiempo mínimo: " + entreno.getListaEjercicios().get(i).getMinMinutos() + "\n");
            }
        }
        try {
            TimeUnit.SECONDS.sleep(6);             //Para pausar la ejecución del código 6 segundos.
        } catch (InterruptedException IE) {
            System.out.println("InterruptedException. Error al pausar la ejecución del código.");
        }
    }

    /**
     * Toma un entrenamiento de tablas y lo guarda en un archivo de texto personalizado para cada programa.
     * @param idEntrenador DNI del entrenador
     * @param idAlumno DNI del alumno
     */
    public static void imprimirEntrenamientoDesdeTabla(String idEntrenador, String idAlumno) throws SQLException {
        int codigo = consultarCodigoEntrenamiento(idEntrenador, idAlumno);
        Entrenamiento entreno = generarEntrenamientoDesdeTabla(codigo);

        String archivo = crearArchivoEntrenamiento(codigo);
        try (BufferedWriter writerMejorado = new BufferedWriter(new FileWriter(archivo, false))) {
            writerMejorado.write("CONSULTA DE ENTRENAMIENTO:\n__________________________________________________________________________________\n");
            writerMejorado.write("Fecha de creación: " + entreno.getFecha() + "\n");    //TODO: añadir fecha de creacion
            writerMejorado.write("Preparado por el entrenador " + entreno.getEntrenador().getNombre() + " " + entreno.getEntrenador().getApellido1() + " "
                    + entreno.getEntrenador().getApellido2() + "\n  (teléfono de contacto: " + entreno.getEntrenador().getTelefono() + "; mail de contacto: "
                    + entreno.getEntrenador().getEmail() + ")\n");
            writerMejorado.write("Para el alumno " + entreno.getAlumno().getNombre() + " " + entreno.getAlumno().getApellido1() + " "
                    + entreno.getAlumno().getApellido2() + "\n  (teléfono de contacto: " + entreno.getAlumno().getTelefono() + "; mail de contacto: "
                    + entreno.getAlumno().getEmail() + ")\n");
            writerMejorado.write("__________________________________________________________________________________\n");
            writerMejorado.write("Tabla de ejercicios:\n");
            for (int i = 0; i < entreno.getListaEjercicios().size(); i++) {
                writerMejorado.write("\n" + (i + 1) + ": \n");
                writerMejorado.write("   " + entreno.getListaEjercicios().get(i).getEjercicio().getNombre() + " (código " + entreno.getListaEjercicios().get(i).getEjercicio().getCodigo() + ")\n");
                writerMejorado.write("   " + entreno.getListaEjercicios().get(i).getEjercicio().getDescripcion() + "\n");
                if (entreno.getListaEjercicios().get(i).getRepeticiones() != 0) {           //El tipo primitivo nunca puede ser nulo, es 0 por defecto
                    writerMejorado.write("   Repeticiones: " + entreno.getListaEjercicios().get(i).getRepeticiones() + "\n");
                }
                if (entreno.getListaEjercicios().get(i).getMinMinutos() != 0) {
                    writerMejorado.write("   Tiempo mínimo: " + entreno.getListaEjercicios().get(i).getMinMinutos() + "\n");
                }
            }
            writerMejorado.write("__________________________________________________________________________________\n\n");
            System.out.println("Impresión realizada. Consultar archivo.");
        } catch (IOException eio) {
            System.out.println("IOException. Error al leer el archivo de destino.");
        }
    }

    /**
     * Genera un nombre único para cada programa de entrenamiento partiendo de su código (PK)
     *
     * @param codigo
     * @return nombre del archivo en que se guarda
     */
    public static String crearArchivoEntrenamiento(int codigo) {
        String archivo = "Programa";
        archivo += codigo;
        archivo += ".txt";
        return archivo;
    }

    public static Entrenamiento generarEntrenamientoDesdeTabla(int codigo) throws SQLException {
        Entrenamiento objetoEntrenamiento = new Entrenamiento();
        String queryEntrenamiento = "SELECT * FROM entrenamiento WHERE train_code = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(queryEntrenamiento);
        prepStat.setInt(1, codigo);
        ResultSet results = prepStat.executeQuery();
        results.next();
        String idEntrenador = results.getString("dni_entrenador");
        String idAlumno = results.getString("dni_alumno");
        objetoEntrenamiento.setCodigo(codigo);
        objetoEntrenamiento.setFecha(results.getString("fecha_creacion"));
        objetoEntrenamiento.setEntrenador(Entrenador.generarEntrenadorDesdeTabla(idEntrenador));
        objetoEntrenamiento.setAlumno(Alumno.generarAlumnoDesdeTabla(idAlumno));
        objetoEntrenamiento.setListaEjercicios(LineaEntrenamiento.generarLineasDesdeTabla(codigo));
        results.close();
        prepStat.close();
        return objetoEntrenamiento;
    }

    public static void imprimirEntrenamientoDesdeTablaPorEntrenador(String id) throws SQLException {
        imprimirEntrenamientoDesdeTabla(id, null);
    }

    public static void imprimirEntrenamientoDesdeTablaPorAlumno(String id) throws SQLException {
        imprimirEntrenamientoDesdeTabla(null, id);
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

    public ArrayList<LineaEntrenamiento> getListaEjercicios() {
        return listaEjercicios;
    }

    public void setListaEjercicios(ArrayList<LineaEntrenamiento> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

}
