package proyecto.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Incluye métodos con utilidades genéricas.
 */
public class Utilidades {
    /**
     * Toma la fecha actual y la adapta al formato de la base de datos.
     * @return tipo String con la fecha en formato yyyy-MM-dd
     */
    public static String obtenerFecha() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
