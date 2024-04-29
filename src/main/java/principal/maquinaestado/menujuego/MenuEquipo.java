/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.entes.AlmacenEquipo;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorTooltip;
import principal.herramientas.MedidorString;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.inventario.armaduras.Armadura;
import principal.inventario.armaduras.ProteccionAlta;
import principal.inventario.armaduras.ProteccionBaja;
import principal.inventario.armaduras.ProteccionLateral;
import principal.inventario.armaduras.ProteccionMedia;
import principal.inventario.armas.Arma;
import principal.inventario.armas.ArmaDosManos;
import principal.inventario.armas.ArmaUnaMano;
import principal.inventario.armas.SinArma;
import principal.inventario.joyas.Accesorio;
import principal.inventario.joyas.Anillo;
import principal.inventario.joyas.Collar;
import principal.inventario.joyas.Joya;
import principal.sprites.HojaSprites;

/**
 *
 * @author GAMER ARRAX
 */
public class MenuEquipo extends SeccionMenu {

    private final ArrayList<Rectangle> etiquetasEquipables;
    public static boolean mostrarTooltip = true;
    private int etiquetaEquipo;

    final Rectangle panelObjetos = new Rectangle(em.FONDO.x + margenGeneral,
            barraPeso.y + barraPeso.height + margenGeneral,
            180, Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);

    final Rectangle panelEquipo = new Rectangle(panelObjetos.x + panelObjetos.width + margenGeneral,
            panelObjetos.y, 146, panelObjetos.height);

    final Rectangle panelAtributos = new Rectangle(panelEquipo.x + panelEquipo.width + margenGeneral,
            panelObjetos.y, 132, panelEquipo.height);
    final Rectangle titularPanelObjetos = new Rectangle(panelObjetos.x, panelObjetos.y,
            panelObjetos.width, 24);

    final Rectangle titularPanelEquipo = new Rectangle(panelEquipo.x, panelEquipo.y,
            panelEquipo.width, 24);

    final Rectangle titularPanelAtributos = new Rectangle(panelAtributos.x, panelAtributos.y,
            panelAtributos.width, 24);

    final Rectangle etiquetaArma = new Rectangle(titularPanelEquipo.x + margenGeneral,
            titularPanelEquipo.y + titularPanelEquipo.height + margenGeneral / 2,
            titularPanelEquipo.width - margenGeneral * 2, margenGeneral * 2 - 16
            + MedidorString.medirAltoPixeles(GestorPrincipal.sd.getGraphics(), "ARMADURA"));
    final Rectangle contenedorArma1 = new Rectangle(etiquetaArma.x + 1, etiquetaArma.y + etiquetaArma.height,
            etiquetaArma.width / 2 - 2,
            Constantes.LADO_SPRITE + 2);
    final Rectangle contenedorArma2 = new Rectangle(etiquetaArma.x + etiquetaArma.width / 2 + 1, etiquetaArma.y
            + etiquetaArma.height, etiquetaArma.width / 2 - 2,
            Constantes.LADO_SPRITE + 2);

    final Rectangle etiquetaArmaduras = new Rectangle(etiquetaArma.x,
            etiquetaArma.y + titularPanelEquipo.height + contenedorArma1.height + margenGeneral / 2,
            titularPanelEquipo.width - margenGeneral * 2, margenGeneral * 2 - 16
            + MedidorString.medirAltoPixeles(GestorPrincipal.sd.getGraphics(), "ARMADURA"));
    final Rectangle contenedorArmadura1 = new Rectangle(etiquetaArmaduras.x + 1, etiquetaArmaduras.y
            + etiquetaArmaduras.height, etiquetaArmaduras.width / 2 - 2,
            Constantes.LADO_SPRITE);
    final Rectangle contenedorArmadura2 = new Rectangle(etiquetaArmaduras.x + etiquetaArmaduras.width / 2 + 1,
            etiquetaArmaduras.y + etiquetaArmaduras.height, etiquetaArmaduras.width / 2 - 2,
            Constantes.LADO_SPRITE);
    final Rectangle contenedorArmadura3 = new Rectangle(etiquetaArmaduras.x + 1,
            contenedorArmadura2.y + contenedorArmadura1.height + 2, etiquetaArmaduras.width / 2 - 2,
            Constantes.LADO_SPRITE);
    final Rectangle contenedorArmadura4 = new Rectangle(etiquetaArmaduras.x + etiquetaArmaduras.width / 2 + 1,
            contenedorArmadura2.y + contenedorArmadura1.height + 2, etiquetaArmaduras.width / 2 - 2,
            Constantes.LADO_SPRITE);

    final Rectangle etiquetaJoyas = new Rectangle(etiquetaArma.x,
            etiquetaArmaduras.y + titularPanelEquipo.height + contenedorArmadura3.height + margenGeneral * 5,
            titularPanelEquipo.width - margenGeneral * 2, margenGeneral * 2 - 16
            + MedidorString.medirAltoPixeles(GestorPrincipal.sd.getGraphics(), "JOYERIA"));

    final Rectangle contenedorCollar = new Rectangle(etiquetaJoyas.x + 1, etiquetaJoyas.y
            + etiquetaJoyas.height, etiquetaJoyas.width / 2 - 2,
            Constantes.LADO_SPRITE);
    final Rectangle contenedorAccesorio = new Rectangle(etiquetaJoyas.x + etiquetaJoyas.width / 2 + 1,
            etiquetaJoyas.y + etiquetaJoyas.height, etiquetaJoyas.width / 2 - 2,
            Constantes.LADO_SPRITE);
    final Rectangle contenedorAnillo1 = new Rectangle(etiquetaJoyas.x + 1,
            contenedorCollar.y + contenedorCollar.height + 2, etiquetaJoyas.width / 2 - 2,
            Constantes.LADO_SPRITE);
    final Rectangle contenedorAnillo2 = new Rectangle(etiquetaJoyas.x + etiquetaJoyas.width / 2 + 1,
            contenedorCollar.y + contenedorCollar.height + 2, etiquetaJoyas.width / 2 - 2,
            Constantes.LADO_SPRITE);

    public static Objeto objetoSeleccionado = null;
    private TipoObjeto tipoObjetoSeleccionado;
    private Rectangle armaEquipada1;
    private Rectangle armaEquipada2;

    public MenuEquipo(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
        super(nombreSeccion, etiquetaMenu, em);
        etiquetaEquipo = 0;
        etiquetasEquipables = new ArrayList<>();

    }

    @Override
    public void actualizar() {
        actualizarEtiquetasEquipables();
        actualizarPosicionesMenu(etiquetaEquipo);
        actualizarSeleccionRaton();
        actualizarObjetoSeleccionado();
        removerObjetoEquipado();

    }

