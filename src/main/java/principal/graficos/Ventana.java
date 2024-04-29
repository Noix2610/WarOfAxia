package principal.graficos;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import principal.herramientas.CargadorRecursos;

/**
 * Clase que representa la ventana principal del juego.
 */
public class Ventana extends JFrame {

    // SerialVersionUID para compatibilidad entre versiones
    private static final long serialVersionUID = 987654321L;

    // Título de la ventana
    private final String titulo;

    // Icono de la ventana
    private final ImageIcon icono;

    /**
     * Constructor de la ventana.
     *
     * @param titulo El título de la ventana.
     * @param sd La superficie de dibujo a mostrar en la ventana.
     */
    public Ventana(final String titulo, final SuperficieDibujo sd) {
        this.titulo = titulo;

        // Cargar el icono de la ventana desde un archivo de imagen
        BufferedImage imagen = CargadorRecursos.cargarImagenCompatibleOpaca("/icons/icono.png");
        this.icono = new ImageIcon(imagen);

        // Configurar la ventana
        configuracionVentana(sd);
    }

    /**
     * Método para configurar la ventana.
     *
     * @param sd La superficie de dibujo a mostrar en la ventana.
     */
    private void configuracionVentana(final SuperficieDibujo sd) {
        // Configurar título, cierre, tamaño, icono, diseño y ubicación de la ventana
        setTitle(titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(icono.getImage());
        setLayout(new BorderLayout());
        add(sd, BorderLayout.CENTER);
        setUndecorated(true); // Sin barra de título
        pack(); // Ajustar tamaño automáticamente
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true); // Hacer visible la ventana
    }
}
