/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.consumibles;

import principal.Constantes;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class Consumible extends Objeto {

    public static HojaSprites hojaConsumibles = new HojaSprites(Constantes.RUTA_HOJA_OBJETOS, 32, false);

    public Consumible(int id, String nombre, int peso, String descripcion, final TipoObjeto tipoObjeto,int precioCompra, int precioVenta) {
        super(id, nombre, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
    }

    public Consumible(int id, String nombre, int peso, String descripcion, int cantidad, final TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        super(id, nombre, peso, descripcion, cantidad, tipoObjeto, precioCompra,  precioVenta);
    }

    public Sprite getSprite() {
        return hojaConsumibles.getSprites(id);
    }

    public static HojaSprites getHojaConsumibles() {
        return hojaConsumibles;
    }

    public static void setHojaConsumibles(HojaSprites hojaConsumibles) {
        Consumible.hojaConsumibles = hojaConsumibles;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadMaxima() {
        return cantidadMaxima;
    }

    public void setCantidadMaxima(int cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }

    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

}
