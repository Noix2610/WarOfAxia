/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.habilidades.Habilidad;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorTooltip;
import principal.herramientas.MedidorString;
import principal.inventario.TipoObjeto;

/**
 *
 * @author GAMER ARRAX
 */
public class MenuCrecimiento extends SeccionMenu{

    public MenuCrecimiento(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
        super(nombreSeccion, etiquetaMenu, em);
    }
    final Rectangle panelHabilidadesActivas = new Rectangle(em.FONDO.x + margenGeneral,
            barraPeso.y + barraPeso.height + margenGeneral,
            Constantes.ANCHO_JUEGO / 2, Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);
    final Rectangle panelHabilidadesPasivas = new Rectangle(panelHabilidadesActivas.x + panelHabilidadesActivas.width
            + margenGeneral, panelHabilidadesActivas.y, 150, panelHabilidadesActivas.height);

    final Rectangle titularPanelHabilidadesActivas = new Rectangle(panelHabilidadesActivas.x, panelHabilidadesActivas.y,
            panelHabilidadesActivas.width, 24);
    final Rectangle titularPanelHabilidadesPasivas = new Rectangle(panelHabilidadesPasivas.x, panelHabilidadesPasivas.y,
            panelHabilidadesPasivas.width, 24);

    public static Habilidad habilidadSeleccionada;

    @Override
    public void actualizar() {
        actualizarPosicionesMenuActivas();
        actualizarPosicionesMenuPasivas();
        actualizarSeleccionRaton();
        actualizarObjetoSeleccionado();
    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        dibujarLimitePeso(g);
        dibujarPaneles(g);
        dibujarHabilidadesActivas(g);
        dibujarHabilidadesPasivas(g);
        dibujarPaginador(g);

        if (MenuEquipo.mostrarTooltip) {
            dibujarTooltipPeso(g, sd);
            dibujarTooltipHabilidades(g, sd);
        }
        
        
        // Otros elementos de dibujo del menú de habilidades
    }

    private void dibujarPaneles(final Graphics g) {
        dibujarPanelActivas(g, panelHabilidadesActivas, titularPanelHabilidadesActivas, "APRENDIZAJE HABILIDADES");
        dibujarPanelPasivas(g, panelHabilidadesPasivas, titularPanelHabilidadesPasivas, "ASIGNAR PUNTOS ATRIBUTOS");
    }

    private void dibujarHabilidadesActivas(Graphics g) {

        List<Habilidad> activas = ElementosPrincipales.inventario.getHabilidades();
        Point pi = new Point(panelHabilidadesActivas.x + margenGeneral,
                titularPanelHabilidadesActivas.height * 2 + margenGeneral * 2);
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanel(g, activas, pi, lado);
        if (habilidadSeleccionada != null) {
            DibujoDebug.dibujarImagen(g, habilidadSeleccionada.getImagenActual(),
                    new Point(habilidadSeleccionada.getPosicionFlotante().x,
                            habilidadSeleccionada.getPosicionFlotante().y));
        }

    }
    
    private void actualizarPosicionesMenuPasivas() {
    if (ElementosPrincipales.inventario.getHabilidades().isEmpty()) {
        return;
    }

    final Point piPasivas = new Point(titularPanelHabilidadesPasivas.x + margenGeneral * 2,
            titularPanelHabilidadesPasivas.y + margenGeneral * 3 + margenGeneral);

    final int lado = Constantes.LADO_SPRITE;
    int contadorPasivas = 0;

    for (Habilidad habilidadActual : ElementosPrincipales.inventario.getHabilidades()) {
        Rectangle nuevaPosicion;
        if (habilidadActual.getTipoHabilidad() == TipoObjeto.PASIVA) {
            int fila = contadorPasivas / 3; // Calcular la fila en base a la cantidad total de pasivas
            int columna = contadorPasivas % 3; // Calcular la columna

            if (fila < 6) { // Si estamos en las primeras 6 filas
                nuevaPosicion = new Rectangle(piPasivas.x + columna * (lado + margenGeneral),
                        piPasivas.y + fila * (lado + margenGeneral), lado, lado);
            } else { // Si estamos en la fila 7
                int offset = 6 * (lado + margenGeneral); // Desplazamiento hacia abajo de las primeras 6 filas
                nuevaPosicion = new Rectangle(piPasivas.x + columna * (lado + margenGeneral),
                        piPasivas.y + offset, lado, lado);
                if (columna >= 2) { // Si estamos en la segunda columna
                    break;
                }
            }
            contadorPasivas++;
        } else {
            continue; // Omitir habilidades de tipo desconocido
        }
        habilidadActual.setPosicionMenu(nuevaPosicion);
    }
}

