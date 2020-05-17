package proyectofinal;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public Entrenador(int programasPreparados, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono, String direccion) {
        super(password, nombre, apellido1, apellido2, dni, email, telefono, direccion);
        this.programasPreparados = programasPreparados;
    }
    
    //Métodos:
    /**
     * Crea un objeto de tipo entrenador a partir de su id
     * @param id identificador del usuario tipo entrenador
     * @return Entrenador objeto tipo entrenador
     */
    public static Entrenador crearObjetoEntrenador(String dni) throws SQLException{
        Entrenador entrenador = new Entrenador();
        ResultSet lineaEntrenador = Usuario.buscarUsuarioPorDni(dni);
        lineaEntrenador.next();
        entrenador.setDni(dni);
        entrenador.setNombre(lineaEntrenador.getString("nombre"));
        entrenador.setApellido1(lineaEntrenador.getString("apellido1"));
        entrenador.setApellido2(lineaEntrenador.getString("apellido2"));
        entrenador.setEmail(lineaEntrenador.getString("email"));
        entrenador.setTelefono(lineaEntrenador.getInt("telefono"));
        entrenador.setDireccion(lineaEntrenador.getString("direccion"));
        //TODO programas preparados
        
        lineaEntrenador.close();
        return entrenador;
    }
    
    //Getters y setters:
    public int getProgramasPreparados() {
        return programasPreparados;
    }

    public void setProgramasPreparados(int programasPreparados) {
        this.programasPreparados = programasPreparados;
    }
    
}
