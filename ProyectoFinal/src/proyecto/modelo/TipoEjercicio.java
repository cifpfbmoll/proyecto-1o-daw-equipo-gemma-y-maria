package proyecto.modelo;

import java.util.EnumSet;

/**
 * Enumerador con los posibles tipos de ejercicios.<br>
 * Los métodos propios se mantienen aquí y no pasan al controlador: son métodos muy específicos de la clase.
 *
 * @author Gemma Díez Cabeza y María Rabanales González
 * @version 20.05.10.am
 */
public enum TipoEjercicio {
    CD("cardio"),
    GM("gymnastics"),
    HT("hypertrophy"),
    MB("mobility"),
    RH("rehabilitation"),
    RT("running technique"),
    ST("strongman"),
    WF("weight lifting"),
    YG("yoga");

    private String textoTipoEjercicio;

    TipoEjercicio(String textoTipoEjercicio) {
        this.textoTipoEjercicio = textoTipoEjercicio;
    }

    public String getTextoTipoEjercicio() {
        return textoTipoEjercicio;
    }

    public void setTextoTipoEjercicio(String textoTipoEjercicio) {
        this.textoTipoEjercicio = textoTipoEjercicio;
    }

    public static boolean comprobarTipo(String texto) {
        for (TipoEjercicio tipo : TipoEjercicio.values()) {
            if (tipo.name().equals(texto)) {
                return true;
            }
        }
        return false;
    }

    public static void imprimirTipo() {
        EnumSet.allOf(TipoEjercicio.class).forEach(tipo -> System.out.println("  -" + tipo + ":" + tipo.getTextoTipoEjercicio()));
    }

}
