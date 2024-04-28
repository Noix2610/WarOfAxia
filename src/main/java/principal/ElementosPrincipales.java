/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import principal.entes.Jugador;
import principal.inventario.Inventario;
import principal.mapas.MapaTiled;
import principal.mapas.MapaTiled2;

/**
 *
 * @author GAMER ARRAX
 */
public class ElementosPrincipales {
    

    //public static MapaTiled mapa = new MapaTiled(Constantes.RUTA_MAPA_TILED);
    public static MapaTiled2 mapa = new MapaTiled2(Constantes.RUTA_MAPA_TILED);
    public static Jugador jugador = new Jugador();
    public static Inventario inventario = new Inventario();
    
    

}
