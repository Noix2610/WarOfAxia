/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.mapas;

import java.awt.Rectangle;

/**
 *
 * @author GAMER ARRAX
 */
public class CapaTransparencias extends CapaTiled{
    private Rectangle[] colisionables;

    public CapaTransparencias(int ancho, int alto, int x, int y, Rectangle[] colisionables) {
        super(ancho, alto, x, y);
        this.colisionables = colisionables;
    }

    public Rectangle[] getColisionables() {
        return colisionables;
    }

    public void setColisionables(Rectangle[] colisionables) {
        this.colisionables = colisionables;
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
