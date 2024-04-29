package principal.graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.control.GestorControles;
import principal.control.Raton;
import principal.herramientas.DatosDebug;
import principal.herramientas.DibujoDebug;
import principal.maquinaestado.GestorEstados;
import principal.maquinaestado.juego.GestorJuego;
import principal.pantallaInicial.PantallaTitulo;

/**
 * Superficie de dibujo donde se renderiza todo el juego.
 */
public class SuperficieDibujo extends Canvas {

    // SerialVersionUID para compatibilidad entre versiones
    private static final long serialVersionUID = 123456789L;

    // Efectos visuales para transiciones
    private final EfectosVisuales efectosVisuales;

    // Variables para controlar cambios de mapa y el inicio del cronómetro
    public boolean cambioMapa = false;
    public boolean cronometroIniciado = false;

    // Ancho y alto de la superficie de dibujo
    private final int ancho;
    private final int alto;

    // Variable para controlar la posición del ratón
    Raton raton = new Raton(this);
    PantallaTitulo pantallaInicial;

    /**
     * Constructor de la superficie de dibujo.
     *
     * @param ancho El ancho de la superficie.
     * @param alto El alto de la superficie.
     */
    public SuperficieDibujo(final int ancho, final int alto) {
        this.alto = alto;
        this.ancho = ancho;

        // Inicialización del ratón y efectos visuales
        this.raton = new Raton(this);
        this.efectosVisuales = new EfectosVisuales();

        // Configuración de la superficie de dibujo
        setIgnoreRepaint(true);
        this.setCursor(raton.getCursor());
        setPreferredSize(new Dimension(ancho, alto));
        addKeyListener(GestorControles.teclado);
        addMouseListener(raton);
        setFocusable(true);
        requestFocus();
        pantallaInicial = new PantallaTitulo();
    }

    /**
     * Método para dibujar en la superficie de dibujo.
     *
     * @param ge El gestor de estados del juego.
     */
    public void dibujar(final GestorEstados ge) {
        // Si estamos en la pantalla de título, dibujamos la pantalla de título
        if (GestorPrincipal.pantallaTitulo) {
            dibujarPantallaTitulo(pantallaInicial);
        }
        else {
            // Si no, dibujamos el juego normalmente

            // Obtener la estrategia de buffer
            BufferStrategy buffer = getBufferStrategy();
            if (buffer == null) {
                createBufferStrategy(3);
                return;
            }

            // Obtener el contexto gráfico
            final Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
            DibujoDebug.reiniciarContadorObjetos();

            // Configuración inicial del contexto gráfico
            g.setFont(Constantes.FUENTE_POR_DEFECTO);
            DibujoDebug.dibujarRectanguloRelleno(g, 0, 0, Constantes.ANCHO_PANTALLA_COMPLETA,
                    Constantes.ALTO_PANTALLA_COMPLETA, Color.black);

            // Dibujar el juego mediante el gestor de estados
            g.scale(Constantes.FACTOR_ESCALADO_X, Constantes.FACTOR_ESCALADO_Y);
            ge.dibujar(g);

            // Dibujar FPS y APS
            g.setColor(Color.white);
            DibujoDebug.dibujarString(g, "FPS:  " + GestorPrincipal.getFps(), 20, 20);
            DibujoDebug.dibujarString(g, "APS:  " + GestorPrincipal.getAps(), 20, 30);
            raton.dibujar(g);

            // Dibujar datos de debug si está activado
            if (GestorControles.teclado.debug) {
                DatosDebug.dibujarDatos(g);
            }
            else {
                DatosDebug.vaciarDatos();
            }

            // Sincronizar la pantalla
            Toolkit.getDefaultToolkit().sync();

            // Si el jugador está muerto, mostrar transición de pantalla de muerte
            if (!ElementosPrincipales.jugador.estaVivo) {
                efectosVisuales.dibujarTransicionNegro(g, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String mensajeMuerte = "¡ESTÁS MUERTO!";
                int anchoTexto = g.getFontMetrics().stringWidth(mensajeMuerte);
                int xTexto = (Constantes.ANCHO_JUEGO - anchoTexto) / 2;
                int yTexto = Constantes.ALTO_JUEGO / 2;
                g.drawString(mensajeMuerte, xTexto, yTexto);

                // Recargar el juego y volver a la pantalla de título al presionar una tecla
                ElementosPrincipales.jugador.getGa().setVida(ElementosPrincipales.jugador.getGa().getVidaMaxima());
                GestorJuego.recargar = true;
                GestorPrincipal.pantallaTitulo = true;
            }

            // Liberar recursos
            g.dispose();

            // Mostrar la siguiente imagen del buffer
            buffer.show();
        }
    }

    /**
     * Método para dibujar la pantalla de título.
     *
     * @param pantallaTitulo La pantalla de título a dibujar.
     */
    public void dibujarPantallaTitulo(PantallaTitulo pantallaTitulo) {
        // Obtener la estrategia de buffer
        BufferStrategy buffer = getBufferStrategy();
        if (buffer == null) {
            createBufferStrategy(3);
            return;
        }

        // Obtener el contexto gráfico
        final Graphics2D g = (Graphics2D) buffer.getDrawGraphics();

        // Escalar la pantalla de título
        g.scale(2, 2);

        // Dibujar la pantalla de título
        pantallaTitulo.dibujar(g);

        // Liberar recursos
        g.dispose();

        // Mostrar la siguiente imagen del buffer
        buffer.show();
    }

    /**
     * Método para cargar una imagen desde una ruta.
     *
     * @param ruta La ruta de la imagen.
     * @return La imagen cargada.
     */
    public BufferedImage cargarImagen(String ruta) {
        try {
            return ImageIO.read(getClass().getResource(ruta));
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Método para dibujar una imagen en la superficie de dibujo.
     *
     * @param x La coordenada X de la posición de la imagen.
     * @param y La coordenada Y de la posición de la imagen.
     * @param imagen La imagen a dibujar.
     */
    public void dibujarImagen(int x, int y, BufferedImage imagen) {
        if (imagen != null) {
            getGraphics().drawImage(imagen, x, y, this);
        }
    }

    /**
     * Método para actualizar la superficie de dibujo.
     */
    public void actualizar() {
        raton.actualizar(this);
        pantallaInicial.actualizar();
    }

    // Getters y Setters
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public Raton getRaton() {
        return raton;
    }

}
