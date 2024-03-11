/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.habilidades;

import java.util.ArrayList;
import java.util.List;
import principal.ElementosPrincipales;
import principal.inventario.TipoObjeto;

/**
 *
 * @author GAMER ARRAX
 */
public class GestorHabilidades {

    private List<Habilidad> habilidades;

    public GestorHabilidades() {
        habilidades = new ArrayList<>();
        inicializarHabilidades();  // Llena la lista con las habilidades disponibles
    }

    private void inicializarHabilidades() {
        habilidades.add(crearCuracion("Curacion Basica", 1, 10,
                ElementosPrincipales.jugador, 10, 0, 30, 1, 0,
                TipoObjeto.ACTIVA));
        habilidades.add(crearCuracion("Curacion Media", 1, 15,
                ElementosPrincipales.jugador, 15, 0, 40, 1, 1,
                TipoObjeto.ACTIVA));
        habilidades.add(crearCuracion("Curacion Avanzada", 1, 30,
                ElementosPrincipales.jugador, 30, 0, 50, 2, 2,
                TipoObjeto.ACTIVA));
        // Agrega más habilidades según sea necesario
    }

    private Curacion crearCuracion(String nombre, int duracion, int tiempoReutilizacion,
            Object objetivo, int manaUtilizado, int vidaUtilizada,
            int cantidadCuracionBase, int montoAdicionalPorInteligencia, int indiceSprite, TipoObjeto tipoHabilidad) {
        return new Curacion(nombre, duracion, tiempoReutilizacion, objetivo, manaUtilizado, vidaUtilizada,
                cantidadCuracionBase, montoAdicionalPorInteligencia, indiceSprite, tipoHabilidad);
    }

    // Agrega otros métodos para crear diferentes tipos de habilidades si es necesario
    public Habilidad obtenerHabilidadPorNombre(String nombreHabilidad) {
        for (Habilidad habilidad : habilidades) {
            if (habilidad.getNombre().equals(nombreHabilidad)) {
                return habilidad;
            }
        }
        return null;  // Devuelve null si no se encuentra la habilidad con el nombre dado
    }

    public long getTiempoReutilizacionHabilidad(String nombreHabilidad) {
        Habilidad habilidad = obtenerHabilidadPorNombre(nombreHabilidad);
        return (habilidad != null) ? habilidad.getTiempoReutilizacion() : 0L;
    }

    public static void usarHabilidad(int indice) {
        if (ElementosPrincipales.jugador.getAr().getAccesoEquipado(indice) instanceof Habilidad) {
            Habilidad habilidad = (Habilidad)ElementosPrincipales.jugador.getAr().getAccesoEquipado(indice);
                habilidad.aplicarEfecto(habilidad.getObjetivo());
                
        }
    }
}
