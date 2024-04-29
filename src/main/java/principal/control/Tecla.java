/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal.control;

/**
 * Clase que representa el estado de una tecla en el teclado.
 */
public class Tecla {

    private boolean pulsada = false; // Indica si la tecla está pulsada o no
    private long ultimaPulsacion = System.nanoTime(); // Tiempo de la última pulsación

    // Método para marcar que la tecla ha sido pulsada
    public void teclaPulsada() {
        pulsada = true; // Establece que la tecla está pulsada
        ultimaPulsacion = System.nanoTime(); // Actualiza el tiempo de la última pulsación
    }

    // Método para marcar que la tecla ha sido liberada
    public void teclaLiberada() {
        pulsada = false; // Establece que la tecla ha sido liberada
    }

    // Método para verificar si la tecla está pulsada
    public boolean estaPulsada() {
        return pulsada; // Devuelve el estado de la tecla
    }

    // Método para obtener el tiempo de la última pulsación
    public long getUltimaPulsacion() {
        return ultimaPulsacion; // Devuelve el tiempo de la última pulsación
    }
}
