/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.juego.menu_tienda;

/**
 *
 * @author GAMER ARRAX
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorTooltip;
import principal.herramientas.MedidorString;
import principal.inventario.Objeto;
import principal.inventario.armaduras.Armadura;
import principal.maquinaestado.menujuego.MenuEquipo;

public class TiendaArmaduras extends SeccionTienda {

    private final int anchoPaneles = 110;
    private final int altoPaneles = 308;

    final Rectangle panelComprar;
    final Rectangle panelObjetosComprados;
    private boolean dibujarVentanaCompra = false;
    private boolean comprando = false;
    private boolean vendiendo = false;
    private boolean excederiaPeso = false;
    private boolean sinDinero = false;

    final Rectangle panelVender;
    final Rectangle panelObjetosVendidos;

    final Rectangle titularPanelComprar;

    final Rectangle titularPanelVender;
    final Rectangle titularPanelComprados;
    final Rectangle titularPanelVenta;

    private Objeto objetoSeleccionadoCompra;
    private Objeto objetoSeleccionadoVenta;

    private final ArrayList<Objeto> canastaCompra;
    private final ArrayList<Objeto> canastaVenta;
    private ArrayList<Objeto> objetosTienda;

    private final int anteriorXVenta;
    private final int anteriorYVenta;
    private final int anteriorXCompra;
    private final int anteriorYCompra;

    private final Rectangle vender;
    private final Rectangle comprar;
    private final Rectangle cancelarVenta;
    private final Rectangle cancelarCompra;
    private Rectangle ventanaCantidad;
    private Rectangle subirUnidad;
    private Rectangle bajarUnidad;
    private Rectangle subirDecena;
    private Rectangle bajarDecena;
    private Rectangle aceptarOperacion;
    private int cantidadObjetos;
    private int totalTransaccionCompra;
    private int totalTransaccionVenta;

    long tiempoUltimaAccion = 0;
    long tiempoDebouncing = 100;

    public TiendaArmaduras(String nombreSeccion, Rectangle etiquetaMenu, EstructuraTienda et) {
        super(nombreSeccion, etiquetaMenu, et);
        panelComprar = new Rectangle(et.FONDO.x + margenGeneral * 2,
                barraPeso.y + barraPeso.height + margenGeneral,
                anchoPaneles, altoPaneles);

        panelObjetosComprados = new Rectangle(panelComprar.x + panelComprar.width + margenGeneral,
                panelComprar.y, anchoPaneles, altoPaneles);

        panelVender = new Rectangle(panelObjetosComprados.x + panelObjetosComprados.width + margenGeneral,
                panelObjetosComprados.y, anchoPaneles, altoPaneles);

        panelObjetosVendidos = new Rectangle(panelVender.x + panelVender.width + margenGeneral,
                panelVender.y, anchoPaneles, altoPaneles);

        titularPanelComprar = new Rectangle(panelComprar.x, panelComprar.y, panelComprar.width, 24);
        titularPanelVender = new Rectangle(panelVender.x, panelVender.y, panelVender.width, 24);
        titularPanelComprados = new Rectangle(panelObjetosComprados.x, panelObjetosComprados.y, panelObjetosComprados.width, 24);
        titularPanelVenta = new Rectangle(panelObjetosVendidos.x, panelObjetosVendidos.y, panelObjetosVendidos.width, 24);

        canastaCompra = new ArrayList<>();
        canastaVenta = new ArrayList<>();
        objetoSeleccionadoCompra = null;
        objetoSeleccionadoVenta = null;

        anteriorXVenta = panelObjetosVendidos.x + Constantes.LADO_SPRITE / 2;
        anteriorYVenta = panelObjetosVendidos.y + panelObjetosVendidos.height - (Constantes.LADO_SPRITE / 2);
        anteriorXCompra = panelObjetosComprados.x + Constantes.LADO_SPRITE / 2;
        anteriorYCompra = panelObjetosComprados.y + panelObjetosComprados.height - (Constantes.LADO_SPRITE / 2);
        vender = new Rectangle(anteriorXVenta - 2, anteriorYVenta, 40, 12);
        comprar = new Rectangle(anteriorXCompra - 2, anteriorYCompra, 40, 12);
        cancelarVenta = new Rectangle(vender.x + vender.width + margenGeneral, vender.y, 40, 12);
        cancelarCompra = new Rectangle(comprar.x + comprar.width + margenGeneral, comprar.y, 40, 12);
        cantidadObjetos = 0;
        totalTransaccionCompra = 0;
        totalTransaccionVenta = 0;
        

    }

    @Override
    public void actualizar() {
        objetosTienda = ElementosPrincipales.mapa.objetosTiendaActual;
        actualizarPosicionesMenu();
        actualizarPosicionesCompraVenta();
        actualizarSeleccionRaton();
        actualizarObjetoSeleccionadoCompra();
        actualizarObjetoSeleccionadoVenta();
        calcularPesoFuturo();
        actualizarCanastaCompra();
        actualizarCanastaVenta();
        

    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {

        dibujarLimitePeso(g);
        dibujarPaneles(g);
        dibujarVentanaParaCompra(g, sd);
        dibujarVentanaParaVenta(g, sd);
        if (MenuEquipo.mostrarTooltip) {
            dibujarTooltipPeso(g, sd);
            dibujarTooltipPaneles(g, sd);

        }

    }

    private void actualizarSeleccionRaton() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();
        if (objetoSeleccionadoCompra == null) {
            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelComprar))) {
                if (objetosTienda.isEmpty()) {
                    return;
                }
                for (Objeto objeto : objetosTienda) {
                    if (objeto instanceof Armadura) {
                        if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                                .intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionTienda()))) {
                            objetoSeleccionadoCompra = objeto;
                            objetoSeleccionadoVenta = null;
                        }
                    }
                }
            }

        }
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelVender))) {
            if (ElementosPrincipales.inventario.getArmaduras().isEmpty()) {
                return;
            }
            for (Objeto objeto : ElementosPrincipales.inventario.getArmaduras()) {
                if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                        .intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMochila()))) {
                    objetoSeleccionadoVenta = objeto;
                    objetoSeleccionadoCompra = null;
                }
            }
        }

    }

    private void actualizarPosicionesMenu() {

        final Point piObjetosTienda = new Point(et.FONDO.x + 10,
                titularPanelComprar.y + margenGeneral * 3);
        final Point piObjetosInventario = new Point(panelVender.x - 6,
                titularPanelVender.y + margenGeneral * 3);

        final int lado = Constantes.LADO_SPRITE;
        int contadorArmasTienda = 0;
        int contadorArmasInventario = 0;
        int margenX = 8; // Nuevo margen desde el borde del panel

        if (!objetosTienda.isEmpty()) {

            for (Objeto objetoActual : objetosTienda) {

                objetoActual.getNombre();
                // Cálculo de la posición X ajustado para el margen desde el borde del panel
                int posX = piObjetosTienda.x + margenX + (contadorArmasTienda % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosTienda.y + contadorArmasTienda / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionTienda = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionTienda(nuevaPosicionTienda);
                contadorArmasTienda++;

            }
        }

        if (!ElementosPrincipales.inventario.getArmaduras().isEmpty()) {

            for (Objeto objetoActual : ElementosPrincipales.inventario.getArmaduras()) {
                if (objetoNoVendible(objetoActual.getId())) {
                    objetoActual.setPosicionMochila(new Rectangle(0, 0, lado, lado));
                    continue;
                }

                // Cálculo de la posición X ajustado para el margen desde el borde del panel
                int posX = piObjetosInventario.x + margenX + (contadorArmasInventario % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosInventario.y + contadorArmasInventario / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionInventario = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionMochila(nuevaPosicionInventario);
                contadorArmasInventario++;

            }
        }
    }

    private void actualizarPosicionesCompraVenta() {

        final Point piObjetosCompra = new Point(panelObjetosComprados.x - 6,
                titularPanelComprados.y + margenGeneral * 3);
        final Point piObjetosVenta = new Point(panelObjetosVendidos.x - 6,
                titularPanelVenta.y + margenGeneral * 3);

        final int lado = Constantes.LADO_SPRITE;
        int contadorArmasCompradas = 0;
        int contadorArmasVendidas = 0;
        int margenX = 8; // Nuevo margen desde el borde del panel

        if (!canastaCompra.isEmpty()) {

            for (Objeto objetoActual : canastaCompra) {
                // Cálculo de la posición X ajustado para el margen desde el borde del panel
                int posX = piObjetosCompra.x + margenX + (contadorArmasCompradas % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosCompra.y + contadorArmasCompradas / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionCompra = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionCompra(nuevaPosicionCompra);
                contadorArmasCompradas++;
            }
        }

        if (!canastaVenta.isEmpty()) {

            for (Objeto objetoActual : canastaVenta) {
                // Cálculo de la posición X ajustado para el margen desde el borde del panel
                int posX = piObjetosVenta.x + margenX + (contadorArmasVendidas % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosVenta.y + contadorArmasVendidas / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionVenta = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionVenta(nuevaPosicionVenta);
                contadorArmasVendidas++;
            }
        }
    }

    private void actualizarObjetoSeleccionadoCompra() {
        if (objetoSeleccionadoCompra != null) {
            if (GestorPrincipal.sd.getRaton().isClick2()) {
                objetoSeleccionadoCompra = null;
                return;
            }
            Point pfc = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            objetoSeleccionadoCompra.setPosicionFlotante(
                    new Rectangle(pfc.x, pfc.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }

    }

    private void actualizarObjetoSeleccionadoVenta() {

        if (objetoSeleccionadoVenta != null) {
            if (GestorPrincipal.sd.getRaton().isClick2()) {
                objetoSeleccionadoVenta = null;
                return;
            }
            Point pfv = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            objetoSeleccionadoVenta.setPosicionFlotante(
                    new Rectangle(pfv.x, pfv.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }

    }

    private void actualizarCanastaCompra() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        long tiempoActual = System.currentTimeMillis();

        // Verifica si ha pasado suficiente tiempo desde la última recogida
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))
                && objetoSeleccionadoCompra != null && GestorPrincipal.sd.getRaton().isClick()) {
            ventanaCantidad = new Rectangle(panelObjetosComprados.x, panelObjetosComprados.height / 2, 42, 40);
            subirUnidad = new Rectangle(ventanaCantidad.x + 2, ventanaCantidad.y + 2, 18, 17);
            bajarUnidad = new Rectangle(ventanaCantidad.x + 2, subirUnidad.y + subirUnidad.height + 2, 18, 17);
            subirDecena = new Rectangle(subirUnidad.x + 2 + subirUnidad.width, subirUnidad.y, 18, 17);
            bajarDecena = new Rectangle(subirDecena.x, subirDecena.y + bajarUnidad.height + 2, 18, 17);
            aceptarOperacion = new Rectangle(ventanaCantidad.x + ventanaCantidad.width + 2, ventanaCantidad.y, 42, 20);
            dibujarVentanaCompra = true;
            comprando = true;

            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return; // Ignora la recogida si está dentro del tiempo de debouncing
            }

            // Actualiza el tiempo de la última recogida
            tiempoUltimaAccion = tiempoActual;

            if (canastaCompra.contains(objetoSeleccionadoCompra)) {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos++;

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos += 10;
                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos--;

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos -= 10;

                }

                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(aceptarOperacion))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    for (Objeto objetoCanasta : canastaCompra) {
                        if (objetoCanasta.equals(objetoSeleccionadoCompra)) {

                            objetoCanasta.setCantidadCompra(objetoCanasta.getCantidadCompra() + cantidadObjetos);
                            if (objetoCanasta.getCantidadCompra() < 0) {
                                objetoCanasta.setCantidadCompra(0);
                            }
                        }
                    }
                    totalTransaccionCompra += cantidadObjetos * objetoSeleccionadoCompra.getPrecioCompra();
                    dibujarVentanaCompra = false;
                    objetoSeleccionadoCompra = null;
                    cantidadObjetos = 0;
                }
            }
            else {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos++;
                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos += 10;
                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos--;
                    if (cantidadObjetos < 0) {
                        cantidadObjetos = 0;
                    }
                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos -= 10;
                    if (cantidadObjetos < 0) {
                        cantidadObjetos = 0;
                    }

                }
                objetoSeleccionadoCompra.setCantidadCompra(cantidadObjetos);
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(aceptarOperacion))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    if (cantidadObjetos == 0) {
                        dibujarVentanaCompra = false;
                        objetoSeleccionadoCompra = null;
                        return;
                    }
                    canastaCompra.add(objetoSeleccionadoCompra);
                    totalTransaccionCompra += objetoSeleccionadoCompra.getCantidadCompra() * objetoSeleccionadoCompra.getPrecioCompra();
                    dibujarVentanaCompra = false;
                    objetoSeleccionadoCompra = null;
                    cantidadObjetos = 0;
                }
            }

        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(comprar))
                && !canastaCompra.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            if (excederiaPeso) {
                return;
            }

            if (ElementosPrincipales.inventario.dinero < totalTransaccionCompra) {
                System.out.println("Dinero Insuficiente");
                sinDinero = true;
                return;
            }
            else {
                sinDinero = false;
            }

            boolean objetoExiste = false;
            for (Objeto objetoComprado : canastaCompra) {

                for (Objeto objetoInventario : ElementosPrincipales.inventario.getArmaduras()) {
                    if (objetoInventario.getId() == objetoComprado.getId()) {
                        objetoInventario.setCantidad(objetoInventario.getCantidad() + objetoComprado.getCantidadCompra());
                        objetoExiste = true;
                    }
                }
                if (!objetoExiste) {
                    objetoComprado.setCantidad(objetoComprado.getCantidadCompra());
                    ElementosPrincipales.inventario.getListaObjetos().add(objetoComprado);

                }
            }

            ElementosPrincipales.inventario.dinero -= totalTransaccionCompra;
            System.out.println("Dinero: " + ElementosPrincipales.inventario.dinero);

            comprando = false;
            totalTransaccionCompra = 0;
            canastaCompra.clear();
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(cancelarCompra))
                && !canastaCompra.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            comprando = false;
            sinDinero = false;
            totalTransaccionCompra = 0;
            canastaCompra.clear();
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))
                && !canastaCompra.isEmpty()) {

            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return; // Ignora la recogida si está dentro del tiempo de debouncing
            }

            // Actualiza el tiempo de la última recogida
            tiempoUltimaAccion = tiempoActual;

            Iterator<Objeto> iterador = canastaCompra.iterator();

            while (iterador.hasNext()) {
                Objeto objetoActual = iterador.next();

                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objetoActual.getPosicionCompra()))
                        && GestorPrincipal.sd.getRaton().isClick2()) {
                    totalTransaccionCompra -= objetoActual.getCantidadCompra() * objetoActual.getPrecioCompra();
                    iterador.remove();
                    if (totalTransaccionCompra <= ElementosPrincipales.inventario.dinero) {
                        sinDinero = false;
                    }
                    break; // Salir del bucle después de recoger un objeto
                }
            }
        }
    }

    private void actualizarCanastaVenta() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        long tiempoActual = System.currentTimeMillis();

        // Verifica si ha pasado suficiente tiempo desde la última recogida
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))
                && objetoSeleccionadoVenta != null && GestorPrincipal.sd.getRaton().isClick()) {
            ventanaCantidad = new Rectangle(panelObjetosVendidos.x, panelObjetosVendidos.height / 2, 42, 40);
            subirUnidad = new Rectangle(ventanaCantidad.x + 2, ventanaCantidad.y + 2, 18, 17);
            bajarUnidad = new Rectangle(ventanaCantidad.x + 2, subirUnidad.y + subirUnidad.height + 2, 18, 17);
            subirDecena = new Rectangle(subirUnidad.x + 2 + subirUnidad.width, subirUnidad.y, 18, 17);
            bajarDecena = new Rectangle(subirDecena.x, subirDecena.y + bajarUnidad.height + 2, 18, 17);
            aceptarOperacion = new Rectangle(ventanaCantidad.x + ventanaCantidad.width + 2, ventanaCantidad.y, 42, 20);
            dibujarVentanaCompra = true;
            vendiendo = true;

            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return; // Ignora la recogida si está dentro del tiempo de debouncing
            }

            // Actualiza el tiempo de la última recogida
            tiempoUltimaAccion = tiempoActual;
            if (canastaVenta.contains(objetoSeleccionadoVenta)) {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos++;
                    if (cantidadObjetos > objetoSeleccionadoVenta.getCantidad()) {
                        cantidadObjetos = objetoSeleccionadoVenta.getCantidad();
                    }

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos += 10;
                    if (cantidadObjetos > objetoSeleccionadoVenta.getCantidad()) {
                        cantidadObjetos = objetoSeleccionadoVenta.getCantidad();
                    }

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos--;

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos -= 10;

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(aceptarOperacion))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    for (Objeto objetoCanasta : canastaVenta) {
                        if (objetoCanasta.getId() == objetoSeleccionadoVenta.getId()) {

                            objetoCanasta.setCantidadVenta(objetoCanasta.getCantidadVenta() + cantidadObjetos);
                            totalTransaccionVenta += cantidadObjetos * objetoCanasta.getPrecioVenta();
                            objetoSeleccionadoVenta.setCantidad(objetoSeleccionadoVenta.getCantidad() - cantidadObjetos);
                            if (objetoCanasta.getCantidadVenta() < 0) {
                                objetoCanasta.setCantidadVenta(0);
                            }
                        }
                    }

                    dibujarVentanaCompra = false;
                    objetoSeleccionadoVenta = null;
                    cantidadObjetos = 0;
                }
            }
            else {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos++;
                    if (cantidadObjetos > objetoSeleccionadoVenta.getCantidad()) {
                        cantidadObjetos = objetoSeleccionadoVenta.getCantidad();
                    }

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(subirDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos += 10;
                    if (cantidadObjetos > objetoSeleccionadoVenta.getCantidad()) {
                        cantidadObjetos = objetoSeleccionadoVenta.getCantidad();
                    }

                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarUnidad))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos--;
                    if (cantidadObjetos < 0) {
                        cantidadObjetos = 0;
                    }
                }
                else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(bajarDecena))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    cantidadObjetos -= 10;
                    if (cantidadObjetos < 0) {
                        cantidadObjetos = 0;
                    }

                }

                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(aceptarOperacion))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    objetoSeleccionadoVenta.setCantidadVenta(cantidadObjetos);
                    objetoSeleccionadoVenta.setCantidad(objetoSeleccionadoVenta.getCantidad() - cantidadObjetos);
                    if (cantidadObjetos == 0) {
                        dibujarVentanaCompra = false;
                        objetoSeleccionadoVenta = null;
                        return;
                    }
                    totalTransaccionVenta += objetoSeleccionadoVenta.getCantidadVenta() * objetoSeleccionadoVenta.getPrecioVenta();
                    canastaVenta.add(objetoSeleccionadoVenta);
                    dibujarVentanaCompra = false;
                    objetoSeleccionadoVenta = null;
                    cantidadObjetos = 0;
                }
            }
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(vender))
                && !canastaVenta.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {

            Iterator<Objeto> iterador = ElementosPrincipales.inventario.getListaObjetos().iterator();

            while (iterador.hasNext()) {
                Objeto objetoActual = iterador.next();

                if (objetoActual.getCantidad() <= 0) {
                    iterador.remove();
                    break; // Salir del bucle después de recoger un objeto
                }
            }
            ElementosPrincipales.inventario.dinero += totalTransaccionVenta;
            System.out.println("" + ElementosPrincipales.inventario.dinero);
            vendiendo = false;
            totalTransaccionVenta = 0;
            canastaVenta.clear();
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(cancelarVenta))
                && !canastaVenta.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            for (Objeto objetoCanasta : canastaVenta) {
                int idObjeto = objetoCanasta.getId();
                for (Objeto objetoInventario : ElementosPrincipales.inventario.getListaObjetos()) {
                    if (idObjeto == objetoInventario.getId()) {
                        objetoInventario.setCantidad(objetoInventario.getCantidad() + objetoCanasta.getCantidadVenta());
                    }
                }
            }
            vendiendo = false;
            totalTransaccionVenta = 0;
            canastaVenta.clear();
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))
                && !canastaVenta.isEmpty()) {

            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return; // Ignora la recogida si está dentro del tiempo de debouncing
            }

            // Actualiza el tiempo de la última recogida
            tiempoUltimaAccion = tiempoActual;

            Iterator<Objeto> iterador = canastaVenta.iterator();

            while (iterador.hasNext()) {
                Objeto objetoActual = iterador.next();

                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objetoActual.getPosicionVenta()))
                        && GestorPrincipal.sd.getRaton().isClick2()) {
                    int idObjeto = objetoActual.getId();
                    for (Objeto objetoInventario : ElementosPrincipales.inventario.getListaObjetos()) {
                        if (idObjeto == objetoInventario.getId()) {
                            objetoInventario.setCantidad(objetoInventario.getCantidad() + objetoActual.getCantidadVenta());
                            totalTransaccionVenta -= objetoActual.getCantidadVenta() * objetoActual.getPrecioVenta();
                        }
                    }
                    iterador.remove();

                    break; // Salir del bucle después de recoger un objeto
                }
            }
        }

    }

    private void calcularPesoFuturo() {

        int pesoFuturo = 0;

        for (Objeto objetoInventario : ElementosPrincipales.inventario.getListaObjetos()) {
            pesoFuturo += objetoInventario.getPeso() * objetoInventario.getCantidad();
        }

        for (Objeto objetoCanasta : canastaCompra) {

            pesoFuturo += objetoCanasta.getPeso() * objetoCanasta.getCantidadCompra();
        }

        if (pesoFuturo > ElementosPrincipales.jugador.getGa().getLimitePeso() && !canastaCompra.isEmpty()) {
            excederiaPeso = true;
        }
        else if (pesoFuturo < ElementosPrincipales.jugador.getGa().getLimitePeso()) {
            excederiaPeso = false;
        }
    }

    private void dibujarPaneles(Graphics g) {
        dibujarPanelComprar(g, panelComprar, titularPanelComprar, "TIENDA");
        dibujarPanelComprados(g, panelObjetosComprados, titularPanelComprados, "CANASTA COMPRA");
        dibujarPanelVender(g, panelVender, titularPanelVender, "MOCHILA");
        dibujarPanelObjetosVendidos(g, panelObjetosVendidos, titularPanelVenta, "CANASTA VENTA");
    }

    private void dibujarPanel(final Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        g.setColor(Color.DARK_GRAY);
        DibujoDebug.dibujarRectanguloContorno(g, panel);
        DibujoDebug.dibujarRectanguloRelleno(g, titularPanel);
        g.setColor(Color.white);
        DibujoDebug.dibujarString(g, nombrePanel, new Point(
                panel.x + titularPanel.width / 2 - MedidorString.medirAnchoPixeles(g, nombrePanel) / 2,
                panel.y + titularPanel.height - MedidorString.medirAltoPixeles(g, nombrePanel) / 2 - 8));
    }

    private void dibujarElementosTienda(final Graphics g) {

        List<Objeto> objetos = objetosTienda;
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanelTienda(g, objetos, lado);
        if (objetoSeleccionado != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionado.getSprite().getImagen(),
                    new Point(objetoSeleccionado.getPosicionFlotante().x,
                            objetoSeleccionado.getPosicionFlotante().y));
        }
    }

    private void dibujarElementosCanastaVenta(final Graphics g) {

        int lado = Constantes.LADO_SPRITE;
        DibujoDebug.dibujarRectanguloContorno(g, vender, Color.blue);
        DibujoDebug.dibujarRectanguloContorno(g, cancelarVenta);
        g.setColor(Color.BLACK);
        DibujoDebug.dibujarString(g, "VENDER", vender.x + 6, vender.y + vender.height - 4);
        DibujoDebug.dibujarString(g, "CANCELAR", cancelarVenta.x + 2, cancelarVenta.y + cancelarVenta.height - 4);
        if (vendiendo && !canastaVenta.isEmpty()) {
            DibujoDebug.dibujarString(g, "$" + totalTransaccionVenta, titularPanelVenta.x + titularPanelVenta.width / 2,
                    titularPanelVenta.y + titularPanelVenta.height - 2, Color.WHITE);
        }

        dibujarElementosPanelTiendaVenta(g, canastaVenta, lado);
        if (objetoSeleccionadoVenta != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionadoVenta.getSprite().getImagen(),
                    new Point(objetoSeleccionadoVenta.getPosicionFlotante().x,
                            objetoSeleccionadoVenta.getPosicionFlotante().y));
        }
    }

    private void dibujarElementosCanastaCompra(final Graphics g) {

        int lado = Constantes.LADO_SPRITE;
        DibujoDebug.dibujarRectanguloContorno(g, comprar, Color.blue);
        DibujoDebug.dibujarRectanguloContorno(g, cancelarCompra);
        g.setColor(Color.BLACK);
        DibujoDebug.dibujarString(g, "COMPRAR", comprar.x + 6, comprar.y + comprar.height - 4);
        DibujoDebug.dibujarString(g, "CANCELAR", cancelarCompra.x + 2, cancelarCompra.y + cancelarCompra.height - 4);
        Color colorTexto = new Color(255, 255, 255);
        if (ElementosPrincipales.inventario.dinero < totalTransaccionCompra || ElementosPrincipales.jugador.isSobrepeso()
                || excederiaPeso) {
            colorTexto = new Color(255, 0, 0);
        }

        if (ElementosPrincipales.jugador.isSobrepeso()) {
            DibujoDebug.dibujarString(g, "No puedes llevar mas objetos...", comprar.x, comprar.y - 4, colorTexto);
        }

        if (!canastaCompra.isEmpty()) {

            if (excederiaPeso) {
                DibujoDebug.dibujarString(g, "La compra excederia el peso maximo", comprar.x, comprar.y - 4, colorTexto);
            }

            else if (comprando) {
                DibujoDebug.dibujarString(g, "$" + totalTransaccionCompra, titularPanelComprados.x + titularPanelComprados.width / 2,
                        titularPanelComprados.y + titularPanelComprados.height - 2, colorTexto);
            }

            else if (sinDinero) {
                DibujoDebug.dibujarString(g, "Dinero Insuficiente", comprar.x,
                        comprar.y - 4, Color.RED);
            }
        }

        dibujarElementosPanelTiendaCompra(g, canastaCompra, lado);
        if (objetoSeleccionadoCompra != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionadoCompra.getSprite().getImagen(),
                    new Point(objetoSeleccionadoCompra.getPosicionFlotante().x,
                            objetoSeleccionadoCompra.getPosicionFlotante().y));
        }
    }

    private void dibujarElementosEnPanelTienda(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        for (int i = 0; i < objetos.size(); i++) {
            Objeto objetoActual = objetos.get(i);
            if (objetoActual instanceof Armadura) {

                Rectangle posicionMenu = objetoActual.getPosicionTienda();

                DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionMenu.x, posicionMenu.y);
                String texto = "$" + objetoActual.getPrecioCompra();

                int xTexto = posicionMenu.x + lado - MedidorString.medirAnchoPixeles(g, texto);
                int yTexto = posicionMenu.y + 31;

                g.setColor(Color.black);
                DibujoDebug.dibujarRectanguloRelleno(g, xTexto - 2, yTexto - 7,
                        MedidorString.medirAnchoPixeles(g, texto) + 4, 8);

                g.setColor(Color.white);

                Color colorTexto = new Color(255, 255, 255);

                if (ElementosPrincipales.inventario.dinero < objetoActual.getPrecioCompra()) {
                    colorTexto = new Color(255, 0, 0);
                }

                DibujoDebug.dibujarString(g, texto, xTexto, yTexto, colorTexto);

            }
        }
    }

    private void dibujarElementosEnPanelInventario(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        for (int i = 0; i < objetos.size(); i++) {
            Objeto objetoActual = objetos.get(i);

            if (objetoActual instanceof Armadura) {

                Rectangle posicionMenu = objetoActual.getPosicionMochila();
                DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionMenu.x, posicionMenu.y);
                String texto = "";
                if (objetoActual.getCantidad() < 10) {
                    texto = "0" + objetoActual.getCantidad();
                }
                else {
                    texto = "" + objetoActual.getCantidad();
                }

                g.setColor(Color.black);
                DibujoDebug.dibujarRectanguloRelleno(g, posicionMenu.x + lado - 12, posicionMenu.y + 32 - 8, 12, 8);

                g.setColor(Color.white);

                // Calcula la posición x del texto para que esté centrado dentro del rectángulo negro
                int xTexto = posicionMenu.x + lado - MedidorString.medirAnchoPixeles(g, texto) - 2;
                // Ajusta la posición del texto de acuerdo con la posición del objeto
                int yTexto = posicionMenu.y + 31;

                DibujoDebug.dibujarString(g, texto, xTexto, yTexto);

            }
        }
    }

    private void dibujarElementosPanelTiendaCompra(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        for (int i = 0; i < objetos.size(); i++) {
            Objeto objetoActual = objetos.get(i);
            if (!(objetoActual instanceof Armadura)) {
                return;

            }
            if (objetoActual.getCantidadCompra() <= 0) {
                return;
            }
            Rectangle posicionTienda = objetoActual.getPosicionCompra();

            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionTienda.x, posicionTienda.y);

            String texto = "";
            if (objetoActual.getCantidadCompra() < 10) {
                texto = "0" + objetoActual.getCantidadCompra();
            }
            else {
                texto = "" + objetoActual.getCantidadCompra();
            }

            g.setColor(Color.black);
            DibujoDebug.dibujarRectanguloRelleno(g, posicionTienda.x + lado - 12, posicionTienda.y + 32 - 8, 12, 8);

            g.setColor(Color.white);

            // Ajusta la posición del texto de acuerdo con la posición del objeto
            int xTexto = posicionTienda.x + lado - MedidorString.medirAnchoPixeles(g, texto);
            int yTexto = posicionTienda.y + 31;

            DibujoDebug.dibujarString(g, texto, xTexto, yTexto);
        }
    }

    private void dibujarElementosPanelTiendaVenta(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        for (int i = 0; i < objetos.size(); i++) {
            Objeto objetoActual = objetos.get(i);
            if (!(objetoActual instanceof Armadura)) {
                return;

            }
            Rectangle posicionVenta = objetoActual.getPosicionVenta();

            DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionVenta.x, posicionVenta.y);

            String texto = "";
            if (objetoActual.getCantidadVenta() < 10) {
                texto = "0" + objetoActual.getCantidadVenta();
            }
            else {
                texto = "" + objetoActual.getCantidadVenta();
            }

            g.setColor(Color.black);
            DibujoDebug.dibujarRectanguloRelleno(g, posicionVenta.x + lado - 12, posicionVenta.y + 32 - 8, 12, 8);

            g.setColor(Color.white);

            // Ajusta la posición del texto de acuerdo con la posición del objeto
            int xTexto = posicionVenta.x + lado - MedidorString.medirAnchoPixeles(g, texto);
            int yTexto = posicionVenta.y + 31;

            DibujoDebug.dibujarString(g, texto, xTexto, yTexto);
        }
    }

    private void dibujarElementosInventario(final Graphics g) {
        List<Objeto> objetos = new ArrayList<>();

        for (Objeto objetoInventario : ElementosPrincipales.inventario.getArmaduras()) {
            if (objetoNoVendible(objetoInventario.getId())) {
                continue;
            }
            objetos.add(objetoInventario);
        }
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanelInventario(g, objetos, lado);
        if (objetoSeleccionadoVenta != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionadoVenta.getSprite().getImagen(),
                    new Point(objetoSeleccionadoVenta.getPosicionFlotante().x,
                            objetoSeleccionadoVenta.getPosicionFlotante().y));
        }

    }

    private void dibujarPanelComprar(Graphics g, Rectangle panelComprar, Rectangle titularPanelComprar, String nombrePanel) {
        dibujarPanel(g, panelComprar, titularPanelComprar, nombrePanel);
        dibujarElementosTienda(g);
    }

    private void dibujarPanelComprados(Graphics g, Rectangle panelComprados, Rectangle titularPanelComprados, String nombrePanel) {
        dibujarPanel(g, panelComprados, titularPanelComprados, nombrePanel);
        dibujarElementosCanastaCompra(g);

    }

    private void dibujarPanelVender(Graphics g, Rectangle panelVender, Rectangle titularPanelVender, String nombrePanel) {
        dibujarPanel(g, panelVender, titularPanelVender, nombrePanel);
        dibujarElementosInventario(g);
    }

    private void dibujarPanelObjetosVendidos(Graphics g, Rectangle panelVendidos, Rectangle titularPanelVendidos, String nombrePanel) {
        dibujarPanel(g, panelVendidos, titularPanelVendidos, nombrePanel);
        dibujarElementosCanastaVenta(g);

    }

    public void dibujarTooltipPeso(final Graphics g, SuperficieDibujo sd) {
        String textoCarga = String.format("%.1f", ElementosPrincipales.jugador.getGa().getPesoActual());
        String textoCargaTotal = String.format("%.1f", ElementosPrincipales.jugador.getGa().getLimitePeso());
        String textoFinal = textoCarga + "/" + textoCargaTotal;
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(barraPeso))) {
            GeneradorTooltip.dibujarTooltip(g, sd, textoFinal);
        }

    }

    private void dibujarVentanaParaCompra(Graphics g, SuperficieDibujo sd) {

        if (dibujarVentanaCompra && objetoSeleccionadoCompra != null) {

            DibujoDebug.dibujarRectanguloRelleno(g, ventanaCantidad, Color.GRAY);
            DibujoDebug.dibujarRectanguloRelleno(g, subirUnidad, Color.BLUE);
            DibujoDebug.dibujarRectanguloRelleno(g, bajarUnidad);
            DibujoDebug.dibujarRectanguloRelleno(g, subirDecena);
            DibujoDebug.dibujarRectanguloRelleno(g, bajarDecena);
            DibujoDebug.dibujarRectanguloRelleno(g, aceptarOperacion, Color.GRAY);
            DibujoDebug.dibujarString(g, "+1", subirUnidad.x + 4, subirUnidad.y + subirUnidad.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "-1", bajarUnidad.x + 4, bajarUnidad.y + bajarUnidad.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "+10", subirDecena.x + 3, subirUnidad.y + subirDecena.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "-10", bajarDecena.x + 3, bajarUnidad.y + bajarDecena.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "ACEPTAR", aceptarOperacion.x + 3, aceptarOperacion.y + aceptarOperacion.height - 6, Color.BLACK);
            DibujoDebug.dibujarString(g, "" + cantidadObjetos, aceptarOperacion.x, aceptarOperacion.y + aceptarOperacion.height + 10);

        }
    }

    private void dibujarVentanaParaVenta(Graphics g, SuperficieDibujo sd) {
        if (dibujarVentanaCompra && objetoSeleccionadoVenta != null) {

            DibujoDebug.dibujarRectanguloRelleno(g, ventanaCantidad, Color.GRAY);
            DibujoDebug.dibujarRectanguloRelleno(g, subirUnidad, Color.BLUE);
            DibujoDebug.dibujarRectanguloRelleno(g, bajarUnidad);
            DibujoDebug.dibujarRectanguloRelleno(g, subirDecena);
            DibujoDebug.dibujarRectanguloRelleno(g, bajarDecena);
            DibujoDebug.dibujarRectanguloRelleno(g, aceptarOperacion, Color.GRAY);
            DibujoDebug.dibujarString(g, "+1", subirUnidad.x + 4, subirUnidad.y + subirUnidad.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "-1", bajarUnidad.x + 4, bajarUnidad.y + bajarUnidad.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "+10", subirDecena.x + 3, subirUnidad.y + subirDecena.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "-10", bajarDecena.x + 3, bajarUnidad.y + bajarDecena.height - 6, Color.WHITE);
            DibujoDebug.dibujarString(g, "ACEPTAR", aceptarOperacion.x + 3, aceptarOperacion.y + aceptarOperacion.height - 6, Color.BLACK);
            DibujoDebug.dibujarString(g, "" + cantidadObjetos, aceptarOperacion.x, aceptarOperacion.y + aceptarOperacion.height + 10);

        }
    }

    private void dibujarTooltipPaneles(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelComprar))) {
            for (Objeto objeto : objetosTienda) {
                if (objeto instanceof Armadura) {
                    if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionTienda()))) {
                        if (objetoSeleccionadoCompra == null) {
                            DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionTienda(), Color.BLUE);
                            // Dibuja el tooltip solo si objetoSeleccionado no es null
                            dibujarTooltipObjeto(g, sd, objeto);
                        }

                    }
                }
            }
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))) {
            for (Objeto objeto : canastaCompra) {
                Armadura armadura = (Armadura) objeto;
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionCompra()))) {
                    if (objetoSeleccionadoCompra == null) {
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionCompra(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionado no es null
                        dibujarTooltipObjeto(g, sd, armadura);

                    }
                }
            }
        }

        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelVender))) {
            for (Objeto objeto : ElementosPrincipales.inventario.getArmaduras()) {
                Armadura armadura = (Armadura) objeto;
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMochila()))) {
                    if (objetoSeleccionadoVenta == null) {
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMochila(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionado no es null
                        dibujarTooltipObjeto(g, sd, armadura);

                    }
                }
            }
        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))) {
            for (Objeto objeto : canastaVenta) {
                Armadura armadura = (Armadura) objeto;
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionVenta()))) {
                    if (objetoSeleccionadoVenta == null) {
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionVenta(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionado no es null
                        dibujarTooltipObjeto(g, sd, armadura);

                    }
                }
            }
        }
    }

    private void dibujarTooltipObjeto(Graphics g, SuperficieDibujo sd, Objeto objeto) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();
        String textoPrecio = "";

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelComprar))) {
            Armadura armadura = (Armadura) objeto;

            textoPrecio += armadura.getPrecioCompra();
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nPRECIO COMPRA: $" + textoPrecio
                    + "\nDEFENSA FISICA: " + armadura.getDefensaF() + "\nDEFENSA MAGICA: " + armadura.getDefensaM()
                    + "\nPESO: " + armadura.getPeso() + "oz.");

        }

        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nTOTAL: $"
                    + objeto.getPrecioCompra() * objeto.getCantidadCompra());

        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelVender))) {

            Armadura armadura = (Armadura) objeto;

            textoPrecio += armadura.getPrecioVenta();

            GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nPRECIO COMPRA: $" + textoPrecio
                    + "\nDEFENSA FISICA: " + armadura.getDefensaF() + "\nDEFENSA MAGICA: " + armadura.getDefensaM()
                    + "\nPESO: " + armadura.getPeso() + "oz.");

        }
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))) {
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nTOTAL: $"
                    + objeto.getPrecioVenta() * objeto.getCantidadVenta());
        }
    }

    public Objeto getObjetoSeleccionadoCompra() {
        return objetoSeleccionadoCompra;
    }

    public Objeto getObjetoSeleccionadoVenta() {
        return objetoSeleccionadoVenta;
    }

    public void eliminarObjetoSeleccionado() {
        objetoSeleccionadoCompra = null;
        objetoSeleccionadoVenta = null;
    }

}
