package principal.entes;

import principal.Constantes;
import principal.inventario.ContenedorObjetos;
import principal.sprites.HojaSprites;

/**
 * Clase que actúa como un registro para obtener instancias de enemigos basados en su ID.
 */
public class RegistroEnemigos {

    /**
     * Método estático para obtener un enemigo basado en su ID.
     *
     * @param idEnemigo El ID del enemigo que se desea obtener.
     * @return Una instancia de la clase Enemigo.
     */
    public static Enemigo obtenerEnemigo(final int idEnemigo) {

        // Se inicializa el enemigo como nulo
        Enemigo enemigo = null;
        // Se declara una hoja de sprites para cada enemigo
        HojaSprites hs;

        // Se utiliza un switch para determinar qué enemigo crear basándose en el idEnemigo
        switch (idEnemigo) {
            case 1:
                // Se crea un enemigo tipo "DEMON" con sus atributos específicos
                hs = new HojaSprites(Constantes.RUTA_DEMON, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "DEMON", 60, "Zombie", 20, hs,
                        400, new ContenedorObjetos(), 0);
                break;
            case 2:
                // Se crea un enemigo tipo "SKELETON WARRIOR" con sus atributos específicos
                hs = new HojaSprites(Constantes.RUTA_SKELETON_SWORD, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON WARRIOR", 60, "Zombie", 20,
                        hs, 400, new ContenedorObjetos(), 0);
                break;
            case 3:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_AXE, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON AXE WARRIOR", 60, "Zombie",
                        1, hs, 400, new ContenedorObjetos(), 0);

                break;
            case 4:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_BLODDY, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "BLOODY SKELETON", 60, "Zombie", 10,
                        hs, 400, new ContenedorObjetos(), 0);

                break;
            case 5:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_MAGE, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON MAGE", 60, "Zombie", 20, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 6:
                hs = new HojaSprites(Constantes.RUTA_SKELETON_MAGE2, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON \nHIGH MAGE", 60, "Zombie", 10, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 7:
                hs = new HojaSprites(Constantes.RUTA_ORC_ARCHER, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON AXE WARRIOR", 60, "Zombie",
                        10, hs, 400, new ContenedorObjetos(), 0);

                break;
            case 8:
                hs = new HojaSprites(Constantes.RUTA_ORC_AXE, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "BLOODY SKELETON", 60, "Zombie", 10,
                        hs, 400, new ContenedorObjetos(), 0);

                break;
            case 9:
                hs = new HojaSprites(Constantes.RUTA_ORC_KNIGHT, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON MAGE", 60, "Zombie", 10, hs,
                        400, new ContenedorObjetos(), 0);

                break;
            case 10:
                // Se crea un enemigo tipo "SKELETON HIGH MAGE" con sus atributos específicos
                hs = new HojaSprites(Constantes.RUTA_SATIRO, Constantes.LADO_SPRITE, false);
                enemigo = new Enemigo(idEnemigo, "SKELETON HIGH MAGE", 60, "Zombie", 10, hs,
                        400, new ContenedorObjetos(), 0);
                break;
        }
        // Se devuelve el enemigo creado
        return enemigo;
    }
}
