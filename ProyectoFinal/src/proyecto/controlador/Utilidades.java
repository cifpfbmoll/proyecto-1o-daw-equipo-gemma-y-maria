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
            //TODO completar el logger de errores y adaptar los catches
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
        String textoAdaptado = texto.substring(0, 1).toUpperCase() + texto.substring(1);;   
        return textoAdaptado.trim();
    }

}
