/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.armas;

import java.awt.Rectangle;
import java.util.ArrayList;
import principal.Constantes;
import principal.entes.Jugador;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class ArmaDosManos extends Arma {

    public ArmaDosManos(int id, String nombre, String descripcion, int peso, int ataqueMin, int ataqueMax, int alcanceFrontal,
            int alcanceLateral, final TipoObjeto tipoObjeto, boolean automatica, boolean penetrante, double ataquesXSegundo,
            final String rutaSonido, String rutaPersonaje,int precioCompra, int precioVenta) {
        super(id, nombre, descripcion, peso, ataqueMin, ataqueMax, alcanceFrontal, alcanceLateral, tipoObjeto, automatica, penetrante,
                ataquesXSegundo, rutaSonido, rutaPersonaje, precioCompra,  precioVenta);
        hojaArmas = new HojaSprites(Constantes.RUTA_HOJA_ARCOS, 32, false);
    }

    public ArrayList<Rectangle> getAlcance(final Jugador jugador) {
        final ArrayList<Rectangle> alcance = new ArrayList<>();

        final Rectangle alcance1 = new Rectangle();

        // 0 = abajo, 1 = izquierda, 2 = derecha, 3 = arriba
        if (jugador.getDireccion() == 3 || jugador.getDireccion() == 0) {
            alcance1.width = alcanceLateral* (Constantes.LADO_SPRITE / 4);
            alcance1.height = alcanceFrontal * Constantes.LADO_SPRITE;

            alcance1.x = Constantes.CENTRO_VENTANA_X-3;
            if (jugador.getDireccion() == 0) {
                alcance1.y = Constantes.CENTRO_VENTANA_Y - 9;
            }
            else {
                alcance1.y = Constantes.CENTRO_VENTANA_Y - 9 - alcance1.height;
            }

        }
        else {
            alcance1.height = alcanceLateral * (Constantes.LADO_SPRITE / 4);
            alcance1.width = alcanceFrontal * Constantes.LADO_SPRITE;

            alcance1.y = Constantes.CENTRO_VENTANA_Y -6;

            if (jugador.getDireccion() == 1) {
                alcance1.x = Constantes.CENTRO_VENTANA_X - alcance1.width;
            }
            else {
                alcance1.x = Constantes.CENTRO_VENTANA_X;
            }
        }

        alcance.add(alcance1);

        return alcance;
    }

    public Sprite getSprite() {
        try {
            return hojaArmas.getSprites(id - 500);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;  // O manejar de otra manera según tus necesidades
        }
    }

    public HojaSprites getHojaArmas() {
        return hojaArmas;
    }

    public int getAtaqueMin() {
        return ataqueMin;
    }

    public void setAtaqueMin(int ataqueMin) {
        this.ataqueMin = ataqueMin;
    }

    public int getAtaqueMax() {
        return ataqueMax;
    }

    public void setAtaqueMax(int ataqueMax) {
        this.ataqueMax = ataqueMax;
    }

    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
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

    public int getAlcanceInt() {
        return alcanceFrontal;
    }

}
