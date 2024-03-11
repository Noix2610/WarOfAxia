/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.mapas;

/**
 *
 * @author GAMER ARRAX
 */
public class CapaSprites extends CapaTiled{
    private int[] sprites;
    
    public CapaSprites(int ancho, int alto, int x, int y, int[] sprites) {
        super(ancho, alto, x, y);
        this.sprites = sprites;
    }

    public int[] getSprites() {
        return sprites;
    }

    public void setSprites(int[] sprites) {
        this.sprites = sprites;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
    
}
