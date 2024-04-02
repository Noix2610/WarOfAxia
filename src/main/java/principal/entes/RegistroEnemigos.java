/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.entes;

import principal.Constantes;
import principal.inventario.ContenedorObjetos;
import principal.sprites.HojaSprites;

/**
 *
 * @author GAMER ARRAX
 */
public class RegistroEnemigos {

    public static Enemigo obtenerEnemigo(final int idEnemigo) {

        Enemigo enemigo = null;
        HojaSprites hs;
        switch (idEnemigo) {
            case 1:
                hs = new HojaSprites(Constantes.RUTA_DEMON, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "DEMON", 60, "sonidos/Zombie.wav", 1, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 2:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_SWORD, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON WARRIOR", 60, "sonidos/Zombie.wav", 1,
                        hs, 400, new ContenedorObjetos(), 0);

                break;
            case 3:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_AXE, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON AXE WARRIOR", 60, "sonidos/Zombie.wav",
                        1, hs, 400, new ContenedorObjetos(), 0);

                break;
            case 4:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_BLODDY, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "BLOODY SKELETON", 60, "sonidos/Zombie.wav", 1,
                        hs, 400, new ContenedorObjetos(), 0);

                break;
            case 5:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_MAGE, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON MAGE", 60, "sonidos/Zombie.wav", 1, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 6:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_MAGE2, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON \nHIGH MAGE", 60, "sonidos/Zombie.wav", 1, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 7:
                hs = new HojaSprites(Constantes.RUTA_ORC_ARCHER, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON AXE WARRIOR", 60, "sonidos/Zombie.wav",
                        1, hs, 400, new ContenedorObjetos(), 0);

                break;
            case 8:
                hs = new HojaSprites(Constantes.RUTA_ORC_AXE, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "BLOODY SKELETON", 60, "sonidos/Zombie.wav", 1,
                        hs, 400, new ContenedorObjetos(), 0);

                break;
            case 9:
                hs = new HojaSprites(Constantes.RUTA_ORC_KNIGHT, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON MAGE", 60, "sonidos/Zombie.wav", 1, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 10:
                hs = new HojaSprites(Constantes.RUTA_SATIRO, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON \nHIGH MAGE", 60, "sonidos/Zombie.wav", 1, hs,
                        400, new ContenedorObjetos(), 0);

                break;

        }
        return enemigo;
    }
}
