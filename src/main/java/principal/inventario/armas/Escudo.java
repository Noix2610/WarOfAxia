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

/**
 *
 * @author GAMER ARRAX
 */
public class Escudo extends Arma {
    private int defensa;
    public Escudo(int id, String nombre, String descripcion, int peso, int ataqueMin, int ataqueMax, int alcanceFrontal,
            int alcanceLateral, TipoObjeto tipoObjeto, boolean automatica, boolean penetrante, double ataquesXSegundo,
            String rutaDisparo, String rutaPersonaje, int precioCompra, int precioVenta, int defensa) {
        super(id, nombre, descripcion, peso, ataqueMin, ataqueMax, alcanceFrontal, alcanceLateral, tipoObjeto,
                automatica, penetrante, ataquesXSegundo, rutaDisparo, rutaPersonaje, precioCompra, precioVenta);
        this.defensa = defensa;
    }

    @Override
    public ArrayList<Rectangle> getAlcance(Jugador jugador) {
        final ArrayList<Rectangle> alcance = new ArrayList<>();

        final Rectangle alcance1 = new Rectangle();

        // 0 = abajo, 1 = izquierda, 2 = derecha, 3 = arriba
        if (jugador.getDireccion() == 3 || jugador.getDireccion() == 0) {
            alcance1.width = alcanceLateral * (Constantes.LADO_SPRITE / 4);
            alcance1.height = alcanceFrontal * Constantes.LADO_SPRITE;

            alcance1.x = Constantes.CENTRO_VENTANA_X - 3;
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

            alcance1.y = Constantes.CENTRO_VENTANA_Y - 6;

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

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    

}
