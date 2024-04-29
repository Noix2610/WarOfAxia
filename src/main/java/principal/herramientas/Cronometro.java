package principal.herramientas;

/**
 * Clase que representa un cronómetro para medir el tiempo transcurrido. Permite iniciar, reiniciar, actualizar y
 * obtener el tiempo transcurrido.
 */
public class Cronometro {

    private long tiempoInicio; // Tiempo en milisegundos en el que se inició el cronómetro
    private long tiempoTranscurrido; // Tiempo en milisegundos transcurrido desde el inicio del cronómetro
    private int tiempoRestante; // Tiempo restante en segundos

    /**
     * Constructor de la clase Cronometro. Inicia el cronómetro.
     */
    public Cronometro() {
        iniciar();
    }

    /**
     * Inicia el cronómetro estableciendo el tiempo de inicio.
     */
    public void iniciar() {
        tiempoInicio = System.currentTimeMillis();
    }

    /**
     * Obtiene el tiempo transcurrido en milisegundos desde el inicio del cronómetro.
     *
     * @return El tiempo transcurrido en milisegundos.
     */
    public long obtenerTiempoTranscurrido() {
        return System.currentTimeMillis() - tiempoInicio;
    }

    /**
     * Reinicia el cronómetro estableciendo un nuevo tiempo de inicio.
     */
    public void reiniciar() {
        tiempoInicio = System.currentTimeMillis();
    }

    /**
     * Actualiza el tiempo transcurrido desde el inicio del cronómetro.
     */
    public void actualizar() {
        tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
    }

    /**
     * Obtiene el tiempo restante en segundos hasta alcanzar el tiempo de reutilización especificado.
     *
     * @param tiempoReutilizacion El tiempo de reutilización en segundos.
     * @return El tiempo restante en segundos.
     */
    public int getTiempoRestante(int tiempoReutilizacion) {
        tiempoRestante = (int) (tiempoReutilizacion - obtenerTiempoTranscurrido() / 1000);
        return tiempoRestante;
    }

    /**
     * Establece el tiempo restante del cronómetro.
     *
     * @param tiempo El tiempo restante en segundos.
     */
    public void setTiempoRestante(int tiempo) {
        tiempoRestante = tiempo;
    }

    /**
     * Obtiene el tiempo transcurrido en segundos desde el inicio del cronómetro.
     *
     * @return El tiempo transcurrido en segundos.
     */
    public int getTiempoTranscurrido() {
        actualizar();
        return (int) tiempoTranscurrido / 1000;
    }

    /**
     * Obtiene el tiempo transcurrido en milisegundos desde el inicio del cronómetro.
     *
     * @return El tiempo transcurrido en milisegundos.
     */
    public long getTiempoTranscurridoMili() {
        actualizar(); // Actualiza el tiempo transcurrido desde un momento inicial
        return tiempoTranscurrido;
    }
}
