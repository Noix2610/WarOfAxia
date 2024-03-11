/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.armaduras;

import principal.inventario.TipoObjeto;
import static principal.inventario.armaduras.Armadura.hojaArmaduras;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class ProteccionMedia extends Armadura {
    public ProteccionMedia(int id, String nombre, int defensaF, int defensaM, double peso, String descripcion, TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        super(id, nombre, defensaF, defensaM, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
    }

    public Sprite getSprite() {

        return hojaArmaduras.getSprites(id - 600);

    }

    public static HojaSprites getHojaArmas() {
        return hojaArmaduras;
    }

    public static void setHojaArmas(HojaSprites hojaArmaduras) {
        Armadura.hojaArmaduras = hojaArmaduras;
    }

    public int getDefensaF() {
        return defensaF;
    }

    public int getDefensaM() {
        return defensaM;
    }
    
    

    
    
    

}
