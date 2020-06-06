package proyecto.controlador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import proyecto.vista.MenuPrincipal;

/**
 * Incluye métodos con utilidades genéricas.
 */
public class Utilidades {

    /**
     * Toma la fecha actual y la adapta al formato de la base de datos.
     *
     * @return tipo String con la fecha en formato yyyy-MM-dd
     */
    public static String obtenerFecha() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * Toma la fecha actual y la adapta al formato del archivo de errores.
     *
     * @return tipo String con la fecha en formato yyyy-MM-dd
     */
    public static String obtenerFechaHora() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * Guarda los errores encontrados en un archivo en el proyecto.
     * @param titulo String con el texto que titula el error
     * @param traza String con printStackTrace()
     */
    public static void logErrores(String titulo, String traza) {
        try (BufferedWriter writerMejorado = new BufferedWriter(new FileWriter("errores.txt", true))) {
            writerMejorado.write("___________________________________ NUEVO ERROR ___________________________________\n");
            writerMejorado.write(titulo + "\n");
            writerMejorado.write(obtenerFechaHora() + "\n\n");
            writerMejorado.write(traza + "\n");
            writerMejorado.write("\n__________________________________________________________________________________\n\n");
            System.out.println("Error guardado en el archivo 'errores.txt'.");
        } catch (IOException eio) {
            System.out.println("IOException. Error al leer el logger de errores.");
        }
    }
    
    /**
     * Modifica un string de manera que la primera letra sea siempre en mayúsculas y no tenga espacios al principio ni al final.
     * 
     * @param texto String para adaptar
     * @return string sin espacios y con la primera letra en mayúcula
     */
    public static String adaptarStringMayusMinus(String texto) {
        if (texto == null) {
            return null;            //Con esto controlo la recepción de valores nulos desde tabla.
        }
        String textoAdaptado = texto.substring(0, 1).toUpperCase() + texto.substring(1);;   
        return textoAdaptado.trim();
    }

    /**
     * Evita la recepción de valores no numéricos en las llamadas al lector que lo requieran, anulando la posibilidad de errores al emplear Integer.parseInt()
     * 
     * @return valor numérico (tipo int) solicitado al usuario
     */
    public static int recibirNumero() {
        int num;
        String posibleNum = MenuPrincipal.lector.nextLine();
        while (!esNumerico(posibleNum)) {
            System.out.println("Error. Se solicita un número:");
            posibleNum = MenuPrincipal.lector.nextLine();
        }
        num = Integer.parseInt(posibleNum);   
        return num;
    }
    
    /**
     * Comprueba si un valor es un integersin tener que recurrir a librerías externas, como por ejemplo para el método alternativo StringUtils.isNumeric()
     * 
     * @param num valor tipo String que se desea comprobar
     * @return true si el valor es integer
     */
    private static boolean esNumerico(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
