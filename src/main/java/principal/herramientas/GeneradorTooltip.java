/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.graficos.SuperficieDibujo;

/**
 * Clase que proporciona métodos para generar y dibujar tooltips en la pantalla. Un tooltip es un pequeño cuadro de
 * texto que aparece alrededor del cursor del ratón para proporcionar información adicional. Esta clase proporciona
 * métodos para generar, calcular la posición y dibujar tooltips en la pantalla, mejorados para soportar texto de varias
 * líneas. Los tooltips son cuadros de texto pequeños que aparecen alrededor del cursor del ratón para proporcionar
 * información adicional al usuario.
 */
public class GeneradorTooltip {

    /**
     * Genera la posición del tooltip basado en la posición del cursor del ratón.
     *
     * @param pi La posición inicial del cursor del ratón.
     * @return La posición del tooltip.
     */
    public static Point generarTooltip(final Point pi) {

        final int x = pi.x;
        final int y = pi.y;

        final Point centroCanvas = new Point(Constantes.CENTRO_VENTANA_X, Constantes.CENTRO_VENTANA_Y);

        final Point centroCanvasEscalado = new Point(EscaladorElementos.escalarArriba(centroCanvas).x,
                EscaladorElementos.escalarArriba(centroCanvas).y);

        final int margenCursor = 5;

        final Point pf = new Point();

        if (x <= centroCanvasEscalado.x) {
            if (y <= centroCanvasEscalado.y) {
                pf.x = x + Constantes.LADO_CURSOR + margenCursor;
                pf.y = y + Constantes.LADO_CURSOR + margenCursor;
            }
            else {
                pf.x = x + Constantes.LADO_CURSOR + margenCursor;
                pf.y = y - Constantes.LADO_CURSOR - margenCursor;
            }
        }
        else {
            if (y <= centroCanvasEscalado.y) {
                pf.x = x - Constantes.LADO_CURSOR - margenCursor;
                pf.y = y + Constantes.LADO_CURSOR + margenCursor;
            }
            else {
                pf.x = x - Constantes.LADO_CURSOR - margenCursor;
                pf.y = y - Constantes.LADO_CURSOR - margenCursor;
            }
        }

        return pf;
    }

    /**
     * Obtiene la posición del tooltip basado en la posición del cursor del ratón.
     *
     * @param pi La posición inicial del cursor del ratón.
     * @return La posición del tooltip.
     */
    public static String getPosicionTooltip(final Point pi) {
        final int x = pi.x;
        final int y = pi.y;

        final Point centroCanvas = new Point(Constantes.CENTRO_VENTANA_X, Constantes.CENTRO_VENTANA_Y);
        final Point centroCanvasEscalado = new Point(EscaladorElementos.escalarArriba(centroCanvas).x,
                EscaladorElementos.escalarArriba(centroCanvas).y);

        String posicion;

        if (x <= centroCanvasEscalado.x) {
            if (y <= centroCanvasEscalado.y) {
                posicion = "no";
            }
            else {
                posicion = "so";
            }
        }
        else {
            if (y <= centroCanvasEscalado.y) {
                posicion = "ne";
            }
            else {
                posicion = "se";
            }
        }

        return posicion;
    }

    /**
     * Dibuja un tooltip en la pantalla.
     *
     * @param g El objeto Graphics utilizado para dibujar.
     * @param sd La superficie de dibujo donde se dibujará el tooltip.
     * @param texto El texto que se mostrará en el tooltip.
     */
    public static void dibujarTooltip(final Graphics g, final SuperficieDibujo sd, final String texto) {

        final Point posicionRaton = sd.getRaton().getPosicion();
        final Point posicionTooltip = GeneradorTooltip.generarTooltip(posicionRaton);
        final String pistaPosicion = GeneradorTooltip.getPosicionTooltip(posicionRaton);
        final Point posicionTooltipEscalada = EscaladorElementos.escalarAbajo(posicionTooltip);

        final int ancho = MedidorString.medirAnchoPixeles(g, texto);
        final int alto = MedidorString.medirAltoPixeles(g, texto);
        final int margenFuente = 2;
        Rectangle tooltip = null;

        switch (pistaPosicion) {

            case "no":
                tooltip = new Rectangle(posicionTooltipEscalada.x, posicionTooltipEscalada.y,
                        ancho + margenFuente * 2, alto);
                break;
            case "ne":
                tooltip = new Rectangle(posicionTooltipEscalada.x - ancho, posicionTooltipEscalada.y,
                        ancho + margenFuente * 2, alto);
                break;
            case "so":
                tooltip = new Rectangle(posicionTooltipEscalada.x, posicionTooltipEscalada.y - alto,
                        ancho + margenFuente * 2, alto);
                break;
            case "se":
                tooltip = new Rectangle(posicionTooltipEscalada.x - ancho, posicionTooltipEscalada.y - alto,
                        ancho + margenFuente * 2, alto);
                break;

        }
        DibujoDebug.dibujarRectanguloRelleno(g, tooltip, Color.black);
        DibujoDebug.dibujarString(g, texto, new Point(tooltip.x + margenFuente, tooltip.y + tooltip.height), Color.white);
    }

