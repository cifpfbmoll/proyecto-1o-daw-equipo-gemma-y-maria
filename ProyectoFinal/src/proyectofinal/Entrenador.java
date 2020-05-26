package proyectofinal;

import java.sql.PreparedStatement;
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

    public Entrenador(int programasPreparados, String password, String nombre, String apellido1, String apellido2, String dni, String email, int telefono, String direccion, TipoEjercicio tipoEjercicio) {
        super(password, nombre, apellido1, apellido2, dni, email, telefono, direccion, tipoEjercicio);
        this.programasPreparados = programasPreparados;
    }
    
    //Métodos:
    /**
     * Crea un objeto de tipo entrenador a partir de su id
     * @param id identificador del usuario tipo entrenador
     * @return Entrenador objeto tipo entrenador
     */
    public static Entrenador generarEntrenadorDesdeTabla(String id) throws SQLException{
        Entrenador user = new Entrenador();
        String queryUsuario = "SELECT * FROM usuario WHERE DNI = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(queryUsuario);
        prepStat.setString(1, id);
        ResultSet results = prepStat.executeQuery();
        results.next();
        user.setDni(id);
        user.setNombre(results.getString("nombre"));
        user.setPassword(results.getString("password"));
        user.setApellido1(results.getString("apellido1"));
        user.setApellido2(results.getString("apellido2"));
        user.setEmail(results.getString("email"));
        user.setTelefono(results.getInt("telefono"));
        user.setDireccion(results.getString("direccion"));
        user.setProgramasPreparados(results.getInt("num_prog_prep"));
        String tipoDeEjercicio = results.getString("tipo_prog_solicitado");
        if (tipoDeEjercicio != null) {
            user.setTipoEjercicio(TipoEjercicio.valueOf(tipoDeEjercicio));
        }
        results.close();
        prepStat.close();
        return user;
    }
    
    public void incrementarPrograma() throws SQLException {
        this.setProgramasPreparados(this.getProgramasPreparados() + 1);
        this.incrementarProgramaEnTabla();
    }
    
    public void incrementarProgramaEnTabla() throws SQLException {
        boolean estadoAC = Menu.con.getAutoCommit();
        try {
            Menu.con.setAutoCommit(false);
            String query = "UPDATE usuario SET num_prog_prep = ? where DNI = ?;";
            PreparedStatement prepStat = Menu.con.prepareStatement(query);
            prepStat.setInt(1, this.getProgramasPreparados());
            prepStat.setString(2, this.getDni());
            prepStat.executeUpdate();
            Menu.con.commit();
            System.out.println("Entrenador: con este, has elaborado un total de " + this.getProgramasPreparados() + " programas.");
            prepStat.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error al incrementar el número de programas del entrenador.");
            Menu.con.rollback();
        } finally {
            Menu.con.setAutoCommit(estadoAC);
        }
    }
    
    //Getters y setters:
    public int getProgramasPreparados() {
        return programasPreparados;
    }

    public void setProgramasPreparados(int programasPreparados) {
        this.programasPreparados = programasPreparados;
    }
    
}
