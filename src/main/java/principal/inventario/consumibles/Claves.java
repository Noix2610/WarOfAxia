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
public class Claves extends Objeto {

    public static HojaSprites hojaClaves = new HojaSprites(Constantes.RUTA_HOJA_OBJETOS, 32, false);

    public Claves(int id, String nombre, int peso, String descripcion, TipoObjeto tipoObjeto,int precioCompra, int precioVenta) {
        super(id, nombre, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
    }

    public void setSprite(HojaSprites hojaClaves) {
        Claves.hojaClaves = hojaClaves;
    }

    @Override
    public Sprite getSprite() {
        return hojaClaves.getSprites(id);
    }

}
