package proyectofinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LineaEntrenamiento {
    //Atributos:
    private Ejercicio ejercicio;
    private int repeticiones;
    private int minMinutos;
    
    //Constructores
    public LineaEntrenamiento() {
    }
    
    public LineaEntrenamiento(Ejercicio ejercicio, int repeticiones, int minMinutos) {
        this.ejercicio = ejercicio;
        this.repeticiones = repeticiones;
        this.minMinutos = minMinutos;
    }
    
    //Métodos
    public static LineaEntrenamiento crearLineaEntrenamiento() throws SQLException{
        LineaEntrenamiento linea = new LineaEntrenamiento();
        Ejercicio.imprimirCodigosExistentens();
        String nuevoCodigo;
        do {
            System.out.println("Introduce el código del ejercicio que quieres añadir al programa:");
            nuevoCodigo = Menu.lector.nextLine().toUpperCase();
            //TODO: mejora validar que el código sólo tenga dos letras
        } while (!Ejercicio.comprobarCodigoExistente(nuevoCodigo));
        linea.setEjercicio(Ejercicio.generarEjercicioDesdeTabla(nuevoCodigo));
        boolean opcionCorrecta = false;
        while (!opcionCorrecta) {
            System.out.println("¿Cuál de las siguientes opciones quieres aplicar?");
            System.out.println("  1- insertar sólo número de repeticiones.");
            System.out.println("  2- insertar sólo tiempo de ejercicio.");
            System.out.println("  3- insertar repeticiones y tiempo.");
            String opcionLinea = Menu.lector.nextLine();

            switch(opcionLinea){
                case "1":
                    System.out.println("Inserta el número de repeticiones de este ejercicio:");
                    int repeticiones = Integer.parseInt(Menu.lector.nextLine());
                    linea.setRepeticiones(repeticiones);
                    opcionCorrecta = true;
                    break;
                case "2":
                    System.out.println("Inserta el tiempo de ejecución de este ejercicio (en minutos):");
                    int minutos = Integer.parseInt(Menu.lector.nextLine());
                    linea.setMinMinutos(minutos);
                    opcionCorrecta = true;
                    break;
                case "3":
                    System.out.println("Inserta el número de repeticiones de este ejercicio:");
                    int nuevasRepeticiones = Integer.parseInt(Menu.lector.nextLine());
                    linea.setRepeticiones(nuevasRepeticiones);
                    System.out.println("Inserta el tiempo de ejecución de este ejercicio (en minutos):");
                    int nuevosMinutos = Integer.parseInt(Menu.lector.nextLine());
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
        PreparedStatement prepStat = Menu.con.prepareStatement(queryLinea);
        prepStat.setInt(1, programa);
        ResultSet results = prepStat.executeQuery();
        while (results.next()) {
            LineaEntrenamiento linea = new LineaEntrenamiento();
            linea.setEjercicio(Ejercicio.generarEjercicioDesdeTabla(results.getString("codigo_ejercicio")));
            linea.setRepeticiones(results.getInt("repeticiones"));
            linea.setMinMinutos(results.getInt("tiempo_min"));
            listaLineas.add(linea);
        }
        results.close();
        prepStat.close();
        return listaLineas;
    }
    
    //Getters y setters
    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getMinMinutos() {
        return minMinutos;
    }

    public void setMinMinutos(int minMinutos) {
        this.minMinutos = minMinutos;
    }
}
