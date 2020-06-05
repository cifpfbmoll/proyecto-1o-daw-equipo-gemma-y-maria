package proyecto.controlador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import proyecto.modelo.*;
import proyecto.vista.MenuPrincipal;

/**
 * Incluye los métodos que trabajan con objetos y procesos relacionados con las clases Entrenamiento y LineaEntrenamiento.
 */
public class ControladorEntrenamiento {
    /**
     * Crea un objeto entrenamiento
     * @param idEntrenador identificador del entrenador que crea el programa de entrenamiento
     * @throws SQLException 
     */
    public static void crearNuevoEntrenamiento(String idEntrenador) throws SQLException {
        Entrenamiento entreno = new Entrenamiento();
        entreno.setEntrenador(ControladorEntrenador.generarEntrenadorDesdeTabla(idEntrenador));
        verSolicitudesEntrenamiento();
        System.out.println("Introduce el DNI del usuario para el que quieres preparar un entrenamiento:");
        String idAlumno = MenuPrincipal.lector.nextLine();
        TipoEjercicio tipoPrograma = ControladorUsuario.obtenerSolicitudUsuario(idAlumno);
        //Con esto compruebo que este DNI tiene asociada una solicitud de entrenamiento, y recibo el dato de la solicitud.
        if (tipoPrograma == null) {
            System.out.println("Este usuario no quiere ningún programa.\nSe cancela el proceso de creación.");
        } else {
            entreno.setTipo(tipoPrograma);
            entreno.setAlumno(ControladorAlumno.generarAlumnoDesdeTabla(idAlumno));
            System.out.println("¿Cuántos ejercicios diferentes tendrá este nuevo programa?");
            int numLineas = Integer.parseInt(MenuPrincipal.lector.nextLine());
            ArrayList<LineaEntrenamiento> listaLineas = new ArrayList<>();
            for (int i = 0; i < numLineas; i++) {
                listaLineas.add(crearLineaEntrenamiento(tipoPrograma.name()));
            }
            entreno.setListaEjercicios(listaLineas);
            insertarEntrenamientoEnTablaDesdeObjeto(entreno);
            System.out.println("Nuevo entrenamiento creado con éxito.");
            ControladorEntrenador.incrementarPrograma(entreno.getEntrenador());
            eliminarSolicitudEntrenamiento(idAlumno);
        }
    }
    
    /**
     * Enlazando con crearNuevoEntrenamiento(), cuando se ha completado un programa se elimina la solicitud.
     */
    public static void eliminarSolicitudEntrenamiento(String idAlumno) throws SQLException{
        boolean estadoAC = MenuPrincipal.con.getAutoCommit();
        try {
            MenuPrincipal.con.setAutoCommit(false);
            String query = "UPDATE usuario SET tipo_prog_solicitado = ? where DNI = ?;";
            PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setString(1, null);
            prepStat.setString(2, idAlumno);
            prepStat.executeUpdate();
            MenuPrincipal.con.commit();
            System.out.println("Se ha desmarcado la solicitud de entrenamiento completada.");
            prepStat.close();
        } catch (SQLException sqle) {
            String tituloError = "Error en la eliminación de la solicitud de entrenamiento completada.";
            Utilidades.logErrores(tituloError, Arrays.toString(sqle.getStackTrace()));
            System.out.println(tituloError);
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
        }
    }

    public static void copiarEntrenamiento(String idEntrenador) throws SQLException {
        //Nota para documentación: sólo se pueden generar entrenamientos nuevos para quien lo pida; se pueden copiar entrenamientos para cualquiera.
        System.out.println("Sólo puedes copiar entrenamientos elaborados por ti mismo.");
        int codigoBase = consultarCodigoEntrenamiento(idEntrenador, null);
        Entrenamiento entreno = generarEntrenamientoDesdeTabla(codigoBase);
        System.out.println("Introduce el DNI del alumno para que el quieres copiar el programa de entrenamiento:");
        String idAlumno = MenuPrincipal.lector.nextLine();
        entreno.setAlumno(ControladorAlumno.generarAlumnoDesdeTabla(idAlumno));
        System.out.println("La tabla de ejercicios que se copiará en el nuevo programa es:");
        entreno.mostrarListaEjercicios();
        insertarEntrenamientoEnTablaDesdeObjeto(entreno);
        System.out.println("Copia realizada con éxito.");
        ControladorEntrenador.incrementarPrograma(entreno.getEntrenador());
    }
    
