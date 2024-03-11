/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.armaduras;

import principal.Constantes;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public abstract class Armadura extends Objeto {

    public static HojaSprites hojaArmaduras = new HojaSprites(Constantes.RUTA_HOJA_ARMADURAS, 32, false);
    public int defensaF;
    public int defensaM;

    public Armadura(int id, String nombre, int defensaF, int defensaM, double peso, String descripcion, TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        super(id, nombre, peso, descripcion, tipoObjeto, precioCompra, precioVenta);
        this.defensaF = defensaF;
        this.defensaM = defensaM;
        this.peso = peso;

    }

    public Sprite getSprite() {

        return hojaArmaduras.getSprites(id - 600);

    }

    public static HojaSprites getHojaArmaduras() {
        return hojaArmaduras;
    }

    public static void setHojaArmaduras(HojaSprites hojaArmaduras) {
        Armadura.hojaArmaduras = hojaArmaduras;
    }

    public int getDefensaF() {
        if (this != null) {
            return defensaF;
        }
        else {
            return 0;
        }
    }

    public void setDefensaF(int defensaF) {
        this.defensaF = defensaF;
    }

    public int getDefensaM() {
        if (this != null) {
            return defensaM;
        }
        else {
            return 0;
        }
    }

    public void setDefensaM(int defensaM) {
        this.defensaM = defensaM;
    }

    @Override
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

}
