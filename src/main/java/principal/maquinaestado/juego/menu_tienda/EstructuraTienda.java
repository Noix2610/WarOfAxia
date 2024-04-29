/*
 * EstructuraTienda.java
 * Clase que define la estructura visual de la tienda en el juego.
 */
package principal.maquinaestado.juego.menu_tienda;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.herramientas.DibujoDebug;

public class EstructuraTienda {

    // Colores de los elementos de la tienda
    public final Color COLOR_BANNER_SUPERIOR;
    public final Color COLOR_BANNER_LATERAL;
    public final Color COLOR_FONDO;

    // Rectángulos que definen la estructura visual de la tienda
    public final Rectangle BANNER_SUPERIOR;
    public final Rectangle BANNER_LATERAL;
    public final Rectangle FONDO;

    // Margen y tamaño de las etiquetas
    public final int MARGEN_HORIZONTAL_ETIQUETAS;
    public final int MARGEN_VERTICAL_ETIQUETAS;
    public final int ANCHO_ETIQUETAS;
    public final int ALTO_ETIQUETAS;

    // Constructor
    public EstructuraTienda() {
        // Definir colores
        COLOR_BANNER_SUPERIOR = Constantes.COLOR_NARANJA;
        COLOR_BANNER_LATERAL = Color.BLACK;
        COLOR_FONDO = Color.WHITE;

        // Definir rectángulos
        BANNER_SUPERIOR = new Rectangle(0, 0, Constantes.ANCHO_JUEGO, 20);
        BANNER_LATERAL = new Rectangle(0, BANNER_SUPERIOR.height, 140,
                Constantes.ALTO_JUEGO - BANNER_SUPERIOR.height);
        FONDO = new Rectangle(BANNER_LATERAL.x + BANNER_LATERAL.width, BANNER_LATERAL.y,
                Constantes.ANCHO_JUEGO - BANNER_LATERAL.width, Constantes.ALTO_JUEGO - BANNER_SUPERIOR.height);

        // Definir margen y tamaño de etiquetas
        MARGEN_HORIZONTAL_ETIQUETAS = 20;
        MARGEN_VERTICAL_ETIQUETAS = 20;
        ANCHO_ETIQUETAS = 100;
        ALTO_ETIQUETAS = 20;
    }

    // Método para actualizar la estructura de la tienda (no se utiliza en este caso)
    public void actualizar() {
        // No hay actualizaciones en la estructura de la tienda
    }

    // Método para dibujar la estructura visual de la tienda
    public void dibujar(Graphics g) {
        // Dibujar los rectángulos que forman la estructura de la tienda
        DibujoDebug.dibujarRectanguloRelleno(g, BANNER_SUPERIOR, COLOR_BANNER_SUPERIOR);
        DibujoDebug.dibujarRectanguloRelleno(g, BANNER_LATERAL, COLOR_BANNER_LATERAL);
        DibujoDebug.dibujarRectanguloRelleno(g, FONDO, COLOR_FONDO);

        // Dibujar el dinero del jugador en la tienda
        DibujoDebug.dibujarString(g, "DINERO: $" + ElementosPrincipales.inventario.dinero,
                Constantes.MARGEN_X / 2 + 4, BANNER_SUPERIOR.y + BANNER_SUPERIOR.height * 2, Color.BLACK);

        // Dibujar el título de la tienda
        g.setFont(g.getFont().deriveFont(18f));
        DibujoDebug.dibujarString(g, "TIENDA",
                BANNER_SUPERIOR.x + BANNER_SUPERIOR.width / 2 + 40, BANNER_SUPERIOR.y + BANNER_SUPERIOR.height + 10, Color.BLACK);
        g.setFont(g.getFont().deriveFont(12f)); // Restaurar el tamaño de fuente predeterminado
    }
}
