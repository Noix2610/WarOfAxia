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
import principal.herramientas.Cronometro;
import principal.herramientas.DatosDebug;
import principal.herramientas.DibujoDebug;
import principal.maquinaestado.GestorEstados;
import principal.maquinaestado.juego.GestorJuego;
import principal.pantallaInicial.PantallaTitulo;

/**
 *
 * @author GAMER ARRAX
 */
public class SuperficieDibujo extends Canvas {

    private static final long serialVersionUID = 123456789L;
    private EfectosVisuales efectosVisuales;
    public boolean cambioMapa = false;
    public boolean cronometroIniciado = false;
    Cronometro cronoMapa;

    private int ancho;
    private int alto;
    int incremento = 0;

    Raton raton = new Raton(this);

    public SuperficieDibujo(final int ancho, final int alto) {
        this.alto = alto;
        this.ancho = ancho;

        this.raton = new Raton(this);
        this.efectosVisuales = new EfectosVisuales();

        setIgnoreRepaint(true);
        this.setCursor(raton.getCursor());
        setPreferredSize(new Dimension(ancho, alto));
        addKeyListener(GestorControles.teclado);
        addMouseListener(raton);
        setFocusable(true);
        requestFocus();

    }

    public void dibujar(final GestorEstados ge) {
        if (GestorPrincipal.pantallaTitulo) {
            // Si pantallaTitulo es true, dibuja la pantalla de título
            dibujarPantallaTitulo(new PantallaTitulo());
        }
        else {
            // Si no, dibuja el juego normalmente
            BufferStrategy buffer = getBufferStrategy();
            if (buffer == null) {
                createBufferStrategy(3);
                return;
            }

            final Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
            DibujoDebug.reiniciarContadorObjetos();

            g.setFont(Constantes.FUENTE_POR_DEFECTO);
            /*DibujoDebug.dibujarRectanguloRelleno(g, 0, 0, Constantes.ANCHO_PANTALLA_COMPLETA,
                    Constantes.ALTO_PANTALLA_COMPLETA, Color.black);

            if (Constantes.FACTOR_ESCALADO_X != 1.0 || Constantes.FACTOR_ESCALADO_Y != 1.0) {
                g.scale(Constantes.FACTOR_ESCALADO_X, Constantes.FACTOR_ESCALADO_Y);
            }*/

            DibujoDebug.dibujarRectanguloRelleno(g, 0, 0, Constantes.ANCHO_JUEGO,
                    Constantes.ALTO_JUEGO, Color.black);

            ge.dibujar(g);

            g.setColor(Color.white);

            DibujoDebug.dibujarString(g, "FPS:  " + GestorPrincipal.getFps(), 20, 20);
            DibujoDebug.dibujarString(g, "APS:  " + GestorPrincipal.getAps(), 20, 30);
            raton.dibujar(g);
            if (GestorControles.teclado.debug) {
                DatosDebug.dibujarDatos(g);
            }
            else {
                DatosDebug.vaciarDatos();
            }

            Toolkit.getDefaultToolkit().sync();
            if (!ElementosPrincipales.jugador.estaVivo) {
                efectosVisuales.dibujarTransicionNegro(g, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String mensajeMuerte = "¡ESTÁS MUERTO!";
                int anchoTexto = g.getFontMetrics().stringWidth(mensajeMuerte);
                int xTexto = (Constantes.ANCHO_JUEGO - anchoTexto) / 2;
                int yTexto = Constantes.ALTO_JUEGO / 2;
                g.drawString(mensajeMuerte, xTexto, yTexto);

                // Verificar si se ha presionado alguna tecla
                // Establecer la variable de control para mostrar la pantalla de título
                ElementosPrincipales.jugador.getGa().setVida(ElementosPrincipales.jugador.getGa().getVidaMaxima());
                GestorJuego.recargar = true;

                GestorPrincipal.pantallaTitulo = true;
            }

            g.dispose();

            buffer.show();

            g.setFont(g.getFont().deriveFont(8f));
        }
    }

    public void dibujarPantallaTitulo(PantallaTitulo pantallaTitulo) {
        BufferStrategy buffer = getBufferStrategy();
        if (buffer == null) {
            createBufferStrategy(3);
            return;
        }

        final Graphics2D g = (Graphics2D) buffer.getDrawGraphics();

        // Dibujar la pantalla de título
        g.scale(2, 2);

        pantallaTitulo.dibujar(g);

        g.dispose();
        buffer.show();
    }

    public BufferedImage cargarImagen(String ruta) {
        try {
            return ImageIO.read(getClass().getResource(ruta));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void dibujarImagen(int x, int y, BufferedImage imagen) {
        if (imagen != null) {
            getGraphics().drawImage(imagen, x, y, this);
        }
    }

    public void actualizar() {
        raton.actualizar(this);
    }

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
