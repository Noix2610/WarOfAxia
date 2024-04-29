package principal.habilidades;

import java.util.ArrayList;
import java.util.List;
import principal.ElementosPrincipales;
import principal.inventario.TipoObjeto;

/**
 * Clase que gestiona las habilidades del jugador.
 */
public class GestorHabilidades {

    private final List<Habilidad> habilidades; // Lista de habilidades disponibles

    /**
     * Constructor de la clase GestorHabilidades.
     */
    public GestorHabilidades() {
        habilidades = new ArrayList<>(); // Inicializa la lista de habilidades
        inicializarHabilidades(); // Llena la lista con las habilidades disponibles
    }

    /**
     * Método privado para inicializar las habilidades disponibles.
     */
    private void inicializarHabilidades() {
        // Agrega las habilidades disponibles a la lista
        habilidades.add(crearCuracion("Curacion Basica", 1, 10,
                ElementosPrincipales.jugador, 10, 0, 30,
                1, 0, TipoObjeto.ACTIVA, TipoObjeto.ATM));
        habilidades.add(crearCuracion("Curacion Media", 1, 15,
                ElementosPrincipales.jugador, 15, 0, 40,
                1, 1, TipoObjeto.ACTIVA, TipoObjeto.ATM));
        habilidades.add(crearCuracion("Curacion Avanzada", 1, 30,
                ElementosPrincipales.jugador, 30, 0, 50,
                2, 2, TipoObjeto.ACTIVA, TipoObjeto.ATM));
        // Agrega más habilidades según sea necesario
    }

    /**
     * Método privado para crear una habilidad de curación.
     *
     * @param nombre El nombre de la habilidad.
     * @param duracion La duración de la habilidad.
     * @param tiempoReutilizacion El tiempo de reutilización de la habilidad.
     * @param objetivo El objetivo de la habilidad.
     * @param manaUtilizado La cantidad de maná utilizado por la habilidad.
     * @param vidaUtilizada La cantidad de vida utilizada por la habilidad.
     * @param cantidadCuracionBase La cantidad base de curación.
     * @param montoAdicionalPorInteligencia El monto adicional de curación por inteligencia.
     * @param indiceSprite El índice del sprite de la habilidad.
     * @param activaPasiva El tipo de activación de la habilidad.
     * @param tipoHabilidad El tipo de habilidad.
     * @return La habilidad de curación creada.
     */
    private Curacion crearCuracion(String nombre, int duracion, int tiempoReutilizacion,
            Object objetivo, int manaUtilizado, int vidaUtilizada,
            int cantidadCuracionBase, int montoAdicionalPorInteligencia, int indiceSprite, TipoObjeto activaPasiva,
            TipoObjeto tipoHabilidad) {
        // Crea y devuelve una nueva habilidad de curación
        return new Curacion(nombre, duracion, tiempoReutilizacion, objetivo, manaUtilizado, vidaUtilizada,
                cantidadCuracionBase, montoAdicionalPorInteligencia, indiceSprite, activaPasiva, tipoHabilidad);
    }

    // Agrega otros métodos para crear diferentes tipos de habilidades si es necesario
    /**
     * Método para obtener una habilidad por su nombre.
     *
     * @param nombreHabilidad El nombre de la habilidad.
     * @return La habilidad con el nombre dado, o null si no se encuentra.
     */
    public Habilidad obtenerHabilidadPorNombre(String nombreHabilidad) {
        for (Habilidad habilidad : habilidades) {
            if (habilidad.getNombre().equals(nombreHabilidad)) {
                return habilidad;
            }
        }
        return null; // Devuelve null si no se encuentra la habilidad con el nombre dado
    }

    /**
     * Método para obtener el tiempo de reutilización de una habilidad por su nombre.
     *
     * @param nombreHabilidad El nombre de la habilidad.
     * @return El tiempo de reutilización de la habilidad, o 0 si no se encuentra.
     */
    public long getTiempoReutilizacionHabilidad(String nombreHabilidad) {
        Habilidad habilidad = obtenerHabilidadPorNombre(nombreHabilidad);
        return (habilidad != null) ? habilidad.getTiempoReutilizacion() : 0L;
    }

    /**
     * Método estático para usar una habilidad.
     *
     * @param indice El índice de la habilidad en el inventario del jugador.
     */
    public static void usarHabilidad(int indice) {
        // Verifica si el objeto en el inventario es una habilidad
        if (ElementosPrincipales.jugador.getAr().getAccesoEquipado(indice) instanceof Habilidad habilidad) {
            habilidad.aplicarEfecto(habilidad.getObjetivo(), habilidad.getTipoHabilidad());
        }
    }
}