    // Otros métodos necesarios para el menú de habilidades
    /*private void actualizarPosicionesMenu() {
        if (ElementosPrincipales.inventario.getHabilidades().isEmpty()) {
            return;
        }

        final Point piActivas = new Point(panelHabilidadesActivas.x + margenGeneral,
                titularPanelHabilidadesActivas.y + margenGeneral * 3 + margenGeneral);
        final Point piPasivas = new Point(titularPanelHabilidadesPasivas.x + margenGeneral * 2,
                titularPanelHabilidadesPasivas.y + margenGeneral * 3 + margenGeneral);

        final int lado = Constantes.LADO_SPRITE;
        int contadorActivas = 0;
        int contadorPasivas = 0;

        for (Habilidad habilidadActual : ElementosPrincipales.inventario.getHabilidades()) {
            Rectangle nuevaPosicion;
            if (habilidadActual.getTipoHabilidad() == TipoObjeto.ACTIVA) {
                nuevaPosicion = new Rectangle(piActivas.x + contadorActivas % 5 * (lado + margenGeneral),
                        piActivas.y + contadorActivas / 5 * (lado + margenGeneral), lado, lado);
                contadorActivas++;
            }
            else if (habilidadActual.getTipoHabilidad() == TipoObjeto.PASIVA) {
                int filaPasiva = contadorPasivas / 5; // Calcular la fila en base a la cantidad total de pasivas
                if (filaPasiva == 6) { // Si estamos en la fila 7
                    nuevaPosicion = new Rectangle(piPasivas.x + contadorPasivas % 3 * (lado + margenGeneral),
                            piPasivas.y + filaPasiva * (lado + margenGeneral), lado, lado);
                }
                else {
                    nuevaPosicion = new Rectangle(piPasivas.x + contadorPasivas % 3 * (lado + margenGeneral),
                            piPasivas.y + filaPasiva * (lado + margenGeneral), lado, lado);
                }
                contadorPasivas++;
            }
            else {
                continue; // Omitir habilidades de tipo desconocido
            }
            habilidadActual.setPosicionMenu(nuevaPosicion);
        }
    }*/
    private void actualizarPosicionesMenuActivas() {
        if (ElementosPrincipales.inventario.getHabilidades().isEmpty()) {
            return;
        }

        final Point piActivas = new Point(panelHabilidadesActivas.x + margenGeneral,
                titularPanelHabilidadesActivas.y + margenGeneral * 3 + margenGeneral);

        final int lado = Constantes.LADO_SPRITE;
        int contadorActivas = 0;

        for (Habilidad habilidadActual : ElementosPrincipales.inventario.getHabilidades()) {
            Rectangle nuevaPosicion;
            if (habilidadActual.getTipoHabilidad() == TipoObjeto.ACTIVA) {
                int fila = contadorActivas / 5; // Calcular la fila en base a la cantidad total de activas
                int columna = contadorActivas % 5; // Calcular la columna

                if (fila < 6) { // Si estamos en las primeras 6 filas
                    nuevaPosicion = new Rectangle(piActivas.x + columna * (lado + margenGeneral),
                            piActivas.y + fila * (lado + margenGeneral), lado, lado);
                }
                else { // Si estamos en la fila 7
                    int offset = 6 * (lado + margenGeneral); // Desplazamiento hacia abajo de las primeras 6 filas
                    nuevaPosicion = new Rectangle(piActivas.x + columna * (lado + margenGeneral),
                            piActivas.y + offset, lado, lado);
                    if (columna >= 3) { // Si estamos en la tercera columna
                        break; // Desplazar hacia la derecha
                    }
                }
                contadorActivas++;
            }
            else {
                continue; // Omitir habilidades de tipo desconocido
            }
            habilidadActual.setPosicionMenu(nuevaPosicion);
        }
    }

