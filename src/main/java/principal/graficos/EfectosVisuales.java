/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import principal.Constantes;
import principal.herramientas.DibujoDebug;

/**
 *
 * @author GAMER ARRAX
 */
public class EfectosVisuales {

    Timer time;

    private static final int DURACION_TRANSICION_SEGUNDOS = 4;
    private static final int FPS = 60;  // Cuadros por segundo
    private static final int INTERVALO_REPINTADO = 2000 / FPS;

    private int intensidadNegro = 0;
    private long tiempoInicioTransicion = 0;

    public EfectosVisuales() {

    }

    private void iniciarTransicionNegro() {
        Timer timer = new Timer(INTERVALO_REPINTADO, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (intensidadNegro < 255) {
                    intensidadNegro += (255 * INTERVALO_REPINTADO) / (DURACION_TRANSICION_SEGUNDOS * 2000);
                }
            }
        });
        timer.start();
    }

    public void dibujarTransicionNegro(Graphics g, int ancho, int alto) {
        iniciarTransicionNegro();
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioTransicion;

        if (tiempoTranscurrido < DURACION_TRANSICION_SEGUNDOS * 1000) {
            intensidadNegro = (int) (255 * tiempoTranscurrido / (DURACION_TRANSICION_SEGUNDOS * 1000));
        }

        // Dibujar fondo negro con la intensidad actual
        Color colorNegro = new Color(0, 0, 0, intensidadNegro);

        // Ajusta el tamaño del paso según sea necesario
        int paso = 10;

        // Dibujar el negro línea por línea
        for (int y = 0; y < alto; y += paso) {
            g.setColor(colorNegro);
            g.fillRect(0, y, ancho, paso);
        }
    }

    public void dibujarTransicionBlancoNegro(Graphics g, int ancho, int alto) {
        iniciarTransicionNegro();
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioTransicion;

        if (tiempoTranscurrido < DURACION_TRANSICION_SEGUNDOS * 1000) {
            intensidadNegro = 255 - (int) (255 * tiempoTranscurrido / (DURACION_TRANSICION_SEGUNDOS * 1000));
        }

        // Dibujar fondo blanco a negro con la intensidad actual
        Color colorBlancoNegro = new Color(255, 255, 255, intensidadNegro);

        // Ajustar el tamaño del paso según sea necesario
        int paso = 10;

        // Dibujar el blanco a negro línea por línea
        for (int y = 0; y < alto; y += paso) {
            g.setColor(colorBlancoNegro);
            g.fillRect(0, y, ancho, paso);
        }
    }

    public void dibujarTransicionTransparenteNegro(Graphics g, int ancho, int alto) {
        iniciarTransicionNegro();
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioTransicion;

        if (tiempoTranscurrido < DURACION_TRANSICION_SEGUNDOS * 1000) {
            int intensidadNegro = (int) (255 * tiempoTranscurrido / (DURACION_TRANSICION_SEGUNDOS * 1000));
            // Usar el canal alfa para la transparencia
            Color colorTransparenteNegro = new Color(0, 0, 0, 255 - intensidadNegro);

            // Ajustar el tamaño del paso según sea necesario
            int paso = 10;

            // Dibujar el transparente a negro línea por línea
            for (int y = 0; y < alto; y += paso) {
                g.setColor(colorTransparenteNegro);
                g.fillRect(0, y, ancho, paso);
            }
        }
    }

    public void pintarLineaNegra(Graphics2D g, int ancho, int alto) {
        Color color = Color.black;
        for (int y = 0; y < alto; y += 32) {
            for (int x = 0; x < ancho; x += 32) {
                DibujoDebug.dibujarRectanguloRelleno(g, new Rectangle(x, y, 32, 32), color);
            }
        }
    }

    public void dibujarDegradadoNegro(Graphics2D g) {
        int ancho = Constantes.ANCHO_JUEGO;
        int alto = Constantes.ALTO_JUEGO;

        for (int i = 255; i >= 0; i -= 5) {
            g.setColor(new Color(0, 0, 0, i)); // Color negro con transparencia variable
            g.fillRect(ancho, alto, -ancho, -alto); // Dibuja un rectángulo desde la parte inferior derecha hasta la parte superior izquierda
            ancho -= 5; // Reduce el ancho para crear el efecto de degradado
            alto -= 5; // Reduce el alto para crear el efecto de degradado
        }
    }

    public void dibujarDegradadoBlanco(Graphics2D g) {
        int ancho = Constantes.ANCHO_JUEGO;
        int alto = Constantes.ALTO_JUEGO;

        for (int i = 0; i <= 255; i += 5) {
            g.setColor(new Color(255, 255, 255, i)); // Color blanco con transparencia variable
            g.fillRect(ancho, alto, -ancho, -alto); // Dibuja un rectángulo desde la parte inferior derecha hasta la parte superior izquierda
            ancho -= 5; // Reduce el ancho para crear el efecto de degradado
            alto -= 5; // Reduce el alto para crear el efecto de degradado
        }
    }
}
