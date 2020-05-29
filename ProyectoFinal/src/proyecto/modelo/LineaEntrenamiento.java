package proyecto.modelo;

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