    public static void insertarEntrenamientoEnTablaDesdeObjeto(Entrenamiento entreno) throws SQLException {
        boolean estadoAC = MenuPrincipal.con.getAutoCommit();
        try {
            MenuPrincipal.con.setAutoCommit(false);
            String queryEntrenamiento = "INSERT INTO entrenamiento (dni_entrenador, dni_alumno, tipo_programa, fecha_creacion) VALUES (?, ?, ?, ?);";
            PreparedStatement prepStatEntrenamiento = MenuPrincipal.con.prepareStatement(queryEntrenamiento);
            prepStatEntrenamiento.setString(1, entreno.getEntrenador().getDni());
            prepStatEntrenamiento.setString(2, entreno.getAlumno().getDni());
            prepStatEntrenamiento.setString(3, entreno.getTipo().name());
            prepStatEntrenamiento.setString(4, Utilidades.obtenerFecha());
            prepStatEntrenamiento.execute();
            Savepoint puntoEntrenamiento = MenuPrincipal.con.setSavepoint();
            System.out.println("Tabla 'entrenamiento' posible de modificar correctamente.");  //traza
            prepStatEntrenamiento.close();
            String queryCodigo = "SELECT MAX(train_code) as train_codigo FROM entrenamiento;";      //sé que el último que he creado es el mayor, por serial
            PreparedStatement prepStatCodigo = MenuPrincipal.con.prepareStatement(queryCodigo);
            ResultSet resultsCodigo = prepStatCodigo.executeQuery();
            resultsCodigo.next();
            entreno.setCodigo(resultsCodigo.getInt("train_codigo"));
            resultsCodigo.close();
            prepStatCodigo.close();
            for (int i = 0; i < entreno.getListaEjercicios().size(); i++) {
                String queryLinea = "INSERT INTO linea_entrenamiento (codigo_entreno, codigo_ejercicio, repeticiones, tiempo_min) VALUES (?, ?, ?, ?);";
                PreparedStatement prepStatLinea = MenuPrincipal.con.prepareStatement(queryLinea);
                prepStatLinea.setInt(1, entreno.getCodigo());
                prepStatLinea.setString(2, entreno.getListaEjercicios().get(i).getEjercicio().getCodigo());
                prepStatLinea.setInt(3, entreno.getListaEjercicios().get(i).getRepeticiones());
                prepStatLinea.setInt(4, entreno.getListaEjercicios().get(i).getMinMinutos());
                prepStatLinea.execute();
                prepStatLinea.close();
            }
            MenuPrincipal.con.commit();
            System.out.println("Tabla 'entrenamiento' modificada correctamente.");
            System.out.println("Tabla 'linea_entrenamiento' modificada correctamente.");    //traza
        } catch (SQLException sqle) {
            String tituloError = "Error en la introducción de un entrenamiento en la tabla.";
            Utilidades.logErrores(tituloError, Arrays.toString(sqle.getStackTrace()));
            System.out.println(tituloError);
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
        }
    }

    //El método de consultas tiene dos parámetros; paso como null, valor imposible en id, el que NO quiero usar como base
    //Para facilitar la lectura y las llamadas es más apropiado separarlo así.
    public static void consultarEntrenamientoPorEntrenador(String id) throws SQLException {
        if (!ControladorUsuario.comprobarNumEjUsuario(id, null)) {
            System.out.println("No tiene ningún entrenamiento disponible como entrenador.");
        } else {
            mostrarEntrenamientoDesdeTabla(id, null);
        }
    }

    public static void consultarEntrenamientoPorAlumno(String id) throws SQLException {
        if (!ControladorUsuario.comprobarNumEjUsuario(null, id)) {
            System.out.println("No tiene ningún entrenamiento disponible como alumno.");
        } else {
            mostrarEntrenamientoDesdeTabla(null, id);
        }
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
        System.out.println("Tipo de entrenamiento: " + entreno.getTipo().getTextoTipoEjercicio() + " (código " + entreno.getTipo().name() + ")");
        System.out.println("Preparado por el entrenador " + entreno.getEntrenador().getNombre() + " " + entreno.getEntrenador().getApellido1() + 
                " " + entreno.getEntrenador().getApellido2());
        System.out.println("Para el alumno " + entreno.getAlumno().getNombre() + " " + entreno.getAlumno().getApellido1() + " " + 
                entreno.getAlumno().getApellido2());
        System.out.println("Tabla de ejercicios:");
        entreno.mostrarListaEjercicios();
        try {
            TimeUnit.SECONDS.sleep(6);             //Para pausar la ejecución del código 6 segundos.
        } catch (InterruptedException IE) {
            String tituloError = "Error al pausar la ejecución del código.";
            Utilidades.logErrores(tituloError, Arrays.toString(IE.getStackTrace()));
            System.out.println(tituloError);
        }
    }
    