    /**
     * Dibuja un tooltip mejorado en la pantalla, con soporte para texto de varias líneas.
     *
     * @param g El objeto Graphics utilizado para dibujar.
     * @param sd La superficie de dibujo donde se dibujará el tooltip.
     * @param texto El texto que se mostrará en el tooltip.
     */
    public static void dibujarTooltipMejorado(Graphics g, SuperficieDibujo sd, String texto) {
        Point posicionRaton = sd.getRaton().getPosicion();
        Point posicionTooltip = GeneradorTooltip.generarTooltip(posicionRaton);
        String pistaPosicion = GeneradorTooltip.getPosicionTooltip(posicionRaton);
        Point posicionTooltipEscalada = EscaladorElementos.escalarAbajo(posicionTooltip);

        int margenFuente = 2;

        // Dividir el texto en líneas
        String[] lineas = texto.split("\n");

        // Medir el ancho máximo y alto total del tooltip
        int anchoMaximoLinea = 0;
        int altoTotal = 0;
        for (String linea : lineas) {
            int anchoLinea = g.getFontMetrics().stringWidth(linea);
            anchoMaximoLinea = Math.max(anchoMaximoLinea, anchoLinea);
            altoTotal += g.getFontMetrics().getHeight();
        }

        Rectangle tooltip = null;

        switch (pistaPosicion) {
            case "no":
                tooltip = new Rectangle(posicionTooltipEscalada.x, posicionTooltipEscalada.y,
                        anchoMaximoLinea + margenFuente * 2, altoTotal + margenFuente * 2);
                break;
            case "ne":
                tooltip = new Rectangle(posicionTooltipEscalada.x - anchoMaximoLinea, posicionTooltipEscalada.y,
                        anchoMaximoLinea + margenFuente * 2, altoTotal + margenFuente * 2);
                break;
            case "so":
                tooltip = new Rectangle(posicionTooltipEscalada.x, posicionTooltipEscalada.y - altoTotal - margenFuente * 2,
                        anchoMaximoLinea + margenFuente * 2, altoTotal + margenFuente * 2);
                break;
            case "se":
                tooltip = new Rectangle(posicionTooltipEscalada.x - anchoMaximoLinea, posicionTooltipEscalada.y - altoTotal - margenFuente * 2,
                        anchoMaximoLinea + margenFuente * 2, altoTotal + margenFuente * 2);
                break;
        }

        g.setColor(Color.yellow);
        g.drawRect(tooltip.x - 1, tooltip.y - 1, tooltip.width + 2, tooltip.height + 2);
        g.setColor(new Color(240, 240, 240));
        g.fillRect(tooltip.x, tooltip.y, tooltip.width, tooltip.height);
        g.setColor(Color.black);

        // Dibujar cada línea de texto en su rectángulo correspondiente
        int offsetY = tooltip.y + margenFuente;
        for (String linea : lineas) {
            int anchoLinea = g.getFontMetrics().stringWidth(linea);
            Rectangle rectanguloLinea = new Rectangle(tooltip.x + margenFuente, offsetY, anchoLinea, g.getFontMetrics().getHeight());
            g.drawString(linea, rectanguloLinea.x, rectanguloLinea.y + g.getFontMetrics().getAscent());

            offsetY += g.getFontMetrics().getHeight();
        }
    }

}
