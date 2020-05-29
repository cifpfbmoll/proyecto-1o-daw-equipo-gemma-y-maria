package proyecto.modelo;

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
     * Imprime los datos del ejercicio por consola. Lo mantengo dentro de la clase y lo convertiré en sobreescribir el .toString()
     */
    public void mostrarDatosEjercicio() {
        System.out.println("  " + this.getNombre() + " (código " + this.getCodigo() + "):");
        System.out.println("  " + this.getDescripcion());
        System.out.println("  Tipos:");
        for (int i = 0; i < this.getTipo().size(); i++){
            System.out.println("      -" + this.getTipo().get(i).getTextoTipoEjercicio() + " (código " + this.getTipo().get(i).name() + ")");
        }
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
