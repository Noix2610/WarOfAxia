/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.menujuego;

import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import principal.entes.Enemigo;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.MedidorString;

/**
 *
 * @author GAMER ARRAX
 */
public class MenuBestiario extends SeccionMenu {

    private Enemigo enemigoSeleccionado;

    final Rectangle panelBestiario = new Rectangle(em.FONDO.x + margenGeneral * 3,
            barraPeso.y + barraPeso.height + margenGeneral,
            Constantes.ANCHO_JUEGO / 3, Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);

    final Rectangle panelDescripcion = new Rectangle(panelBestiario.x + panelBestiario.width + margenGeneral * 3,
            panelBestiario.y, panelBestiario.width, panelBestiario.height);

    final Rectangle titularPanelBestiario = new Rectangle(panelBestiario.x, panelBestiario.y,
            panelBestiario.width, 24);

    final Rectangle titularPanelDescripcion = new Rectangle(panelDescripcion.x, panelDescripcion.y,
            panelDescripcion.width, 24);

    /*final Rectangle panelAtributos = new Rectangle(panelDescripcion.x + panelDescripcion.width + margenGeneral,
            panelBestiario.y, 132, panelDescripcion.height);

    final Rectangle titularPanelAtributos = new Rectangle(panelAtributos.x, panelAtributos.y,
            panelAtributos.width, 24);*/
    public MenuBestiario(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
        super(nombreSeccion, etiquetaMenu, em);
    }

    @Override
    public void actualizar() {
        actualizarPosicionesMenu();
        actualizarSeleccionRaton();
    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        dibujarLimitePeso(g);
        dibujarPaneles(g);

    }

    private void dibujarPaneles(final Graphics g) {
        dibujarPanelBestiario(g, panelBestiario, titularPanelBestiario, "BESTIARIO");
        dibujarPanelDescripcion(g, panelDescripcion, titularPanelDescripcion, "DESCRIPCION");

    }

    private void dibujarPanelBestiario(Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        dibujarElementosBestiario(g, panel);

    }

    private void dibujarPanelDescripcion(Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);
        mostrarDescripcion(g);
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

    private void dibujarElementosBestiario(final Graphics g, final Rectangle panel) {
        if (ElementosPrincipales.inventario.enemigosEliminados.isEmpty()) {
            return;
        }

        List<Enemigo> enemigos = ElementosPrincipales.inventario.enemigosEliminados;
        Point pi = new Point(panel.x + margenGeneral,
                panel.height * 2 + margenGeneral * 2);
        int lado = Constantes.LADO_SPRITE;

        dibujarElementosEnPanel(g, enemigos, pi, lado);

    }

