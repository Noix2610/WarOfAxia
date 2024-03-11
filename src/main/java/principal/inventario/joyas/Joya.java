/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.joyas;

import principal.Constantes;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;

/**
 *
 * @author GAMER ARRAX
 */
public abstract class Joya extends Objeto {

    public static HojaSprites hojaJoyas = new HojaSprites(Constantes.RUTA_HOJA_JOYAS, 32, false);
    public int defensaF;
    public int defensaM;
    public double resF;
    public double resM;
    public int atkF;
    public int atkM;
    public double crit;
    public double eva;

    public Joya(int id, int defensaF, int defensaM, double resF, double resM, int atkF, int atkM, double crit, double eva,
            String nombre, double peso, String descripcion, TipoObjeto tipoObjeto,int precioCompra, int precioVenta) {
        super(id, nombre, peso, descripcion, tipoObjeto, precioCompra,  precioVenta);
        this.defensaF = defensaF;
        this.defensaM = defensaM;
        this.resF = resF;
        this.resM = resM;
        this.atkF = atkF;
        this.atkM = atkM;
        this.crit = crit;
        this.eva = eva;
    }

    public static HojaSprites getHojaJoyas() {
        return hojaJoyas;
    }

    public static void setHojaJoyas(HojaSprites hojaJoyas) {
        Joya.hojaJoyas = hojaJoyas;
    }

    public int getDefensaF() {
        return defensaF;
    }

    public void setDefensaF(int defensaF) {
        this.defensaF = defensaF;
    }

    public int getDefensaM() {
        return defensaM;
    }

    public void setDefensaM(int defensaM) {
        this.defensaM = defensaM;
    }

    public double getResF() {
        return resF;
    }

    public void setResF(double resF) {
        this.resF = resF;
    }

    public double getResM() {
        return resM;
    }

    public void setResM(double resM) {
        this.resM = resM;
    }

    public int getAtkF() {
        return atkF;
    }

    public void setAtkF(int atkF) {
        this.atkF = atkF;
    }

    public int getAtkM() {
        return atkM;
    }

    public void setAtkM(int atkM) {
        this.atkM = atkM;
    }

    public double getCrit() {
        return crit;
    }

    public void setCrit(double crit) {
        this.crit = crit;
    }

    public double getEva() {
        return eva;
    }

    public void setEva(double eva) {
        this.eva = eva;
    }

    @Override
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

}
