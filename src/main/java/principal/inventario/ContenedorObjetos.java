/*
 * Esta clase representa un contenedor de objetos en el juego.
 * Puede contener varios objetos y se dibuja en una posición determinada en la pantalla.
 */
package principal.inventario;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import principal.Constantes;
import principal.herramientas.DibujoDebug;
import principal.sprites.HojaSprites;

public class ContenedorObjetos {

    private Point posicion; // Posición del contenedor en la pantalla
    private ArrayList<Objeto> objetos; // Lista de objetos contenidos en el contenedor
    private int indiceSprite; // Índice del sprite que representa el contenedor
    private HojaSprites hs; // Hoja de sprites que contiene las imágenes de los contenedores
    private BufferedImage imagen; // Imagen del contenedor
    private Rectangle area; // Área del contenedor en la pantalla

    // Constructor por defecto
    public ContenedorObjetos() {
        this.objetos = new ArrayList<>();
    }

    // Constructor con parámetros
    public ContenedorObjetos(Point posicion, int indiceSprite, Rectangle area) {
        // Carga de la hoja de sprites y configuración de parámetros iniciales
        hs = new HojaSprites(Constantes.RUTA_HOJA_CONTENEDORES, 32, false);
        this.posicion = posicion;
        this.indiceSprite = indiceSprite;
        this.imagen = hs.getSprites(indiceSprite).getImagen();
        this.objetos = new ArrayList<>();
        this.area = area;
    }

    // Método para dibujar el contenedor en la pantalla
    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        DibujoDebug.dibujarImagen(g, imagen, puntoX, puntoY);
    }

    // Getters y setters para los atributos de la clase
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
