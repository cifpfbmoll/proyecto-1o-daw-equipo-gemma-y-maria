package proyectofinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    public static void menuCrearEntrenamiento(String id) throws SQLException{
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

    //El método de consultas tiene dos parámetros; paso como null, valor imposible en id, el que NO quiero usar como base
    //Para facilitar la lectura y las llamadas es más apropiado separarlo así.
    public static void consultarEntrenamientoPorEntrenador(String id) throws SQLException {
        int codigo = Entrenamiento.consultarEntrenamiento(id, null);
        Entrenamiento entrenamiento = crearObjetoEntrenamientoDesdeTabla(codigo);
        //TODO ver si esto hace falta
    }

    public static void consultarEntrenamientoPorAlumno(String id) throws SQLException {
        int codigo = Entrenamiento.consultarEntrenamiento(null, id);
        Entrenamiento entrenamiento = crearObjetoEntrenamientoDesdeTabla(codigo);
    }

    public static int consultarEntrenamiento(String idEntrenador, String idAlumno) throws SQLException {
        //TODO: cambiar el select para que de también el nombre de alumno a través de su id
        int codigo = 0;             //TODO: si hago el bucle nunca devolverá esto
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
            System.out.println("  código " + queryResult.getInt("train_code") + "; fecha de creación: " + queryResult.getString("fecha_creacion"));          
        }
        if (queryResult != null) {
            queryResult.close();
        }
        prepStat.close();
        System.out.println("Introduce el código del programa de entrenamiento que quieres consultar:");
        int opcionCodigo = Menu.lector.nextInt();
        if (codigosEncontrados.contains(opcionCodigo)) {
            return opcionCodigo;
        } else {
            System.out.println("La opción introducida no existe.");
        }
        //TODO: replantear lo anterior para convertirlo en un bucle   
        return codigo;
    }

    public static Entrenamiento crearObjetoEntrenamientoDesdeTabla(int codigo) throws SQLException {
        Entrenamiento programa = new Entrenamiento();
        //falta fecha
        String query = "SELECT * FROM ENTRENAMIENTO WHERE train_code = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        prepStat.setInt(1, codigo);
        ResultSet queryResult = prepStat.executeQuery();
        queryResult.next();
        programa.setCodigo(codigo);
        programa.setEntrenador(Entrenador.crearObjetoEntrenador(queryResult.getString("entrenador")));
        //TODO completar
        //TODO SETALUMNO
        if (queryResult != null) {
            queryResult.close();
        }
        prepStat.close();
        return programa;
    }

    public void mostrarEntrenamiento() {
        System.out.println("Información del programa de entrenamiento:");
        System.out.println("  -código: " + this.getCodigo());
        System.out.println("  -entrenador: " + this.entrenador.getNombre() + " " + this.entrenador.getApellido1() + " " + this.entrenador.getApellido2() + 
                " (DNI: " + this.entrenador.getDni() + ")");
        System.out.println("  -alumno: " + this.alumno.getNombre() + " " + this.alumno.getApellido1() + " " + this.alumno.getApellido2() + 
                " (DNI: " + this.alumno.getDni() + ")");
        System.out.println("  -fecha de creación: " + this.getFecha());
        System.out.println("  -tabla de ejercicios:");
        for (int i = 0; i < listaEjercicios.size(); i++) {
            listaEjercicios.get(i).mostrarLineaEjercicio();
            //TODO: aqui faltarian las repeticiones/tiempo
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
            System.out.println("Nuevo entrenamiento (" + this.getCodigo() + " " + this.getEntrenador().getDni() + " " + this.getAlumno().getDni()+" " +this.getFecha()+" introducido correctamente.");
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
    
    /**
     * Toma un entrenamiento de tablas y lo guarda en un archivo txt.
     */
    public static void archivarEntrenamientoDesdeTabla(String idEntrenador, String idAlumno) throws SQLException{
        System.out.println("Por ahora, el entrenamiento se guardará en el archivo 'entrenamiento.txt' de la carpeta del proyecto.");
        //TODO que cada entrenamiento se guarde en su propio archivos
        int codigo = consultarEntrenamiento(idEntrenador, idAlumno);

        String queryEntrenamiento = "SELECT dni_entrenador, dni_alumno, fecha_creacion FROM entrenamiento WHERE train_code = ?;";
        String queryEntrenador = "SELECT nombre, apellido1, apellido2, telefono, email FROM usuario WHERE DNI = ?;";
        String queryAlumno = "SELECT nombre, apellido1, apellido2, telefono, email FROM usuario WHERE DNI = ?;";
        String queryLineaEntrenamiento = "SELECT E.nombre, E.ex_code, E.descripcion, L.repeticiones, L.tiempo_min from linea_entrenamiento as L, ejercicio as E where L.codigo_entreno = ? and L.codigo_ejercicio = E.ex_code;";
    
        PreparedStatement prepStatEntrenamiento = Menu.con.prepareStatement(queryEntrenamiento);    
        prepStatEntrenamiento.setInt(1, codigo);
        ResultSet resultsEntrenamiento = prepStatEntrenamiento.executeQuery();
        resultsEntrenamiento.next();
        //Nota: esto me machaca los valores recibidos (uno será null, otro no), pero total los NOT NULL debieran coincidir.
        idEntrenador = resultsEntrenamiento.getString("dni_entrenador");
        idAlumno = resultsEntrenamiento.getString("dni_alumno");
        String fechaCreacion = resultsEntrenamiento.getString("fecha_creacion");
        resultsEntrenamiento.close();
        prepStatEntrenamiento.close();
    
        PreparedStatement prepStatEntrenador = Menu.con.prepareStatement(queryEntrenador);
        prepStatEntrenador.setString(1, idEntrenador);
        ResultSet resultsEntrenador = prepStatEntrenador.executeQuery();
        resultsEntrenador.next();
        String nombreEntrenador = resultsEntrenador.getString("nombre");
        nombreEntrenador += " " + resultsEntrenador.getString("apellido1");
        nombreEntrenador += " " + resultsEntrenador.getString("apellido2");
        int telefonoEntrenador = resultsEntrenador.getInt("telefono");
        String mailEntrenador = resultsEntrenador.getString("email");
        resultsEntrenador.close();
        prepStatEntrenador.close();
        
        PreparedStatement prepStatAlumno = Menu.con.prepareStatement(queryAlumno); 
        prepStatAlumno.setString(1, idAlumno);
        ResultSet resultsAlumno = prepStatAlumno.executeQuery();
        resultsAlumno.next();
        String nombreAlumno = resultsAlumno.getString("nombre");
        nombreAlumno += " " + resultsAlumno.getString("apellido1");
        nombreAlumno += " " + resultsAlumno.getString("apellido2");
        int telefonoAlumno = resultsAlumno.getInt("telefono");
        String mailAlumno = resultsAlumno.getString("email");
        resultsAlumno.close();
        prepStatAlumno.close();
        
        ArrayList<String[]> ejercicios = new ArrayList<>();
        PreparedStatement prepStatLineaEntrenamiento = Menu.con.prepareStatement(queryLineaEntrenamiento);
        prepStatLineaEntrenamiento.setInt(1, codigo);
        ResultSet resultsLineaEntrenamiento = prepStatLineaEntrenamiento.executeQuery();
        while (resultsLineaEntrenamiento.next()) {
            //TODO simplificar esto en menos líneas
            String ejercicioNombre = resultsLineaEntrenamiento.getString("nombre");
            String ejercicioCodigo = resultsLineaEntrenamiento.getString("ex_code");
            String ejercicioDescripcion = resultsLineaEntrenamiento.getString("descripcion");
            String ejercicioRepeticiones = Integer.toString(resultsLineaEntrenamiento.getInt("repeticiones"));
            String ejercicioTiempo = Integer.toString(resultsLineaEntrenamiento.getInt("tiempo_min"));
            String[] lineaEntrenamiento = {ejercicioNombre, ejercicioCodigo, ejercicioDescripcion, ejercicioRepeticiones, ejercicioTiempo};
            ejercicios.add(lineaEntrenamiento);
        }
        resultsLineaEntrenamiento.close();
        prepStatLineaEntrenamiento.close();
        
        //TODO separar en funciones atomizadas
        try (BufferedWriter writerMejorado = new BufferedWriter(new FileWriter("entrenamiento.txt", true))) {
            writerMejorado.write("CONSULTA DE ENTRENAMIENTO:\n__________________________________________________________________________________\n");
            writerMejorado.write("Fecha de creación: " + fechaCreacion + "\n");    //TODO: añadir fecha de creacion
            writerMejorado.write("Preparado por el entrenador " + nombreEntrenador + "\n  (teléfono de contacto: " + telefonoEntrenador + "; mail de contacto: " + mailEntrenador + ")\n");
            //TODO añadir total de entrenamientos preparados
            writerMejorado.write("Para el alumno " + nombreAlumno + "\n  (teléfono de contacto: " + telefonoAlumno + "; mail de contacto: " + mailAlumno + ")\n");
            writerMejorado.write("__________________________________________________________________________________\n");
            writerMejorado.write("Tabla de ejercicios:\n");
            for (int i = 0; i < ejercicios.size(); i++) {
                writerMejorado.write("\n" + (i+1) + ": \n");
                writerMejorado.write("   " + ejercicios.get(i)[0] + " (código " + ejercicios.get(i)[1] + " )\n");
                writerMejorado.write("   " + ejercicios.get(i)[2] + "\n");
                if (ejercicios.get(i)[3] != null) {
                    writerMejorado.write("   Repeticiones: " + ejercicios.get(i)[3] + "\n");
                }
                if (ejercicios.get(i)[4] != null) {
                    writerMejorado.write("   Tiempo mínimo: " + ejercicios.get(i)[4] + "\n");
                }
            }
            writerMejorado.write("__________________________________________________________________________________\n");            
        } catch (IOException eio) {
            System.out.println("IOException. Error al leer el archivo errores.txt.");
        }
    }
    
    public static void archivarEntrenamientoDesdeTablaPorEntrenador(String id) throws SQLException{
        archivarEntrenamientoDesdeTabla(id, null);
    }
    
    public static void archivarEntrenamientoDesdeTablaPorAlumno(String id) throws SQLException{
        archivarEntrenamientoDesdeTabla(null, id);
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
