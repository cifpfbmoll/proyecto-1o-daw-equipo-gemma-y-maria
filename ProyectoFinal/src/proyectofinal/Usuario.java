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
    private String dni;
    private String password;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private int telefono;
    private String direccion;
    private TipoEjercicio tipoEjercicio;

    //Constructores:
    public Usuario() {
    }

    public Usuario(String dni, String password, String nombre, String apellido1, String apellido2, String email, int telefono, String direccion, TipoEjercicio tipoEjercicio) {
        this.password = password;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipoEjercicio = tipoEjercicio;
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

    /**
     * Pide los datos del nuevo usuario por consola y los setea en el objeto.
     */
    public void crearNuevoUsuario() {
        String userDni = "";
        do {
            System.out.println("  -dni:");
            userDni = Menu.lector.nextLine().toUpperCase().trim();
        } while (!validarDni(userDni));
        this.setDni(userDni);
        System.out.println("  -contraseña:");
        String userPw = Menu.lector.nextLine().toLowerCase().trim();
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
        System.out.println("  -telefono:");
        int userPhone = Integer.parseInt(Menu.lector.nextLine());
        this.setTelefono(userPhone);
        System.out.println("  -direccion:");
        String userAdress = Menu.lector.nextLine();
        this.setDireccion(userAdress);
    }

    /**
     * Es una validación básica, no buscamos que la letra coincida porque los datos preintroducidos son inventados.<br>
     * Dos comprobaciones: que los ocho primeros caracteres sean números (ascii entre 48 y 57) y que el último sea letra.
     * Sólo consideramos DNI, no NIE ni pasaportes.
     * @param dni
     * @return true si cumple los requisitos de validez
     */
    public static boolean validarDni(String dni){
        boolean dniValido = true;
        if (dni.length() != 9 || !Character.isLetter(dni.charAt(8))) {
            dniValido = false;
        } else {
            for (int i = 0; i < dni.length() - 1; i++) {
                int numAscii = dni.codePointAt(i);
                boolean numValido = (numAscii > 47 && numAscii < 58);
                if (numValido == false) {
                    dniValido = false;
                }
            }
        }
        if (dniValido == false) {
            System.out.println("Este DNI no cumple los requisitos.");
        }
        return dniValido;
    }
    
    public static boolean validarPassword(String pw){
        //TODO
        return false;
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

    public TipoEjercicio getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(TipoEjercicio tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }

}
