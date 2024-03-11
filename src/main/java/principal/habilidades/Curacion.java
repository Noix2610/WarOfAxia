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
public class Curacion extends Habilidad {

    private final int cantidadCuracionBase;
    private final double montoAdicionalPorInteligencia;

    public Curacion(String nombre, int duracion, int tiempoReutilizacion,
            Object objetivo, int manaUtilizado, int vidaUtilizada,
            int cantidadCuracionBase, int montoAdicionalPorInteligencia, int indiceSprite, TipoObjeto tipoHabilidad) {
        super(nombre,duracion, tiempoReutilizacion, objetivo, manaUtilizado, vidaUtilizada, indiceSprite, tipoHabilidad);
        this.cantidadCuracionBase = cantidadCuracionBase;
        this.montoAdicionalPorInteligencia = montoAdicionalPorInteligencia;
        super.setDescripcion("Restaura 30 pts de VIT + \nun adicional basado en\nla INT del conjurador") ;
        

    }

    @Override
    public void aplicarEfecto(Object object) {
        // Verificar si ha pasado el tiempo de reutilización desde el último uso
        if (cronometro.obtenerTiempoTranscurrido() / 1000 >= getTiempoReutilizacion()) {
            if (object instanceof EntidadCurable entidadCurable) {

                if (entidadCurable.getVidaActual() < entidadCurable.getVidaMaxima() && entidadCurable.getMana() >= getManaUtilizado()) {
                    ElementosPrincipales.jugador.getCronometro().reiniciar();
                    //ElementosPrincipales.jugador.dibujarHabilidad = true;
                    // Calcular la cantidad total de curación (base + adicional por inteligencia)
                    int cantidadTotalCuracion = cantidadCuracionBase + calcularMontoAdicionalPorInteligencia(entidadCurable);
                    super.setMontoTotal(cantidadTotalCuracion);

                    entidadCurable.curarVida(cantidadTotalCuracion);

                    entidadCurable.setMana(entidadCurable.getMana() - getManaUtilizado());
                    setTiempoReutilizacion(15);
                    
                    cronometro.reiniciar();
                }

            }

        }

    }

    private int calcularMontoAdicionalPorInteligencia(EntidadCurable entidadCurable) {

        return (int) (entidadCurable.getInteligencia() * montoAdicionalPorInteligencia);
    }
    
    

}
