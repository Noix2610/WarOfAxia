/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.juego;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.EfectosVisuales;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.interfaz_usuario.MenuInferior;
import principal.mapas.MapaTiled2;
import principal.mapas.Salida;
import principal.maquinaestado.EstadoJuego;

/**
 *
 * @author GAMER ARRAX
 */
public class GestorJuego implements EstadoJuego {

    BufferedImage logo;
    MenuInferior menuInferior;
    EfectosVisuales ev;
    public static boolean recargar;

    public GestorJuego() {
        menuInferior = new MenuInferior();
        logo = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.RUTA_LOGO);
        this.ev = new EfectosVisuales();
        recargar = false;
    }

    @Override
    public void actualizar() {
        revisarZonaSalida();
        ElementosPrincipales.mapa.actualizar();
        ElementosPrincipales.jugador.actualizar();
        recargarJuego();
    }

    /*private void revisarZonaSalida() {
        if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida1)) {

            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(0);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
            }
            recargarJuego();

        }

        else if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida2)) {
            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(1);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
            }
            recargarJuego();
        }
        else if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida3)) {
            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(2);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
            }
            recargarJuego();
        }
        else if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida4)) {
            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(3);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
            }
            recargarJuego();
        }
        else if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida5)) {
            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(4);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
            }
            recargarJuego();
        }
        else if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida6)) {
            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(5);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
            }
            recargarJuego();
        }
        else if (ElementosPrincipales.jugador.getLIMITE_ARRIBA().intersects(ElementosPrincipales.mapa.zonaSalida7)) {
            List<Salida> salidas = Salida.getSalidas();
            if (!salidas.isEmpty()) {
                Salida salidaActual = salidas.get(6);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());

            }
            recargarJuego();
        }
    }*/
    public void recargarJuego() {
        if (recargar) {
            ElementosPrincipales.mapa = new MapaTiled2("textos/" + ElementosPrincipales.mapa.getSiguienteMapa());

            // Establecer la posición del jugador en el nuevo mapa
            ElementosPrincipales.jugador.setPosicionX(ElementosPrincipales.mapa.getPuntoInicial().x);
            ElementosPrincipales.jugador.setPosicionY(ElementosPrincipales.mapa.getPuntoInicial().y);
            recargar = false;
        }

    }

    @Override
    public void dibujar(Graphics2D g) {

        ElementosPrincipales.mapa.dibujar(g);
        ElementosPrincipales.jugador.dibujar(g);
        ElementosPrincipales.mapa.dibujar2daCapa(g);
        menuInferior.dibujar(g);
        DibujoDebug.dibujarImagen(g, logo, Constantes.ANCHO_JUEGO - logo.getWidth(), 0);
        if (ElementosPrincipales.jugador.dibujarHabilidad) {
            ElementosPrincipales.jugador.dibujarHabilidad(g);
        }
        //g.fillRect((int) ElementosPrincipales.mapa.getZonaSalida().getX(), (int) ElementosPrincipales.mapa.getZonaSalida().getY(), (int) ElementosPrincipales.mapa.getZonaSalida().getWidth(), (int) ElementosPrincipales.mapa.getZonaSalida().getHeight());
    }

    private void revisarZonaSalida() {
        List<Salida> salidas = Salida.getSalidas();

        for (int i = 0; i < salidas.size(); i++) {
            Rectangle zonaSalida = ElementosPrincipales.mapa.zonasSalida.get(i);

            if (ElementosPrincipales.jugador.getArea().intersects(zonaSalida)) {
                Salida salidaActual = salidas.get(i);

                Salida.puntoInicialSiguiente = salidaActual.getPuntoInicioSiguienteMapa();
                ElementosPrincipales.mapa.setSiguienteMapa(salidaActual.getNombreSiguienteMapa());
                GestorPrincipal.sd.cambioMapa = true;
                recargar = true;

                break;  // Salir del bucle una vez que se ha detectado la intersección
            }
        }
    }

}
