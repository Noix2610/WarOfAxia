/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;

/**
 *
 * @author GAMER ARRAX
 */
public class EscaladorElementos {

    public static Rectangle escalarRectangleArriba(final Rectangle r) {
        int x = (int) (r.x * Constantes.FACTOR_ESCALADO_X);
        int y = (int) (r.y * Constantes.FACTOR_ESCALADO_Y);
        int ancho = (int) (r.width * Constantes.FACTOR_ESCALADO_X);
        int alto = (int) (r.height * Constantes.FACTOR_ESCALADO_Y);

        final Rectangle rr = new Rectangle(x, y, ancho, alto);

        return rr;
    }

    public static Point escalarArriba(final Point p) {
        int x = (int) (p.x * Constantes.FACTOR_ESCALADO_X);
        int y = (int) (p.y * Constantes.FACTOR_ESCALADO_Y);

        final Point pr = new Point(x, y);

        return pr;
    }
    
    public static Point escalarAbajo(final Point p) {
        int x = (int) (p.x / Constantes.FACTOR_ESCALADO_X);
        int y = (int) (p.y / Constantes.FACTOR_ESCALADO_Y);

        final Point pr = new Point(x, y);

        return pr;
    }
}
