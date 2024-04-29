/*
 * GestorTienda.java
 * Clase que gestiona la visualización y funcionalidad de la tienda en el juego.
 */
package principal.maquinaestado.juego.menu_tienda;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import principal.Constantes;
import principal.GestorPrincipal;
import principal.control.GestorControles;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;

public class GestorTienda implements EstadoJuego {

    private final SeccionTienda[] secciones;
    private SeccionTienda seccionActual;

    private final EstructuraTienda estructuraTienda;
    private final Rectangle etiquetaArmas;
    private final Rectangle etiquetaArmaduras;
    private final Rectangle etiquetaAccesorios;
    private final Rectangle etiquetaComida;
    private final Rectangle etiquetaHabilidades;
    private final Rectangle etiquetaMateriales;
    private final Rectangle etiquetaPociones;
    public final Rectangle salir;

    // Constructor
    public GestorTienda() {
        estructuraTienda = new EstructuraTienda();

        secciones = new SeccionTienda[7]; // Ajusta la longitud del arreglo según la cantidad de secciones

        // Definir las etiquetas para cada sección de la tienda
        etiquetaArmas = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS, estructuraTienda.BANNER_LATERAL.y
                + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS, estructuraTienda.ANCHO_ETIQUETAS,
                estructuraTienda.ALTO_ETIQUETAS);

        secciones[0] = new TiendaArmas("ARMAS", etiquetaArmas, estructuraTienda);

        etiquetaArmaduras = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaArmas.y + etiquetaArmas.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        secciones[1] = new TiendaArmaduras("ARMADURAS", etiquetaArmaduras, estructuraTienda);

        etiquetaAccesorios = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaArmaduras.y + etiquetaArmaduras.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        secciones[2] = new TiendaAccesorios("ACCESORIOS", etiquetaAccesorios, estructuraTienda);

        // Nuevas etiquetas
        etiquetaComida = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaAccesorios.y + etiquetaAccesorios.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        secciones[3] = new TiendaComida("COMIDA", etiquetaComida, estructuraTienda);

        etiquetaHabilidades = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaComida.y + etiquetaComida.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        secciones[4] = new TiendaHabilidades("HABILIDADES", etiquetaHabilidades, estructuraTienda);

        etiquetaMateriales = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaHabilidades.y + etiquetaHabilidades.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        secciones[5] = new TiendaMateriales("MATERIALES", etiquetaMateriales, estructuraTienda);

        etiquetaPociones = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaMateriales.y + etiquetaMateriales.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        secciones[6] = new TiendaPociones("POCIONES", etiquetaPociones, estructuraTienda);

        // Etiqueta para salir de la tienda
        salir = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaPociones.y + etiquetaPociones.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        seccionActual = secciones[0];
    }

    // Método para actualizar el estado de la tienda
    @Override
    public void actualizar() {
        for (int i = 0; i < secciones.length; i++) {
            if (GestorPrincipal.sd.getRaton().isClick()
                    && GestorPrincipal.sd.getRaton().getPosicionRectangle().intersects(secciones[i].getEtiquetaMenuEscalada())) {
                seccionActual = secciones[i];
            }
        }
        seccionActual.actualizar();
    }

    // Método para dibujar la tienda en pantalla
    @Override
    public void dibujar(Graphics2D g) {
        // Dibujar la estructura visual de la tienda
        estructuraTienda.dibujar(g);

        // Dibujar las etiquetas de las secciones de la tienda
        for (int i = 0; i < secciones.length; i++) {
            if (seccionActual == secciones[i]) {
                if (GestorPrincipal.sd.getRaton().getPosicionRectangle().intersects(secciones[i].getEtiquetaMenuEscalada())) {
                    secciones[i].dibujarEtiquetaActivaResaltada(g);
                }
                else {
                    secciones[i].dibujarEtiquetaActiva(g);
                }
            }
            else {
                if (GestorPrincipal.sd.getRaton().getPosicionRectangle().intersects(secciones[i].getEtiquetaMenuEscalada())) {
                    secciones[i].dibujarEtiquetaInactResaltada(g);
                }
                else {
                    secciones[i].dibujarEtiquetaInactiva(g);
                }
            }
        }

        // Dibujar la sección actual de la tienda
        seccionActual.dibujar(g, GestorPrincipal.sd);

        // Dibujar el botón de salir de la tienda
        DibujoDebug.dibujarRectanguloRelleno(g, this.salir, Color.WHITE);

        if (GestorPrincipal.sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(salir))) {
            g.setFont(g.getFont().deriveFont(14f));
            DibujoDebug.dibujarString(g, "SALIR", salir.x + Constantes.LADO_SPRITE / 2 - 4,
                    salir.y + salir.height - Constantes.LADO_SPRITE / 4, Color.black);
            final Rectangle etiquetaResaltadaSalir = new Rectangle(salir.x + salir.width - 10, salir.y + 5, 5, salir.height - 10);
            DibujoDebug.dibujarRectanguloRelleno(g, etiquetaResaltadaSalir, Color.RED);
            if (GestorPrincipal.sd.getRaton().isClick()) {
                GestorControles.teclado.tiendaActiva = false;
            }
        }
        else {
            g.setFont(g.getFont().deriveFont(12f));
            DibujoDebug.dibujarString(g, "SALIR", salir.x + Constantes.LADO_SPRITE / 2,
                    salir.y + salir.height - Constantes.LADO_SPRITE / 4, Color.black);
        }
    }

}
