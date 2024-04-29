/*
 * Esta clase representa un objeto genérico en el juego.
 * Contiene atributos como ID, nombre, descripción, cantidad, tipo de objeto, entre otros.
 * Proporciona métodos para gestionar la cantidad de objetos, obtener su sprite y sus atributos.
 */
package principal.inventario;

import java.awt.Rectangle;
import principal.sprites.Sprite;

public abstract class Objeto {

    // Atributos comunes a todos los objetos
    protected final int id; // Identificador único del objeto
    protected final String descripcion; // Descripción del objeto
    protected int cantidad; // Cantidad de este objeto en posesión
    protected int cantidadMaxima; // Cantidad máxima permitida
    public double peso; // Peso del objeto
    protected final String nombre; // Nombre del objeto
    protected TipoObjeto tipoObjeto; // Tipo de objeto

    // Rectángulos para posicionar el objeto en diferentes lugares del juego
    protected Rectangle posicionMenu;
    protected Rectangle posicionFlotante;
    protected Rectangle posicionMochila;
    protected Rectangle posicionTienda;
    protected Rectangle posicionVenta;
    protected Rectangle posicionCompra;
    protected int cantidadCompra; // Cantidad a comprar en la tienda
    protected int cantidadVenta; // Cantidad a vender en la tienda
    protected int precioCompra; // Precio de compra en la tienda
    protected int precioVenta; // Precio de venta en la tienda

    // Constructor con parámetros
    public Objeto(final int id, final String nombre, double peso, final String descripcion, final TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoObjeto = tipoObjeto;
        this.peso = peso;
        this.cantidadMaxima = 99; // Cantidad máxima por defecto

        // Inicialización de rectángulos y valores relacionados con la tienda
        posicionMenu = new Rectangle();
        posicionFlotante = new Rectangle();
        posicionTienda = new Rectangle();
        posicionMochila = new Rectangle();
        posicionVenta = new Rectangle();
        posicionCompra = new Rectangle();
        cantidadCompra = 0;
        cantidadVenta = 0;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    // Constructor adicional con cantidad inicial
    public Objeto(final int id, final String nombre, double peso, final String descripcion, final int cantidad, final TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        this(id, nombre, peso, descripcion, tipoObjeto, precioCompra, precioVenta);
        if (cantidad <= cantidadMaxima) {
            this.cantidad = cantidad;
            this.cantidadMaxima = 99; // Cantidad máxima por defecto
            this.peso = peso;
            this.precioCompra = precioCompra;
            this.precioVenta = precioVenta;
        }
    }

    // Método para incrementar la cantidad de objetos
    public boolean incrementarCantidad(final int incremento) {
        boolean incrementado = false;

        if (cantidad + incremento <= cantidadMaxima) {
            cantidad += incremento;
            incrementado = true;
        }

        return incrementado;
    }

    // Método para reducir la cantidad de objetos
    public boolean reducirCantidad(final int reduccion) {
        boolean reducido = false;
        if (cantidad - reduccion >= 0) {
            cantidad -= reduccion;
            reducido = true;
        }
        return reducido;
    }

    // Método abstracto para obtener el sprite del objeto
    public abstract Sprite getSprite();

    // Métodos getter y setter para diversos atributos del objeto
    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCantidadMaxima(int cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }

    public int getCantidadMaxima() {
        return cantidadMaxima;
    }

    public Rectangle getPosicionMenu() {
        return posicionMenu;
    }

    public Rectangle getPosicionFlotante() {
        return posicionFlotante;
    }

    public void setPosicionMenu(final Rectangle posicionMenu) {
        this.posicionMenu = posicionMenu;
    }

    public void setPosicionFlotante(final Rectangle posicionFlotante) {
        this.posicionFlotante = posicionFlotante;
    }

    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public double getPeso() {
        return peso;
    }

    public int getCantidadCompra() {
        return cantidadCompra;
    }

    public void setCantidadCompra(int cantidadCompra) {
        this.cantidadCompra = cantidadCompra;
    }

    public int getCantidadVenta() {
        return cantidadVenta;
    }

    public void setCantidadVenta(int cantidadVenta) {
        this.cantidadVenta = cantidadVenta;
    }

    public Rectangle getPosicionMochila() {
        return posicionMochila;
    }

    public void setPosicionMochila(Rectangle posicionMochila) {
        this.posicionMochila = posicionMochila;
    }

    public Rectangle getPosicionTienda() {
        return posicionTienda;
    }

    public void setPosicionTienda(Rectangle posicionTienda) {
        this.posicionTienda = posicionTienda;
    }

    public Rectangle getPosicionVenta() {
        return posicionVenta;
    }

    public void setPosicionVenta(Rectangle posicionVenta) {
        this.posicionVenta = posicionVenta;
    }

    public Rectangle getPosicionCompra() {
        return posicionCompra;
    }

    public void setPosicionCompra(Rectangle posicionCompra) {
        this.posicionCompra = posicionCompra;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public int getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(int precioVenta) {
        this.precioVenta = precioVenta;
    }

}
