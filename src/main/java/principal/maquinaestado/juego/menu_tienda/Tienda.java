/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.juego.menu_tienda;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import principal.inventario.Objeto;

/**
 *
 * @author GAMER ARRAX
 */
public class Tienda {
    private int idTienda;
    private Point posicion;
    private Rectangle areaTienda;
    private ArrayList<Objeto> objetosTienda;

    public Tienda(int idTienda, Point posicion) {
        this.idTienda = idTienda;
        this.posicion = posicion;
        this.objetosTienda = new ArrayList<>();
    }

    public Tienda() {
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public Rectangle getAreaTienda() {
        return areaTienda;
    }

    public void setAreaTienda(Rectangle areaTienda) {
        this.areaTienda = areaTienda;
    }

    public ArrayList<Objeto> getObjetosTienda() {
        return objetosTienda;
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }
    
    
    
    
    
    
    
    
    
    
}
