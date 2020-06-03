package proyecto.controlador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     */
    public static void logErrores() {
        try (BufferedWriter writerMejorado = new BufferedWriter(new FileWriter("errores.txt", true))) {
            writerMejorado.write("\n__________________________________________________________________________________\n");
            writerMejorado.write(obtenerFechaHora());
            //TODO completar el logger de errores y adaptar los catches
            System.out.println("Error guardado en el archivo errores.txt");
        } catch (IOException eio) {
            System.out.println("IOException. Error al leer el logger de errores.");
        }
    }
    
    /**
     * 
     * 
     * @param str
     * @return string sin espacios y con la primera letra en mayúcula
     */

    public static String modificarString(String str) {
        String strsinespacios = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ') {
                strsinespacios += str.charAt(i);
            }
        }
        if (strsinespacios == null || strsinespacios.isEmpty()) {
            return strsinespacios;
        } else {
            return strsinespacios.substring(0, 1).toUpperCase() + strsinespacios.substring(1);
        }
    }

}
