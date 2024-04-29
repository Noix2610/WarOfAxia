/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal.control;

import java.awt.Cursor; // Importa la clase Cursor de awt
import java.awt.Graphics; // Importa la clase Graphics de awt
import java.awt.MouseInfo; // Importa la clase MouseInfo de awt
import java.awt.Point; // Importa la clase Point de awt
import java.awt.Rectangle; // Importa la clase Rectangle de awt
import java.awt.Toolkit; // Importa la clase Toolkit de awt
import java.awt.event.MouseAdapter; // Importa la clase MouseAdapter de awt.event
import java.awt.event.MouseEvent; // Importa la clase MouseEvent de awt.event
import java.awt.image.BufferedImage; // Importa la clase BufferedImage de awt.image
import javax.swing.SwingUtilities; // Importa la clase SwingUtilities de javax.swing
import principal.Constantes; // Importa la clase Constantes del paquete principal
import principal.graficos.SuperficieDibujo; // Importa la clase SuperficieDibujo del paquete principal.graficos
import principal.herramientas.CargadorRecursos; // Importa la clase CargadorRecursos del paquete principal.herramientas
import principal.herramientas.DatosDebug; // Importa la clase DatosDebug del paquete principal.herramientas

/**
 * Clase que gestiona las interacciones del ratón.
 */
public class Raton extends MouseAdapter {

    private final Cursor cursor; // Cursor personalizado
    private Point posicion; // Posición del ratón

    private double tiempoSgteClick; // Tiempo hasta el siguiente clic
    private double tiempoInicio; // Tiempo de inicio del clic

    private boolean click; // Indica si se ha realizado un clic
    private boolean click2; // Indica si se ha realizado un segundo clic
    private boolean recogiendo = false; // Indica si se está recogiendo algo

    // Constructor
    public Raton(final SuperficieDibujo sd) {
        Toolkit configuracion = Toolkit.getDefaultToolkit();

        // Carga de la imagen del cursor
        BufferedImage iconoCursor = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.RUTA_RATON);

        Constantes.LADO_CURSOR = iconoCursor.getWidth();

        Point punta = new Point(2, 2);
        this.cursor = configuracion.createCustomCursor(iconoCursor, punta, "Cursor defecto");

        posicion = new Point();
        actualizarPosicion(sd);

        click = false;
        click2 = false;
        tiempoInicio = System.currentTimeMillis();
    }

    // Método para actualizar la posición del ratón
    public void actualizar(final SuperficieDibujo sd) {
        actualizarPosicion(sd);
    }

    // Método para dibujar el ratón
    public void dibujar(Graphics g) {
        DatosDebug.recogerDatos("RatonX:  " + posicion.getX());
        DatosDebug.recogerDatos("RatonY:  " + posicion.getY());
        /*DibujoDebug.dibujarString(g, "RatonX:  " + posicion.getX(), 30, 60, Color.white);
        DibujoDebug.dibujarString(g, "RatonX:  " + posicion.getX(), 30, 70, Color.white);*/
    }

    // Método para actualizar la posición del ratón
    private void actualizarPosicion(final SuperficieDibujo sd) {
        final Point posicionInicial = MouseInfo.getPointerInfo().getLocation();

        SwingUtilities.convertPointFromScreen(posicionInicial, sd);

        posicion.setLocation(posicionInicial.getX(), posicionInicial.getY());
    }

    // Método para manejar el clic del ratón
    public void mousePressed(MouseEvent e) {
        tiempoSgteClick = tiempoInicio - System.currentTimeMillis();
        if (!e.isConsumed()) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (!click && tiempoSgteClick < 500) {
                    click = true;
                    click2 = false;
                    tiempoInicio = System.currentTimeMillis();
                }
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                if (!click2 && tiempoSgteClick < 500) {
                    click2 = true;
                    click = false;
                    recogiendo = true;
                    tiempoInicio = System.currentTimeMillis();
                }
            }

            // Marcar el evento como consumido para evitar procesar clics adicionales hasta que se libere el botón del ratón
            e.consume();
        }
    }

    // Método para manejar el release del ratón
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            click = false;
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            click2 = false;
            recogiendo = false;
            return;
        }
    }

    // Método para manejar el clic del ratón
    public void mouseTyped(MouseEvent e) {
    }

    // Método para obtener el área de la posición del ratón
    public Rectangle getPosicionRectangle() {
        final Rectangle area = new Rectangle(posicion.x, posicion.y, 1, 1);
        return area;
    }

    // Método para obtener la posición del ratón
    public Point getPosicion() {
        return posicion;
    }

    // Método para obtener el cursor
    public Cursor getCursor() {
        return this.cursor;
    }

    // Método para comprobar si se ha realizado un clic
    public boolean isClick() {
        return click;
    }

    // Método para comprobar si se ha realizado un segundo clic
    public boolean isClick2() {
        return click2;
    }

    // Método para comprobar si se está recogiendo algo
    public boolean isRecogiendo() {
        return recogiendo;
    }

}
