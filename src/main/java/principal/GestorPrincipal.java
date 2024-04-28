/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal;

import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.graficos.Ventana;
import principal.maquinaestado.GestorEstados;
import principal.sonido.SoundThread;

/**
 *
 * @author GAMER ARRAX
 */
public class GestorPrincipal {

    private boolean enFuncionamiento = false;
    private String titulo;
    private int ancho;
    private int alto;
    public static boolean pantallaTitulo = true;

    public static SuperficieDibujo sd;
    private Ventana ventana;
    private GestorEstados ge;

    public static SoundThread sonido = new SoundThread("Ala Flair");

    private static int fps = 0;
    private static int aps = 0;

    private GestorPrincipal(final String titulo, final int ancho, final int alto) {
        this.titulo = titulo;
        this.alto = alto;
        this.ancho = ancho;
    }

    public static void main(String[] args) throws InterruptedException {
        
        System.setProperty("sun.java2d.opengl", "true");
        GestorPrincipal gp = new GestorPrincipal("Juego", Constantes.ANCHO_PANTALLA_COMPLETA,
                Constantes.ALTO_PANTALLA_COMPLETA);

        gp.iniciarJuego();
        gp.iniciarBuclePrincipal();
    }

    private void iniciarJuego() {
        
        enFuncionamiento = true;
        inicializar();
        sonido.repetir(0.7f);
    }

    private void inicializar() {
        sd = new SuperficieDibujo(ancho, alto);
        ventana = new Ventana(titulo, sd);
        ge = new GestorEstados(sd);
        
    }

    private void iniciarBuclePrincipal() throws InterruptedException {
        int actualizacionesAcumuladas = 0;
        int framesAcumulados = 0;

        final int NS_POR_SEGUNDO = 1000000000;
        final int APS_OBJETIVO = 60;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

        long referenciaActualizacion = System.nanoTime();
        long referenciaContador = System.nanoTime();

        double tiempoTranscurrido;
        double delta = 0;

        while (enFuncionamiento) {
            final long inicioBucle = System.nanoTime();

            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;

            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

            while (delta >= 1) {
                actualizar();
                actualizacionesAcumuladas++;
                delta--;
            }
            dibujar();
            framesAcumulados++;

            if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {
                fps = framesAcumulados;
                aps = actualizacionesAcumuladas;

                actualizacionesAcumuladas = 0;
                framesAcumulados = 0;
                referenciaContador = System.nanoTime();
            }
        }
    }

    private void actualizar() throws InterruptedException {
        if(pantallaTitulo){
            ge.cambiarEstadoActual(3);
        }
        else if (GestorControles.teclado.inventarioActivo) {
            ge.cambiarEstadoActual(1);
            GestorControles.teclado.tiendaActiva = false;
        }
        else if(GestorControles.teclado.tiendaActiva){
            ge.cambiarEstadoActual(2);
            GestorControles.teclado.inventarioActivo = false;
        }else{
            ge.cambiarEstadoActual(0);
        }
        ge.actualizar();
        sd.actualizar();
    }

    private void dibujar() {
        sd.dibujar(ge);
    }

    public static int getFps() {
        return fps;
    }

    public static int getAps() {
        return aps;
    }

}
