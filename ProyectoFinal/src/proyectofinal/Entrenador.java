package proyectofinal;

/**
 * Subclase de Usuario para los usuarios tipo 'entrenador'
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public class Entrenador extends Usuario {
    
    //Atributos:
    private int programasPreparados;
    //este atributos sumará 1 por cada programa preparado; como los entrenadores vendrán de crear otros programas no puede iniciarse a 0
    
    //Constructores:
    public Entrenador() {
    }

    public Entrenador(int programasPreparados, String id, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono) {
        super(id, password, nombre, apellido1, apellido2, dni, email, telefono);
        this.programasPreparados = programasPreparados;
    }
    
    //Métodos:
    //Getters y setters:

    public int getProgramasPreparados() {
        return programasPreparados;
    }

    public void setProgramasPreparados(int programasPreparados) {
        this.programasPreparados = programasPreparados;
    }
    

    
    
    
}
