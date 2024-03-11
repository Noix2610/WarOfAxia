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
                enemigo = new Enemigo(idEnemigo, "Demon", 60, "/sonidos/Zombie.wav", 10, hs, 400,
                        new ContenedorObjetos(),0);
                break;
            case 2:
                hs = new HojaSprites(Constantes.RUTA_SKELETON, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "Skeleton", 60, "/sonidos/Zombie.wav", 10, hs,
                        400, new ContenedorObjetos(),0);
                break;

        }
        return enemigo;
    }
}
