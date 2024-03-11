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
public class ProteccionLateral extends Armadura{
    public static HojaSprites hojaArmaduraLateral = new HojaSprites(Constantes.RUTA_HOJA_GUANTE, 32, false);
    public ProteccionLateral(int id, String nombre, int defensaF, int defensaM, double peso, String descripcion, TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        super(id, nombre, defensaF, defensaM, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
    }
    
    @Override
    public Sprite getSprite() {

        return hojaArmaduraLateral.getSprites(id - 632);

    }

    public static HojaSprites getHojaArmas() {
        return hojaArmaduraLateral;
    }

    public static void setHojaArmas(HojaSprites hojaArmaduras) {
        Armadura.hojaArmaduras = hojaArmaduras;
    }

    @Override
    public int getDefensaF() {
        return defensaF;
    }

    @Override
    public int getDefensaM() {
        return defensaM;
    }
}
