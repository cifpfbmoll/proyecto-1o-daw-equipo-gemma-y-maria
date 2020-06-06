package proyecto.vista;

import javax.swing.*;
import java.awt.*;
import proyecto.modelo.Entrenamiento;

/**
 * Clase para el mostrar el trabajo realizado en interfaces.<br>
 * Imprime un programa de entrenamiento por una aplicación nueva.
 *
 * @author Gemma Díez Cabeza y María Rabanales González
 * @version 20.06.06.am
 */
public class InterfazEntrenamiento {
    //Variables y métodos de clase básicos para operar:
    static Toolkit pantalla = Toolkit.getDefaultToolkit();     //para obtener la resolucion de cada pantalla
    static Dimension tamanoPantalla = pantalla.getScreenSize();
    private static final int alturaPantalla = tamanoPantalla.height;
    private static final int anchoPantalla = tamanoPantalla.width;

    public static void mostrarInterfaz(Entrenamiento entreno) {
        JFrame marco = new JFrame();

        //Para definir las características del marco:
        marco.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);      //comportamiento al cerrar. otros: EXIT_ON_CLOSE, DO_NOTHING_ON_CLOSE

        marco.setSize(anchoPantalla / 3, alturaPantalla / 2);
        marco.setLocation(anchoPantalla / 3, alturaPantalla / 4);
        marco.setResizable(false);
        marco.setTitle("PROGRAMA DE ENTRENAMIENTO (by Gemma & Maria)");

        Image icono = pantalla.getImage("src/graficos/biceps.png");
        marco.setIconImage(icono);

        //Para crear la lámina
        JPanel panel = new JPanel();
        marco.add(panel);        //para añadir la primera lámina
        panel.setLayout(new BorderLayout(5, 5));

        //Para añadir el título:
        JLabel titulo = new JLabel ("CONSULTA DE ENTRENAMIENTO:");
        Font fuenteTitulo = new Font (titulo.getFont().getName(), Font.BOLD, (titulo.getFont().getSize() + 12));
        titulo.setFont(fuenteTitulo);
        titulo.setForeground(Color.BLUE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);
        
        //Para añadir el texto:
        JTextArea texto = new JTextArea("\n   Fecha de creación: " + entreno.getFecha() + "\n");
        texto.append("   Tipo de entrenamiento: " + entreno.getTipo().getTextoTipoEjercicio() + " (código " + entreno.getTipo().name() + ")\n");
        texto.append("   Preparado por el entrenador " + entreno.getEntrenador().getNombre() + " " + entreno.getEntrenador().getApellido1() + 
                " " + entreno.getEntrenador().getApellido2() + "\n");
        texto.append("   Para el alumno " + entreno.getAlumno().getNombre() + " " + entreno.getAlumno().getApellido1() + " " + 
                entreno.getAlumno().getApellido2() + "\n\n");
        texto.append("   TABLA DE EJERCICIOS:\n\n");
        for (int i = 0; i < entreno.getListaEjercicios().size(); i++) {
            texto.append("        " + (i + 1) + ":\n");
            texto.append("           " + entreno.getListaEjercicios().get(i).getEjercicio().getNombre() + " (código " + 
                    entreno.getListaEjercicios().get(i).getEjercicio().getCodigo() + ")\n");
            texto.append("           " + entreno.getListaEjercicios().get(i).getEjercicio().getDescripcion() + "\n");
            if (entreno.getListaEjercicios().get(i).getRepeticiones() != 0) {           //El tipo primitivo nunca puede ser nulo, es 0 por defecto
                texto.append("           Repeticiones: " + entreno.getListaEjercicios().get(i).getRepeticiones() + "\n");
            }
            if (entreno.getListaEjercicios().get(i).getMinMinutos() != 0) {
                texto.append("           Tiempo mínimo: " + entreno.getListaEjercicios().get(i).getMinMinutos() + " minutos\n");
            }
            texto.append("\n");
        }
        texto.setLocation(10, 10);
        texto.setOpaque(false);
        texto.setBorder(null);
        texto.setSize (anchoPantalla/3 - 20, alturaPantalla/3 - 20);
        texto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(texto);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        //Para verlo todo:
        marco.setVisible(true);    //para hacerlo visible
    }    
}
