/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.sprites;

import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.herramientas.CargadorRecursos;

/**
 *
 * @author GAMER ARRAX
 */
public class HojaSprites {

    final private int anchoHojaPix;
    final private int altoHojaPix;

    final private int anchoHojaSprites;
    final private int altoHojaSprites;

    final private int anchoSprites;
    final private int altoSprites;

    final private Sprite[] sprites;

    public HojaSprites(final String ruta, final int tamanhoSprites, final boolean hojaOpaca) {
        final BufferedImage imagen;

        if (hojaOpaca) {
            imagen = CargadorRecursos.cargarImagenCompatibleOpaca(ruta);
        }
        else {
            imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(ruta);
        }

        anchoHojaPix = imagen.getWidth();
        altoHojaPix = imagen.getHeight();

        anchoHojaSprites = anchoHojaPix / tamanhoSprites;
        altoHojaSprites = altoHojaPix / tamanhoSprites;
        
        anchoSprites = tamanhoSprites;
        altoSprites = tamanhoSprites;

        sprites = new Sprite[anchoHojaSprites * altoHojaSprites];

        rellenarSpritesDesdeImagen(imagen);
    }

    public HojaSprites(final String ruta, final int anchoSprites, final int altoSprites, final boolean hojaOpaca) {
        final BufferedImage imagen;

        if (hojaOpaca) {
            imagen = CargadorRecursos.cargarImagenCompatibleOpaca(ruta);
        }
        else {
            imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(ruta);
        }

        anchoHojaPix = imagen.getWidth();
        altoHojaPix = imagen.getHeight();

        anchoHojaSprites = anchoHojaPix / anchoSprites;
        altoHojaSprites = altoHojaPix / altoSprites;

        this.anchoSprites = anchoSprites;
        this.altoSprites = altoSprites;

        sprites = new Sprite[anchoHojaSprites * altoHojaSprites];

        rellenarSpritesDesdeImagen(imagen);
    }

    private void rellenarSpritesDesdeImagen(final BufferedImage imagen) {
        for (int y = 0; y < altoHojaSprites; y++) {
            for (int x = 0; x < anchoHojaSprites; x++) {
                final int posicionX = x * anchoSprites;
                final int posicionY = y * altoSprites;
                sprites[x + y * anchoHojaSprites] = new Sprite(imagen
                        .getSubimage(posicionX, posicionY, anchoSprites, altoSprites), Constantes.LADO_SPRITE,
                        Constantes.LADO_SPRITE);
            }
        }
    }

    public Sprite getSprites(final int indice) {
        return sprites[indice];
    }

    public Sprite getSprites(final int x, final int y) {
        return sprites[x + y * anchoHojaSprites];
    }
}
