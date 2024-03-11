/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.juego.menu_tienda;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.herramientas.DibujoDebug;

/**
 *
 * @author GAMER ARRAX
 */
public class EstructuraTienda {

    public final Color COLOR_BANNER_SUPERIOR;
    public final Color COLOR_BANNER_LATERAL;
    public final Color COLOR_FONDO;

    public final Rectangle BANNER_SUPERIOR;
    public final Rectangle BANNER_LATERAL;
    public final Rectangle FONDO;

    public final int MARGEN_HORIZONTAL_ETIQUETAS;
    public final int MARGEN_VERTICAL_ETIQUETAS;
    public final int ANCHO_ETIQUETAS;
    public final int ALTO_ETIQUETAS;

    public EstructuraTienda() {
        COLOR_BANNER_SUPERIOR = Constantes.COLOR_NARANJA;
        COLOR_BANNER_LATERAL = Color.BLACK;
        COLOR_FONDO = Color.WHITE;

        BANNER_SUPERIOR = new Rectangle(0, 0, Constantes.ANCHO_JUEGO, 20);
        BANNER_LATERAL = new Rectangle(0, BANNER_SUPERIOR.height, 140,
                Constantes.ALTO_JUEGO - BANNER_SUPERIOR.height);
        FONDO = new Rectangle(BANNER_LATERAL.x + BANNER_LATERAL.width, BANNER_LATERAL.y,
                Constantes.ANCHO_JUEGO - BANNER_LATERAL.width, Constantes.ALTO_JUEGO - BANNER_SUPERIOR.height);

        MARGEN_HORIZONTAL_ETIQUETAS = 20;
        MARGEN_VERTICAL_ETIQUETAS = 20;
        ANCHO_ETIQUETAS = 100;
        ALTO_ETIQUETAS = 20;
    }

    public void actualizar() {

    }

    public void dibujar(Graphics g) {

        DibujoDebug.dibujarRectanguloRelleno(g, BANNER_SUPERIOR, COLOR_BANNER_SUPERIOR);
        DibujoDebug.dibujarRectanguloRelleno(g, BANNER_LATERAL, COLOR_BANNER_LATERAL);
        DibujoDebug.dibujarRectanguloRelleno(g, FONDO, COLOR_FONDO);

        DibujoDebug.dibujarString(g, "DINERO: $" + ElementosPrincipales.inventario.dinero,
                Constantes.MARGEN_X / 2 + 4, BANNER_SUPERIOR.y + BANNER_SUPERIOR.height * 2, Color.BLACK);

        g.setFont(g.getFont().deriveFont(18f));
        DibujoDebug.dibujarString(g, "TIENDA",
                BANNER_SUPERIOR.x + BANNER_SUPERIOR.width / 2 + 40, BANNER_SUPERIOR.y + BANNER_SUPERIOR.height + 10, Color.BLACK);
        g.setFont(g.getFont().deriveFont(12f));
    }
}
