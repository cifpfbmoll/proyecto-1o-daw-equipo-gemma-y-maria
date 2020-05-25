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
        System.out.println("");
        System.out.println("¿Qué tipo de entrenamiento quieres crear?");
        System.out.println("  1- crear programa nuevo");
        System.out.println("  2- copiar programa existente");
        String opcionCrear = Menu.lector.nextLine();

        switch (opcionCrear) {
            case "1":
                crearNuevoEntrenamiento(id);
                break;
            case "2":
                copiarEntrenamiento(id);
                break;
            default:
                System.out.println("La opción seleccionada no existe.");
        }
    }

    public static void crearNuevoEntrenamiento(String idEntrenador) throws SQLException {
        Entrenamiento entreno = new Entrenamiento();
        entreno.setEntrenador(Entrenador.generarEntrenadorDesdeTabla(idEntrenador));
        Menu.verSolicitudesEntrenamiento();
        System.out.println("Introduce el DNI del alumno para el que quieres preparar un entrenamiento:");
        String idAlumno = Menu.lector.nextLine();
        //TODO: comprobar que este dni coincide con los de la base de datos y que necesita entrenamiento
        //TODO: confirmar que existe ese DNI para alumnos, como en copiarEntrenamiento
        entreno.setAlumno(Alumno.generarAlumnoDesdeTabla(idAlumno));
        System.out.println("¿Cuántos ejercicios diferentes tendrá este nuevo programa?");
        int numLineas = Integer.parseInt(Menu.lector.nextLine());
        ArrayList<LineaEntrenamiento> listaLineas = new ArrayList<>();
        //TODO falta que sólo se puedan añadir ejercicios del tipo solicitado por el usuario. IMPORTANTE.
        for (int i = 0; i < numLineas; i++) {
            listaLineas.add(LineaEntrenamiento.crearLineaEntrenamiento());
        }
        entreno.setListaEjercicios(listaLineas);
        entreno.insertarEntrenamientoEnTablaDesdeObjeto();
        System.out.println("Nuevo entrenamiento creado con éxito.");
        //TODO mejora devolver el código de entrenamiento
        //TODO quitar la solicitud de tipo de entrenamiento del alumno
    }

    public static void copiarEntrenamiento(String idEntrenador) throws SQLException {
        //Nota para documentación: sólo se pueden generar entrenamientos nuevos para quien lo pida; se pueden copiar entrenamientos para cualquiera.
        System.out.println("Sólo puedes copiar entrenamientos elaborados por ti mismo.");
        int codigoBase = consultarCodigoEntrenamiento(idEntrenador, null);
        Entrenamiento entreno = generarEntrenamientoDesdeTabla(codigoBase);
        System.out.println("Introduce el DNI del alumno para que el quieres copiar el programa de entrenamiento:");
        String idAlumno = Menu.lector.nextLine();
        //TODO: confirmar que existe ese DNI para alumnos
        entreno.setAlumno(Alumno.generarAlumnoDesdeTabla(idAlumno));
        System.out.println("La tabla de ejercicios que se copiará en el nuevo programa es:");
        entreno.mostrarListaEjercicios();
        //TODO mejora: añadir otros ejercicios en la tabla
        entreno.insertarEntrenamientoEnTablaDesdeObjeto();
        System.out.println("Copia realizada con éxito.");
        //TODO mejora devolver el nuevo código de entrenamiento
    }
    
    public void insertarEntrenamientoEnTablaDesdeObjeto() throws SQLException {
        boolean estadoAC = Menu.con.getAutoCommit();
        try {
            Menu.con.setAutoCommit(false);
            String queryEntrenamiento = "INSERT INTO entrenamiento (dni_entrenador, dni_alumno) VALUES (?, ?);";
            PreparedStatement prepStatEntrenamiento = Menu.con.prepareStatement(queryEntrenamiento);
            prepStatEntrenamiento.setString(1, this.getEntrenador().getDni());
            prepStatEntrenamiento.setString(2, this.getEntrenador().getDni());
            //TODO falta fecha
            prepStatEntrenamiento.execute();
            Menu.con.commit();
            System.out.println("Tabla 'entrenamiento' modificada correctamente.");  //traza
            //TODO rehacer esto con savepoints en vez de commits
            prepStatEntrenamiento.close();
            String queryCodigo = "SELECT MAX(train_code) as train_codigo FROM entrenamiento;";      //sé que el último que he creado es el mayor, por serial
            PreparedStatement prepStatCodigo = Menu.con.prepareStatement(queryCodigo);
            ResultSet resultsCodigo = prepStatCodigo.executeQuery();
            resultsCodigo.next();
            this.setCodigo(resultsCodigo.getInt("train_codigo"));
            resultsCodigo.close();
            prepStatCodigo.close();
            for (int i = 0; i < this.getListaEjercicios().size(); i++) {
                String queryLinea = "INSERT INTO linea_entrenamiento (codigo_entreno, codigo_ejercicio, repeticiones, tiempo_min) VALUES (?, ?, ?, ?);";
                PreparedStatement prepStatLinea = Menu.con.prepareStatement(queryLinea);
                prepStatLinea.setInt(1, this.getCodigo());
                prepStatLinea.setString(2, this.getListaEjercicios().get(i).getEjercicio().getCodigo());
                prepStatLinea.setInt(3, this.getListaEjercicios().get(i).getRepeticiones());
                prepStatLinea.setInt(4, this.getListaEjercicios().get(i).getMinMinutos());
                prepStatLinea.execute();
                Menu.con.commit();
                prepStatLinea.close();
            }
            System.out.println("Tabla 'linea_entrenamiento' modificada correctamente.");    //traza
            //TODO comprobar aqui todos los commits
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error en la introducción del alumno.");
            Menu.con.rollback();
        } finally {
            Menu.con.setAutoCommit(estadoAC);
        }
    }

    //El método de consultas tiene dos parámetros; paso como null, valor imposible en id, el que NO quiero usar como base
    //Para facilitar la lectura y las llamadas es más apropiado separarlo así.
    public static void consultarEntrenamientoPorEntrenador(String id) throws SQLException {
        mostrarEntrenamientoDesdeTabla(id, null);
    }

    public static void consultarEntrenamientoPorAlumno(String id) throws SQLException {
        mostrarEntrenamientoDesdeTabla(null, id);
    }

    /**
     * Toma un entrenamiento de tablas y lo muestra por consola.
     * 
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
        entreno.mostrarListaEjercicios();
        try {
            TimeUnit.SECONDS.sleep(6);             //Para pausar la ejecución del código 6 segundos.
        } catch (InterruptedException IE) {
            System.out.println("InterruptedException. Error al pausar la ejecución del código.");
        }
    }
    
    public void mostrarListaEjercicios() {
        for (int i = 0; i < this.getListaEjercicios().size(); i++) {
            System.out.println(i + 1 + ":");
            System.out.println("   " + this.getListaEjercicios().get(i).getEjercicio().getNombre() + " (código " + 
                    this.getListaEjercicios().get(i).getEjercicio().getCodigo() + ")\n");
            System.out.println("   " + this.getListaEjercicios().get(i).getEjercicio().getDescripcion() + "\n");
            if (this.getListaEjercicios().get(i).getRepeticiones() != 0) {           //El tipo primitivo nunca puede ser nulo, es 0 por defecto
                System.out.println("   Repeticiones: " + this.getListaEjercicios().get(i).getRepeticiones() + "\n");
            }
            if (this.getListaEjercicios().get(i).getMinMinutos() != 0) {
                System.out.println("   Tiempo mínimo: " + this.getListaEjercicios().get(i).getMinMinutos() + "\n");
            }
        }
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
        System.out.println("Introduce el código del programa de entrenamiento que te interesa:");
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
     * Toma un entrenamiento de tablas y lo guarda en un archivo de texto personalizado para cada programa.
     * 
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
     * @param codigo identificador del programa de entrenamiento que se archivará
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
