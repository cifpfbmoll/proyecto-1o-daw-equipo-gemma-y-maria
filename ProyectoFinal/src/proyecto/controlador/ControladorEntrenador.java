package proyecto.controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import proyecto.modelo.Entrenador;
import proyecto.modelo.TipoEjercicio;
import proyecto.vista.MenuPrincipal;

public class ControladorEntrenador extends ControladorUsuario{
    /**
     * Crea un objeto de tipo entrenador a partir de su id
     * @param id identificador del usuario tipo entrenador
     * @return Entrenador objeto tipo entrenador
     * @throws java.sql.SQLException excepción SQL por la conexión a la base de datos
     */
    public static Entrenador generarEntrenadorDesdeTabla(String id) throws SQLException{
        Entrenador user = new Entrenador();
        String queryUsuario = "SELECT * FROM usuario WHERE DNI = ?;";
        PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(queryUsuario);
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
    
    /**
     * Aumenta en 1 el atributo que refleja el número de programas creados por cada entrenador.
     * @param entrenador objeto tipo Entrenador
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static void incrementarPrograma(Entrenador entrenador) throws SQLException {
        entrenador.setProgramasPreparados(entrenador.getProgramasPreparados() + 1);
        incrementarProgramaEnTabla(entrenador);
    }
    
    /**
     * Modifica el número de programas creados por un entrenador en tablas.
     * @param entrenador objeto tipo Entrenador
     * @throws SQLException excepción SQL por la conexión a la base de datos
     */
    public static void incrementarProgramaEnTabla(Entrenador entrenador) throws SQLException {
        boolean estadoAC = MenuPrincipal.con.getAutoCommit();
        try {
            MenuPrincipal.con.setAutoCommit(false);
            String query = "UPDATE usuario SET num_prog_prep = ? where DNI = ?;";
            PreparedStatement prepStat = MenuPrincipal.con.prepareStatement(query);
            prepStat.setInt(1, entrenador.getProgramasPreparados());
            prepStat.setString(2, entrenador.getDni());
            prepStat.executeUpdate();
            MenuPrincipal.con.commit();
            System.out.println("Entrenador: con este, has elaborado un total de " + entrenador.getProgramasPreparados() + " programas.");
            prepStat.close();
        } catch (SQLException sqle) {
            String tituloError = "Error al incrementar el número de programas creados por el entrenador.";
            Utilidades.logErrores(tituloError, Arrays.toString(sqle.getStackTrace()));
            System.out.println(tituloError);
            MenuPrincipal.con.rollback();
        } finally {
            MenuPrincipal.con.setAutoCommit(estadoAC);
        }
    }
}
