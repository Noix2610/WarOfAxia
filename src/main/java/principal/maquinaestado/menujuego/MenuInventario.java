/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.menujuego;

import java.awt.Color;
import java.awt.Font;
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
import principal.interfaz_usuario.MenuInferior;
import principal.inventario.Objeto;
import principal.inventario.consumibles.Claves;
import principal.inventario.consumibles.Consumible;

/**
 *
 * @author GAMER ARRAX
 */
public class MenuInventario extends SeccionMenu {

    public ArrayList<Rectangle> rectangulosAccesoRapido = new ArrayList<>();

    private Rectangle botonUsar = new Rectangle();
    private Rectangle botonTirar = new Rectangle();

    final Rectangle panelConsumibles = new Rectangle(em.FONDO.x + margenGeneral,
            barraPeso.y + barraPeso.height + margenGeneral,
            Constantes.ANCHO_JUEGO / 3, Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);
    final Rectangle panelAccesoRapido = new Rectangle(panelConsumibles.x + panelConsumibles.width + margenGeneral,
            panelConsumibles.y, 100, panelConsumibles.height);

    final Rectangle panelClave = new Rectangle(panelAccesoRapido.x + panelAccesoRapido.width + margenGeneral,
            panelAccesoRapido.y, 150, panelAccesoRapido.height);

    final Rectangle titularPanelConsumibles = new Rectangle(panelConsumibles.x, panelConsumibles.y, panelConsumibles.width, 24);

    final Rectangle titularPanelClave = new Rectangle(panelClave.x, panelClave.y, panelClave.width, 24);
    final Rectangle titularPanelAccesoRapido = new Rectangle(panelAccesoRapido.x, panelAccesoRapido.y, panelAccesoRapido.width, 24);
    final Rectangle acceso1 = new Rectangle(titularPanelAccesoRapido.x + margenGeneral - 2, titularPanelAccesoRapido.y + Constantes.LADO_SPRITE - 2,
            Constantes.LADO_SPRITE + 2,
            Constantes.LADO_SPRITE + 2);

    public MenuInventario(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
        super(nombreSeccion, etiquetaMenu, em);
        crearRectangulosAccesoRapido();
    }

    public void actualizar() {
        actualizarPosicionesMenu();
        actualizarSeleccionRaton();
        actualizarObjetoSeleccionado();
    }

    private void actualizarPosicionesMenu() {
        if (ElementosPrincipales.inventario.getConsumibles().isEmpty() && ElementosPrincipales.inventario.getClaves().isEmpty()) {
            return;
        }

        final Point piConsumibles = new Point(em.FONDO.x + margenGeneral,
                titularPanelConsumibles.y + margenGeneral * 3);
        final Point piClaves = new Point(titularPanelClave.x + margenGeneral * 2,
                titularPanelClave.y + margenGeneral * 3);

        final int lado = Constantes.LADO_SPRITE;
        int contadorConsumibles = 0;
        int contadorClaves = 0;

        ArrayList<Objeto> objetos = new ArrayList<>();

        for (Objeto objetoActual : ElementosPrincipales.inventario.getConsumibles()) {
            objetos.add(objetoActual);
        }

        for (Objeto objetoActual : ElementosPrincipales.inventario.getClaves()) {
            objetos.add(objetoActual);
        }

        for (Objeto objetoActual : objetos) {
            if (objetoActual instanceof Consumible) {
                Rectangle nuevaPosicion = new Rectangle(piConsumibles.x + contadorConsumibles % 6 * (lado + margenGeneral),
                        piConsumibles.y + contadorConsumibles / 6 * (lado + margenGeneral), lado, lado);
                objetoActual.setPosicionMenu(nuevaPosicion);
                contadorConsumibles++;
            }
            else if (objetoActual instanceof Claves) {
                Rectangle nuevaPosicion = new Rectangle(piClaves.x + contadorClaves % 3 * (lado + margenGeneral),
                        piClaves.y + contadorClaves / 3 * (lado + margenGeneral), lado, lado);
                objetoActual.setPosicionMenu(nuevaPosicion);
                contadorClaves++;
            }
        }
    }

