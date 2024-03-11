/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.armaduras;

import principal.Constantes;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class ProteccionBaja extends Armadura{
    public static HojaSprites hojaArmaduraBaja = new HojaSprites(Constantes.RUTA_HOJA_BOTA, 32, false);
    public ProteccionBaja(int id, String nombre, int defensaF, int defensaM, double peso, String descripcion, TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        super(id, nombre, defensaF, defensaM, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
    }
    
    @Override
    public Sprite getSprite() {

        return hojaArmaduraBaja.getSprites(id - 648);
    }

}
