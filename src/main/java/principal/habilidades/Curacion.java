package principal.habilidades;

import principal.ElementosPrincipales;
import principal.entes.EntidadCurable;
import principal.inventario.TipoObjeto;
import principal.sonido.SoundThread;

/**
 * Clase que representa la habilidad de curación.
 */
public class Curacion extends Habilidad {

    // Atributos específicos de la habilidad de curación
    private final int cantidadCuracionBase; // Cantidad base de curación
    private final double montoAdicionalPorInteligencia; // Monto adicional de curación basado en la inteligencia
    private final SoundThread sonido; // Sonido de la habilidad
    private final int tiempoCarga; // Tiempo de carga de la habilidad

    /**
     * Constructor de la habilidad de curación.
     *
     * @param nombre El nombre de la habilidad.
     * @param duracion La duración de la habilidad.
     * @param tiempoCarga El tiempo de carga de la habilidad.
     * @param objetivo El objetivo de la habilidad.
     * @param manaUtilizado La cantidad de maná utilizada por la habilidad.
     * @param vidaUtilizada La cantidad de vida utilizada por la habilidad.
     * @param cantidadCuracionBase La cantidad base de curación.
     * @param montoAdicionalPorInteligencia El monto adicional de curación por inteligencia.
     * @param indiceSprite El índice del sprite de la habilidad.
     * @param activaPasiva El tipo de activación de la habilidad.
     * @param tipoHabilidad El tipo de habilidad.
     */
    public Curacion(String nombre, int duracion, int tiempoCarga,
            Object objetivo, int manaUtilizado, int vidaUtilizada,
            int cantidadCuracionBase, int montoAdicionalPorInteligencia, int indiceSprite, TipoObjeto activaPasiva,
            TipoObjeto tipoHabilidad) {
        super(nombre, duracion, objetivo, manaUtilizado, vidaUtilizada, indiceSprite, activaPasiva, tipoHabilidad);
        this.cantidadCuracionBase = cantidadCuracionBase;
        this.montoAdicionalPorInteligencia = montoAdicionalPorInteligencia;
        super.setTiempoReutilizacion(0);
        this.tiempoCarga = tiempoCarga;
        super.setDescripcion("Restaura 30 pts de VIT + \nun adicional basado en\nla INT del conjurador");
        sonido = new SoundThread("Heal"); // Inicializar el sonido de la habilidad
    }

    /**
     * Método para aplicar el efecto de la habilidad de curación.
     *
     * @param object El objeto sobre el cual se aplica la curación.
     * @param tipoCuracion El tipo de curación (no utilizado en este caso).
     */
    @Override
    public void aplicarEfecto(Object object, TipoObjeto tipoCuracion) {
        curacionAutomatica(object); // Aplicar curación automática
    }

    /**
     * Método para aplicar la curación automáticamente.
     *
     * @param object El objeto sobre el cual se aplica la curación.
     */
    private void curacionAutomatica(Object object) {
        // Verificar si ha pasado el tiempo de recarga de la habilidad
        if (cronometro.obtenerTiempoTranscurrido() / 1000 >= getTiempoReutilizacion()) {
            // Verificar si el objetivo es una entidad curable
            if (object instanceof EntidadCurable) {
                EntidadCurable entidadCurable = (EntidadCurable) object;

                // Verificar si la entidad necesita curación y tiene suficiente maná
                if (entidadCurable.getVidaActual() < entidadCurable.getVidaMaxima() && entidadCurable.getMana() >= getManaUtilizado()) {
                    ElementosPrincipales.jugador.getCronometro().reiniciar();

                    // Calcular la cantidad total de curación (base + adicional por inteligencia)
                    int cantidadTotalCuracion = cantidadCuracionBase + calcularMontoAdicionalPorInteligencia(entidadCurable);
                    super.setMontoTotal(cantidadTotalCuracion);

                    // Aplicar la curación y actualizar el maná
                    entidadCurable.curarVida(cantidadTotalCuracion);
                    entidadCurable.setMana(entidadCurable.getMana() - getManaUtilizado());

                    // Actualizar el tiempo de recarga de la habilidad y reproducir el sonido
                    super.setTiempoReutilizacion(tiempoCarga);
                    sonido.reproducir(0.7f);
                    cronometro.reiniciar();
                }
            }
        }
    }

    /**
     * Método para calcular el monto adicional de curación basado en la inteligencia.
     *
     * @param entidadCurable La entidad sobre la cual se aplica la curación.
     * @return El monto adicional de curación.
     */
    private int calcularMontoAdicionalPorInteligencia(EntidadCurable entidadCurable) {
        return (int) (entidadCurable.getInteligencia() * montoAdicionalPorInteligencia);
    }
}
