/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package principal.entes;

/**
 *
 * @author GAMER ARRAX
 */
public interface EntidadCurable {
    int getVidaMaxima();
    int getVidaActual();
    void setVidaActual(int vidaActual);
    int getMana();
    void setMana(int mana);
    int getInteligencia();
    void curarVida(int montoCuracion);
}

