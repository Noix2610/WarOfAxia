/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.joyas;

import principal.Constantes;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class Anillo extends Joya {

    public static HojaSprites hojaAnillos = new HojaSprites(Constantes.RUTA_HOJA_ANILLOS, 32, false);

    public Anillo(int id, int defensaF, int defensaM, double resF, double resM, int atkF, int atkM, double crit,
            double eva, String nombre, double peso, String descripcion, TipoObjeto tipoObjeto,int precioCompra, int precioVenta) {
        super(id, defensaF, defensaM, resF, resM, atkF, atkM, crit, eva, nombre, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
    }

    @Override
    public Sprite getSprite() {
        return hojaAnillos.getSprites(id - 732);
    }

}
