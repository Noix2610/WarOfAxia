/*
 * Esta clase proporciona un método estático para obtener objetos del juego según su identificador.
 * Cada identificador de objeto está asociado a un objeto específico del juego.
 */
package principal.inventario;

import principal.Constantes;
import principal.inventario.armaduras.ProteccionAlta;
import principal.inventario.armaduras.ProteccionBaja;
import principal.inventario.armaduras.ProteccionLateral;
import principal.inventario.armaduras.ProteccionMedia;
import principal.inventario.armas.ArmaDosManos;
import principal.inventario.armas.ArmaUnaMano;
import principal.inventario.armas.SinArma;
import principal.inventario.consumibles.Claves;
import principal.inventario.consumibles.Consumible;
import principal.inventario.joyas.Accesorio;
import principal.inventario.joyas.Anillo;
import principal.inventario.joyas.Collar;

public class RegistroObjetos {

    // Método estático para obtener un objeto según su identificador
    public static Objeto obtenerObjeto(final int idObjeto) {
        Objeto objeto = null;
        switch (idObjeto) {
            case 0:
                objeto = new Consumible(idObjeto, "MORA", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 1:
                objeto = new Consumible(idObjeto, "GUINDA", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 2:
                objeto = new Consumible(idObjeto, "MANZANA VERDE", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 3:
                objeto = new Consumible(idObjeto, "MANZANA ROJA", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 4:
                objeto = new Consumible(idObjeto, "NARANJA", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 5:
                objeto = new Claves(idObjeto, "UVAS", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 6:
                objeto = new Consumible(idObjeto, "RABANO", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 7:
                objeto = new Consumible(idObjeto, "ZANAHORIA", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 8:
                objeto = new Consumible(idObjeto, "PIMENTON VERDE", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 9:
                objeto = new Consumible(idObjeto, "PIMENTON NARANJA", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;
            case 10:
                objeto = new Consumible(idObjeto, "PIMENTON ROJO", 1, "", TipoObjeto.CONSUMIBLE,
                        1, 1);
                break;

            case 400:
                objeto = new ArmaUnaMano(idObjeto, "BUSCADORA DE JUSTICIA", "", 2, 7, 8,
                        1, 1, TipoObjeto.ESPADA_LIGERA, true, false,
                        0.7, "sword_clash.1", Constantes.RUTA_ARMA_ESPADA, 1550, 780);
                break;
            case 401:
                objeto = new ArmaUnaMano(idObjeto, "ESPADA DE ACERO NEGRO", "", 4, 10, 15,
                        2, 2, TipoObjeto.ESPADA_PESADA, false, true,
                        1.3, "sword_clash.1", Constantes.RUTA_ARMA_ESPADA, 1550, 600);
                break;
            case 402:
                objeto = new ArmaUnaMano(idObjeto, "ESPADA CORRUPTA", "", 2, 9,
                        12, 1, 1, TipoObjeto.ESPADA_MEDIA,
                        true, false, 0.8, "sword_clash.1",
                        Constantes.RUTA_ARMA_ESPADA, 1550, 750);
                break;
            case 403:
                objeto = new ArmaUnaMano(idObjeto, "OXIDIANA", "", 1, 5, 6,
                        1, 1, TipoObjeto.ESPADA_LIGERA, true, false, 0.9,
                        "sword_clash.1", Constantes.RUTA_ARMA_ESPADA, 1550, 400);
                break;
            case 404:
                objeto = new ArmaUnaMano(idObjeto, "OBSIDIANA", "", 2, 10, 12,
                        1, 1, TipoObjeto.ESPADA_LIGERA, true, false,
                        0.8, "sword_clash.1", Constantes.RUTA_ARMA_ESPADA, 1550, 900);
                break;
            case 405:
                objeto = new ArmaUnaMano(idObjeto, "KOBRA", "", 1, 7, 12,
                        1, 1, TipoObjeto.ESPADA_LIGERA, true, false,
                        0.8, "sword_clash.1", Constantes.RUTA_ARMA_ESPADA, 1550, 900);
                break;
            case 406:
                objeto = new ArmaUnaMano(idObjeto, "ARCO DE NELISS", "", 2, 7, 14,
                        4, 2, TipoObjeto.ARCO, false, true,
                        1.6, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 407:
                objeto = new ArmaUnaMano(idObjeto, "AURORA CELESTIAL", "", 3, 9,
                        12, 3, 1, TipoObjeto.ARCO, false, true,
                        1.1, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 408:
                objeto = new ArmaUnaMano(idObjeto, "SAGITARIO ESTELAR", "", 2, 9,
                        14, 3, 1, TipoObjeto.ARCO, false, false,
                        1.2, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 409:
                objeto = new ArmaUnaMano(idObjeto, "CAZADOR", "", 3, 8, 10,
                        3, 1, TipoObjeto.ARCO, false, false,
                        1.3, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 410:
                objeto = new ArmaDosManos(idObjeto, "SUSURRO DEL VIENTO", "", 2, 10,
                        13, 3, 1, TipoObjeto.ARCO, false, true,
                        1.5, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 411:
                objeto = new ArmaUnaMano(idObjeto, "DESTELLO COSMICO", "", 2, 10,
                        14, 4, 1, TipoObjeto.ARCO, false, true,
                        1.1, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 412:
                objeto = new ArmaUnaMano(idObjeto, "LAGRIMA DE LUNA", "", 3, 11,
                        13, 3, 2, TipoObjeto.ARCO, false, true,
                        1.7, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;

            case 413:
                objeto = new ArmaUnaMano(idObjeto, "ARCO DE ORION", "", 3, 12, 15,
                        5, 1, TipoObjeto.ARCO, false, false,
                        1.0, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 414:
                objeto = new ArmaUnaMano(idObjeto, "FUEGO DE AGUILA", "", 2, 8, 16,
                        4, 2, TipoObjeto.ARCO, false, true, 1.8,
                        "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 415:
                objeto = new ArmaUnaMano(idObjeto, "ARTEMISA'S GLORY", "", 2, 16,
                        20, 5, 2, TipoObjeto.ESPADA_LIGERA, false, false,
                        0.8, "arrow", Constantes.RUTA_ARMA_ESPADA, 1550, 20);
                break;
            case 500:
                objeto = new ArmaDosManos(idObjeto, "ARCO SIMPLE", "", 1, 3, 4,
                        3, 1, TipoObjeto.ARCO, false, false,
                        1.3, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 501:
                objeto = new ArmaDosManos(idObjeto, "FLECHADOR", "", 1, 4, 6,
                        3, 1, TipoObjeto.ARCO, false, false,
                        1.3, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 502:
                objeto = new ArmaDosManos(idObjeto, "ESTRELLA NOCTURNA", "", 1, 5,
                        8, 3, 1, TipoObjeto.ARCO,
                        false, false, 1.2, "arrow",
                        Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 503:
                objeto = new ArmaDosManos(idObjeto, "SUSPIRO DRACONICO", "", 2, 5, 7,
                        3, 2, TipoObjeto.ARCO, false, true, 1.5,
                        "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 504:
                objeto = new ArmaDosManos(idObjeto, "VELA ETERNA", "", 2, 8, 10,
                        3, 1, TipoObjeto.ARCO, false, false,
                        1.2, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 505:
                objeto = new ArmaDosManos(idObjeto, "TRUENO SILENTE", "", 1, 8, 12,
                        4, 2, TipoObjeto.ARCO, false, false,
                        1.1, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 506:
                objeto = new ArmaDosManos(idObjeto, "ARCO DE NELISS", "", 2, 7, 14,
                        4, 2, TipoObjeto.ARCO, false, true,
                        1.6, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 507:
                objeto = new ArmaDosManos(idObjeto, "AURORA CELESTIAL", "", 3, 9,
                        12, 3, 1, TipoObjeto.ARCO, false, true,
                        1.1, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 508:
                objeto = new ArmaDosManos(idObjeto, "SAGITARIO ESTELAR", "", 2, 9,
                        14, 3, 1, TipoObjeto.ARCO, false, false,
                        1.2, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 509:
                objeto = new ArmaDosManos(idObjeto, "CAZADOR", "", 3, 8, 10,
                        3, 1, TipoObjeto.ARCO, false, false,
                        1.3, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 510:
                objeto = new ArmaDosManos(idObjeto, "SUSURRO DEL VIENTO", "", 2, 10,
                        13, 3, 1, TipoObjeto.ARCO, false, true,
                        1.5, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 511:
                objeto = new ArmaDosManos(idObjeto, "DESTELLO COSMICO", "", 2, 10,
                        14, 4, 1, TipoObjeto.ARCO, false, true,
                        1.1, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 512:
                objeto = new ArmaDosManos(idObjeto, "LAGRIMA DE LUNA", "", 3, 11,
                        13, 3, 2, TipoObjeto.ARCO, false, true,
                        1.7, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;

            case 513:
                objeto = new ArmaDosManos(idObjeto, "ARCO DE ORION", "", 3, 12, 15,
                        5, 1, TipoObjeto.ARCO, false, false,
                        1.0, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 514:
                objeto = new ArmaDosManos(idObjeto, "FUEGO DE AGUILA", "", 2, 8, 16,
                        4, 2, TipoObjeto.ARCO, false, true, 1.8,
                        "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 515:
                objeto = new ArmaDosManos(idObjeto, "ARTEMISA'S GLORY", "", 2, 16,
                        20, 5, 2, TipoObjeto.ARCO, false, false,
                        0.8, "arrow", Constantes.RUTA_ARMA_ARCO, 1550, 20);
                break;
            case 599:
                objeto = new SinArma(idObjeto, "PUÑOS", "", 0, 1, 1, 1,
                        1, TipoObjeto.NINGUNO, false, false, 1.5, "",
                        "", 30, 20);
                break;
            case 600:
                objeto = new ProteccionMedia(idObjeto, "ARMADURA DE CUERO", 2, 2, 5, "",
                        TipoObjeto.ARMADURA_LIGERA, 30, 20);
                break;
            case 601:
                objeto = new ProteccionMedia(idObjeto, "ARMADURA DE CAMPEON", 2, 2, 5, "",
                        TipoObjeto.ARMADURA_LIGERA, 30, 20);
                break;
            case 616:
                objeto = new ProteccionAlta(idObjeto, "CASCO DE HIERRO", 3, 2, 5, "",
                        TipoObjeto.CASCO, 30, 20);
                break;
            case 617:
                objeto = new ProteccionAlta(idObjeto, "CASCO DE GLADIADOR", 3, 2, 5, "",
                        TipoObjeto.CASCO, 30, 20);
                break;
            case 632:
                objeto = new ProteccionLateral(idObjeto, "GUANTE DE HIERRO", 1, 1, 2, "",
                        TipoObjeto.GUANTE, 30, 20);
                break;
            case 648:
                objeto = new ProteccionBaja(idObjeto, "BOTAS DE CUERO", 1, 1, 1, "",
                        TipoObjeto.BOTA, 30, 20);
                break;
            case 700:
                objeto = new Collar(idObjeto, 0, 0, 3, 3, 0, 0, 1, 1,
                        "COLLAR DE PLATA", 0.2, "", TipoObjeto.COLLAR, 30, 20);
                break;
            case 716:
                objeto = new Accesorio(idObjeto, 1, 2, 0, 0, 1, 1, 0, 0,
                        "ARETES DE ÁMBAR", 0.1, "", TipoObjeto.ACCESORIO, 30, 20);
                break;
            case 732:
                objeto = new Anillo(idObjeto, 1, 2, 0, 0, 2, 1, 0, 0,
                        "ANILLO CEREMONIAL", 0.1, "", TipoObjeto.ANILLO, 30, 20);
                break;
            case 733:
                objeto = new Anillo(idObjeto, 0, 2, 0, 0, 2, 0, 0, 0,
                        "ANILLOS DE PLATA", 0.1, "", TipoObjeto.ANILLO, 30, 20);
                break;
        }
        return objeto;
    }
}