    public static int consultarCodigoEntrenamiento(String idEntrenador, String idAlumno) throws SQLException {
        String query = "SELECT train_code, fecha_creacion FROM ENTRENAMIENTO ";
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
        ArrayList<Integer> codigosEncontrados = new ArrayList<>();
        System.out.println("Entrenamientos encontrados:");
        while (queryResult.next()) {
            codigosEncontrados.add(queryResult.getInt("train_code"));
            System.out.println("  código " + queryResult.getInt("train_code") + "; fecha de creación: " + queryResult.getString("fecha_creacion"));
        }
        queryResult.close();
        prepStat.close();
        int codigo = 0;             //nunca podrá tener este valor: el serial de códigos de entrenamiento empieza en 1
        do {
            System.out.println("Introduce el código del programa de entrenamiento que te interesa:");
            int opcionCodigo = Integer.parseInt(MenuPrincipal.lector.nextLine());
            if (codigosEncontrados.contains(opcionCodigo)) {
                codigo = opcionCodigo;
            } else {
                System.out.println("La opción introducida no existe.");
            }
        } while (codigo == 0);
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
            writerMejorado.write("Fecha de creación: " + entreno.getFecha() + "\n");
            writerMejorado.write("Tipo de entrenamiento: " + entreno.getTipo().getTextoTipoEjercicio() + " (código " + entreno.getTipo().name() + ")\n");
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
            String tituloError = "Error al leer el archivo de destino.";
            Utilidades.logErrores(tituloError, Arrays.toString(eio.getStackTrace()));
            System.out.println(tituloError);
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
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(queryEntrenamiento);
        prepStat.setInt(1, codigo);
        ResultSet results = prepStat.executeQuery();
        results.next();
        String idEntrenador = results.getString("dni_entrenador");
        String idAlumno = results.getString("dni_alumno");
        objetoEntrenamiento.setCodigo(codigo);
        objetoEntrenamiento.setTipo(TipoEjercicio.valueOf(results.getString("tipo_programa")));
        objetoEntrenamiento.setFecha(results.getString("fecha_creacion"));
        objetoEntrenamiento.setEntrenador(ControladorEntrenador.generarEntrenadorDesdeTabla(idEntrenador));
        objetoEntrenamiento.setAlumno(ControladorAlumno.generarAlumnoDesdeTabla(idAlumno));
        objetoEntrenamiento.setListaEjercicios(generarLineasDesdeTabla(codigo));
        results.close();
        prepStat.close();
        return objetoEntrenamiento;
    }

    public static void imprimirEntrenamientoDesdeTablaPorEntrenador(String id) throws SQLException {
        if (!ControladorUsuario.comprobarNumEjUsuario(id, null)) {
            System.out.println("No tiene ningún entrenamiento disponible como entrenador.");
        } else {
            imprimirEntrenamientoDesdeTabla(id, null);
        }
    }

    public static void imprimirEntrenamientoDesdeTablaPorAlumno(String id) throws SQLException {
        if (!ControladorUsuario.comprobarNumEjUsuario(null, id)) {
            System.out.println("No tiene ningún entrenamiento disponible como alumno.");
        } else {
            imprimirEntrenamientoDesdeTabla(null, id);
        }
    }
    
    /**
     * Menús de solicitud y búsqueda de entrenamientos.
     */
    public static void solicitarEntrenamiento(String id) throws SQLException {
        if (!comprobarExistenciaSolicitudEntrenamiento(id)) {
            System.out.println("¿Qué tipo de entrenamiento quieres solicitar?");
            TipoEjercicio.imprimirTipo();
            System.out.println("Introduce el código de la opción elegida:");
            String opcionTipo = MenuPrincipal.lector.nextLine().toUpperCase().trim();
            while (!TipoEjercicio.comprobarTipo(opcionTipo)) {
                System.out.println("Error en la selección.");
                System.out.println("Introduce de nuevo el código de la opción elegida:");
                opcionTipo = MenuPrincipal.lector.nextLine();
            }
            guardarSolicitudEntrenamiento(id, opcionTipo);
        } else {
            System.out.println("Ya has solicitado un entrenamiento previamente.\nEstá en proceso de elaboración.");
        }
    }

    public static void guardarSolicitudEntrenamiento(String id, String tipo) throws SQLException {
        boolean estadoAC = MenuPrincipal.con.getAutoCommit();
        try {
            MenuPrincipal.con.setAutoCommit(false);
            String query = "UPDATE usuario SET tipo_prog_solicitado = ? where DNI = ?;";
            PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setString(1, tipo);
            prepStat.setString(2, id);
            prepStat.execute();
            MenuPrincipal.con.commit();
            System.out.println("Solicitud de programa de entrenamiento realizada.");
            prepStat.close();
        } catch (SQLException sqle) {
            String tituloError = "Error en la solicitud de entrenamiento.";
            Utilidades.logErrores(tituloError, Arrays.toString(sqle.getStackTrace()));
            System.out.println(tituloError);
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
        }
    }

