/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.pantallaInicial;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import principal.Constantes;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.maquinaestado.EstadoJuego;
import principal.sprites.HojaSprites;

/**
 *
 * @author GAMER ARRAX
 */
public class PantallaTitulo implements EstadoJuego {

    private final BufferedImage titulo;
    private final HojaSprites ht;
    private final HojaSprites s1;
    private final HojaSprites s2;
    private BufferedImage start;
    private final Timer timer;

    public PantallaTitulo(SuperficieDibujo sd) {
        s1 = new HojaSprites("/fondos/Start1.png", 79, 46, false);
        s2 = new HojaSprites("/fondos/Start2.png", 79, 46, false);
        ht = new HojaSprites("/fondos/titulo.png", 639, 353, true);
        titulo = ht.getSprites(0).getImagen();
        start = s1.getSprites(0).getImagen();

        // Crear un temporizador que cambie la imagen cada 1000 ms (1 segundo)
        timer = new Timer(1000, e -> cambiarImagen());
        timer.start();
    }

    private void cambiarImagen() {
        // Determinar qué imagen mostrar basándose en la imagen actual
        if (start == s1.getSprites(0).getImagen()) {
            start = s2.getSprites(0).getImagen();
        } else {
            start = s1.getSprites(0).getImagen();
        }
    }

    @Override
    public void actualizar() {
        // No es necesario hacer nada en este método para este caso
    }

    @Override
    public void dibujar(Graphics2D g) {
        DibujoDebug.dibujarImagen(g, titulo, 0, 5);
        DibujoDebug.dibujarImagen(g, start, Constantes.ANCHO_JUEGO / 2 - 35, Constantes.ALTO_JUEGO / 2+20 );
    }
}