    public void crearRectangulosAccesoRapido() {
        final int numFilas = 5;
        final int numColumnas = 2;
        final int margen = 10; // Ajusta este margen según tus necesidades

        int anchoRectangulo = 32; // Ancho fijo de 32 pixeles
        int altoRectangulo = 32; // Alto fijo de 32 pixeles

        int xInicial = panelAccesoRapido.x + margen;
        int yInicial = panelAccesoRapido.y + titularPanelAccesoRapido.height + margen;

        // Restar la posición y el alto del titular del panel de acceso rápido
        for (int fila = 0; fila < numFilas; fila++) {
            for (int columna = 0; columna < numColumnas; columna++) {
                int x = xInicial + columna * (anchoRectangulo + margen);
                int y = yInicial + fila * (altoRectangulo + margen * 2);
                Rectangle rectangulo = new Rectangle(x, y, anchoRectangulo, altoRectangulo);
                rectangulosAccesoRapido.add(rectangulo);
            }
        }
    }

    private void dibujarRectangulosAccesoRapido(Graphics g) {
        for (Rectangle rectangulo : rectangulosAccesoRapido) {
            DibujoDebug.dibujarImagen(g, MenuInferior.bordesRanuras.getSprites(0).getImagen(), rectangulo.x - 3, rectangulo.y - 4);
        }
    }

    private void dibujarClaveAccesoRapido(Graphics g) {
        int i = 1;
        for (Rectangle rectangulo : rectangulosAccesoRapido) {
            if (i < 10) {
                DibujoDebug.dibujarString(g, "" + i, rectangulo.x + 1, rectangulo.y + 7, Color.black);
            }
            else {
                DibujoDebug.dibujarString(g, "0", rectangulo.x + 1, rectangulo.y + 7, Color.black);
            }
            i++;
        }
    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        dibujarPaneles(g);
        dibujarLimitePeso(g);
        dibujarRectangulosAccesoRapido(g);
        //dibujarElementosInventario(g, em);
        //dibujarSpritesInventario(g, em);
        if (MenuEquipo.mostrarTooltip) {
            dibujarTooltipPeso(g, sd);
            dibujarTooltipConsumibles(g, sd);
            dibujarTooltipClaves(g, sd);
            dibujarTooltipAccRapido(g, sd);
        }
        dibujarVentanaEmergente(g);
        dibujarClaveAccesoRapido(g);
        dibujarPaginador(g);

    }

    private void dibujarPaneles(final Graphics g) {
        // Lógica para dibujar paneles comunes...
        dibujarPanelConsumibles(g, panelConsumibles, titularPanelConsumibles, "CONSUMIBLES");
        dibujarPanelAccesoRapido(g, panelAccesoRapido, titularPanelAccesoRapido, "ACCESO RAPIDO");
        dibujarPanelClave(g, panelClave, titularPanelClave, "OBJETOS CLAVE");

        // Otros paneles que puedas tener...
    }

    private void dibujarPanelClave(Graphics g, final Rectangle panel, final Rectangle titularPanel, final String nombrePanel) {
        // Lógica para dibujar el panel de objetos clave...
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        dibujarElementosClaves(g);
    }

    private void dibujarPanelAccesoRapido(Graphics g, final Rectangle panel, final Rectangle titularPanel, final String nombrePanel) {
        // Lógica para dibujar el panel de objetos clave...
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        dibujarAccesosRapidos(g);
    }