    public static boolean comprobarExistenciaSolicitudEntrenamiento(String id) throws SQLException {
        String query = "SELECT tipo_prog_solicitado FROM usuario WHERE DNI = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        prepStat.setString(1, id);
        ResultSet queryResult = prepStat.executeQuery();
        queryResult.next();
        if (queryResult.getString("tipo_prog_solicitado") != null) {
            return true;
        }
        queryResult.close();
        prepStat.close();
        return false;
    }

    public static void verSolicitudesEntrenamiento() throws SQLException {
        String query = "SELECT DNI, nombre, apellido1, apellido2, tipo_prog_solicitado FROM usuario WHERE tipo_prog_solicitado is not NULL;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
        ResultSet queryResult = prepStat.executeQuery();
        System.out.println("Entrenamientos solicitados actualmente:");
        while (queryResult.next()) {
            String codigoTipo = queryResult.getString("tipo_prog_solicitado");
            System.out.println("  -tipo " + TipoEjercicio.valueOf(codigoTipo).getTextoTipoEjercicio() + " (código " + codigoTipo + ") para alumno "
                    + queryResult.getString("nombre") + " " + queryResult.getString("apellido1") + " " + queryResult.getString("apellido2")
                    + " (DNI " + queryResult.getString("DNI") + ")");
        }
        queryResult.close();
        prepStat.close();
    }
    
    //Los métodos a continuación afectan a las diferentes líneas de entrenamiento:
    public static LineaEntrenamiento crearLineaEntrenamiento(String tipoEntrenamiento) throws SQLException{
        LineaEntrenamiento linea = new LineaEntrenamiento();
        ControladorEjercicio.imprimirCodigosExistentes(tipoEntrenamiento);
        String nuevoCodigo;
        do {
            System.out.println("Introduce el código del ejercicio que quieres añadir al programa:");
            nuevoCodigo = MenuPrincipal.lector.nextLine().toUpperCase();
            //Así garantizamos que el nuevo código cumpla el requisito de sólo tener dos caracteres:
            if (nuevoCodigo.length() != 2) {
                System.out.println("El código del ejercicio debe tener exactamente dos caracteres.");
            }
        } while ((!ControladorEjercicio.comprobarCodigoExistente(nuevoCodigo, tipoEntrenamiento)) && (nuevoCodigo.length() != 2));
        linea.setEjercicio(ControladorEjercicio.generarEjercicioDesdeTabla(nuevoCodigo));
        boolean opcionCorrecta = false;
        while (!opcionCorrecta) {
            System.out.println("¿Cuál de las siguientes opciones quieres aplicar?");
            System.out.println("  1- insertar sólo número de repeticiones.");
            System.out.println("  2- insertar sólo tiempo de ejercicio.");
            System.out.println("  3- insertar repeticiones y tiempo.");
            String opcionLinea = MenuPrincipal.lector.nextLine();

            switch(opcionLinea){
                case "1":
                    System.out.println("Inserta el número de repeticiones de este ejercicio:");
                    int repeticiones = Integer.parseInt(MenuPrincipal.lector.nextLine());
                    linea.setRepeticiones(repeticiones);
                    opcionCorrecta = true;
                    break;
                case "2":
                    System.out.println("Inserta el tiempo de ejecución de este ejercicio (en minutos):");
                    int minutos = Integer.parseInt(MenuPrincipal.lector.nextLine());
                    linea.setMinMinutos(minutos);
                    opcionCorrecta = true;
                    break;
                case "3":
                    System.out.println("Inserta el número de repeticiones de este ejercicio:");
                    int nuevasRepeticiones = Integer.parseInt(MenuPrincipal.lector.nextLine());
                    linea.setRepeticiones(nuevasRepeticiones);
                    System.out.println("Inserta el tiempo de ejecución de este ejercicio (en minutos):");
                    int nuevosMinutos = Integer.parseInt(MenuPrincipal.lector.nextLine());
                    linea.setMinMinutos(nuevosMinutos);
                    opcionCorrecta = true;
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        }
        return linea;
    }
    
    
    public static ArrayList<LineaEntrenamiento> generarLineasDesdeTabla(int programa) throws SQLException{
        ArrayList<LineaEntrenamiento> listaLineas = new ArrayList<>();
        String queryLinea = "SELECT * FROM linea_entrenamiento WHERE codigo_entreno = ? ORDER BY line_num ASC;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(queryLinea);
        prepStat.setInt(1, programa);
        ResultSet results = prepStat.executeQuery();
        while (results.next()) {
            LineaEntrenamiento linea = new LineaEntrenamiento();
            linea.setEjercicio(ControladorEjercicio.generarEjercicioDesdeTabla(results.getString("codigo_ejercicio")));
            linea.setRepeticiones(results.getInt("repeticiones"));
            linea.setMinMinutos(results.getInt("tiempo_min"));
            listaLineas.add(linea);
        }
        results.close();
        prepStat.close();
        return listaLineas;
    }
    
}
