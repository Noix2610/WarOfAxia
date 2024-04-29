/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado;

import java.awt.Graphics2D;
import principal.graficos.SuperficieDibujo;
import principal.maquinaestado.juego.GestorJuego;
import principal.maquinaestado.juego.menu_tienda.GestorTienda;
import principal.maquinaestado.menujuego.GestorMenu;
import principal.pantallaInicial.PantallaTitulo;

/**
 *
 * @author GAMER ARRAX
 */
public class GestorEstados {
    private EstadoJuego[] estados;
    private EstadoJuego estadoActual;
    
    public GestorEstados(final SuperficieDibujo sd){
        iniciarEstados();
        iniciarEstadoActual();
    }

    private void iniciarEstados() {
        estados = new EstadoJuego[3];
        estados[0] = new GestorJuego();
        estados[1] = new GestorMenu();
        estados[2] = new GestorTienda();
        
       
        //Añadir e iniciar los demas estados a medida que los creemos
    }

    private void iniciarEstadoActual() {
        estadoActual = estados[0];
        
    }
    
    public void actualizar(){
        estadoActual.actualizar();
    }
    
    public void dibujar(final Graphics2D g){
        estadoActual.dibujar(g);
    }
    
    public void cambiarEstadoActual(final int nuevoEstado){
        estadoActual = estados[nuevoEstado];
    }
    
    public EstadoJuego getEstadoActual(){
        return estadoActual;
    }
}
