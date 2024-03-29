/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.control;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import principal.Constantes;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DatosDebug;
import principal.herramientas.DibujoDebug;

/**
 *
 * @author GAMER ARRAX
 */
public class Raton extends MouseAdapter {

    private final Cursor cursor;
    private Point posicion;
    
    private double tiempoSgteClick;
    private double tiempoInicio;

    private boolean click;
    private boolean click2;
    private boolean recogiendo = false;

    public Raton(final SuperficieDibujo sd) {
        Toolkit configuracion = Toolkit.getDefaultToolkit();

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

    public void actualizar(final SuperficieDibujo sd) {
        actualizarPosicion(sd);
    }

    public void dibujar(Graphics g) {

        DatosDebug.recogerDatos("RatonX:  " + posicion.getX());
        DatosDebug.recogerDatos("RatonY:  " + posicion.getY());
        /*DibujoDebug.dibujarString(g, "RatonX:  " + posicion.getX(), 30, 60, Color.white);
        DibujoDebug.dibujarString(g, "RatonX:  " + posicion.getX(), 30, 70, Color.white);*/

    }

    private void actualizarPosicion(final SuperficieDibujo sd) {
        final Point posicionInicial = MouseInfo.getPointerInfo().getLocation();

        SwingUtilities.convertPointFromScreen(posicionInicial, sd);

        posicion.setLocation(posicionInicial.getX(), posicionInicial.getY());

    }

    public void mousePressed(MouseEvent e) {
        tiempoSgteClick = tiempoInicio - System.currentTimeMillis();
        if (!e.isConsumed()) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (!click && tiempoSgteClick <500) {
                    click = true;
                    click2 = false;
                    tiempoInicio = System.currentTimeMillis();

                }
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                if (!click2 && tiempoSgteClick <500) {
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

    public void mouseTyped(MouseEvent e) {
    }

    public Rectangle getPosicionRectangle() {
        final Rectangle area = new Rectangle(posicion.x, posicion.y, 1, 1);
        return area;
    }

    public Point getPosicion() {
        return posicion;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public boolean isClick() {
        return click;
    }

    public boolean isClick2() {
        return click2;
    }

    public boolean isRecogiendo() {
        return recogiendo;
    }

}
