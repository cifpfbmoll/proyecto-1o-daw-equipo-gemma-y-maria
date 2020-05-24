package proyectofinal;

/**
 * Clase que refleja cada ejercicio posible.
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Ejercicio {
    
    //Atributos:
    private int codigo;
    private String nombre;
    private String descripcion;
    private TipoEjercicio tipo;
    
    //Constructores:
    public Ejercicio() {
    }
    
    
    //TODO
    
    //Métodos:
    public void mostrarLineaEjercicio() {
        System.out.println("    ." + this.getNombre() + ": " + this.getDescripcion());
    }
    
    
    
    //Getters y setters:
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
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

    public TipoEjercicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoEjercicio tipo) {
        this.tipo = tipo;
    }
    
    
}