    public void dibujarElementosEnPanel(final Graphics g, List<Enemigo> enemigos, Point pi, int lado) {
        if (enemigos.isEmpty()) {
            return;
        }

        int idEnemigo = 0;

        if (enemigoSeleccionado != null) {
            idEnemigo = enemigoSeleccionado.getIdEnemigo();
        }

        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigoActual = enemigos.get(i);
            Rectangle posicionMenu = enemigoActual.getPosicionMenu();

            enemigoActual.idlePosition(g, posicionMenu, idEnemigo);

            /*String texto = "" + enemigoActual.getNombre();

            FontMetrics fm = g.getFontMetrics();
            int anchoTexto = MedidorString.medirAnchoPixeles(fm, texto) + 4;
            int altoTexto = MedidorString.medirAltoPixeles(fm);

            int xCuadro = posicionMenu.x;
            int yCuadro = posicionMenu.y + 32;

            int xTexto = xCuadro + 2; // Ajuste para centrar horizontalmente el texto dentro del cuadro
            int yTexto = yCuadro + (8 + altoTexto) / 2 - 2; // Ajuste para centrar verticalmente el texto dentro del cuadro

            g.setColor(Color.black);
            DibujoDebug.dibujarRectanguloRelleno(g, xCuadro, yCuadro, anchoTexto, 8);
            g.setColor(Color.white);
            DibujoDebug.dibujarString(g, texto, xTexto, yTexto);*/
        }
    }

    @Override
    protected void dibujarLimitePeso(final Graphics g) {
        Color color = null;
        String carga = "CARGA";
        int x = barraPeso.x - 35;

        // Calcular el porcentaje de peso actual en relación con el límite de peso
        double porcentajePeso = (ElementosPrincipales.jugador.getGa().getPesoActual() * 100)
                / ElementosPrincipales.jugador.getGa().getLimitePeso();

        // Calcular la longitud de la parte coloreada de la barra
        int longitudColoreada = (int) ((porcentajePeso / 100) * (barraPeso.width - 2));
        if (ElementosPrincipales.jugador.isSobrepeso()) {
            longitudColoreada = 100;
        }

        final Rectangle contenidoBarra = new Rectangle(barraPeso.x + 1, barraPeso.y + 1, longitudColoreada, barraPeso.height - 2);

        if (porcentajePeso < 25) {
            color = Color.green;
        }
        else if (porcentajePeso >= 25 && porcentajePeso < 50) {
            color = Color.yellow;
        }
        else if (porcentajePeso >= 50 && porcentajePeso < 75) {
            color = Constantes.COLOR_NARANJA;
        }
        else if (porcentajePeso >= 100) {
            color = Color.red;
            carga = "SOBREPESO";
            x = barraPeso.x - 60;
        }
        DibujoDebug.dibujarString(g, carga, x, barraPeso.y + 8, color);
        DibujoDebug.dibujarRectanguloRelleno(g, barraPeso, Color.gray);

        DibujoDebug.dibujarRectanguloRelleno(g, contenidoBarra, color);
    }

    private void mostrarDescripcion(Graphics g) {
        if (enemigoSeleccionado != null) {
            String nombreEnemigo = enemigoSeleccionado.getNombre();
            String descripcion = enemigoSeleccionado.getDescripcion();
            String[] lineas = descripcion.split("\n");

            int xNombre = MedidorString.medirAnchoTextoCentrado(g, nombreEnemigo, panelDescripcion.x, panelDescripcion.width);
            int yNombre = titularPanelDescripcion.y + titularPanelDescripcion.height + 8;

            DibujoDebug.dibujarString(g, nombreEnemigo, xNombre, yNombre, Color.BLACK);

            int y = yNombre + MedidorString.calcularAlturaTexto(g, nombreEnemigo) + margenGeneral;
            int anchoRectangulo = panelDescripcion.width - 2 * margenGeneral;

            dibujarDescripcion(g, lineas, anchoRectangulo, y);
        }
    }

    private void dibujarDescripcion(Graphics g, String[] lineas, int anchoRectangulo, int yInicial) {
        FontMetrics metrics = g.getFontMetrics();

        for (String linea : lineas) {
            StringBuilder lineaActual = new StringBuilder();
            String[] palabras = linea.split(" ");
            for (String palabra : palabras) {
                if (metrics.stringWidth(lineaActual.toString() + palabra) > anchoRectangulo) {
                    DibujoDebug.dibujarString(g, lineaActual.toString(), panelDescripcion.x + margenGeneral, yInicial, Color.BLACK);
                    yInicial += metrics.getHeight();
                    lineaActual = new StringBuilder(palabra + " ");
                }
                else {
                    lineaActual.append(palabra).append(" ");
                }
            }
            DibujoDebug.dibujarString(g, lineaActual.toString(), panelDescripcion.x + margenGeneral, yInicial, Color.BLACK);
            yInicial += metrics.getHeight();
        }
    }

    private void actualizarPosicionesMenu() {
        if (ElementosPrincipales.inventario.enemigosEliminados.isEmpty()) {
            return;
        }

        final Point piEnemigos = new Point(panelBestiario.x + margenGeneral, titularPanelBestiario.y + margenGeneral * 3);
        final int lado = Constantes.LADO_SPRITE;
        int contadorBestiario = 0;

        ArrayList<Enemigo> enemigos = new ArrayList<>();

        for (Enemigo enemigoActual : ElementosPrincipales.inventario.enemigosEliminados) {
            enemigos.add(enemigoActual);
        }

        int columnasPorFila = 5; // Número de columnas por fila
        int columnaActual = 0; // Variable para rastrear la columna actual

        for (Enemigo enemigoActual : enemigos) {
            int fila = contadorBestiario / columnasPorFila; // Calcula la fila actual
            int columna = contadorBestiario % columnasPorFila; // Calcula la columna actual

            int x = piEnemigos.x + columna * (lado + margenGeneral);
            int y = piEnemigos.y + fila * (lado + margenGeneral) + (fila * 8); // Agregamos 8 píxeles más a la separación

            Rectangle nuevaPosicion = new Rectangle(x, y, lado, lado);
            enemigoActual.setPosicionMenu(nuevaPosicion);

            contadorBestiario++;

            // Si la columna actual es igual al número de columnas por fila, resetea la columna a 0
            if (columna == columnasPorFila - 1) {
                columnaActual = 0;
            }
        }
    }

    private void actualizarSeleccionRaton() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangle();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectangleArriba(panelBestiario))) {
            if (!ElementosPrincipales.inventario.enemigosEliminados.isEmpty()) {
                for (Enemigo enemigo : ElementosPrincipales.inventario.enemigosEliminados) {
                    if (GestorPrincipal.sd.getRaton().isClick() && posicionRaton
                            .intersects(EscaladorElementos.escalarRectangleArriba(enemigo.getPosicionMenu()))) {
                        enemigoSeleccionado = enemigo;
                        break; // Salir del bucle una vez que se selecciona un enemigo
                    }
                }
            }
        }

        if (GestorPrincipal.sd.getRaton().isClick2()) {
            enemigoSeleccionado = null; // Deseleccionar el enemigo si se hace clic fuera de su área

        }
    }

}
