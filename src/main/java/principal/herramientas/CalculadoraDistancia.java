package principal.herramientas;

import java.awt.Point;

/**
 * Clase que proporciona métodos para calcular la distancia entre dos puntos.
 */
public class CalculadoraDistancia {

    /**
     * Calcula la distancia entre dos puntos.
     * @param p1 El primer punto.
     * @param p2 El segundo punto.
     * @return La distancia entre los dos puntos.
     */
    public static double getDistanciaEntrePuntos(final Point p1, final Point p2) {
        // Utiliza la fórmula de distancia entre dos puntos en un plano cartesiano
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }
}
