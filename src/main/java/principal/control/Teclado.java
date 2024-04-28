/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.habilidades.GestorHabilidades;
import principal.habilidades.Habilidad;
import principal.maquinaestado.menujuego.MenuEquipo;

/**
 *
 * @author GAMER ARRAX
 */
public class Teclado implements KeyListener {

    public Tecla arriba = new Tecla();
    public Tecla abajo = new Tecla();
    public Tecla izquierda = new Tecla();
    public Tecla derecha = new Tecla();
    public Tecla aumentar = new Tecla();
    public Tecla disminuir = new Tecla();
    public Tecla enter = new Tecla();
    public GestorHabilidades  gh = new GestorHabilidades();

    public boolean recogiendo = false;
    public boolean corriendo = false;
    public boolean debug = false;
    public boolean inventarioActivo = false;
    public boolean tiendaActiva = false;

    private int ultimaTeclaPulsada = KeyEvent.VK_UNDEFINED;

    private final int[] teclas = {KeyEvent.VK_ENTER, KeyEvent.VK_SPACE, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};

    // Array que almacena el estado de cada tecla
    private final boolean[] keyState = new boolean[teclas.length];

    public void teclaPresionada(KeyEvent e) {
        // Actualiza el estado de la tecla presionada
        for (int i = 0; i < teclas.length; i++) {
            if (e.getKeyCode() == teclas[i]) {
                keyState[i] = true;
            }
        }
    }

    public void teclaLiberada(KeyEvent e) {
        // Actualiza el estado de la tecla liberada
        for (int i = 0; i < teclas.length; i++) {
            if (e.getKeyCode() == teclas[i]) {
                keyState[i] = false;
            }
        }
    }

    public boolean hayTeclaPresionada() {
        // Verifica si alguna tecla está presionada
        for (boolean estado : keyState) {
            if (estado) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if(GestorPrincipal.pantallaTitulo){
                    GestorPrincipal.pantallaTitulo = false;
                }else if(!GestorPrincipal.pantallaTitulo && !ElementosPrincipales.jugador.estaVivo){
                    GestorPrincipal.pantallaTitulo = true;
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
        ultimaTeclaPulsada = e.getKeyCode();
    }

    @Override
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public int getUltimaTeclaPulsada() {
        return ultimaTeclaPulsada;
    }

}
