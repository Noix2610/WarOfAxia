/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.sprites;

import java.awt.image.BufferedImage;

/**
 *
 * @author GAMER ARRAX
 */
public class Sprite {
    private final BufferedImage imagen;
    
    private final int ancho;
    private final int alto;
    
    public Sprite(final BufferedImage imagen,int ancho, int alto){
        this.imagen = imagen;
        this.ancho = imagen.getWidth();
        this.alto = imagen.getHeight();
    }
    
    public BufferedImage getImagen(){
        return imagen;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}
