package proyecto.modelo;

import java.util.ArrayList;

/**
 * Recoge cada entrenamiento elaborado por un entrenador para un alumno.<br>
 * La lista de ejercicios se tomará de la tabla linea_entrenamiento en base al código del entrenamiento.
 *
 * @author Gemma Díez Cabeza y María Rabanales González
 * @version 20.05.10.am
 */
public class Entrenamiento {

    //Atributos:
    private int codigo;
    private Entrenador entrenador;
    private Alumno alumno;
    private String fecha;
    private TipoEjercicio tipo;
    private ArrayList<LineaEntrenamiento> listaEjercicios;

    //Constructores:
    /**
     * Constructor vacío
     */
    public Entrenamiento() {
    }

    /**
     * Constructor completo
     * @param codigo identificador del entrenamiento
     * @param entrenador objeto tipo Entrenador
     * @param alumno objeto tipo Alumno
     * @param fecha fecha de creación del entrenamiento en formato determinado por método de Utilidades
     * @param tipo según lista de enumerador
     * @param listaEjercicios conjunto de ejercicios de un entrenamiento
     */
    public Entrenamiento(int codigo, Entrenador entrenador, Alumno alumno, String fecha, TipoEjercicio tipo, ArrayList<LineaEntrenamiento> listaEjercicios) {
        this.codigo = codigo;
        this.entrenador = entrenador;
        this.alumno = alumno;
        this.fecha = fecha;
        this.tipo = tipo;
        this.listaEjercicios = listaEjercicios;
    }

    //Métodos equivalentes a toString():
    public void mostrarListaEjercicios() {
        for (int i = 0; i < this.getListaEjercicios().size(); i++) {
            System.out.println(i + 1 + ":");
            System.out.println("   " + this.getListaEjercicios().get(i).getEjercicio().getNombre() + " (código " + 
                    this.getListaEjercicios().get(i).getEjercicio().getCodigo() + ")");
            System.out.println("   " + this.getListaEjercicios().get(i).getEjercicio().getDescripcion());
            if (this.getListaEjercicios().get(i).getRepeticiones() != 0) {           //El tipo primitivo nunca puede ser nulo, es 0 por defecto
                System.out.println("   Repeticiones: " + this.getListaEjercicios().get(i).getRepeticiones());
            }
            if (this.getListaEjercicios().get(i).getMinMinutos() != 0) {
                System.out.println("   Tiempo mínimo: " + this.getListaEjercicios().get(i).getMinMinutos());
            }
            System.out.println("");
        }
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

    public TipoEjercicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoEjercicio tipo) {
        this.tipo = tipo;
    }

    public ArrayList<LineaEntrenamiento> getListaEjercicios() {
        return listaEjercicios;
    }

    public void setListaEjercicios(ArrayList<LineaEntrenamiento> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

}
