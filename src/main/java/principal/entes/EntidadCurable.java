/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package principal.entes;

import principal.inventario.TipoObjeto;

/**
 * Interfaz que define el comportamiento de una entidad que puede ser curada en el juego. Las entidades curables son
 * aquellas que tienen una cantidad de vida y pueden ser curadas o recibir daño.
 *
 * @author GAMER ARRAX
 */
public interface EntidadCurable {

    /**
     * Retorna la cantidad máxima de puntos de vida que puede tener la entidad.
     *
     * @return La cantidad máxima de puntos de vida.
     */
    int getVidaMaxima();

    /**
     * Retorna la cantidad actual de puntos de vida de la entidad.
     *
     * @return La cantidad actual de puntos de vida.
     */
    int getVidaActual();

    /**
     * Establece la cantidad actual de puntos de vida de la entidad.
     *
     * @param vidaActual La nueva cantidad actual de puntos de vida.
     */
    void setVidaActual(int vidaActual);

    /**
     * Retorna la cantidad de puntos de maná de la entidad.
     *
     * @return La cantidad de puntos de maná.
     */
    int getMana();

    /**
     * Establece la cantidad de puntos de maná de la entidad.
     *
     * @param mana La nueva cantidad de puntos de maná.
     */
    void setMana(int mana);

    /**
     * Retorna el nivel de inteligencia de la entidad.
     *
     * @return El nivel de inteligencia.
     */
    int getInteligencia();

    /**
     * Incrementa la cantidad actual de puntos de vida de la entidad en función del monto de curación proporcionado.
     *
     * @param montoCuracion El monto de curación a aplicar.
     */
    void curarVida(int montoCuracion);

    /**
     * Reduce la cantidad actual de puntos de vida de la entidad en función del daño recibido y del tipo de habilidad
     * que lo causó.
     *
     * @param danho El daño recibido.
     * @param tipoDeHabilidad El tipo de habilidad que causó el daño.
     */
    void recibirDanho(int danho, TipoObjeto tipoDeHabilidad);
}
