/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.juego.menu_tienda;

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
import principal.inventario.joyas.Joya;
import principal.maquinaestado.menujuego.MenuEquipo;

/**
 *
 * @author GAMER ARRAX
 */
public class TiendaAccesorios extends SeccionTienda {

    // Dimensiones de los paneles
    private final int anchoPaneles = 110;
    private final int altoPaneles = 308;

    // Rectángulos para los paneles de compra y objetos comprados
    final Rectangle panelComprar;
    final Rectangle panelObjetosComprados;

    // Variables de control para la interfaz de compra
    private boolean dibujarVentanaCompra = false;
    private boolean sinDinero = false;
    private boolean comprando = false;
    private boolean vendiendo = false;
    private boolean excederiaPeso = false;

    // Rectángulos para los paneles de venta y objetos vendidos
    final Rectangle panelVender;
    final Rectangle panelObjetosVendidos;

    // Titulares de los paneles
    final Rectangle titularPanelComprar;
    final Rectangle titularPanelVender;
    final Rectangle titularPanelComprados;
    final Rectangle titularPanelVenta;

    // Objeto seleccionado para compra y venta
    private Objeto objetoSeleccionadoCompra;
    private Objeto objetoSeleccionadoVenta;

    // Listas para los objetos en la canasta de compra y venta
    private final ArrayList<Objeto> canastaCompra;
    private final ArrayList<Objeto> canastaVenta;
    private ArrayList<Objeto> objetosTienda;

    // Posiciones anteriores para botones de venta y compra
    private final int anteriorXVenta;
    private final int anteriorYVenta;
    private final int anteriorXCompra;
    private final int anteriorYCompra;

    // Rectángulos para botones de operaciones y cantidad de objetos
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

    // Contadores y variables relacionadas con la cantidad de objetos y transacciones
    private int cantidadObjetos;
    private int totalTransaccionCompra;
    private int totalTransaccionVenta;

    // Variables de tiempo para evitar rebotes en la interfaz
    long tiempoUltimaAccion = 0;
    long tiempoDebouncing = 100;

    // Constructor
    public TiendaAccesorios(String nombreSeccion, Rectangle etiquetaMenu, EstructuraTienda et) {
        super(nombreSeccion, etiquetaMenu, et);

        // Inicialización de los rectángulos de los paneles
        panelComprar = new Rectangle(et.FONDO.x + margenGeneral * 2,
                barraPeso.y + barraPeso.height + margenGeneral,
                anchoPaneles, altoPaneles);

        panelObjetosComprados = new Rectangle(panelComprar.x + panelComprar.width + margenGeneral,
                panelComprar.y, anchoPaneles, altoPaneles);

        panelVender = new Rectangle(panelObjetosComprados.x + panelObjetosComprados.width + margenGeneral,
                panelObjetosComprados.y, anchoPaneles, altoPaneles);

        panelObjetosVendidos = new Rectangle(panelVender.x + panelVender.width + margenGeneral,
                panelVender.y, anchoPaneles, altoPaneles);

        // Inicialización de los rectángulos titulares de los paneles
        titularPanelComprar = new Rectangle(panelComprar.x, panelComprar.y, panelComprar.width, 24);
        titularPanelVender = new Rectangle(panelVender.x, panelVender.y, panelVender.width, 24);
        titularPanelComprados = new Rectangle(panelObjetosComprados.x, panelObjetosComprados.y, panelObjetosComprados.width, 24);
        titularPanelVenta = new Rectangle(panelObjetosVendidos.x, panelObjetosVendidos.y, panelObjetosVendidos.width, 24);

        // Inicialización de listas y variables
        canastaCompra = new ArrayList<>();
        canastaVenta = new ArrayList<>();
        objetoSeleccionadoCompra = null;
        objetoSeleccionadoVenta = null;

        // Posiciones iniciales de los botones de venta y compra
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

    // Método para dibujar la interfaz de la tienda
    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        // Dibujar el límite de peso
        dibujarLimitePeso(g);
        // Dibujar los paneles de la tienda
        dibujarPaneles(g);
        // Dibujar la ventana para la compra de objetos
        dibujarVentanaParaCompra(g, sd);
        // Dibujar la ventana para la venta de objetos
        dibujarVentanaParaVenta(g, sd);
        // Si se muestra el tooltip, dibujar tooltips de peso y paneles
        if (MenuEquipo.mostrarTooltip) {
            dibujarTooltipPeso(g, sd);
            dibujarTooltipPaneles(g, sd);
        }
    }

    // Método para actualizar la lógica de la tienda
    @Override
    public void actualizar() {
        // Obtener la lista de objetos disponibles en la tienda
        objetosTienda = ElementosPrincipales.mapa.objetosTiendaActual;
        // Actualizar las posiciones de los elementos de la tienda
        actualizarPosicionesMenu();
        // Actualizar las posiciones para compra y venta
        actualizarPosicionesCompraVenta();
        // Actualizar la selección del ratón
        actualizarSeleccionRaton();
        // Actualizar el objeto seleccionado para compra
        actualizarObjetoSeleccionadoCompra();
        // Actualizar el objeto seleccionado para venta
        actualizarObjetoSeleccionadoVenta();
        // Calcular el peso futuro en caso de compra
        calcularPesoFuturo();
        // Actualizar la canasta de compra
        actualizarCanastaCompra();
        // Actualizar la canasta de venta
        actualizarCanastaVenta();
    }

