/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.dijkstra;

import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;

/**
 *
 * @author GAMER ARRAX
 */
public class Nodo {

    private Point posicion;
    private double distancia;

    public Nodo(Point posicion, double distancia) {
        this.posicion = posicion;
        this.distancia = distancia;
    }

    public Rectangle getAreaPixeles() {
        return new Rectangle(posicion.x * Constantes.LADO_SPRITE, posicion.y * Constantes.LADO_SPRITE,
                Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    public Rectangle getArea() {
        return new Rectangle(posicion.x, posicion.y,
                Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

}
