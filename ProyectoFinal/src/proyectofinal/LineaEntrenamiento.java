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