    private void actualizarSeleccionRaton() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();
        if (habilidadSeleccionada == null) {
            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelHabilidadesActivas))) {
                if (ElementosPrincipales.inventario.getHabilidades().isEmpty()) {
                    return;
                }
                for (Habilidad habilidad : ElementosPrincipales.inventario.getHabilidades()) {
                    if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                            .intersects(EscaladorElementos.escalarRectangleArriba(habilidad.getPosicionMenu()))) {
                        habilidadSeleccionada = habilidad;
                    }
                }
            }
            else {
                habilidadSeleccionada = null;
            }
        }
    }

    private void actualizarObjetoSeleccionado() {
        if (habilidadSeleccionada != null) {

            if (GestorPrincipal.sd.getRaton().isClick2()) {
                habilidadSeleccionada = null;
                return;
            }
            Point pr = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            habilidadSeleccionada.setPosicionFlotante(
                    new Rectangle(pr.x, pr.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }

    }

    private void dibujarPanelActivas(Graphics g, Rectangle panelHabilidadesActivas, Rectangle titularPanelHabilidadesActivas, String habilidades_activas) {
        // Lógica para dibujar el panel de consumibles...
        dibujarPanel(g, panelHabilidadesActivas, titularPanelHabilidadesActivas, habilidades_activas);
        dibujarHabilidadesActivas(g);

    }

    private void dibujarElementosEnPanel(final Graphics g, List<Habilidad> habilidades, Point pi, int lado) {
        if (habilidades.isEmpty()) {
            return;
        }

        for (int i = 0; i < habilidades.size(); i++) {
            Habilidad habilidadActual = habilidades.get(i);
            Rectangle posicionMenu = habilidadActual.getPosicionMenu();

            DibujoDebug.dibujarImagen(g, habilidadActual.getImagenActual(), posicionMenu.x, posicionMenu.y);

        }

    }

    private void dibujarPanel(Graphics g, Rectangle panelHabilidadesActivas, Rectangle titularPanelHabilidadesActivas,
            String habilidades_activas) {

        g.setColor(Color.DARK_GRAY);
        DibujoDebug.dibujarRectanguloContorno(g, panelHabilidadesActivas);
        DibujoDebug.dibujarRectanguloRelleno(g, titularPanelHabilidadesActivas);
        g.setColor(Color.white);
        DibujoDebug.dibujarString(g, habilidades_activas, new Point(
                panelHabilidadesActivas.x + titularPanelHabilidadesActivas.width / 2
                - MedidorString.medirAnchoPixeles(g, habilidades_activas) / 2, panelHabilidadesActivas.y
                + titularPanelHabilidadesActivas.height - MedidorString.medirAltoPixeles(g, habilidades_activas) / 2));

    }

    private void dibujarPanelPasivas(Graphics g, final Rectangle panel, final Rectangle titularPanel, final String nombrePanel) {
        // Lógica para dibujar el panel de objetos clave...
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        dibujarHabilidadesPasivas(g);
    }

    private void dibujarHabilidadesPasivas(final Graphics g) {
        List<Habilidad> habilidades = ElementosPrincipales.inventario.getHabilidades();
        Point pi = new Point(titularPanelHabilidadesPasivas.x + margenGeneral,
                titularPanelHabilidadesPasivas.height * 2 + margenGeneral * 2);
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanel(g, habilidades, pi, lado);
        if (habilidadSeleccionada != null) {
            DibujoDebug.dibujarImagen(g, habilidadSeleccionada.getImagenActual(),
                    new Point(habilidadSeleccionada.getPosicionFlotante().x,
                            habilidadSeleccionada.getPosicionFlotante().y));
        }

    }

    private void dibujarTooltipPeso(final Graphics g, SuperficieDibujo sd) {
        String textoCarga = String.format("%.1f", ElementosPrincipales.jugador.getGa().getPesoActual());
        String textoCargaTotal = String.format("%.1f", ElementosPrincipales.jugador.getGa().getLimitePeso());
        String textoFinal = textoCarga + "/" + textoCargaTotal;
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(barraPeso))) {
            GeneradorTooltip.dibujarTooltip(g, sd, textoFinal);
        }

    }

    private void dibujarTooltipHabilidades(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelHabilidadesActivas))
                || posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelHabilidadesPasivas))) {
            for (Habilidad habilidadActual : ElementosPrincipales.inventario.getHabilidades()) {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(habilidadActual.getPosicionMenu()))) {
                    if (habilidadSeleccionada == null) {
                        DibujoDebug.dibujarRectanguloContorno(g, habilidadActual.getPosicionMenu(), Color.DARK_GRAY);
                        // Dibuja el tooltip solo si objetoSeleccionado no es null
                        dibujarTooltipHabilidad(g, sd, habilidadActual);
                    }
                    else if (habilidadSeleccionada != null) {
                        dibujarTooltipHabilidad(g, sd, habilidadSeleccionada);
                    }
                }
            }
        }
    }

    private void dibujarTooltipHabilidad(Graphics g, SuperficieDibujo sd, Habilidad habilidad) {
        // Aquí puedes personalizar la apariencia del tooltip según tus necesidades
        String tooltipText = habilidad.getNombre() + "\n" + "Mana: "+habilidad.getManaUtilizado()+"\n"+habilidad.getDescripcion();
        GeneradorTooltip.dibujarTooltipMejorado(g, sd, tooltipText);
    }

    private void dibujarPaginador(final Graphics g) {
        final int anchoBoton = 24;
        final int altoBoton = 12;

        int anteriorX = panelHabilidadesActivas.x + panelHabilidadesActivas.width - Constantes.LADO_SPRITE * 2 + 4;

        int anteriorY = panelHabilidadesActivas.y + panelHabilidadesActivas.height - (Constantes.LADO_SPRITE / 2);

        final Rectangle anterior = new Rectangle(anteriorX - 4, anteriorY, anchoBoton, altoBoton);
        final Rectangle siguiente = new Rectangle(anterior.x + anterior.width + margenGeneral, anterior.y,
                anchoBoton, altoBoton);
        g.setColor(Color.blue);

        DibujoDebug.dibujarRectanguloContorno(g, anterior);
        DibujoDebug.dibujarRectanguloContorno(g, siguiente);
        DibujoDebug.dibujarString(g, "<<", anterior.x + anterior.width - 18, anterior.y + anterior.height - 5);
        DibujoDebug.dibujarString(g, ">>", siguiente.x + siguiente.width - 18, siguiente.y + siguiente.height - 5);
    }
    
    public Habilidad getTipoObjetoSeleccionado() {
        return habilidadSeleccionada;
    }

    public Habilidad getObjetoSeleccionado() {
        return habilidadSeleccionada;
    }

    public void eliminarObjetoSeleccionado() {
        habilidadSeleccionada = null;
    }
    
}