    private void dibujarAccesosRapidos(Graphics g) {
        for (int i = 0; i < rectangulosAccesoRapido.size(); i++) {
            Rectangle rectangulo = rectangulosAccesoRapido.get(i);
            Object objeto = ElementosPrincipales.jugador.getAr().accesosEquipados[i];

            // Verificar si el objeto en este índice no es nulo y luego dibujarlo
            if (objeto != null) {
                // Suponiendo que tienes un método para dibujar el objeto en el rectángulo
                // Reemplaza esto con el método que utilices para dibujar el objeto
                dibujarObjetoEnRectangulo(g, objeto, rectangulo);
            }
        }
    }

// Método para dibujar un objeto en un rectángulo
    private void dibujarObjetoEnRectangulo(Graphics g, Object objeto, Rectangle rectangulo) {
        if (objeto instanceof Consumible) {
            DibujoDebug.dibujarImagen(g, ((Consumible) objeto).getSprite().getImagen(), rectangulo.x, rectangulo.y);
        }
        else if (objeto instanceof Habilidad) {
            DibujoDebug.dibujarImagen(g, ((Habilidad) objeto).getImagenActual(), rectangulo.x, rectangulo.y);
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
                panel.y + titularPanel.height - MedidorString.medirAltoPixeles(g, nombrePanel) / 2));
    }

    private void dibujarPanelConsumibles(Graphics g, final Rectangle panel, final Rectangle titularPanel, final String nombrePanel) {
        // Lógica para dibujar el panel de consumibles...
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        dibujarElementosConsumibles(g);

    }

    private void dibujarElementosConsumibles(final Graphics g) {
        List<Objeto> consumibles = ElementosPrincipales.inventario.getConsumibles();
        Point pi = new Point(em.FONDO.x + margenGeneral,
                titularPanelConsumibles.height * 2 + margenGeneral * 2);
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanel(g, consumibles, pi, lado);
        if (objetoSeleccionado != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionado.getSprite().getImagen(),
                    new Point(objetoSeleccionado.getPosicionFlotante().x,
                            objetoSeleccionado.getPosicionFlotante().y));
        }
    }

    private void dibujarElementosEnPanel(final Graphics g, List<Objeto> objetos, Point pi, int lado) {
        if (objetos.isEmpty()) {
            return;
        }

        for (int i = 0; i < objetos.size(); i++) {
            Objeto objetoActual = objetos.get(i);
            Rectangle posicionMenu = objetoActual.getPosicionMenu();

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

            // Ajusta la posición del texto de acuerdo con la posición del objeto
            int xTexto = posicionMenu.x + lado - MedidorString.medirAnchoPixeles(g, texto);
            int yTexto = posicionMenu.y + 31;

            DibujoDebug.dibujarString(g, texto, xTexto, yTexto);
        }
    }

    private void dibujarElementosClaves(final Graphics g) {
        List<Objeto> claves = ElementosPrincipales.inventario.getClaves();
        Point pi = new Point(titularPanelClave.x + margenGeneral,
                titularPanelClave.height * 2 + margenGeneral * 2);
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanel(g, claves, pi, lado);
        if (objetoSeleccionado != null) {
            DibujoDebug.dibujarImagen(g, objetoSeleccionado.getSprite().getImagen(),
                    new Point(objetoSeleccionado.getPosicionFlotante().x,
                            objetoSeleccionado.getPosicionFlotante().y));
        }

    }

    /**
     * Método para dibujar los botones de paginación en el panel de consumibles.
     *
     * @param g Objeto Graphics para dibujar en el contexto gráfico.
     */
    private void dibujarPaginador(final Graphics g) {
        // Definir el ancho y alto de los botones
        final int anchoBoton = 24;
        final int altoBoton = 12;

        // Calcular la posición del botón "anterior"
        int anteriorX = panelConsumibles.x + panelConsumibles.width - Constantes.LADO_SPRITE * 2 + 4;
        int anteriorY = panelConsumibles.y + panelConsumibles.height - (Constantes.LADO_SPRITE / 2);

        // Crear los rectángulos para los botones "anterior" y "siguiente"
        final Rectangle anterior = new Rectangle(anteriorX - 4, anteriorY, anchoBoton, altoBoton);
        final Rectangle siguiente = new Rectangle(anterior.x + anterior.width + margenGeneral, anterior.y,
                anchoBoton, altoBoton);

        // Establecer el color de los botones
        g.setColor(Color.blue);

        // Dibujar el contorno de los botones "anterior" y "siguiente"
        DibujoDebug.dibujarRectanguloContorno(g, anterior);
        DibujoDebug.dibujarRectanguloContorno(g, siguiente);

        // Dibujar las flechas dentro de los botones
        DibujoDebug.dibujarString(g, "<<", anterior.x + anterior.width - 18, anterior.y + anterior.height - 5);
        DibujoDebug.dibujarString(g, ">>", siguiente.x + siguiente.width - 18, siguiente.y + siguiente.height - 5);
    }

    /**
     * Método para dibujar un tooltip que muestra información sobre la carga actual del jugador.
     *
     * @param g Objeto Graphics para dibujar en el contexto gráfico.
     * @param sd SuperficieDibujo que contiene información sobre la posición del ratón.
     */
    public void dibujarTooltipPeso(final Graphics g, SuperficieDibujo sd) {
        String textoCarga = String.format("%.1f", ElementosPrincipales.jugador.getGa().getPesoActual());
        String textoCargaTotal = String.format("%.1f", ElementosPrincipales.jugador.getGa().getLimitePeso());
        String textoFinal = textoCarga + "/" + textoCargaTotal;
        if (sd.getRaton().getPosicionRectangle().intersects(EscaladorElementos.escalarRectangleArriba(barraPeso))) {
            GeneradorTooltip.dibujarTooltip(g, sd, textoFinal);
        }

    }

    private void dibujarTooltipConsumibles(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelConsumibles))) {
            for (Objeto objeto : ElementosPrincipales.inventario.getConsumibles()) {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMenu()))) {
                    if (objetoSeleccionado == null) {
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu(), Color.DARK_GRAY);
                        // Dibuja el tooltip solo si objetoSeleccionado no es null
                        dibujarTooltipObjeto(g, sd, objeto);
                    }
                    else if (objetoSeleccionado != null && (objetoSeleccionado instanceof Consumible)) {
                        dibujarTooltipObjeto(g, sd, objetoSeleccionado);
                    }
                }
            }
        }
    }

    private void dibujarTooltipAccRapido(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelAccesoRapido))) {
            for (int i = 0; i < rectangulosAccesoRapido.size(); i++) {
                Rectangle rectangulo = rectangulosAccesoRapido.get(i);

                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(rectangulo))) {
                    Object objeto = ElementosPrincipales.jugador.getAr().getAccesoEquipado(i);

                    if (objeto != null) {
                        dibujarTooltipObjetosAccRapido(g, sd, objeto);
                        return; // Salir del método después de dibujar un solo tooltip
                    }
                }
            }
        }
    }

    private void dibujarTooltipClaves(final Graphics g, final SuperficieDibujo sd) {
        Rectangle posicionRaton = sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelClave))) {
            for (Objeto objeto : ElementosPrincipales.inventario.getClaves()) {
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMenu()))) {
                    if (objetoSeleccionado == null) {
                        DibujoDebug.dibujarRectanguloContorno(g, objeto.getPosicionMenu(), Color.BLUE);
                        // Dibuja el tooltip solo si objetoSeleccionado no es null
                        dibujarTooltipObjeto(g, sd, objeto);
                    }
                    else if (objetoSeleccionado != null && (objetoSeleccionado instanceof Claves)) {
                        dibujarTooltipObjeto(g, sd, objetoSeleccionado);
                    }
                }
            }
        }
    }

    private void dibujarTooltipObjeto(Graphics g, SuperficieDibujo sd, Object objeto) {
        // Aquí puedes personalizar la apariencia del tooltip según tus necesidades
        if (objeto instanceof Consumible) {
            Consumible consumible = (Consumible) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, consumible.getNombre() + "\nPESO: " + consumible.getPeso() + " oz.");
        }
        else if (objeto instanceof Claves) {
            Claves claves = (Claves) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, claves.getNombre() + "\nPESO: " + claves.getPeso() + " oz.");
        }
    }

    private void dibujarTooltipObjetosAccRapido(Graphics g, SuperficieDibujo sd, Object objeto) {
        // Aquí puedes personalizar la apariencia del tooltip según tus necesidades
        if (objeto instanceof Consumible) {
            Consumible consumible = (Consumible) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, consumible.getNombre() + "\nCANTIDAD: " + consumible.getCantidad());
        }
        else if (objeto instanceof Habilidad) {
            Habilidad habilidad = (Habilidad) objeto;
            GeneradorTooltip.dibujarTooltipMejorado(g, sd, habilidad.getNombre());
        }
    }

    private void actualizarSeleccionRaton() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();
        if (objetoSeleccionado == null) {
            if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelConsumibles))) {
                if (ElementosPrincipales.inventario.getConsumibles().isEmpty()) {
                    return;
                }
                for (Objeto objeto : ElementosPrincipales.inventario.getConsumibles()) {
                    if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                            .intersects(EscaladorElementos.escalarRectangleArriba(objeto.getPosicionMenu()))) {
                        objetoSeleccionado = objeto;
                    }
                }
            }
            else {
                objetoSeleccionado = null;
            }
        }

        actualizarSeleccionUsar();
        actualizarSeleccionTirar();
        actualizarSeleccionConsumible();
    }

    private void actualizarSeleccionConsumible() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelAccesoRapido))
                && objetoSeleccionado != null && objetoSeleccionado instanceof Consumible
                && GestorPrincipal.sd.getRaton().isClick()) {
            for (int i = 0; i < rectangulosAccesoRapido.size(); i++) {
                Rectangle rectangulo = rectangulosAccesoRapido.get(i);
                if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(rectangulo))) {
                    // Eliminar el objeto en el índice i
                    ElementosPrincipales.jugador.getAr().accesosEquipados[i] = objetoSeleccionado;
                    System.out.println(ElementosPrincipales.jugador.getAr().getAccesoEquipado(i));
                    objetoSeleccionado = null; // Limpiar el objeto seleccionado después de asignarlo
                    break; // Salir del bucle después de realizar la asignación
                }
            }
        }
    }

    private void dibujarVentanaEmergente(final Graphics g) {
        if (objetoSeleccionado != null) {

            // Dibuja la ventana emergente con opciones "usar" y "tirar"
            Rectangle borde = new Rectangle(objetoSeleccionado.getPosicionMenu().x + 32,
                    objetoSeleccionado.getPosicionMenu().y, 32, 32);
            Rectangle ventana = new Rectangle(objetoSeleccionado.getPosicionMenu().x + Constantes.LADO_SPRITE + 1,
                    objetoSeleccionado.getPosicionMenu().y + 1, borde.width - 2, borde.height - 2);
            botonUsar = new Rectangle(ventana.x + 3, ventana.y + 4, 24, 10);
            botonTirar = new Rectangle(botonUsar.x, botonUsar.y + botonUsar.height + 4, botonUsar.width, botonUsar.height);

            DibujoDebug.dibujarRectanguloContorno(g, borde, Color.GRAY);
            DibujoDebug.dibujarRectanguloRelleno(g, ventana, Color.WHITE);
            DibujoDebug.dibujarRectanguloRelleno(g, botonUsar, Color.BLACK);
            DibujoDebug.dibujarRectanguloRelleno(g, botonTirar, Color.BLACK);
            DibujoDebug.dibujarString(g, "Usar", botonUsar.x + 3, botonUsar.y + botonUsar.height - 3, Color.white);
            DibujoDebug.dibujarString(g, "Tirar", botonTirar.x + 3, botonTirar.y + botonTirar.height - 3, Color.white);

        }
    }

    /**
     * Método para actualizar la selección y uso de un objeto del inventario. Comprueba si se ha hecho clic en el botón
     * "Usar" y realiza la acción correspondiente.
     */
    private void actualizarSeleccionUsar() {
        // Obtener la posición del ratón
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        // Comprobar si el ratón está sobre el panel de consumibles, se ha seleccionado un objeto,
        // se ha hecho clic y el ratón está sobre el botón "Usar"
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelConsumibles))
                && objetoSeleccionado != null && objetoSeleccionado instanceof Consumible
                && GestorPrincipal.sd.getRaton().isClick2()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(botonUsar))) {
            // Acción a realizar cuando se usa un objeto
            System.out.println("Usado");
        }
    }

    /**
     * Método para actualizar la posición del objeto seleccionado. Mueve el objeto seleccionado junto con el cursor del
     * ratón.
     */
    private void actualizarObjetoSeleccionado() {
        // Comprobar si hay un objeto seleccionado
        if (objetoSeleccionado != null) {
            // Comprobar si se ha hecho clic derecho para deseleccionar el objeto
            if (GestorPrincipal.sd.getRaton().isClick2()) {
                objetoSeleccionado = null; // Desseleccionar el objeto
                return;
            }

            // Obtener la posición del ratón y escalarla
            Point pr = EscaladorElementos.escalarAbajo(GestorPrincipal.sd.getRaton().getPosicion());

            // Establecer la posición del objeto seleccionado junto al cursor del ratón
            objetoSeleccionado.setPosicionFlotante(
                    new Rectangle(pr.x, pr.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }
    }

    /**
     * Método para actualizar la selección y tirado de un objeto del inventario. Comprueba si se ha hecho clic en el
     * botón "Tirar" y realiza la acción correspondiente.
     */
    private void actualizarSeleccionTirar() {
        // Obtener la posición del ratón
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        // Comprobar si el ratón está sobre el panel de consumibles, se ha seleccionado un objeto,
        // se ha hecho clic y el ratón está sobre el botón "Tirar"
        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelConsumibles))
                && objetoSeleccionado != null && objetoSeleccionado instanceof Consumible
                && GestorPrincipal.sd.getRaton().isClick()
                && posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(botonTirar))) {
            // Acción a realizar cuando se tira un objeto
            System.out.println("Tirado");
        }
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public Object getTipoObjetoSeleccionado() {
        return objetoSeleccionado;
    }

    public Objeto getObjetoSeleccionado() {
        return objetoSeleccionado;
    }

    public void eliminarObjetoSeleccionado() {
        objetoSeleccionado = null;
    }

}
