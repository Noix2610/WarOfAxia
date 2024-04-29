/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal.control;

import java.awt.event.KeyEvent; // Importa la clase KeyEvent de awt.event
import java.awt.event.KeyListener; // Importa la clase KeyListener de awt.event
import principal.ElementosPrincipales; // Importa la clase ElementosPrincipales del paquete principal
import principal.GestorPrincipal; // Importa la clase GestorPrincipal del paquete principal
import principal.habilidades.GestorHabilidades; // Importa la clase GestorHabilidades del paquete principal.habilidades
import principal.habilidades.Habilidad;
import principal.maquinaestado.menujuego.MenuEquipo;

/**
 * Clase que maneja los eventos del teclado en el juego.
 */
public class Teclado implements KeyListener {

    // Teclas para controlar el movimiento y otras acciones
    public Tecla arriba = new Tecla();
    public Tecla abajo = new Tecla();
    public Tecla izquierda = new Tecla();
    public Tecla derecha = new Tecla();
    public Tecla aumentar = new Tecla();
    public Tecla disminuir = new Tecla();
    public Tecla enter = new Tecla();
    public GestorHabilidades gh = new GestorHabilidades();

    // Variables para controlar el estado del juego y otras acciones
    public boolean recogiendo = false;
    public boolean corriendo = false;
    public boolean debug = false;
    public boolean inventarioActivo = false;
    public boolean tiendaActiva = false;

    private int ultimaTeclaPulsada = KeyEvent.VK_UNDEFINED; // Última tecla pulsada

    // Teclas utilizadas en el juego
    private final int[] teclas = {KeyEvent.VK_ENTER, KeyEvent.VK_SPACE, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};

    // Array que almacena el estado de cada tecla
    private final boolean[] keyState = new boolean[teclas.length];

    // Método para manejar la tecla presionada
    public void teclaPresionada(KeyEvent e) {
        // Actualiza el estado de la tecla presionada
        for (int i = 0; i < teclas.length; i++) {
            if (e.getKeyCode() == teclas[i]) {
                keyState[i] = true;
            }
        }
    }

    // Método para manejar la tecla liberada
    public void teclaLiberada(KeyEvent e) {
        // Actualiza el estado de la tecla liberada
        for (int i = 0; i < teclas.length; i++) {
            if (e.getKeyCode() == teclas[i]) {
                keyState[i] = false;
            }
        }
    }

    // Método para verificar si alguna tecla está presionada
    public boolean hayTeclaPresionada() {
        // Verifica si alguna tecla está presionada
        for (boolean estado : keyState) {
            if (estado) {
                return true;
            }
        }
        return false;
    }

    // Método para manejar el evento de tecla presionada
    @Override
    public void keyPressed(KeyEvent e) {
        // Manejo de eventos para diferentes teclas
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (GestorPrincipal.pantallaTitulo) {
                    // Establece la bandera para iniciar el juego
                    GestorPrincipal.pantallaTitulo = false;
                }
                break;
            case KeyEvent.VK_W:
                arriba.teclaPulsada();
                break;
            case KeyEvent.VK_S:
                abajo.teclaPulsada();
                break;
            case KeyEvent.VK_A:
                izquierda.teclaPulsada();
                break;
            case KeyEvent.VK_D:
                derecha.teclaPulsada();
                break;
            case KeyEvent.VK_SHIFT:
                if (!ElementosPrincipales.jugador.isSobrepeso()) {
                    corriendo = true;
                }
                break;
            case KeyEvent.VK_UP:
                aumentar.teclaPulsada();
                break;
            case KeyEvent.VK_DOWN:
                disminuir.teclaPulsada();
                break;

            case KeyEvent.VK_F1:
                debug = !debug;
                break;
            case KeyEvent.VK_I:
                inventarioActivo = !inventarioActivo;

                break;
            case KeyEvent.VK_T:
                tiendaActiva = !tiendaActiva;
                break;
            case KeyEvent.VK_SPACE:
                ElementosPrincipales.jugador.atacando = true;
                break;
            case KeyEvent.VK_F12:
                MenuEquipo.mostrarTooltip = !MenuEquipo.mostrarTooltip;
                break;
            case KeyEvent.VK_1:
                GestorHabilidades.usarHabilidad(0);
                break;
            case KeyEvent.VK_2:
                GestorHabilidades.usarHabilidad(1);
                break;
            case KeyEvent.VK_F5:
                Habilidad habilidad = gh.obtenerHabilidadPorNombre("Curacion Basica");
                ElementosPrincipales.inventario.habilidades.add(habilidad);
                break;
        }
        ultimaTeclaPulsada = e.getKeyCode(); // Actualiza la última tecla pulsada
    }

    // Método para manejar el evento de tecla liberada
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                arriba.teclaLiberada();
                break;
            case KeyEvent.VK_S:
                abajo.teclaLiberada();
                break;
            case KeyEvent.VK_A:
                izquierda.teclaLiberada();
                break;
            case KeyEvent.VK_D:
                derecha.teclaLiberada();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_SHIFT:
                corriendo = false;
                break;
            case KeyEvent.VK_SPACE:
                ElementosPrincipales.jugador.atacando = false;
                break;
            case KeyEvent.VK_UP:
                aumentar.teclaLiberada();
                break;
            case KeyEvent.VK_DOWN:
                disminuir.teclaLiberada();
                break;

        }

    }

    // Método para manejar el evento de tecla tipeada
    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Método para obtener la última tecla pulsada
    public int getUltimaTeclaPulsada() {
        return ultimaTeclaPulsada;
    }

}
