/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import principal.Constantes;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.sprites.HojaSprites;

/**
 *
 * @author GAMER ARRAX
 */
public class ContenedorObjetos {

    private  Point  posicion;
    private ArrayList<Objeto> objetos;
    private int indiceSprite;
    private HojaSprites hs;
    private BufferedImage imagen;
    private Rectangle area;

    public ContenedorObjetos() {
        this.objetos = new ArrayList<>();
    }

    public ContenedorObjetos(Point posicion, int indiceSprite, Rectangle area) {
        hs = new HojaSprites(Constantes.RUTA_HOJA_CONTENEDORES, 32, false);
        this.posicion = posicion;
        this.indiceSprite = indiceSprite;
        this.imagen = hs.getSprites(indiceSprite).getImagen();
        this.objetos = new ArrayList<>();
        this.area = area;
        
    }

    public void dibujar(final Graphics g, final int puntoX,final int puntoY) {
        DibujoDebug.dibujarImagen(g, imagen, puntoX, puntoY);
    }

    public Point getPosicion() {
        return posicion;
    }

    public ArrayList<Objeto> getObjetos() {
        return objetos;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public void setObjetos(Objeto objeto) {
        this.objetos.add(objeto);
    }

    public int getIndiceSprite() {
        return indiceSprite;
    }

    public void setIndiceSprite(int indiceSprite) {
        this.indiceSprite = indiceSprite;
    }

    public HojaSprites getHs() {
        return hs;
    }

    public void setHs(HojaSprites hs) {
        this.hs = hs;
    }

    public BufferedImage getImagen() {
        return imagen;
    }

    public void setImagen(BufferedImage imagen) {
        this.imagen = imagen;
    }

    public Rectangle getArea() {
        return area;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }
    
    
    
    
    
    
    
    

}