    // Método privado para actualizar la selección del ratón
    private void actualizarSeleccionRaton() {
        // Obtener la posición del ratón
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        // Verificar la selección de objetos para compra
        if (objetoSeleccionadoCompra == null) {
            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelComprar))) {
                if (objetosTienda.isEmpty()) {
                    return;
                }
                // Iterar sobre los objetos disponibles en la tienda
                for (Objeto objeto : objetosTienda) {
                    // Verificar si el objeto es una joya
                    if (objeto instanceof Joya) {
                        // Verificar si se hizo clic sobre el objeto
                        if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                                .intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionTienda()))) {
                            // Seleccionar el objeto para compra
                            objetoSeleccionadoCompra = objeto;
                            objetoSeleccionadoVenta = null;
                        }
                    }
                }
            }
        }

        // Verificar la selección de objetos para venta
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelVender))) {
            if (ElementosPrincipales.inventario.getJoyas().isEmpty()) {
                return;
            }
            // Iterar sobre las joyas en el inventario
            for (Objeto objeto : ElementosPrincipales.inventario.getJoyas()) {
                // Verificar si se hizo clic sobre el objeto
                if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                        .intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMochila()))) {
                    // Seleccionar el objeto para venta
                    objetoSeleccionadoVenta = objeto;
                    objetoSeleccionadoCompra = null;
                }
            }
        }
    }

    // Método privado para actualizar las posiciones de los objetos en el menú
    private void actualizarPosicionesMenu() {
        // Punto inicial para los objetos en la tienda
        final Point piObjetosTienda = new Point(et.FONDO.x + 10, titularPanelComprar.y + margenGeneral * 3);
        // Punto inicial para los objetos en el inventario
        final Point piObjetosInventario = new Point(panelVender.x - 6, titularPanelVender.y + margenGeneral * 3);

        final int lado = Constantes.LADO_SPRITE;
        int contadorArmasTienda = 0;
        int contadorArmasInventario = 0;
        int margenX = 8; // Nuevo margen desde el borde del panel

        // Actualizar las posiciones de los objetos en la tienda
        if (!objetosTienda.isEmpty()) {
            for (Objeto objetoActual : objetosTienda) {
                if (objetoActual instanceof Joya) {
                    // Calcular la posición para el objeto en la tienda
                    int posX = piObjetosTienda.x + margenX + (contadorArmasTienda % 3) * (lado + margenGeneral / 2);
                    int posY = piObjetosTienda.y + contadorArmasTienda / 3 * (lado + margenGeneral / 2);
                    Rectangle nuevaPosicionTienda = new Rectangle(posX, posY, lado, lado);
                    objetoActual.setPosicionTienda(nuevaPosicionTienda);
                    contadorArmasTienda++;
                }
            }
        }

        // Actualizar las posiciones de los objetos en el inventario
        if (!ElementosPrincipales.inventario.getJoyas().isEmpty()) {
            for (Objeto objetoActual : ElementosPrincipales.inventario.getJoyas()) {
                if (objetoNoVendible(objetoActual.getId())) {
                    objetoActual.setPosicionMochila(new Rectangle(0, 0, lado, lado));
                    continue;
                }
                // Calcular la posición para el objeto en el inventario
                int posX = piObjetosInventario.x + margenX + (contadorArmasInventario % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosInventario.y + contadorArmasInventario / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionInventario = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionMochila(nuevaPosicionInventario);
                contadorArmasInventario++;
            }
        }
    }

    // Método privado para actualizar las posiciones de los objetos en compra y venta
    private void actualizarPosicionesCompraVenta() {
        // Punto inicial para los objetos comprados
        final Point piObjetosCompra = new Point(panelObjetosComprados.x - 6, titularPanelComprados.y + margenGeneral * 3);
        // Punto inicial para los objetos vendidos
        final Point piObjetosVenta = new Point(panelObjetosVendidos.x - 6, titularPanelVenta.y + margenGeneral * 3);

        final int lado = Constantes.LADO_SPRITE;
        int contadorArmasCompradas = 0;
        int contadorArmasVendidas = 0;
        int margenX = 8; // Nuevo margen desde el borde del panel

        // Actualizar las posiciones de los objetos comprados
        if (!canastaCompra.isEmpty()) {
            for (Objeto objetoActual : canastaCompra) {
                // Calcular la posición para el objeto comprado
                int posX = piObjetosCompra.x + margenX + (contadorArmasCompradas % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosCompra.y + contadorArmasCompradas / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionCompra = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionCompra(nuevaPosicionCompra);
                contadorArmasCompradas++;
            }
        }

        // Actualizar las posiciones de los objetos vendidos
        if (!canastaVenta.isEmpty()) {
            for (Objeto objetoActual : canastaVenta) {
                // Calcular la posición para el objeto vendido
                int posX = piObjetosVenta.x + margenX + (contadorArmasVendidas % 3) * (lado + margenGeneral / 2);
                int posY = piObjetosVenta.y + contadorArmasVendidas / 3 * (lado + margenGeneral / 2);
                Rectangle nuevaPosicionVenta = new Rectangle(posX, posY, lado, lado);
                objetoActual.setPosicionVenta(nuevaPosicionVenta);
                contadorArmasVendidas++;
            }
        }
    }

    // Método privado para actualizar el objeto seleccionado para compra
    private void actualizarObjetoSeleccionadoCompra() {
        // Verificar si hay un objeto seleccionado para compra
        if (objetoSeleccionadoCompra != null) {
            // Verificar si se hizo clic derecho para cancelar la selección
            if (GestorPrincipal.sd.getRaton().isClick2()) {
                objetoSeleccionadoCompra = null;
                return;
            }
            // Obtener la posición del ratón y escalarla
            Point pfc = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            // Establecer la posición flotante del objeto seleccionado
            objetoSeleccionadoCompra.setPosicionFlotante(new Rectangle(pfc.x, pfc.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }
    }

    // Método privado para actualizar el objeto seleccionado para venta
    private void actualizarObjetoSeleccionadoVenta() {
        // Verificar si hay un objeto seleccionado para venta
        if (objetoSeleccionadoVenta != null) {
            // Verificar si se hizo clic derecho para cancelar la selección
            if (GestorPrincipal.sd.getRaton().isClick2()) {
                objetoSeleccionadoVenta = null;
                return;
            }
            // Obtener la posición del ratón y escalarla
            Point pfv = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            // Establecer la posición flotante del objeto seleccionado para venta
            objetoSeleccionadoVenta.setPosicionFlotante(new Rectangle(pfv.x, pfv.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }
    }

    // Método privado para actualizar la canasta de compra
    private void actualizarCanastaCompra() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        long tiempoActual = System.currentTimeMillis();

        // Verificar si el ratón está sobre el panel de objetos comprados y hay un objeto seleccionado para compra
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))
                && objetoSeleccionadoCompra != null && GestorPrincipal.sd.getRaton().isClick()) {
            // Verificar si el jugador excedería su peso máximo al agregar más objetos
            if (ElementosPrincipales.jugador.isSobrepeso()) {
                return;
            }
            // Crear las áreas para la ventana de cantidad y botones
            ventanaCantidad = new Rectangle(panelObjetosComprados.x, panelObjetosComprados.height / 2, 42, 40);
            subirUnidad = new Rectangle(ventanaCantidad.x + 2, ventanaCantidad.y + 2, 18, 17);
            bajarUnidad = new Rectangle(ventanaCantidad.x + 2, subirUnidad.y + subirUnidad.height + 2, 18, 17);
            subirDecena = new Rectangle(subirUnidad.x + 2 + subirUnidad.width, subirUnidad.y, 18, 17);
            bajarDecena = new Rectangle(subirDecena.x, subirDecena.y + bajarUnidad.height + 2, 18, 17);
            aceptarOperacion = new Rectangle(ventanaCantidad.x + ventanaCantidad.width + 2, ventanaCantidad.y, 42, 20);
            dibujarVentanaCompra = true;
            comprando = true;

            // Ignorar la acción si está dentro del tiempo de debouncing
            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return;
            }

            // Actualizar el tiempo de la última acción
            tiempoUltimaAccion = tiempoActual;

            // Procesar la cantidad de objetos a comprar
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

                // Actualizar la cantidad de objetos en la canasta
                for (Objeto objetoCanasta : canastaCompra) {
                    if (objetoCanasta.equals(objetoSeleccionadoCompra)) {
                        objetoCanasta.setCantidadCompra(objetoCanasta.getCantidadCompra() + cantidadObjetos);
                        if (objetoCanasta.getCantidadCompra() < 0) {
                            objetoCanasta.setCantidadCompra(0);
                        }
                    }
                }
                // Actualizar el total de la transacción de compra
                totalTransaccionCompra += cantidadObjetos * objetoSeleccionadoCompra.getPrecioCompra();
                // Ocultar la ventana de compra y restablecer valores
                dibujarVentanaCompra = false;
                objetoSeleccionadoCompra = null;
                cantidadObjetos = 0;
            }
            else {
                // Procesar la cantidad de objetos a comprar si no están en la canasta
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
                // Establecer la cantidad de objetos en el objeto seleccionado para compra
                objetoSeleccionadoCompra.setCantidadCompra(cantidadObjetos);
                // Agregar el objeto seleccionado a la canasta de compra
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(aceptarOperacion))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    if (cantidadObjetos == 0) {
                        // Ocultar la ventana de compra si no hay objetos seleccionados
                        dibujarVentanaCompra = false;
                        objetoSeleccionadoCompra = null;
                        return;
                    }
                    // Agregar el objeto seleccionado a la canasta de compra
                    canastaCompra.add(objetoSeleccionadoCompra);
                    // Actualizar el total de la transacción de compra
                    totalTransaccionCompra += objetoSeleccionadoCompra.getCantidadCompra() * objetoSeleccionadoCompra.getPrecioCompra();
                    // Ocultar la ventana de compra y restablecer valores
                    dibujarVentanaCompra = false;
                    objetoSeleccionadoCompra = null;
                    cantidadObjetos = 0;
                }
            }
        }
        // Procesar la transacción de compra si se hace clic en el botón de compra
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(comprar))
                && !canastaCompra.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            // Verificar si el jugador excedería su peso máximo
            if (excederiaPeso) {
                return;
            }
            // Verificar si el jugador tiene suficiente dinero
            if (ElementosPrincipales.inventario.dinero < totalTransaccionCompra) {
                System.out.println("Dinero Insuficiente");
                sinDinero = true;
                return;
            }
            else {
                sinDinero = false;
            }
            // Procesar la transacción de compra
            for (Objeto objetoComprado : canastaCompra) {
                boolean objetoExiste = false;
                for (Objeto objetoInventario : ElementosPrincipales.inventario.getJoyas()) {
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
            // Descontar el dinero del jugador y mostrar el nuevo saldo
            ElementosPrincipales.inventario.dinero -= totalTransaccionCompra;
            System.out.println("Dinero: " + ElementosPrincipales.inventario.dinero);
            // Restablecer valores y limpiar la canasta de compra
            comprando = false;
            totalTransaccionCompra = 0;
            canastaCompra.clear();
        }
        // Cancelar la compra si se hace clic en el botón de cancelar compra
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(cancelarCompra))
                && !canastaCompra.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            comprando = false;
            sinDinero = false;
            totalTransaccionCompra = 0;
            canastaCompra.clear();
        }
        // Procesar la eliminación de objetos de la canasta de compra si se hace clic sobre ellos
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))
                && !canastaCompra.isEmpty()) {
            // Ignorar la acción si está dentro del tiempo de debouncing
            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return;
            }
            // Actualizar el tiempo de la última acción
            tiempoUltimaAccion = tiempoActual;
            // Iterar sobre la canasta de compra y eliminar el objeto seleccionado si se hace clic sobre él
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

    // Método privado para actualizar la canasta de venta
    private void actualizarCanastaVenta() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        long tiempoActual = System.currentTimeMillis();

        // Verificar si el ratón está sobre el panel de objetos vendidos y hay un objeto seleccionado para venta
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))
                && objetoSeleccionadoVenta != null && GestorPrincipal.sd.getRaton().isClick()) {
            // Crear las áreas para la ventana de cantidad y botones
            ventanaCantidad = new Rectangle(panelObjetosVendidos.x, panelObjetosVendidos.height / 2, 42, 40);
            subirUnidad = new Rectangle(ventanaCantidad.x + 2, ventanaCantidad.y + 2, 18, 17);
            bajarUnidad = new Rectangle(ventanaCantidad.x + 2, subirUnidad.y + subirUnidad.height + 2, 18, 17);
            subirDecena = new Rectangle(subirUnidad.x + 2 + subirUnidad.width, subirUnidad.y, 18, 17);
            bajarDecena = new Rectangle(subirDecena.x, subirDecena.y + bajarUnidad.height + 2, 18, 17);
            aceptarOperacion = new Rectangle(ventanaCantidad.x + ventanaCantidad.width + 2, ventanaCantidad.y, 42, 20);
            dibujarVentanaCompra = true;
            vendiendo = true;

            // Ignorar la acción si está dentro del tiempo de debouncing
            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return;
            }

            // Actualizar el tiempo de la última acción
            tiempoUltimaAccion = tiempoActual;

            // Procesar la cantidad de objetos en la canasta de venta
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
                    // Actualizar la cantidad de objetos en la canasta de venta
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
                    // Ocultar la ventana de compra y restablecer valores
                    dibujarVentanaCompra = false;
                    objetoSeleccionadoVenta = null;
                    cantidadObjetos = 0;
                }
            }
            else {
                // Procesar la cantidad de objetos en la canasta de venta si no están en la canasta
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

                // Procesar la aceptación de la cantidad de objetos en la canasta de venta
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(aceptarOperacion))
                        && GestorPrincipal.sd.getRaton().isClick()) {
                    objetoSeleccionadoVenta.setCantidadVenta(cantidadObjetos);
                    objetoSeleccionadoVenta.setCantidad(objetoSeleccionadoVenta.getCantidad() - cantidadObjetos);
                    if (cantidadObjetos == 0) {
                        // Ocultar la ventana de compra si no hay objetos seleccionados
                        dibujarVentanaCompra = false;
                        objetoSeleccionadoVenta = null;
                        return;
                    }
                    // Actualizar el total de la transacción de venta y agregar el objeto a la canasta de venta
                    totalTransaccionVenta += objetoSeleccionadoVenta.getCantidadVenta() * objetoSeleccionadoVenta.getPrecioVenta();
                    canastaVenta.add(objetoSeleccionadoVenta);
                    // Ocultar la ventana de compra y restablecer valores
                    dibujarVentanaCompra = false;
                    objetoSeleccionadoVenta = null;
                    cantidadObjetos = 0;
                }
            }
        }
        // Procesar la transacción de venta si se hace clic en el botón de venta
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(vender))
                && !canastaVenta.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            // Eliminar objetos del inventario si su cantidad llega a cero
            Iterator<Objeto> iterador = ElementosPrincipales.inventario.getListaObjetos().iterator();
            while (iterador.hasNext()) {
                Objeto objetoActual = iterador.next();
                if (objetoActual.getCantidad() <= 0) {
                    iterador.remove();
                    break; // Salir del bucle después de recoger un objeto
                }
            }
            // Agregar el total de la transacción de venta al dinero del inventario y mostrar el nuevo saldo
            ElementosPrincipales.inventario.dinero += totalTransaccionVenta;
            System.out.println("" + ElementosPrincipales.inventario.dinero);
            // Restablecer valores y limpiar la canasta de venta
            vendiendo = false;
            totalTransaccionVenta = 0;
            canastaVenta.clear();
        }
        // Cancelar la venta si se hace clic en el botón de cancelar venta
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(cancelarVenta))
                && !canastaVenta.isEmpty() && GestorPrincipal.sd.getRaton().isClick()) {
            // Devolver los objetos de la canasta de venta al inventario
            for (Objeto objetoCanasta : canastaVenta) {
                int idObjeto = objetoCanasta.getId();
                for (Objeto objetoInventario : ElementosPrincipales.inventario.getListaObjetos()) {
                    if (idObjeto == objetoInventario.getId()) {
                        objetoInventario.setCantidad(objetoInventario.getCantidad() + objetoCanasta.getCantidadVenta());
                    }
                }
            }
            // Restablecer valores y limpiar la canasta de venta
            vendiendo = false;
            totalTransaccionVenta = 0;
            canastaVenta.clear();
        }
        // Procesar la eliminación de objetos de la canasta de venta si se hace clic sobre ellos
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))
                && !canastaVenta.isEmpty()) {
            // Ignorar la acción si está dentro del tiempo de debouncing
            if (tiempoActual - tiempoUltimaAccion < tiempoDebouncing) {
                return;
            }
            // Actualizar el tiempo de la última acción
            tiempoUltimaAccion = tiempoActual;
            // Iterar sobre la canasta de venta y eliminar el objeto seleccionado si se hace clic sobre él
            Iterator<Objeto> iterador = canastaVenta.iterator();
            while (iterador.hasNext()) {
                Objeto objetoActual = iterador.next();
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objetoActual.getPosicionVenta()))
                        && GestorPrincipal.sd.getRaton().isClick2()) {
                    // Agregar la cantidad de objetos eliminados de la canasta de venta de nuevo al inventario
                    int idObjeto = objetoActual.getId();
                    for (Objeto objetoInventario : ElementosPrincipales.inventario.getListaObjetos()) {
                        if (idObjeto == objetoInventario.getId()) {
                            objetoInventario.setCantidad(objetoInventario.getCantidad() + objetoActual.getCantidadVenta());
                            // Restar el valor de la transacción de venta
                            totalTransaccionVenta -= objetoActual.getCantidadVenta() * objetoActual.getPrecioVenta();
                        }
                    }
                    // Eliminar el objeto de la canasta de venta
                    iterador.remove();
                    break; // Salir del bucle después de recoger un objeto
                }
            }
        }
    }

    // Método privado para calcular el peso futuro considerando los objetos en el inventario y la canasta de compra
    private void calcularPesoFuturo() {
        int pesoFuturo = 0;

        // Calcular el peso futuro sumando el peso de cada objeto en el inventario
        for (Objeto objetoInventario : ElementosPrincipales.inventario.getListaObjetos()) {
            pesoFuturo += objetoInventario.getPeso() * objetoInventario.getCantidad();
        }

        // Calcular el peso futuro sumando el peso de cada objeto en la canasta de compra
        for (Objeto objetoCanasta : canastaCompra) {
            pesoFuturo += objetoCanasta.getPeso() * objetoCanasta.getCantidadCompra();
        }

        // Verificar si el peso futuro excede el límite de peso del jugador
        if (pesoFuturo > ElementosPrincipales.jugador.getGa().getLimitePeso() && !canastaCompra.isEmpty()) {
            excederiaPeso = true;
        }
        else if (pesoFuturo < ElementosPrincipales.jugador.getGa().getLimitePeso()) {
            excederiaPeso = false;
        }
    }

    // Método privado para dibujar los paneles de la interfaz de usuario
    private void dibujarPaneles(Graphics g) {
        dibujarPanel(g, panelComprar, titularPanelComprar, "TIENDA");
        dibujarPanel(g, panelObjetosComprados, titularPanelComprados, "CANASTA COMPRA");
        dibujarPanel(g, panelVender, titularPanelVender, "MOCHILA");
        dibujarPanel(g, panelObjetosVendidos, titularPanelVenta, "CANASTA VENTA");
    }

    // Método privado para dibujar un panel con un título
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

    // Método privado para dibujar los elementos en la tienda
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

    // Método privado para dibujar los elementos en la canasta de venta
    private void dibujarElementosCanastaVenta(final Graphics g) {
        int lado = Constantes.LADO_SPRITE;

        // Dibujar los botones "VENDER" y "CANCELAR"
        DibujoDebug.dibujarRectanguloContorno(g, vender, Color.blue);
        DibujoDebug.dibujarRectanguloContorno(g, cancelarVenta);
        g.setColor(Color.BLACK);
        DibujoDebug.dibujarString(g, "VENDER", vender.x + 6, vender.y + vender.height - 4);
        DibujoDebug.dibujarString(g, "CANCELAR", cancelarVenta.x + 2, cancelarVenta.y + cancelarVenta.height - 4);

        // Mostrar el total de la transacción de venta si se está vendiendo y la canasta no está vacía
        if (vendiendo && !canastaVenta.isEmpty()) {
            DibujoDebug.dibujarString(g, "$" + totalTransaccionVenta, titularPanelVenta.x + titularPanelVenta.width / 2,
                    titularPanelVenta.y + titularPanelVenta.height - 2, Color.WHITE);
        }

        // Dibujar los elementos en la canasta de venta
        dibujarElementosPanelTiendaVenta(g, canastaVenta, lado);
        // Dibujar el objeto seleccionado de la canasta de venta si existe
        if (objetoSeleccionadoVenta != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionadoVenta.getSprite().getImagen(),
                    new Point(objetoSeleccionadoVenta.getPosicionFlotante().x,
                            objetoSeleccionadoVenta.getPosicionFlotante().y));
        }
    }

    // Método privado para dibujar los elementos en la canasta de compra
    private void dibujarElementosCanastaCompra(final Graphics g) {
        int lado = Constantes.LADO_SPRITE;

        // Dibujar los botones "COMPRAR" y "CANCELAR"
        DibujoDebug.dibujarRectanguloContorno(g, comprar, Color.blue);
        DibujoDebug.dibujarRectanguloContorno(g, cancelarCompra);
        g.setColor(Color.BLACK);
        DibujoDebug.dibujarString(g, "COMPRAR", comprar.x + 6, comprar.y + comprar.height - 4);
        DibujoDebug.dibujarString(g, "CANCELAR", cancelarCompra.x + 2, cancelarCompra.y + cancelarCompra.height - 4);

        // Determinar el color del texto de acuerdo con las condiciones de compra
        Color colorTexto = Color.WHITE;
        if (ElementosPrincipales.inventario.dinero < totalTransaccionCompra || ElementosPrincipales.jugador.isSobrepeso()
                || excederiaPeso) {
            colorTexto = Color.RED;
        }

        // Mostrar mensajes de error si hay condiciones de compra incorrectas
        if (ElementosPrincipales.jugador.isSobrepeso()) {
            DibujoDebug.dibujarString(g, "No puedes llevar más objetos...", comprar.x, comprar.y - 4, colorTexto);
        }

        if (!canastaCompra.isEmpty()) {
            if (excederiaPeso) {
                DibujoDebug.dibujarString(g, "La compra excedería el peso máximo", comprar.x, comprar.y - 4, colorTexto);
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

        // Dibujar los elementos en la canasta de compra
        dibujarElementosPanelTiendaCompra(g, canastaCompra, lado);
        // Dibujar el objeto seleccionado de la canasta de compra si existe
        if (objetoSeleccionadoCompra != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionadoCompra.getSprite().getImagen(),
                    new Point(objetoSeleccionadoCompra.getPosicionFlotante().x,
                            objetoSeleccionadoCompra.getPosicionFlotante().y));
        }
    }

    // Método privado para dibujar los elementos en un panel de tienda
    private void dibujarElementosEnPanelTienda(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        // Iterar sobre los objetos en la lista y dibujar cada uno en la posición correspondiente
        for (Objeto objetoActual : objetos) {
            if (objetoActual instanceof Joya) {
                Rectangle posicionMenu = objetoActual.getPosicionTienda();
                DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionMenu.x, posicionMenu.y);
                String texto = objetoActual.getPrecioCompra() < 10 ? "$0" + objetoActual.getPrecioCompra() : "$" + objetoActual.getPrecioCompra();
                g.setColor(Color.BLACK);
                DibujoDebug.dibujarRectanguloRelleno(g, posicionMenu.x + 4, posicionMenu.y + 32 - 8, 32, 8);
                g.setColor(Color.WHITE);
                int xTexto = posicionMenu.x + lado - MedidorString.medirAnchoPixeles(g, texto);
                int yTexto = posicionMenu.y + 31;
                Color colorTexto = ElementosPrincipales.inventario.dinero < objetoActual.getPrecioCompra() ? Color.RED : Color.WHITE;
                DibujoDebug.dibujarString(g, texto, xTexto, yTexto, colorTexto);
            }
        }
    }

    // Método privado para dibujar los elementos en el panel de inventario
    private void dibujarElementosEnPanelInventario(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        // Iterar sobre los objetos en la lista y dibujar cada uno en la posición correspondiente
        for (Objeto objetoActual : objetos) {
            if (objetoActual instanceof Joya) {
                Rectangle posicionMenu = objetoActual.getPosicionMochila();
                DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionMenu.x, posicionMenu.y);
                String texto = objetoActual.getCantidad() < 10 ? "0" + objetoActual.getCantidad() : String.valueOf(objetoActual.getCantidad());
                g.setColor(Color.BLACK);
                DibujoDebug.dibujarRectanguloRelleno(g, posicionMenu.x + lado - 12, posicionMenu.y + 32 - 8, 12, 8);
                g.setColor(Color.WHITE);
                int xTexto = posicionMenu.x + lado - MedidorString.medirAnchoPixeles(g, texto) - 2;
                int yTexto = posicionMenu.y + 31;
                DibujoDebug.dibujarString(g, texto, xTexto, yTexto);
            }
        }
    }

    // Método privado para dibujar los elementos en el panel de tienda de compra
    private void dibujarElementosPanelTiendaCompra(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        // Iterar sobre los objetos en la lista y dibujar cada uno en la posición correspondiente
        for (Objeto objetoActual : objetos) {
            if (objetoActual instanceof Joya && objetoActual.getCantidadCompra() > 0) {
                Rectangle posicionTienda = objetoActual.getPosicionCompra();
                DibujoDebug.dibujarImagen(g, objetoActual.getSprite().getImagen(), posicionTienda.x, posicionTienda.y);
                String texto = objetoActual.getCantidadCompra() < 10 ? "0" + objetoActual.getCantidadCompra() : String.valueOf(objetoActual.getCantidadCompra());
                g.setColor(Color.BLACK);
                DibujoDebug.dibujarRectanguloRelleno(g, posicionTienda.x + lado - 12, posicionTienda.y + 32 - 8, 12, 8);
                g.setColor(Color.WHITE);
                int xTexto = posicionTienda.x + lado - MedidorString.medirAnchoPixeles(g, texto);
                int yTexto = posicionTienda.y + 31;
                Color colorTexto = ElementosPrincipales.inventario.dinero < totalTransaccionCompra ? Color.RED : Color.WHITE;
                DibujoDebug.dibujarString(g, texto, xTexto, yTexto, colorTexto);
            }
        }
    }

    // Método para dibujar los elementos en el panel de venta de la tienda
    private void dibujarElementosPanelTiendaVenta(final Graphics g, List<Objeto> objetos, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        for (int i = 0; i < objetos.size(); i++) {
            Objeto objetoActual = objetos.get(i);
            if (!(objetoActual instanceof Joya)) {
                continue; // Cambiado de return a continue para continuar con el bucle
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

            g.setColor(Color.BLACK);
            DibujoDebug.dibujarRectanguloRelleno(g, posicionVenta.x + lado - 12, posicionVenta.y + 32 - 8, 12, 8);

            g.setColor(Color.WHITE);

            // Ajusta la posición del texto de acuerdo con la posición del objeto
            int xTexto = posicionVenta.x + lado - MedidorString.medirAnchoPixeles(g, texto);
            int yTexto = posicionVenta.y + 31;

            DibujoDebug.dibujarString(g, texto, xTexto, yTexto);
        }
    }

// Método para dibujar los elementos en el inventario del jugador
    private void dibujarElementosInventario(final Graphics g) {
        List<Objeto> objetos = new ArrayList<>();

        for (Objeto objetoInventario : ElementosPrincipales.inventario.getJoyas()) {
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

    // Método para dibujar el panel de compra de la tienda
    private void dibujarPanelComprar(Graphics g, Rectangle panelComprar, Rectangle titularPanelComprar, String nombrePanel) {
        dibujarPanel(g, panelComprar, titularPanelComprar, nombrePanel);
        dibujarElementosTienda(g);
    }

// Método para dibujar el panel de los objetos comprados
    private void dibujarPanelComprados(Graphics g, Rectangle panelComprados, Rectangle titularPanelComprados, String nombrePanel) {
        dibujarPanel(g, panelComprados, titularPanelComprados, nombrePanel);
        dibujarElementosCanastaCompra(g);
    }

// Método para dibujar el panel de venta de la mochila del jugador
    private void dibujarPanelVender(Graphics g, Rectangle panelVender, Rectangle titularPanelVender, String nombrePanel) {
        dibujarPanel(g, panelVender, titularPanelVender, nombrePanel);
        dibujarElementosInventario(g);
    }

// Método para dibujar el panel de los objetos vendidos
    private void dibujarPanelObjetosVendidos(Graphics g, Rectangle panelVendidos, Rectangle titularPanelVendidos, String nombrePanel) {
        dibujarPanel(g, panelVendidos, titularPanelVendidos, nombrePanel);
        dibujarElementosCanastaVenta(g);
    }

// Método para dibujar el tooltip del peso del jugador
    public void dibujarTooltipPeso(final Graphics g, SuperficieDibujo sd) {
        String textoCarga = String.format("%.1f", ElementosPrincipales.jugador.getGa().getPesoActual());
        String textoCargaTotal = String.format("%.1f", ElementosPrincipales.jugador.getGa().getLimitePeso());
        String textoFinal = textoCarga + "/" + textoCargaTotal;
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(barraPeso))) {
            GeneradorTooltip.dibujarTooltip(g, sd, textoFinal);
        }
    }

    // Método para dibujar la ventana de compra
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

// Método para dibujar la ventana de venta
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

// Método para dibujar los tooltips de los paneles de compra, venta y los objetos
    private void dibujarTooltipPaneles(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        // Comprueba si el ratón está sobre el panel de compra
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelComprar))) {
            // Recorre los objetos en la tienda
            for (Objeto objeto : objetosTienda) {
                if (objeto instanceof Joya) {
                    // Comprueba si el ratón está sobre un objeto de la tienda
                    if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionTienda()))) {
                        if (objetoSeleccionadoCompra == null) {
                            // Dibuja el contorno del objeto solo si no hay un objeto seleccionado
                            DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionTienda(), Color.BLUE);
                            // Dibuja el tooltip solo si objetoSeleccionadoCompra no es null
                            dibujarTooltipObjeto(g, sd, objeto);
                        }
                    }
                }
            }
        }
        // Comprueba si el ratón está sobre el panel de objetos comprados
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))) {
            // Recorre los objetos en la canasta de compra
            for (Objeto objeto : canastaCompra) {
                Joya accesorio = (Joya) objeto;
                // Comprueba si el ratón está sobre un objeto de la canasta de compra
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionCompra()))) {
                    if (objetoSeleccionadoCompra == null) {
                        // Dibuja el contorno del objeto solo si no hay un objeto seleccionado
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionCompra(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionadoCompra no es null
                        dibujarTooltipObjeto(g, sd, accesorio);
                    }
                }
            }
        }
        // Comprueba si el ratón está sobre el panel de venta
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelVender))) {
            // Recorre los objetos en el inventario del jugador
            for (Objeto objeto : ElementosPrincipales.inventario.getJoyas()) {
                Joya accesorio = (Joya) objeto;
                // Comprueba si el ratón está sobre un objeto del inventario del jugador
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMochila()))) {
                    if (objetoSeleccionadoVenta == null) {
                        // Dibuja el contorno del objeto solo si no hay un objeto seleccionado
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMochila(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionadoVenta no es null
                        dibujarTooltipObjeto(g, sd, accesorio);
                    }
                }
            }
        }
        // Comprueba si el ratón está sobre el panel de objetos vendidos
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))) {
            // Recorre los objetos en la canasta de venta
            for (Objeto objeto : canastaVenta) {
                Joya accesorio = (Joya) objeto;
                // Comprueba si el ratón está sobre un objeto de la canasta de venta
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionVenta()))) {
                    if (objetoSeleccionadoVenta == null) {
                        // Dibuja el contorno del objeto solo si no hay un objeto seleccionado
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionVenta(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionadoVenta no es null
                        dibujarTooltipObjeto(g, sd, accesorio);
                    }
                }
            }
        }
    }

    private void dibujarTooltipObjeto(Graphics g, SuperficieDibujo sd, Objeto objeto) {
        // Obtiene la posición del ratón
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();
        // Variable para almacenar el precio del objeto en texto
        String textoPrecio = "";

        // Verifica si el ratón está sobre el panel de compra
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelComprar))) {
            // Si el objeto es una joya
            if (objeto instanceof Joya) {
                // Convierte el objeto a tipo Joya
                Joya accesorio = (Joya) objeto;
                // Obtiene el precio de compra de la joya
                textoPrecio += accesorio.getPrecioCompra();
                // Dibuja un tooltip detallado con información sobre la joya
                GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nPRECIO COMPRA: $" + textoPrecio
                        + "\nDEFENSA FISICA: " + accesorio.getDefensaF() + "\nDEFENSA MAGICA: " + accesorio.getDefensaM()
                        + "\nPESO: " + accesorio.getPeso() + "oz.");
            }
        }
        // Verifica si el ratón está sobre el panel de objetos comprados
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosComprados))) {
            // Dibuja un tooltip con el total gastado en los objetos comprados
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nTOTAL: $"
                    + objeto.getPrecioCompra() * objeto.getCantidadCompra());
        }
        // Verifica si el ratón está sobre el panel de venta
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelVender))) {
            // Si el objeto es una joya
            if (objeto instanceof Joya) {
                // Convierte el objeto a tipo Joya
                Joya accesorio = (Joya) objeto;
                // Obtiene el precio de venta de la joya
                textoPrecio += accesorio.getPrecioVenta();
                // Dibuja un tooltip detallado con información sobre la joya
                GeneradorTooltip.dibujarTooltipMejorado(g, sd, objeto.getNombre() + "\nPRECIO COMPRA: $" + textoPrecio
                        + "\nDEFENSA FISICA: " + accesorio.getDefensaF() + "\nDEFENSA MAGICA: " + accesorio.getDefensaM()
                        + "\nPESO: " + accesorio.getPeso() + "oz.");
            }
        }
        // Verifica si el ratón está sobre el panel de objetos vendidos
        else if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelObjetosVendidos))) {
            // Dibuja un tooltip con el total ganado por los objetos vendidos
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
