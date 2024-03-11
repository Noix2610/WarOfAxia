/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario;

import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;

/**
 *
 * @author GAMER ARRAX
 */
public class ObjetoUnicoTiled {

    private Point posicion;
    private Objeto objeto;
    private int cantidad;
    private Rectangle area;

    public ObjetoUnicoTiled(Point posicion, Objeto objeto, int cantidad) {
        this.posicion = posicion;
        this.objeto = objeto;
        this.cantidad = cantidad;
        
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Rectangle getArea() {
        return area;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

}
