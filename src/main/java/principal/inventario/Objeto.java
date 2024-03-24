/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario;

import java.awt.Rectangle;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public abstract class Objeto {

    protected final int id;
    protected final String descripcion;
    protected int cantidad;
    protected int cantidadMaxima;
    public double peso;
    protected final String nombre;
    protected TipoObjeto tipoObjeto;

    protected Rectangle posicionMenu;
    protected Rectangle posicionFlotante;
    protected Rectangle posicionMochila;
    protected Rectangle posicionTienda;
    protected Rectangle posicionVenta;
    protected Rectangle posicionCompra;
    protected int cantidadCompra;
    protected int cantidadVenta;
    protected int precioCompra;
    protected int precioVenta;

    public Objeto(final int id, final String nombre, double peso, final String descripcion, final TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoObjeto = tipoObjeto;
        this.peso = peso;
        //this.sprite = hojaObjetos.getSprites(id);
        this.cantidadMaxima = 99;

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

    public Objeto(final int id, final String nombre, double peso, final String descripcion, final int cantidad, final TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        this(id, nombre, peso, descripcion, tipoObjeto, precioCompra, precioVenta);
        if (cantidad <= cantidadMaxima) {
            this.cantidad = cantidad;
            this.cantidadMaxima = 99;
            this.peso = peso;
            this.precioCompra = precioCompra;
            this.precioVenta = precioVenta;
        }
    }

    public boolean incrementarCantidad(final int incremento) {
        boolean incrementado = false;

        if (cantidad + incremento <= cantidadMaxima) {
            cantidad += incremento;
            incrementado = true;
        }

        return incrementado;
    }

    public boolean reducirCantidad(final int reduccion) {

        boolean reducido = false;
        if (cantidad - reduccion >= 0) {
            cantidad -= reduccion;
            reducido = true;
        }
        return reducido;
    }

    public abstract Sprite getSprite();

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
