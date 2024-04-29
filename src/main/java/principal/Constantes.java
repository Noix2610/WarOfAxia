/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.awt.Color;
import java.awt.Font;
import principal.herramientas.CargadorRecursos;

/**
 *
 * @author GAMER ARRAX
 */
public class Constantes {

    public static final int LADO_SPRITE = 32;

    public static int ANCHO_JUEGO = 1280;
    public static int ALTO_JUEGO = 720;

    public static int LADO_CURSOR = 0;

    public static int ANCHO_PANTALLA_COMPLETA = 1280;
    public static int ALTO_PANTALLA_COMPLETA = 720;

    public static double FACTOR_ESCALADO_X = (double) (ANCHO_PANTALLA_COMPLETA / ANCHO_JUEGO);
    public static double FACTOR_ESCALADO_Y = (double) (ALTO_PANTALLA_COMPLETA / ALTO_JUEGO);

    public static int CENTRO_VENTANA_X = ANCHO_JUEGO / 2;
    public static int CENTRO_VENTANA_Y = ALTO_JUEGO / 2;
    
    public static int MARGEN_X = ANCHO_JUEGO /2 - LADO_SPRITE / 2;
    public static int MARGEN_Y = ALTO_JUEGO /2 - LADO_SPRITE / 2;

    public final static Color COLOR_NARANJA = new Color(0xff6700);
    
    public static int MAX_OBJETOS_POR_HOJA = 52;
    public static String RUTA_MAPA_TILED = "textos/ciudad1.json";
    public static String RUTA_MAPA = "textos/prueba";
    public static String RUTA_MAPA2 = "textos/mapa3";
    public static String RUTA_RATON = "/icons/Cursor.png";
    public static String RUTA_LOGO = "/icons/logo3.png";
    public static String RUTA_SKELETON_SWORD = "/personajes/enemigos/2.png";
    public static String RUTA_DEMON = "/personajes/enemigos/1.png";
    public static String RUTA_SKELETON_AXE = "/personajes/enemigos/skeletonAxeShield.png";
    public static String RUTA_SKELETON_BLODDY = "/personajes/enemigos/skeletonBloddyDagger.png";
    public static String RUTA_SKELETON_MAGE = "/personajes/enemigos/skeletonMage1.png";
    public static String RUTA_SKELETON_MAGE2 = "/personajes/enemigos/skeletonMage2.png";
    public static String RUTA_ORC_KNIGHT = "/personajes/enemigos/OrcKnight.png";
    public static String RUTA_ORC_ARCHER = "/personajes/enemigos/OrcArcher.png";
    public static String RUTA_ORC_AXE = "/personajes/enemigos/AxeOrc.png";
    public static String RUTA_SATIRO = "/personajes/enemigos/Satiro.png";
    
    public static String RUTA_PERSONAJE = "/personajes/personajesJugables/pj2.png";
    public static String RUTA_PERSONAJE_TRANSPARENTE = "/personajes/personajesJugables/pj2T.png";
    public static String RUTA_ARMA_ARCO = "/personajes/personajesJugables/PjBow.png";
    public static String RUTA_ARMA_ESPADA = "/personajes/personajesJugables/PjSword.png";
    //public static String RUTA_ICONO_VENTANA = "/icons/icono.png";
    public static String RUTA_HOJA_OBJETOS = "/hojasObjetos/objetosConsumibles/consumibles1.png";
    public static String RUTA_HOJA_ARCOS = "/hojasObjetos/armas/Arcos.png";
    public static String RUTA_HOJA_ESPADAS = "/hojasObjetos/armas/Espadas.png";
    public static String RUTA_HOJA_HACHAS = "/hojasObjetos/armas/Hachas.png";
    public static String RUTA_HOJA_MAZOS = "/hojasObjetos/armas/Mazos.png";
    public static String RUTA_HOJA_ARMADURAS = "/hojasObjetos/armaduras/Armaduras.png";
    public static String RUTA_HOJA_JOYAS = "/hojasObjetos/joyas/Collares.png";
    public static String RUTA_HOJA_ACCESORIOS = "/hojasObjetos/joyas/Accesorios.png";
    public static String RUTA_HOJA_ANILLOS = "/hojasObjetos/joyas/Anillos.png";
    public static String RUTA_HOJA_CASCO = "/hojasObjetos/armaduras/Cascos.png";
    public static String RUTA_HOJA_GUANTE = "/hojasObjetos/armaduras/Guantes.png";
    public static String RUTA_HOJA_BOTA = "/hojasObjetos/armaduras/Botas.png";
    public static String RUTA_HOJA_HABILIDADES = "/habilidades/skills.png";
    public static String RUTA_HOJA_CONTENEDORES = "/hojasObjetos/armaduras/cofre1.png";
    
    //public static String RUTA_HOJA_CONTENEDORES = "/hojasObjetos/individuales/cofre1.png";

    public static final Font FUENTE_POR_DEFECTO = CargadorRecursos.cargarFuente("fuentes/EXEPixelPerfectNoVenta.ttf", 12f);

}
