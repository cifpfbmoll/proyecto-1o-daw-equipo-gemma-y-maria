package proyectofinal;

import java.util.stream.Stream;

/**
 * Enumerador con los posibles tipos de ejercicios.
 * @author Gemma Díez Cabeza & María Rabanales González
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
        for (TipoEjercicio tipo : TipoEjercicio.values() ) {
            if (tipo.name().equals(texto)) {
                return true;
            }
        }
        return false;
    }
}
