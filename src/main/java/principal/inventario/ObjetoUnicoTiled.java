/*
 * Esta clase representa un objeto único en una ubicación específica del mapa.
 * Contiene atributos como la posición, el objeto en sí y la cantidad del objeto.
 */
package principal.inventario;

import java.awt.Point;
import java.awt.Rectangle;

public class ObjetoUnicoTiled {

    // Atributos de la clase
    private Point posicion; // Posición del objeto en el mapa
    private Objeto objeto; // El objeto en sí
    private int cantidad; // Cantidad del objeto en esta ubicación
    private Rectangle area; // Área del objeto en el mapa

    // Constructor con parámetros
    public ObjetoUnicoTiled(Point posicion, Objeto objeto, int cantidad) {
        this.posicion = posicion;
        this.objeto = objeto;
        this.cantidad = cantidad;
    }

    // Métodos getter y setter para los atributos
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