    @Override
    public void dibujar(final Graphics g, final SuperficieDibujo sd) {
        dibujarLimitePeso(g);
        dibujarPaneles(g);

        if (mostrarTooltip) {
            dibujarTooltipEquipo(g, sd);
            dibujarTooltipPeso(g, sd);
            if (etiquetaEquipo < 2) {
                dibujarTooltipArmas(g, sd);
            }
            else if (etiquetaEquipo > 1 && etiquetaEquipo < 6) {
                dibujarTooltipArmaduras(g, sd);
            }
            else {
                dibujarTooltipJoyas(g, sd);
            }
        }
        dibujarPaginador(g);
    }

    private void actualizarSeleccionArma1() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo))
                && objetoSeleccionado != null && objetoSeleccionado instanceof Arma
                && GestorPrincipal.sd.getRaton().isClick()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArma1))) {

            if (!sonMismasArmas((Arma) objetoSeleccionado, ElementosPrincipales.jugador.getAe().getArma1())) {
                Objeto arma1 = (Objeto) ElementosPrincipales.jugador.getAe().getArma1();
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(arma1)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(arma1);
                }
                ElementosPrincipales.jugador.getAe().cambiarArma1((Arma) objetoSeleccionado);

                // Actualiza la lista de equipo actual
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(objetoSeleccionado)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(objetoSeleccionado);
                }
                tipoObjetoSeleccionado = ElementosPrincipales.jugador.getTipoObjetoFromEquipoActual(objetoSeleccionado);
                ElementosPrincipales.jugador.getAe().equipoActual.add(objetoSeleccionado);
                objetoSeleccionado = null;
            }

        }
    }

    private void actualizarSeleccionArma2() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo))
                && objetoSeleccionado != null && objetoSeleccionado instanceof Arma
                && GestorPrincipal.sd.getRaton().isClick()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArma2))) {

            if (!sonMismasArmas((Arma) objetoSeleccionado, ElementosPrincipales.jugador.getAe().getArma1())) {
                // Verifica si el contenedor de arma2 tiene un arma a distancia
                if (ElementosPrincipales.jugador.getAe().getArma2() instanceof ArmaDosManos
                        || ElementosPrincipales.jugador.getAe().getArma2() == null) {
                    // Remueve el arma a distancia del contenedor 2 y de la lista de equipo actual
                    Arma armaEnContenedor2 = ElementosPrincipales.jugador.getAe().getArma2();
                    ElementosPrincipales.jugador.getAe().cambiarArma2(null);
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(armaEnContenedor2);
                }
                Objeto arma2 = (Objeto) ElementosPrincipales.jugador.getAe().getArma1();
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(arma2)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(arma2);
                }
                ElementosPrincipales.jugador.getAe().cambiarArma1((Arma) objetoSeleccionado);

                // Actualiza la lista de equipo actual
                ElementosPrincipales.jugador.getAe().equipoActual.add(objetoSeleccionado);
                objetoSeleccionado = null;
            }

        }
    }

    private void actualizarSeleccionArmadura(Rectangle contenedor) {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo))
                && objetoSeleccionado != null
                && GestorPrincipal.sd.getRaton().isClick()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedor))) {

            if (objetoSeleccionado instanceof ProteccionMedia) {
                Objeto armadura = (Objeto) ElementosPrincipales.jugador.getAe().getArmaduraMedia();
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(armadura)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(armadura);
                }
                ElementosPrincipales.jugador.getAe().setArmaduraMedia((Armadura) objetoSeleccionado);
            }
            else if (objetoSeleccionado instanceof ProteccionAlta) {
                Objeto casco = (Objeto) ElementosPrincipales.jugador.getAe().getCasco();
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(casco)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(casco);
                }
                ElementosPrincipales.jugador.getAe().setCasco((Armadura) objetoSeleccionado);
            }
            else if (objetoSeleccionado instanceof ProteccionLateral) {
                Objeto guantes = (Objeto) ElementosPrincipales.jugador.getAe().getGuante();
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(guantes)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(guantes);
                }
                ElementosPrincipales.jugador.getAe().setGuante((Armadura) objetoSeleccionado);
            }
            else if (objetoSeleccionado instanceof ProteccionBaja) {
                Objeto botas = (Objeto) ElementosPrincipales.jugador.getAe().getBota();
                if (ElementosPrincipales.jugador.getAe().equipoActual.contains(botas)) {
                    ElementosPrincipales.jugador.getAe().equipoActual.remove(botas);
                }
                ElementosPrincipales.jugador.getAe().setBota((Armadura) objetoSeleccionado);
            }

            ElementosPrincipales.jugador.getAe().equipoActual.add(objetoSeleccionado);
            objetoSeleccionado = null;
        }

    }

    private void actualizarSeleccionCollar() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo))
                && objetoSeleccionado != null
                && (objetoSeleccionado instanceof Collar)
                && GestorPrincipal.sd.getRaton().isClick()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorCollar))) {

            Objeto collar = (Objeto) ElementosPrincipales.jugador.getAe().getCollar();
            if (ElementosPrincipales.jugador.getAe().equipoActual.contains(collar)) {
                ElementosPrincipales.jugador.getAe().equipoActual.remove(collar);
            }

            ElementosPrincipales.jugador.getAe().setCollar((Joya) objetoSeleccionado);
            ElementosPrincipales.jugador.getAe().equipoActual.add(objetoSeleccionado);
            objetoSeleccionado = null;
        }

    }

    private void actualizarSeleccionAccesorio() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo))
                && objetoSeleccionado != null
                && (objetoSeleccionado instanceof Accesorio)
                && GestorPrincipal.sd.getRaton().isClick()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAccesorio))) {

            Objeto accesorio = (Objeto) ElementosPrincipales.jugador.getAe().getAccesorio();
            if (ElementosPrincipales.jugador.getAe().equipoActual.contains(accesorio)) {
                ElementosPrincipales.jugador.getAe().equipoActual.remove(accesorio);
            }
            ElementosPrincipales.jugador.getAe().setAccesorio((Joya) objetoSeleccionado);

            ElementosPrincipales.jugador.getAe().equipoActual.add(objetoSeleccionado);
            objetoSeleccionado = null;
        }
    }

    private void actualizarSeleccionAnillo() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo))
                && objetoSeleccionado != null
                && (objetoSeleccionado instanceof Anillo)
                && GestorPrincipal.sd.getRaton().isClick()
                && (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo1))
                || posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo2)))) {

            AlmacenEquipo ae = ElementosPrincipales.jugador.getAe();

            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo1))) {
                Joya anillo1 = ae.getAnillo1();
                Joya anillo2 = ae.getAnillo2();

                if (!sonMismosAnillos((Joya) objetoSeleccionado, anillo1)) {
                    if (ElementosPrincipales.jugador.getAe().equipoActual.contains(anillo1)) {
                        ElementosPrincipales.jugador.getAe().equipoActual.remove(anillo1);
                    }
                    ElementosPrincipales.jugador.getAe().setAnillo1((Joya) objetoSeleccionado);

                    if (anillo2 != null && sonMismosAnillos(anillo2, (Joya) objetoSeleccionado)) {
                        ae.setAnillo2(null);
                        ae.equipoActual.remove(anillo2);
                    }
                }
            }
            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo2))) {
                Joya anillo1 = ae.getAnillo1();
                Joya anillo2 = ae.getAnillo2();

                if (!sonMismosAnillos((Joya) objetoSeleccionado, anillo2)) {
                    if (ElementosPrincipales.jugador.getAe().equipoActual.contains(anillo2)) {
                        ElementosPrincipales.jugador.getAe().equipoActual.remove(anillo2);
                    }
                    ElementosPrincipales.jugador.getAe().setAnillo2((Joya) objetoSeleccionado);

                    if (anillo1 != null && sonMismosAnillos(anillo1, (Joya) objetoSeleccionado)) {
                        ae.setAnillo1(null);
                        ae.equipoActual.remove(anillo1);
                    }
                }
            }
            ElementosPrincipales.jugador.getAe().equipoActual.add(objetoSeleccionado);
            objetoSeleccionado = null;
        }
    }

    private void actualizarSeleccionRaton() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetos))) {
            if (ElementosPrincipales.inventario.getEquipo().isEmpty()) {
                return;
            }

            for (Objeto objeto : ElementosPrincipales.inventario.getEquipo()) {
                if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                        .intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMenu()))) {
                    switch (etiquetaEquipo) {
                        case 0:
                            if (objeto instanceof ArmaUnaMano) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                        case 1:
                            if (objeto instanceof ArmaDosManos) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                        case 2:
                            if (objeto instanceof ProteccionMedia) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                        case 3:
                            if (objeto instanceof ProteccionAlta) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                        case 4:
                            if (objeto instanceof ProteccionLateral) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                        case 5:
                            if (objeto instanceof ProteccionBaja) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                        case 6:
                            if (objeto instanceof Joya) {
                                objetoSeleccionado = objeto;
                            }
                            break;
                    }

                }
            }

        }
        actualizarSeleccionArma1();
        //actualizarSeleccionArma2();
        actualizarSeleccionArmadura(contenedorArmadura1);
        actualizarSeleccionArmadura(contenedorArmadura2);
        actualizarSeleccionArmadura(contenedorArmadura3);
        actualizarSeleccionArmadura(contenedorArmadura4);
        actualizarSeleccionCollar();
        actualizarSeleccionAccesorio();
        actualizarSeleccionAnillo();
    }

    private boolean sonMismasArmas(Arma arma1, Arma arma2) {
        return arma1 != null && arma2 != null && arma1.equals(arma2);
    }

    private boolean sonMismosAnillos(Joya anillo1, Joya anillo2) {
        boolean flag = false;
        if (anillo1 != null && anillo2 != null) {
            if (anillo1.getId() == anillo2.getId()) {
                flag = true;
            }
        }
        return flag;
    }

    private void actualizarEtiquetasEquipables() {
        int i = 0;
        for (Rectangle rectangulo : etiquetasEquipables) {
            if (GestorPrincipal.sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(rectangulo))
                    && GestorPrincipal.sd.getRaton().isClick()) {
                etiquetaEquipo = i;
                break;
            }
            i++;
        }
    }

    private void actualizarObjetoSeleccionado() {
        if (objetoSeleccionado != null) {

            if (GestorPrincipal.sd.getRaton().isClick2()) {
                objetoSeleccionado = null;
                return;
            }
            Point pr = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            objetoSeleccionado.setPosicionFlotante(
                    new Rectangle(pr.x, pr.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }
    }

    private void removerObjetoEquipado() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelEquipo)) && objetoSeleccionado == null) {

            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArma1))
                    && GestorPrincipal.sd.getRaton().isClick2()) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto armaEquipada = iterator.next();
                    if (armaEquipada.getId() == ElementosPrincipales.jugador.getAe().getArma1().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().cambiarArma1(null);

            }
            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura1))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getArmaduraMedia()
                    != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto armaduraMedia = iterator.next();
                    if (armaduraMedia.getId() == ElementosPrincipales.jugador.getAe().getArmaduraMedia().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setArmaduraMedia(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura2))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getCasco() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto casco = iterator.next();
                    if (casco.getId() == ElementosPrincipales.jugador.getAe().getCasco().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setCasco(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura3))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getGuante() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto guantes = iterator.next();
                    if (guantes.getId() == ElementosPrincipales.jugador.getAe().getGuante().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setGuante(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura4))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getBota() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto bota = iterator.next();
                    if (bota.getId() == ElementosPrincipales.jugador.getAe().getBota().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setBota(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorCollar))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getCollar() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto collar = iterator.next();
                    if (collar.getId() == ElementosPrincipales.jugador.getAe().getCollar().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setCollar(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAccesorio))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getAccesorio() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto accesorio = iterator.next();
                    if (accesorio.getId() == ElementosPrincipales.jugador.getAe().getAccesorio().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setAccesorio(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo1))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getAnillo1() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto anillo1 = iterator.next();
                    if (anillo1.getId() == ElementosPrincipales.jugador.getAe().getAnillo1().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setAnillo1(null);
            }

            else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo2))
                    && GestorPrincipal.sd.getRaton().isClick2() && ElementosPrincipales.jugador.getAe().getAnillo2() != null) {
                for (Iterator<Objeto> iterator = ElementosPrincipales.jugador.getAe().getEquipoActual().iterator(); iterator.hasNext();) {
                    Objeto anillo2 = iterator.next();
                    if (anillo2.getId() == ElementosPrincipales.jugador.getAe().getAnillo2().getId()) {
                        iterator.remove();
                        break;
                    }
                }
                ElementosPrincipales.jugador.getAe().setAnillo2(null);

            }
        }
    }

    private void actualizarPosicionesMenu(int etiqueta) {
        if (ElementosPrincipales.inventario.getEquipo().isEmpty()) {
            return;
        }

        int contadorArmasUnaMano = 0;
        int contadorArmasDosManos = 0;
        int contadorProteccionMedia = 0;
        int contadorProteccionAlta = 0;
        int contadorProteccionLateral = 0;
        int contadorProteccionBaja = 0;
        int contadorJoya = 0;
        Rectangle posicion;

        final int lado = Constantes.LADO_SPRITE;
        final Point pi = new Point(titularPanelObjetos.x + margenGeneral,
                titularPanelObjetos.y + titularPanelObjetos.height + margenGeneral);

        switch (etiqueta) {
            case 0:
                for (int i = 0; i < ElementosPrincipales.inventario.getUnaMano().size(); i++) {
                    if (i > 51) {
                        i = 0;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getUnaMano().get(i);

                    posicion = new Rectangle(pi.x + contadorArmasUnaMano % 4 * (lado + margenGeneral),
                            pi.y + contadorArmasUnaMano / 4 * (lado + margenGeneral), lado, lado);
                    contadorArmasUnaMano++;
                    objetoActual.setPosicionMenu(posicion);
                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {
                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof ArmaUnaMano)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;
            case 1:
                for (int i = 0; i < ElementosPrincipales.inventario.getDosManos().size(); i++) {
                    if (i > 51) {
                        i = 0;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getDosManos().get(i);

                    posicion = new Rectangle(pi.x + contadorArmasDosManos % 4 * (lado + margenGeneral),
                            pi.y + contadorArmasDosManos / 4 * (lado + margenGeneral), lado, lado);
                    contadorArmasDosManos++;
                    objetoActual.setPosicionMenu(posicion);
                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {
                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof ArmaDosManos)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;
            case 2:

                for (int i = 0; i < ElementosPrincipales.inventario.getArmaduras().size(); i++) {
                    if (i > 51) {
                        break;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getArmaduras().get(i);
                    if (objetoActual instanceof ProteccionMedia) {
                        posicion = new Rectangle(pi.x + contadorProteccionMedia % 4 * (lado + margenGeneral),
                                pi.y + contadorProteccionMedia / 4 * (lado + margenGeneral), lado, lado);
                        contadorProteccionMedia++;
                        objetoActual.setPosicionMenu(posicion);
                    }

                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {
                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof ProteccionMedia)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;
            case 3:
                for (int i = 0; i < ElementosPrincipales.inventario.getArmaduras().size(); i++) {
                    if (i > 51) {
                        break;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getArmaduras().get(i);
                    if (objetoActual instanceof ProteccionAlta) {
                        posicion = new Rectangle(pi.x + contadorProteccionAlta % 4 * (lado + margenGeneral),
                                pi.y + contadorProteccionAlta / 4 * (lado + margenGeneral), lado, lado);
                        contadorProteccionAlta++;
                        objetoActual.setPosicionMenu(posicion);
                    }
                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {
                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof ProteccionAlta)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;
            case 4:
                for (int i = 0; i < ElementosPrincipales.inventario.getArmaduras().size(); i++) {
                    if (i > 51) {
                        break;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getArmaduras().get(i);
                    if (objetoActual instanceof ProteccionLateral) {
                        posicion = new Rectangle(pi.x + contadorProteccionLateral % 4 * (lado + margenGeneral),
                                pi.y + contadorProteccionLateral / 4 * (lado + margenGeneral), lado, lado);
                        contadorProteccionLateral++;
                        objetoActual.setPosicionMenu(posicion);
                    }
                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {
                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof ProteccionLateral)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;
            case 5:
                for (int i = 0; i < ElementosPrincipales.inventario.getArmaduras().size(); i++) {
                    if (i > 51) {
                        break;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getArmaduras().get(i);
                    if (objetoActual instanceof ProteccionBaja) {
                        posicion = new Rectangle(pi.x + contadorProteccionBaja % 4 * (lado + margenGeneral),
                                pi.y + contadorProteccionBaja / 4 * (lado + margenGeneral), lado, lado);
                        contadorProteccionBaja++;
                        objetoActual.setPosicionMenu(posicion);
                    }
                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {
                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof ProteccionBaja)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;
            case 6:
                for (int i = 0; i < ElementosPrincipales.inventario.getJoyas().size(); i++) {
                    if (i > 51) {
                        break;
                    }
                    Objeto objetoActual = ElementosPrincipales.inventario.getJoyas().get(i);

                    posicion = new Rectangle(pi.x + contadorJoya % 4 * (lado + margenGeneral),
                            pi.y + contadorJoya / 4 * (lado + margenGeneral), lado, lado);
                    contadorJoya++;
                    objetoActual.setPosicionMenu(posicion);

                }

                /*for (int i = 0; i < ElementosPrincipales.inventario.getEquipo().size(); i++) {

                    Objeto objeto = ElementosPrincipales.inventario.getEquipo().get(i);
                    if (!(objeto instanceof Joya)) {
                        posicion = new Rectangle(0, 0, 32, 32);
                        objeto.setPosicionMenu(posicion);
                    }
                }*/
                break;

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

    private void dibujarTooltipEquipo(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetos))) {
            for (Objeto objeto : ElementosPrincipales.inventario.getEquipo()) {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMenu()))) {

                    // Dibuja el tooltip solo si objetoSeleccionado no es null
                    dibujarTooltipObjetosEquipados(g, sd, objeto);
                }
            }
        }
    }

    private void dibujarTooltipObjeto(Graphics g, SuperficieDibujo sd, Objeto objeto) {
        // Aquí puedes personalizar la apariencia del tooltip según tus necesidades
        if (etiquetaEquipo < 2 && objeto instanceof Arma) {

            switch (etiquetaEquipo) {
                case 0:
                    if (!(objeto instanceof ArmaUnaMano)) {
                        break;
                    }
                    DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
                    ArmaUnaMano arma = (ArmaUnaMano) objeto;
                    GeneradorTooltip.dibujarTooltipMejorado(g, sd, arma.getNombre() + "\nATAQUE: " + arma.getAtaque()
                            + "\nALCANCE: " + arma.getAlcanceInt() + "\nPESO: " + arma.getPeso() + " oz.");
                    break;
                case 1:
                    if (!(objeto instanceof ArmaDosManos)) {
                        break;
                    }
                    DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
                    ArmaDosManos armaDosManos = (ArmaDosManos) objeto;
                    GeneradorTooltip.dibujarTooltipMejorado(g, sd, armaDosManos.getNombre() + "\nATAQUE: " + armaDosManos.getAtaque()
                            + "\nALCANCE: " + armaDosManos.getAlcanceInt() + "\nPESO: " + armaDosManos.getPeso() + " oz.");
                    break;

            }

        }
        else if (etiquetaEquipo > 1 || etiquetaEquipo < 6 && objeto instanceof Armadura) {
            switch (etiquetaEquipo) {
                case 2:
                    if (!(objeto instanceof ProteccionMedia)) {
                        break;
                    }
                    DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
                    ProteccionMedia media = (ProteccionMedia) objeto;
                    GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nDEF FISICA: "
                            + media.getDefensaF() + "\nDEF MAGICA: " + media.getDefensaM() + "\nPESO: " + objeto.getPeso() + " oz.");
                    break;
                case 3:
                    if (!(objeto instanceof ProteccionAlta)) {
                        break;
                    }
                    DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
                    ProteccionAlta alta = (ProteccionAlta) objeto;
                    GeneradorTooltip.dibujarTooltipMejorado(g, sd, alta.getNombre() + "\nDEF FISICA: "
                            + alta.getDefensaF() + "\nDEF MAGICA: " + alta.getDefensaM() + "\nPESO: " + alta.getPeso() + " oz.");
                    break;
                case 4:
                    if (!(objeto instanceof ProteccionLateral)) {
                        break;
                    }
                    DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
                    ProteccionLateral lateral = (ProteccionLateral) objeto;
                    GeneradorTooltip.dibujarTooltipMejorado(g, sd, lateral.getNombre() + "\nDEF FISICA: "
                            + lateral.getDefensaF() + "\nDEF MAGICA: " + lateral.getDefensaM() + "\nPESO: " + lateral.getPeso() + " oz.");
                    break;
                case 5:
                    if (!(objeto instanceof ProteccionBaja)) {
                        break;
                    }
                    DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
                    ProteccionBaja baja = (ProteccionBaja) objeto;
                    GeneradorTooltip.dibujarTooltipMejorado(g, sd, baja.getNombre() + "\nDEF FISICA: "
                            + baja.getDefensaF() + "\nDEF MAGICA: " + baja.getDefensaM() + "\nPESO: " + baja.getPeso() + " oz.");
                    break;
            }
        }
        if (etiquetaEquipo == 6 && objeto instanceof Joya) {

            DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
            Joya joya = (Joya) objeto;
            String texto = devolverStringJoyas(joya);
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, joya.getNombre() + texto);
            System.out.println("Joya");
        }
    }

    private void dibujarTooltipObjetosEquipados(Graphics g, SuperficieDibujo sd, Objeto objeto) {
        // Aquí puedes personalizar la apariencia del tooltip según tus necesidades

        if ((objeto instanceof Arma)) {

            DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
            Arma arma = (Arma) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, arma.getNombre() + "\nATAQUE: " + arma.getAtaque()
                    + "\nALCANCE: " + arma.getAlcanceInt() + "\nPESO: " + arma.getPeso() + " oz.");

        }

        else if (objeto instanceof Armadura) {

            DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
            Armadura media = (Armadura) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nDEF FISICA: "
                    + media.getDefensaF() + "\nDEF MAGICA: " + media.getDefensaM() + "\nPESO: " + objeto.getPeso() + " oz.");

        }
        else if (objeto instanceof Joya) {

            DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu());
            Joya joya = (Joya) objeto;
            String texto = devolverStringJoyas(joya);
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, joya.getNombre() + texto);

        }
    }

    private void dibujarTooltipArmas(final Graphics g, final SuperficieDibujo sd) {
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorArma1))
                && ElementosPrincipales.jugador.getAe().getArma1() != null) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getArma1().getNombre()
                    + "\nATAQUE: " + ElementosPrincipales.jugador.getAe().getArma1().getAtaque() + "\nALCANCE: "
                    + ElementosPrincipales.jugador.getAe().getArma1().getAlcanceInt());
        }
        else if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorArma2))
                && ElementosPrincipales.jugador.getAe().getArma2() != null) {

            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getArma2().getNombre()
                    + "\nATAQUE: " + ElementosPrincipales.jugador.getAe().getArma2().getAtaque() + "\nALCANCE: "
                    + ElementosPrincipales.jugador.getAe().getArma2().getAlcanceInt());
        }
    }

    private void dibujarTooltipArmaduras(final Graphics g, final SuperficieDibujo sd) {
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura1))
                && ElementosPrincipales.jugador.getAe().getArmaduraMedia() != null) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getArmaduraMedia().getNombre()
                    + "\nDEF FISICA: " + ElementosPrincipales.jugador.getAe().getArmaduraMedia().getDefensaF()
                    + "\nDEF MAGICA: " + ElementosPrincipales.jugador.getAe().getArmaduraMedia().getDefensaM());
        }
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura2))
                && ElementosPrincipales.jugador.getAe().getCasco() != null) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getCasco().getNombre()
                    + "\nDEF FISICA: " + ElementosPrincipales.jugador.getAe().getCasco().getDefensaF()
                    + "\nDEF MAGICA: " + ElementosPrincipales.jugador.getAe().getCasco().getDefensaM());
        }

        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura3))
                && ElementosPrincipales.jugador.getAe().getGuante() != null) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd,
                    ElementosPrincipales.jugador.getAe().getGuante().getNombre() + "\nDEF FISICA: "
                    + ElementosPrincipales.jugador.getAe().getGuante().getDefensaF() + "\nDEF MAGICA: "
                    + ElementosPrincipales.jugador.getAe().getGuante().getDefensaM());
        }
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorArmadura4))
                && ElementosPrincipales.jugador.getAe().getBota() != null) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd,
                    ElementosPrincipales.jugador.getAe().getBota().getNombre() + "\nDEF FISICA: "
                    + ElementosPrincipales.jugador.getAe().getBota().getDefensaF() + "\nDEF MAGICA: "
                    + ElementosPrincipales.jugador.getAe().getBota().getDefensaM());
        }
    }

    private void dibujarTooltipJoyas(final Graphics g, final SuperficieDibujo sd) {

        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorCollar))
                && ElementosPrincipales.jugador.getAe().getCollar() != null) {
            String texto = devolverStringJoyas(ElementosPrincipales.jugador.getAe().getCollar());
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getCollar().getNombre() + texto);
        }

        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorAccesorio))
                && ElementosPrincipales.jugador.getAe().getAccesorio() != null) {
            String texto = devolverStringJoyas(ElementosPrincipales.jugador.getAe().getAccesorio());
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getAccesorio().getNombre() + texto);
        }

        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo1))
                && ElementosPrincipales.jugador.getAe().getAnillo1() != null) {
            String texto = devolverStringJoyas(ElementosPrincipales.jugador.getAe().getAnillo1());
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getAnillo1().getNombre() + texto);
        }

        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(contenedorAnillo2))
                && ElementosPrincipales.jugador.getAe().getAnillo2() != null) {
            String texto = devolverStringJoyas(ElementosPrincipales.jugador.getAe().getAnillo2());
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, ElementosPrincipales.jugador.getAe().getAnillo2().getNombre() + texto);
        }
    }

    private void dibujarPaneles(final Graphics g) {
        dibujarPanelObjetos(g, panelObjetos, titularPanelObjetos, "EQUIPABLES");
        dibujarPanelEquipo(g, panelEquipo, titularPanelEquipo, "EQUIPO ACTUAL");
        dibujarPanelAtributos(g, panelAtributos, titularPanelAtributos, "ATRIBUTOS");
    }

    private void dibujarPanelObjetos(Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        dibujarElementosEquipables(g, panel);

    }

    private void dibujarElementosEquipables(final Graphics g, final Rectangle panelObjetos) {
        int z = panelObjetos.x + 2;
        for (int i = 0; i < 7; i++) {
            Rectangle rectangulo = new Rectangle(z, panelObjetos.y + 14, 19, 10);
            etiquetasEquipables.add(rectangulo);
            z += 23;
        }
        BufferedImage hojaEtiquetas = CargadorRecursos.cargarImagenCompatibleOpaca("/icons/EtiquetasEquipables.png");
        DibujoDebug.dibujarImagen(g, hojaEtiquetas, panelObjetos.x, panelObjetos.y + 14);
        if (!ElementosPrincipales.inventario.getEquipo().isEmpty()) {

            switch (etiquetaEquipo) {
                case 0:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getArmas()) {

                        if (objetoActual instanceof ArmaUnaMano) {
                            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                    objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                            DibujoDebug.dibujarRectanguloRelleno(g,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                    12, 8, Color.black);

                            g.setColor(Color.white);

                            String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                    : String.valueOf(objetoActual.getCantidad());

                            DibujoDebug.dibujarString(g, texto,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                    - MedidorString.medirAnchoPixeles(g, texto),
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                        }
                    }
                    break;

                case 1:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getArmas()) {
                        if (objetoActual instanceof ArmaDosManos) {
                            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                    objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                            DibujoDebug.dibujarRectanguloRelleno(g,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                    12, 8, Color.black);

                            g.setColor(Color.white);

                            String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                    : String.valueOf(objetoActual.getCantidad());

                            DibujoDebug.dibujarString(g, texto,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                    - MedidorString.medirAnchoPixeles(g, texto),
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                        }
                    }
                    break;

                case 2:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getArmaduras()) {
                        if (objetoActual instanceof ProteccionMedia) {

                            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                    objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                            DibujoDebug.dibujarRectanguloRelleno(g,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                    12, 8, Color.black);

                            g.setColor(Color.white);

                            String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                    : String.valueOf(objetoActual.getCantidad());

                            DibujoDebug.dibujarString(g, texto,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                    - MedidorString.medirAnchoPixeles(g, texto),
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                        }
                    }
                    break;

                case 3:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getArmaduras()) {
                        if (objetoActual instanceof ProteccionAlta) {

                            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                    objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                            DibujoDebug.dibujarRectanguloRelleno(g,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                    12, 8, Color.black);

                            g.setColor(Color.white);

                            String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                    : String.valueOf(objetoActual.getCantidad());

                            DibujoDebug.dibujarString(g, texto,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                    - MedidorString.medirAnchoPixeles(g, texto),
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                        }
                    }
                    break;

                case 4:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getArmaduras()) {
                        if (objetoActual instanceof ProteccionLateral) {

                            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                    objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                            DibujoDebug.dibujarRectanguloRelleno(g,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                    12, 8, Color.black);

                            g.setColor(Color.white);

                            String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                    : String.valueOf(objetoActual.getCantidad());

                            DibujoDebug.dibujarString(g, texto,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                    - MedidorString.medirAnchoPixeles(g, texto),
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                        }
                    }
                    break;

                case 5:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getArmaduras()) {
                        if (objetoActual instanceof ProteccionBaja) {

                            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                    objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                            DibujoDebug.dibujarRectanguloRelleno(g,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                    12, 8, Color.black);

                            g.setColor(Color.white);

                            String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                    : String.valueOf(objetoActual.getCantidad());

                            DibujoDebug.dibujarString(g, texto,
                                    objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                    - MedidorString.medirAnchoPixeles(g, texto),
                                    objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                        }
                    }
                    break;

                case 6:
                    for (Objeto objetoActual : ElementosPrincipales.inventario.getJoyas()) {
                        DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(),
                                objetoActual.getPosicionMenu().x, objetoActual.getPosicionMenu().y);

                        DibujoDebug.dibujarRectanguloRelleno(g,
                                objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width - 12,
                                objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 8,
                                12, 8, Color.black);

                        g.setColor(Color.white);

                        String texto = (objetoActual.getCantidad() < 10) ? "0" + objetoActual.getCantidad()
                                : String.valueOf(objetoActual.getCantidad());

                        DibujoDebug.dibujarString(g, texto,
                                objetoActual.getPosicionMenu().x + objetoActual.getPosicionMenu().width
                                - MedidorString.medirAnchoPixeles(g, texto),
                                objetoActual.getPosicionMenu().y + objetoActual.getPosicionMenu().height - 1);
                    }
                    break;
            }

            if (objetoSeleccionado != null) {
                DibujoDebug.dibujarImagen(g, objetoSeleccionado.getSprite().getImagen(),
                        new Point(objetoSeleccionado.getPosicionFlotante().x,
                                objetoSeleccionado.getPosicionFlotante().y));
            }

        }
    }

    private void dibujarPanelEquipo(Graphics g, final Rectangle panel, final Rectangle titularPanel, final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        g.setColor(Color.black);

        if (ElementosPrincipales.jugador.getAe().getArma1() != null) {
            Point coordenadaImagen1 = new Point(contenedorArma1.x + contenedorArma1.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorArma1.y + 1);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getArma1().getSprite().getImagen(),
                    coordenadaImagen1);
        }
        if (ElementosPrincipales.jugador.getAe().getArma2() != null) {
            if (!(ElementosPrincipales.jugador.getAe().getArma2() instanceof SinArma)) {
                Point coordenadaImagen2 = new Point(contenedorArma2.x + contenedorArma2.width / 2 - Constantes.LADO_SPRITE / 2,
                        contenedorArma2.y + 1);

                DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getArma2().getSprite().getImagen(),
                        coordenadaImagen2);
            }
        }

        if (ElementosPrincipales.jugador.getAe().getArmaduraMedia() != null) {

            Point coordenadaImagen3 = new Point(contenedorArmadura1.x + contenedorArmadura1.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorArmadura1.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getArmaduraMedia().getSprite().getImagen(),
                    coordenadaImagen3);

        }
        if (ElementosPrincipales.jugador.getAe().getCasco() != null) {

            Point coordenadaImagen4 = new Point(contenedorArmadura2.x + contenedorArmadura2.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorArmadura2.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getCasco().getSprite().getImagen(),
                    coordenadaImagen4);

        }

        if (ElementosPrincipales.jugador.getAe().getGuante() != null) {

            Point coordenadaImagen5 = new Point(contenedorArmadura3.x + contenedorArmadura3.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorArmadura3.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getGuante().getSprite().getImagen(),
                    coordenadaImagen5);

        }

        if (ElementosPrincipales.jugador.getAe().getBota() != null) {

            Point coordenadaImagen6 = new Point(contenedorArmadura4.x + contenedorArmadura4.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorArmadura4.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getBota().getSprite().getImagen(),
                    coordenadaImagen6);

        }
        if (ElementosPrincipales.jugador.getAe().getCollar() != null) {

            Point coordenadaImagen7 = new Point(contenedorCollar.x + contenedorCollar.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorCollar.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getCollar().getSprite().getImagen(),
                    coordenadaImagen7);

        }

        if (ElementosPrincipales.jugador.getAe().getAccesorio() != null) {

            Point coordenadaImagen8 = new Point(contenedorAccesorio.x + contenedorAccesorio.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorAccesorio.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getAccesorio().getSprite().getImagen(),
                    coordenadaImagen8);

        }

        if (ElementosPrincipales.jugador.getAe().getAnillo1() != null) {

            Point coordenadaImagen9 = new Point(contenedorAnillo1.x + contenedorAnillo1.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorAnillo1.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getAnillo1().getSprite().getImagen(),
                    coordenadaImagen9);

        }

        if (ElementosPrincipales.jugador.getAe().getAnillo2() != null) {

            Point coordenadaImagen10 = new Point(contenedorAnillo2.x + contenedorAnillo2.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorAnillo2.y);

            DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.getAe().getAnillo2().getSprite().getImagen(),
                    coordenadaImagen10);

        }

        // Dibuja la etiqueta "ARMA"
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaArma);
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaArmaduras);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorArma1);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorArma2);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorArmadura1);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorArmadura2);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorArmadura3);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorArmadura4);
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaJoyas);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorCollar);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorAccesorio);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorAnillo1);
        DibujoDebug.dibujarRectanguloContorno(g, contenedorAnillo2);

        for (Objeto objeto : ElementosPrincipales.inventario.getEquipo()) {
            switch (etiquetaEquipo) {
                case 0:
                    if (objeto instanceof ArmaUnaMano && objeto == ElementosPrincipales.jugador.getAe().getArma1()) {
                        armaEquipada1 = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, armaEquipada1, Color.green);
                        DibujoDebug.dibujarString(g, "H1", armaEquipada1.x + armaEquipada1.width - 10, armaEquipada1.y + 7);
                    }

                    break;
                case 1:
                    if (objeto instanceof ArmaDosManos && objeto == ElementosPrincipales.jugador.getAe().getArma1()) {
                        armaEquipada2 = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, armaEquipada2, Color.BLACK);
                        DibujoDebug.dibujarString(g, "H2", armaEquipada2.x + 2, armaEquipada2.y + 7);
                    }
                    break;

                case 2:
                    if (objeto == ElementosPrincipales.jugador.getAe().getArmaduraMedia()) {
                        Rectangle armaduraEquipada1 = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, armaduraEquipada1, Color.gray);
                        DibujoDebug.dibujarString(g, "E", armaduraEquipada1.x + 1, armaduraEquipada1.y + 7);
                    }
                    break;
                case 3:

                    if (objeto == ElementosPrincipales.jugador.getAe().getCasco()) {
                        Rectangle armaduraEquipada2 = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, armaduraEquipada2, Color.gray);
                        DibujoDebug.dibujarString(g, "E", armaduraEquipada2.x + 1, armaduraEquipada2.y + 7);
                    }
                    break;
                case 4:
                    if (objeto == ElementosPrincipales.jugador.getAe().getGuante()) {
                        Rectangle armaduraEquipada3 = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, armaduraEquipada3, Color.gray);
                        DibujoDebug.dibujarString(g, "E", armaduraEquipada3.x + 1, armaduraEquipada3.y + 7);
                    }
                    break;
                case 5:
                    if (objeto == ElementosPrincipales.jugador.getAe().getBota()) {
                        Rectangle armaduraEquipada4 = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, armaduraEquipada4, Color.gray);
                        DibujoDebug.dibujarString(g, "E", armaduraEquipada4.x + 1, armaduraEquipada4.y + 7);
                    }
                    break;
                case 6:
                    if (objeto == ElementosPrincipales.jugador.getAe().getCollar()) {
                        Rectangle collarEquipado = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, collarEquipado, Color.gray);
                        DibujoDebug.dibujarString(g, "E", collarEquipado.x + 1, collarEquipado.y + 7);
                    }
                    if (objeto == ElementosPrincipales.jugador.getAe().getAccesorio()) {
                        Rectangle accesorioEquipado = objeto.getPosicionMenu();
                        DibujoDebug.dibujarRectanguloContorno(g, accesorioEquipado, Color.gray);
                        DibujoDebug.dibujarString(g, "E", accesorioEquipado.x + 1, accesorioEquipado.y + 7);
                    }
                    break;

            }

        }

        int xEtiquetaArma = etiquetaArma.x + etiquetaArma.width / 2 - MedidorString.medirAnchoPixeles(g, "ARMAS") / 2;
        int yEtiquetaArma = etiquetaArma.y + etiquetaArma.height / 2 - 2 + MedidorString.medirAltoPixeles(g, "ARMAS") / 2;
        DibujoDebug.dibujarString(g, "ARMAS", new Point(xEtiquetaArma, yEtiquetaArma), Color.white);

        int xEtiquetaArmadura = etiquetaArmaduras.x + etiquetaArmaduras.width / 2
                - MedidorString.medirAnchoPixeles(g, "ARMADURA") / 2;
        int yEtiquetaArmadura = etiquetaArmaduras.y + etiquetaArmaduras.height / 2
                - 2 + MedidorString.medirAltoPixeles(g, "ARMADURA") / 2;
        DibujoDebug.dibujarString(g, "ARMADURA", new Point(xEtiquetaArmadura, yEtiquetaArmadura), Color.white);

        int xEtiquetaJoyeria = etiquetaArmaduras.x + etiquetaArmaduras.width / 2
                - MedidorString.medirAnchoPixeles(g, "ARMADURA") / 2;
        int yEtiquetaJoyeria = etiquetaJoyas.y + etiquetaJoyas.height / 2
                - 2 + MedidorString.medirAltoPixeles(g, "JOYERIA") / 2;
        DibujoDebug.dibujarString(g, "JOYERIA", new Point(xEtiquetaJoyeria, yEtiquetaJoyeria), Color.white);
    }

    private void dibujarPanelAtributos(Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        g.setColor(Color.black);

        int xAtributos = panelAtributos.x + margenGeneral;
        int yAtributos = panelAtributos.y + titularPanelAtributos.height + margenGeneral;

        double eva = (ElementosPrincipales.jugador.getGa().getDestreza() * 1.2
                - ElementosPrincipales.jugador.getGa().getPesoActual() * 0.3);
        double crit = (ElementosPrincipales.jugador.getGa().getSuerte() * 0.5);

        String[] bloque1 = {
            "Nivel: " + ElementosPrincipales.jugador.getGa().getNivel(),
            "Vida: " + ElementosPrincipales.jugador.getVidaActual() + " / " + ElementosPrincipales.jugador.getVidaMaxima(),
            "Mana: " + ElementosPrincipales.jugador.getMana() + " / " + ElementosPrincipales.jugador.getManaMaximo(),
            "Experiencia: " + ElementosPrincipales.jugador.getGa().getExperiencia() + " / "
            + ElementosPrincipales.jugador.getGa().getExperienciaMaxima(),
            "Resistencia: " + ElementosPrincipales.jugador.getGa().getResistencia() + " / "
            + ElementosPrincipales.jugador.getGa().getResistenciaMaxima()
        };

        String[] bloque2 = {
            "Ataque: " + (ElementosPrincipales.jugador.getGa().getFuerza() + ElementosPrincipales.jugador.getGa().getNivel())
            + " + " + getAtaqueFisicoJugador(),
            "Magia: " + (ElementosPrincipales.jugador.getInteligencia() + ElementosPrincipales.jugador.getGa().getNivel())
            + " + " + (int) (getMagiaJugador()),
            "Defensa F: " + ((int) (ElementosPrincipales.jugador.getVidaMaxima() * 0.01
            + ElementosPrincipales.jugador.getGa().getConstitucion() * 1.1)) + " + " + getDFJugador(),
            "Defensa M: " + ((int) (ElementosPrincipales.jugador.getManaMaximo() * 0.01
            + ElementosPrincipales.jugador.getInteligencia() * 1.1)) + " + " + getDMJugador(),
            String.format("Evasion: %.1f ", eva) + "+ " + String.format("%.1f %%", getEvasionJugador()),
            String.format("Critico: %.1f ", crit) + "+ " + String.format("%.1f %%", getCritJugador()),
            String.format("Resistencia F: %.1f%% ", getResFJugador()),
            String.format("Resistencia M: %.1f%% ", getResMJugador())
        };

        String[] bloque3 = {
            "Fuerza: " + ElementosPrincipales.jugador.getGa().getFuerza(),
            "Constitucion: " + ElementosPrincipales.jugador.getGa().getConstitucion(),
            "Inteligencia: " + ElementosPrincipales.jugador.getGa().getInteligencia(),
            "Destreza: " + ElementosPrincipales.jugador.getGa().getDestreza(),
            "Suerte: " + ElementosPrincipales.jugador.getGa().getSuerte(),};

        dibujarBloque(g, xAtributos, yAtributos, bloque1);
        yAtributos += 60;

        dibujarBloque(g, xAtributos, yAtributos, bloque2);
        yAtributos += 90;

        dibujarBloque(g, xAtributos, yAtributos, bloque3);
    }

    private void dibujarBloque(Graphics g, int x, int y, String[] bloque) {
        for (String atributo : bloque) {
            DibujoDebug.dibujarString(g, atributo, new Point(x, y));
            y += 10;
        }
    }

    private void dibujarPanel(final Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        g.setColor(Color.DARK_GRAY);
        DibujoDebug.dibujarRectanguloContorno(g, panel);
        DibujoDebug.dibujarRectanguloRelleno(g, titularPanel);
        g.setColor(Color.white);
        DibujoDebug.dibujarString(g, nombrePanel, new Point(
                panel.x + titularPanel.width / 2 - MedidorString.medirAnchoPixeles(g, nombrePanel) / 2,
                panel.y + titularPanel.height - MedidorString.medirAltoPixeles(g, nombrePanel) - 4));
    }

    private int getDFJugador() {
        int defensaFisica = ElementosPrincipales.jugador.calcularDefensaF();

        return defensaFisica;
    }

    private int getAtaqueFisicoJugador() {
        int ataque = ElementosPrincipales.jugador.calcularAtqFisico();
        return ataque;
    }

    private int getMagiaJugador() {
        int magia = ElementosPrincipales.jugador.calcularAtqMagico();

        return magia;
    }

    private int getDMJugador() {
        int defensaMagica = ElementosPrincipales.jugador.calcularDefensaM();

        return defensaMagica;
    }

    private double getEvasionJugador() {
        double eva = ElementosPrincipales.jugador.calcularEvasion();

        return eva;
    }

    private double getCritJugador() {
        double crit = ElementosPrincipales.jugador.calcularCrit();

        return crit;
    }

    private double getResFJugador() {
        double resF = ElementosPrincipales.jugador.calcularResF();

        return resF;
    }

    private double getResMJugador() {
        double resM = ElementosPrincipales.jugador.calcularResM();

        return resM;
    }

    private String devolverStringJoyas(Joya joya) {
        String[] propiedades = {
            (joya.getAtkF() > 0) ? "\nATAQUE: " + joya.getAtkF() : "",
            (joya.getDefensaF() > 0) ? "\nDEF FISICA: " + joya.getDefensaF() : "",
            (joya.getAtkM() > 0) ? "\nMAGIA: " + joya.getAtkM() : "",
            (joya.getDefensaM() > 0) ? "\nDEF MAGICA: " + joya.getDefensaM() : "",
            (joya.getCrit() > 0.0) ? "\nCRITICO: " + joya.getCrit() : "",
            (joya.getEva() > 0.0) ? "\nEVASION: " + joya.getEva() : "",
            (joya.getResF() > 0.0) ? "\nRES FISICA: " + joya.getResF() : "",
            (joya.getResM() > 0.0) ? "\nRES MAGICA: " + joya.getResM() : "",
            (joya.getPeso() > 0) ? "\nPESO: " + joya.getPeso() + " oz." : ""
        };

        return String.join("", propiedades);
    }

    private void dibujarPaginador(final Graphics g) {
        final int anchoBoton = 24;
        final int altoBoton = 12;

        int anteriorX = panelObjetos.x + panelObjetos.width - Constantes.LADO_SPRITE * 2 + 4;

        int anteriorY = panelObjetos.y + panelObjetos.height - (Constantes.LADO_SPRITE / 2);

        final Rectangle anterior = new Rectangle(anteriorX - 4, anteriorY, anchoBoton, altoBoton);
        final Rectangle siguiente = new Rectangle(anterior.x + anterior.width + margenGeneral, anterior.y,
                anchoBoton, altoBoton);
        g.setColor(Color.blue);

        DibujoDebug.dibujarRectanguloContorno(g, anterior);
        DibujoDebug.dibujarRectanguloContorno(g, siguiente);
        DibujoDebug.dibujarString(g, "<<", anterior.x + anterior.width - 18, anterior.y + anterior.height - 5);
        DibujoDebug.dibujarString(g, ">>", siguiente.x + siguiente.width - 18, siguiente.y + siguiente.height - 5);
    }

    public TipoObjeto getTipoObjetoSeleccionado() {
        return tipoObjetoSeleccionado;
    }

    public Objeto getObjetoSeleccionado() {
        return objetoSeleccionado;
    }

    public void eliminarObjetoSeleccionado() {
        objetoSeleccionado = null;
    }

}
