package proyectofinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Superclase abstracta con los atributos básicos de cualquier usuario (sea
 * entrenador o alumno) que se conecta a la aplicación.
 *
 * @author Gemma Díez Cabeza & María Rabanales González
 * @version 20.05.10.am
 */
public abstract class Usuario {

    //Atributos:
    private String dni;     //TODO preparar validación para num/letra
    private String password;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private int telefono;
    private String direccion;

    //Constructores:
    public Usuario() {
    }

    public Usuario(String dni, String password, String nombre, String apellido1, String apellido2, String email, int telefono, String direccion) {
        this.password = password;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    //Métodos:
    /**
     * Permite encontrar un usuario en base a su DNI.
     *
     * @param dni
     * @return queryResult (tipoResultSet)
     * @throws SQLException
     */
    public static ResultSet buscarUsuarioPorDni(String dni) throws SQLException {
        String query = "SELECT * FROM usuario WHERE DNI = ?;";
        PreparedStatement prepStat = Menu.con.prepareStatement(query);
        prepStat.setString(1, dni);
        ResultSet queryResult = prepStat.executeQuery();
        //queryResult no se debe cerrar aquí sino en el método que lo llama
        prepStat.close();
        return queryResult;
    }

    public void crearNuevoUsuario() {
        System.out.println("  -dni:");
        String userDni = Menu.lector.nextLine();
        this.setDni(userDni);
        System.out.println("  -contraseña:");
        String userPw = Menu.lector.nextLine();
        this.setPassword(userPw);
        //TODO: confirmarContraseña()
        System.out.println("  -nombre:");
        String userNombre = Menu.lector.nextLine();
        this.setNombre(userNombre);
        System.out.println("  -primer apellido:");
        String userApellido1 = Menu.lector.nextLine();
        this.setApellido1(userApellido1);
        System.out.println("  -segundo apellido:");
        String userApellido2 = Menu.lector.nextLine();
        this.setApellido2(userApellido2);
        System.out.println("  -e-mail:");
        String userEmail = Menu.lector.nextLine();
        this.setEmail(userEmail);
        //TODO acabar de completar
    }

    //Getters y setters:
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
