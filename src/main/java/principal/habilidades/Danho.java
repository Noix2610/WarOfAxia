/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.habilidades;

import principal.ElementosPrincipales;
import principal.entes.EntidadCurable;
import principal.inventario.TipoObjeto;

/**
 *
 * @author GAMER ARRAX
 */
public class Danho extends Habilidad {

    private final double montoAdicionalPorInteligencia;
    private int danhoBase;

    public Danho(String nombre, int duracion, int tiempoReutilizacion, Object objetivo, int manaUtilizado, int vidaUtilizada,
            double montoAdicionalPorInt, int danhoBase, int indiceSprite, TipoObjeto activaPasiva, TipoObjeto tipoHabilidad) {
        super(nombre, duracion, objetivo, manaUtilizado, vidaUtilizada, indiceSprite, activaPasiva, tipoHabilidad);
        
        montoAdicionalPorInteligencia =montoAdicionalPorInt;
        this.danhoBase = danhoBase;
    }

    /*public void aplicarEfecto(Object object, TipoObjeto tipoHabilidad) {
        if(ElementosPrincipales.jugador.getArea()))
        danhar(object, tipoHabilidad);
    }*/

    private void danhar(Object object, TipoObjeto tipoHabilidad) {
        
        if (cronometro.obtenerTiempoTranscurrido() / 1000 >= getTiempoReutilizacion()) {
            if (object instanceof EntidadCurable) {
                EntidadCurable entidadCurable = (EntidadCurable) object;

                if (entidadCurable.getVidaActual() < entidadCurable.getVidaMaxima() && entidadCurable.getMana() >= getManaUtilizado()) {
                    ElementosPrincipales.jugador.getCronometro().reiniciar();
                    //ElementosPrincipales.jugador.dibujarHabilidad = true;
                    // Calcular la cantidad total de curación (base + adicional por inteligencia)
                    int cantidadTotalDanho = danhoBase + calcularMontoAdicionalPorInteligencia(entidadCurable);
                    super.setMontoTotal(cantidadTotalDanho);

                    entidadCurable.recibirDanho(cantidadTotalDanho,tipoHabilidad);

                    ElementosPrincipales.jugador.setMana(ElementosPrincipales.jugador.getMana() - getManaUtilizado());
                    setTiempoReutilizacion(super.getTiempoReutilizacion());

                    cronometro.reiniciar();
                }

            }

        }
    }

    private int calcularMontoAdicionalPorInteligencia(EntidadCurable entidadCurable) {

        return (int) (entidadCurable.getInteligencia() * montoAdicionalPorInteligencia);
    }

    @Override
    public void aplicarEfecto(Object object, TipoObjeto tipoHabilidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
