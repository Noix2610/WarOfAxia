/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import principal.entes.Jugador; // Importa la clase Jugador del paquete principal.entes
import principal.inventario.Inventario; // Importa la clase Inventario del paquete principal.inventario
import principal.mapas.MapaTiled; // Importa la clase MapaTiled del paquete principal.mapas
import principal.mapas.MapaTiled2; // Importa la clase MapaTiled2 del paquete principal.mapas

/**
 * Clase que contiene las instancias principales de los elementos del juego.
 */
public class ElementosPrincipales {

    // Instancias principales del juego
    //public static MapaTiled mapa = new MapaTiled(Constantes.RUTA_MAPA_TILED);
    // La instancia de MapaTiled está comentada, se utiliza MapaTiled2 en su lugar
    public static MapaTiled2 mapa = new MapaTiled2(Constantes.RUTA_MAPA_TILED); // Instancia del mapa del juego
    public static Jugador jugador = new Jugador(); // Instancia del jugador del juego
    public static Inventario inventario = new Inventario(); // Instancia del inventario del juego

}
