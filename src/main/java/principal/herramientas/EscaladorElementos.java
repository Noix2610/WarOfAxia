package principal.herramientas;

import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;

/**
 * Clase que proporciona métodos para escalar rectángulos y puntos hacia arriba y hacia abajo según un factor de escala
 * definido en Constantes.
 */
public class EscaladorElementos {

    /**
     * Escala un rectángulo hacia arriba según el factor de escala definido en Constantes.
     *
     * @param r El rectángulo a escalar.
     * @return El rectángulo escalado hacia arriba.
     */
    public static Rectangle escalarRectangleArriba(final Rectangle r) {
        int x = (int) (r.x * Constantes.FACTOR_ESCALADO_X);
        int y = (int) (r.y * Constantes.FACTOR_ESCALADO_Y);
        int ancho = (int) (r.width * Constantes.FACTOR_ESCALADO_X);
        int alto = (int) (r.height * Constantes.FACTOR_ESCALADO_Y);

        final Rectangle rr = new Rectangle(x, y, ancho, alto);

        return rr;
    }

    /**
     * Escala un punto hacia arriba según el factor de escala definido en Constantes.
     *
     * @param p El punto a escalar.
     * @return El punto escalado hacia arriba.
     */
    public static Point escalarArriba(final Point p) {
        int x = (int) (p.x * Constantes.FACTOR_ESCALADO_X);
        int y = (int) (p.y * Constantes.FACTOR_ESCALADO_Y);

        final Point pr = new Point(x, y);

        return pr;
    }

    /**
     * Escala un punto hacia abajo según el factor de escala definido en Constantes.
     *
     * @param p El punto a escalar.
     * @return El punto escalado hacia abajo.
     */
    public static Point escalarAbajo(final Point p) {
        int x = (int) (p.x / Constantes.FACTOR_ESCALADO_X);
        int y = (int) (p.y / Constantes.FACTOR_ESCALADO_Y);

        final Point pr = new Point(x, y);

        return pr;
    }
}
