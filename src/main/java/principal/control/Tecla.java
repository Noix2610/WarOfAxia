/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.control;

/**
 *
 * @author GAMER ARRAX
 */
public class Tecla {

    private boolean pulsada = false;
    private long ultimaPulsacion = System.nanoTime();

    public void teclaPulsada() {
        pulsada = true;
        ultimaPulsacion = System.nanoTime();
    }

    public void teclaLiberada() {
        pulsada = false;
    }

    public boolean estaPulsada() {
        return pulsada;
    }

    public long getUltimaPulsacion() {
        return ultimaPulsacion;
    }
}
