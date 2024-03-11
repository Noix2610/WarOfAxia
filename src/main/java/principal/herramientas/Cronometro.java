/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

/**
 *
 * @author GAMER ARRAX
 */
public class Cronometro {

    private long tiempoInicio;
    private long tiempoTranscurrido;
    private int tiempoRestante;

    public Cronometro() {
        iniciar();
    }

    public void iniciar() {
        tiempoInicio = System.currentTimeMillis();
    }

    public long obtenerTiempoTranscurrido() {
        return System.currentTimeMillis() - tiempoInicio;
    }

    public void reiniciar() {
        tiempoInicio = System.currentTimeMillis();
    }

    public void actualizar() {
        tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
    }

    public int getTiempoRestante(int tiempoReutilizacion) {
        tiempoRestante = (int) (tiempoReutilizacion - obtenerTiempoTranscurrido() / 1000);
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempo) {
        tiempoRestante = tiempo;
    }

    public int getTiempoTranscurrido() {
        actualizar();
        return (int) tiempoTranscurrido / 1000;
    }
    
    public long getTiempoTranscurridoMili() {
    actualizar(); // Supongo que este método actualiza el tiempo transcurrido desde un momento inicial
    return tiempoTranscurrido;
}

}
