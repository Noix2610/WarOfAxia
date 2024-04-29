/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal;

import principal.control.GestorControles; // Importa la clase GestorControles del paquete principal.control
import principal.graficos.SuperficieDibujo; // Importa la clase SuperficieDibujo del paquete principal.graficos
import principal.graficos.Ventana; // Importa la clase Ventana del paquete principal.graficos
import principal.maquinaestado.GestorEstados; // Importa la clase GestorEstados del paquete principal.maquinaestado
import principal.sonido.SoundThread; // Importa la clase SoundThread del paquete principal.sonido

/**
 * Clase que gestiona el funcionamiento principal del juego.
 */
public class GestorPrincipal {

    private boolean enFuncionamiento = false; // Variable que indica si el juego está en funcionamiento
    private String titulo; // Título de la ventana del juego
    private int ancho; // Ancho de la ventana del juego
    private int alto; // Alto de la ventana del juego
    public static boolean pantallaTitulo = true; // Variable que indica si se muestra la pantalla de título

    // Instancias principales del juego
    public static SuperficieDibujo sd; // Superficie de dibujo del juego
    private Ventana ventana; // Ventana del juego
    private GestorEstados ge; // Gestor de estados del juego

    public static SoundThread musica = new SoundThread("Ala Flair"); // Reproductor de música del juego

    private static int fps = 0; // FPS (cuadros por segundo) del juego
    private static int aps = 0; // APS (actualizaciones por segundo) del juego

    // Constructor privado para evitar instanciación externa
    private GestorPrincipal(final String titulo, final int ancho, final int alto) {
        this.titulo = titulo;
        this.alto = alto;
        this.ancho = ancho;
    }

    // Método principal del programa
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("sun.java2d.opengl", "true"); // Configuración de OpenGL
        GestorPrincipal gp = new GestorPrincipal("Juego", Constantes.ANCHO_PANTALLA_COMPLETA,
                Constantes.ALTO_PANTALLA_COMPLETA); // Creación de una instancia del gestor principal

        gp.iniciarJuego(); // Inicio del juego
        gp.iniciarBuclePrincipal(); // Inicio del bucle principal del juego
    }

    // Método para iniciar el juego
    private void iniciarJuego() {
        enFuncionamiento = true; // Establece que el juego está en funcionamiento
        inicializar(); // Inicializa los componentes del juego
        musica.repetir(0.7f); // Reproduce la música del juego
    }

    // Método para inicializar los componentes del juego
    private void inicializar() {
        sd = new SuperficieDibujo(ancho, alto); // Inicializa la superficie de dibujo
        ventana = new Ventana(titulo, sd); // Inicializa la ventana del juego
        ge = new GestorEstados(sd); // Inicializa el gestor de estados del juego
    }

    // Método para iniciar el bucle principal del juego
    private void iniciarBuclePrincipal() throws InterruptedException {
        // Variables para el control del bucle principal
        int actualizacionesAcumuladas = 0;
        int framesAcumulados = 0;

        final int NS_POR_SEGUNDO = 1000000000;
        final int APS_OBJETIVO = 60;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

        long referenciaActualizacion = System.nanoTime();
        long referenciaContador = System.nanoTime();

        double tiempoTranscurrido;
        double delta = 0;

        // Bucle principal del juego
        while (enFuncionamiento) {
            final long inicioBucle = System.nanoTime();

            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;

            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

            while (delta >= 1) {
                actualizar(); // Actualiza el estado del juego
                actualizacionesAcumuladas++;
                delta--;
            }
            dibujar(); // Dibuja el estado del juego
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

    // Método para actualizar el estado del juego
    private void actualizar() throws InterruptedException {
        // Cambia el estado del juego según la interacción del jugador
        if (GestorControles.teclado.inventarioActivo) {
            ge.cambiarEstadoActual(1);
            GestorControles.teclado.tiendaActiva = false;
        }
        else if (GestorControles.teclado.tiendaActiva) {
            ge.cambiarEstadoActual(2);
            GestorControles.teclado.inventarioActivo = false;
        }
        else {
            ge.cambiarEstadoActual(0);
        }
        // Actualiza el estado del juego si no se muestra la pantalla de título
        if (!pantallaTitulo) {
            ge.actualizar();
            sd.actualizar();
        }
    }

    // Método para dibujar el estado del juego
    private void dibujar() {
        sd.dibujar(ge);
    }

    // Método para obtener los FPS del juego
    public static int getFps() {
        return fps;
    }

    // Método para obtener los APS del juego
    public static int getAps() {
        return aps;
    }

}
