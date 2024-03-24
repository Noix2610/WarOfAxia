/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.juego.menu_tienda;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;

/**
 *
 * @author GAMER ARRAX
 */
public class GestorTienda implements EstadoJuego {

    private final SuperficieDibujo sd;

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

    public GestorTienda(final SuperficieDibujo sd) {
        this.sd = sd;
        estructuraTienda = new EstructuraTienda();

        secciones = new SeccionTienda[7]; // Ajusta la longitud del arreglo según la cantidad de secciones

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

        salir = new Rectangle(estructuraTienda.BANNER_LATERAL.x
                + estructuraTienda.MARGEN_HORIZONTAL_ETIQUETAS,
                etiquetaPociones.y + etiquetaPociones.height + estructuraTienda.MARGEN_VERTICAL_ETIQUETAS,
                estructuraTienda.ANCHO_ETIQUETAS, estructuraTienda.ALTO_ETIQUETAS);

        seccionActual = secciones[0];
        ElementosPrincipales.inventario.objetosTienda = ElementosPrincipales.mapa.getTienda
        (ElementosPrincipales.mapa.idTiendaAbierta).getObjetosTienda();
        System.out.println("id tienda abierta: "+ ElementosPrincipales.mapa.idTiendaAbierta);
    }

    @Override
    public void actualizar() {
        for (int i = 0; i < secciones.length; i++) {
            if (sd.getRaton().isClick()
                    && sd.getRaton().getPosicionRectangle().intersects(secciones[i].getEtiquetaMenuEscalada())) {

                if (secciones[i] instanceof TiendaArmas) {
                    TiendaArmas seccion = (TiendaArmas) secciones[i];
                    if (seccion.getObjetoSeleccionadoCompra() != null || seccion.getObjetoSeleccionadoVenta() != null) {
                        seccion.eliminarObjetoSeleccionado();
                    }
                }
                else if (secciones[i] instanceof TiendaArmaduras) {
                    TiendaArmaduras seccion = (TiendaArmaduras) secciones[i];
                    if (seccion.getObjetoSeleccionadoCompra() != null || seccion.getObjetoSeleccionadoVenta() != null) {
                        seccion.eliminarObjetoSeleccionado();
                    }
                }
                else if (secciones[i] instanceof TiendaAccesorios) {
                    TiendaAccesorios seccion = (TiendaAccesorios) secciones[i];
                    if (seccion.getObjetoSeleccionadoCompra() != null || seccion.getObjetoSeleccionadoVenta() != null) {
                        seccion.eliminarObjetoSeleccionado();
                    }
                }
                else if (secciones[i] instanceof TiendaComida) {
                    TiendaComida seccion = (TiendaComida) secciones[i];
                    if (seccion.getObjetoSeleccionadoCompra() != null || seccion.getObjetoSeleccionadoVenta() != null) {
                        seccion.eliminarObjetoSeleccionado();
                    }
                }/*else if(secciones[i] instanceof MenuInventario){
                    MenuInventario seccion = (MenuInventario) secciones[i];
                    if(seccion.getObjetoSeleccionado() != null){
                        seccion.eliminarObjetoSeleccionado();
                    }
                }*/
                seccionActual = secciones[i];
            }
        }

        seccionActual.actualizar();
    }

    @Override
    public void dibujar(Graphics2D g) {
        estructuraTienda.dibujar(g);
        for (int i = 0; i < secciones.length; i++) {

            if (seccionActual == secciones[i]) {
                if (sd.getRaton().getPosicionRectangle().intersects(secciones[i].getEtiquetaMenuEscalada())) {
                    secciones[i].dibujarEtiquetaActivaResaltada(g);
                }
                else {
                    secciones[i].dibujarEtiquetaActiva(g);
                }
            }
            else {
                if (sd.getRaton().getPosicionRectangle().intersects(secciones[i].getEtiquetaMenuEscalada())) {
                    secciones[i].dibujarEtiquetaInactResaltada(g);
                }
                else {
                    secciones[i].dibujarEtiquetaInactiva(g);

                }

            }
        }

        seccionActual.dibujar(g, sd);

        DibujoDebug.dibujarRectanguloRelleno(g, this.salir, Color.WHITE);

        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(salir))) {
            g.setFont(g.getFont().deriveFont(14f));
            DibujoDebug.dibujarString(g, "SALIR", salir.x + Constantes.LADO_SPRITE / 2 - 4,
                    salir.y + salir.height - Constantes.LADO_SPRITE / 4, Color.black);
            final Rectangle etiquetaResaltadaSalir = new Rectangle(salir.x + salir.width - 10, salir.y + 5, 5, salir.height - 10);
            DibujoDebug.dibujarRectanguloRelleno(g, etiquetaResaltadaSalir, Color.RED);
            if (sd.getRaton().isClick()) {
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
