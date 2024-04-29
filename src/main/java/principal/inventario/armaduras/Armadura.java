/*
 * Esta clase abstracta representa una armadura en el juego.
 * Proporciona métodos y atributos comunes a todas las armaduras.
 */
package principal.inventario.armaduras;

import principal.Constantes;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

public abstract class Armadura extends Objeto {

    // Hoja de sprites que contiene las imágenes de las armaduras
    public static HojaSprites hojaArmaduras = new HojaSprites(Constantes.RUTA_HOJA_ARMADURAS, 32, false);

    // Atributos de la armadura
    public int defensaF; // Defensa física
    public int defensaM; // Defensa mágica

    // Constructor de la clase Armadura
    public Armadura(int id, String nombre, int defensaF, int defensaM, double peso, String descripcion, TipoObjeto tipoObjeto,
            int precioCompra, int precioVenta) {
        super(id, nombre, peso, descripcion, tipoObjeto, precioCompra, precioVenta);
        this.defensaF = defensaF;
        this.defensaM = defensaM;
        this.peso = peso;
    }

    // Método abstracto para obtener el sprite de la armadura
    public abstract Sprite getSprite();

    // Métodos getter y setter para los atributos de la armadura
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

    // Método getter para el peso de la armadura
    @Override
    public double getPeso() {
        return peso;
    }

    // Método setter para el peso de la armadura
    public void setPeso(double peso) {
        this.peso = peso;
    }

    // Método estático getter para obtener la hoja de sprites de las armaduras
    public static HojaSprites getHojaArmaduras() {
        return hojaArmaduras;
    }

    // Método estático setter para establecer la hoja de sprites de las armaduras
    public static void setHojaArmaduras(HojaSprites hojaArmaduras) {
        Armadura.hojaArmaduras = hojaArmaduras;
    }
}
