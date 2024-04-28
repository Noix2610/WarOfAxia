/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.habilidades;

import principal.ElementosPrincipales;
import principal.entes.EntidadCurable;
import principal.inventario.TipoObjeto;
import principal.sonido.SoundThread;

/**
 *
 * @author GAMER ARRAX
 */
public class Curacion extends Habilidad {

    private final int cantidadCuracionBase;
    private final double montoAdicionalPorInteligencia;
    private final SoundThread sonido;
    private int tiempoCarga;

    public Curacion(String nombre, int duracion, int tiempoCarga,
            Object objetivo, int manaUtilizado, int vidaUtilizada,
            int cantidadCuracionBase, int montoAdicionalPorInteligencia, int indiceSprite, TipoObjeto activaPasiva,
            TipoObjeto tipoHabilidad) {
        super(nombre, duracion, objetivo, manaUtilizado, vidaUtilizada, indiceSprite, activaPasiva, tipoHabilidad);
        this.cantidadCuracionBase = cantidadCuracionBase;
        this.montoAdicionalPorInteligencia = montoAdicionalPorInteligencia;
        super.setTiempoReutilizacion(0);
        this.tiempoCarga = tiempoCarga;
        super.setDescripcion("Restaura 30 pts de VIT + \nun adicional basado en\nla INT del conjurador");
        sonido = new SoundThread("Heal");

    }

    @Override
    public void aplicarEfecto(Object object, TipoObjeto tipoCuracion) {
        curacionAutomatica(object);

    }

    private void curacionAutomatica(Object object) {
        if (cronometro.obtenerTiempoTranscurrido() / 1000 >= getTiempoReutilizacion()) {
            if (object instanceof EntidadCurable) {
                EntidadCurable entidadCurable = (EntidadCurable) object;

                if (entidadCurable.getVidaActual() < entidadCurable.getVidaMaxima() && entidadCurable.getMana() >= getManaUtilizado()) {
                    ElementosPrincipales.jugador.getCronometro().reiniciar();
                    //ElementosPrincipales.jugador.dibujarHabilidad = true;
                    // Calcular la cantidad total de curación (base + adicional por inteligencia)
                    int cantidadTotalCuracion = cantidadCuracionBase + calcularMontoAdicionalPorInteligencia(entidadCurable);
                    super.setMontoTotal(cantidadTotalCuracion);

                    entidadCurable.curarVida(cantidadTotalCuracion);

                    entidadCurable.setMana(entidadCurable.getMana() - getManaUtilizado());
                    super.setTiempoReutilizacion(tiempoCarga);
                    sonido.reproducir(0.7f);
                    cronometro.reiniciar();
                }

            }

        }
    }

    private int calcularMontoAdicionalPorInteligencia(EntidadCurable entidadCurable) {

        return (int) (entidadCurable.getInteligencia() * montoAdicionalPorInteligencia);
    }
    
    

}
