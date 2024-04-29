package principal.habilidades;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import principal.Constantes;
import principal.herramientas.Cronometro;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;

/**
 * Clase abstracta que representa una habilidad.
 */
public abstract class Habilidad {

    private boolean efectoActivado; // Indica si el efecto de la habilidad está activado
    Cronometro cronometro; // Cronómetro para controlar el tiempo de reutilización de la habilidad
    private TipoObjeto tipoHabilidad; // Tipo de la habilidad
    private TipoObjeto activaPasiva; // Tipo de activación de la habilidad
    protected Rectangle posicionMenu; // Posición de la habilidad en el menú
    protected Rectangle posicionFlotante; // Posición flotante de la habilidad
    HojaSprites hojaHabilidad; // Hoja de sprites que contiene la imagen de la habilidad
    private final BufferedImage imagenActual; // Imagen actual de la habilidad
    private String nombre; // Nombre de la habilidad
    private String descripcion; // Descripción de la habilidad
    private int duracion; // Duración de la habilidad
    private int tiempoReutilizacion; // Tiempo de reutilización de la habilidad
    private int manaUtilizado; // Cantidad de maná utilizado por la habilidad
    private int vidaUtilizada; // Cantidad de vida utilizada por la habilidad
    private final Object objetivo; // Objetivo de la habilidad
    private int montoTotal; // Monto total de la habilidad

    /**
     * Constructor de la clase Habilidad.
     *
     * @param nombre El nombre de la habilidad.
     * @param duracion La duración de la habilidad.
     * @param objetivo El objetivo de la habilidad.
     * @param manaUtilizado La cantidad de maná utilizado por la habilidad.
     * @param vidaUtilizada La cantidad de vida utilizada por la habilidad.
     * @param indiceSprite El índice del sprite de la habilidad.
     * @param activaPasiva El tipo de activación de la habilidad.
     * @param tipoHabilidad El tipo de habilidad.
     */
    public Habilidad(String nombre, int duracion,
            Object objetivo, int manaUtilizado, int vidaUtilizada, int indiceSprite, TipoObjeto activaPasiva,
            TipoObjeto tipoHabilidad) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.objetivo = objetivo;
        this.manaUtilizado = manaUtilizado;
        this.vidaUtilizada = vidaUtilizada;
        hojaHabilidad = new HojaSprites(Constantes.RUTA_HOJA_HABILIDADES, 32, true);
        imagenActual = hojaHabilidad.getSprites(indiceSprite).getImagen();
        this.activaPasiva = activaPasiva;
        this.tipoHabilidad = tipoHabilidad;
        posicionMenu = new Rectangle(0, 0, 0, 0);
        posicionFlotante = new Rectangle(0, 0, 0, 0);
        cronometro = new Cronometro();
        efectoActivado = false;
    }

    // Método abstracto que debe ser implementado por las subclases
    public abstract void aplicarEfecto(Object object, TipoObjeto tipoHabilidad);

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getTiempoReutilizacion() {
        return tiempoReutilizacion;
    }

    public void setTiempoReutilizacion(int tiempoReutilizacion) {
        this.tiempoReutilizacion = tiempoReutilizacion;
    }

    public int getManaUtilizado() {
        return manaUtilizado;
    }

    public void setManaUtilizado(int manaUtilizado) {
        this.manaUtilizado = manaUtilizado;
    }

    public int getVidaUtilizada() {
        return vidaUtilizada;
    }

    public void setVidaUtilizada(int vidaUtilizada) {
        this.vidaUtilizada = vidaUtilizada;
    }

    public Object getObjetivo() {
        return objetivo;
    }

    public HojaSprites getHojaHabilidad() {
        return hojaHabilidad;
    }

    public BufferedImage getImagenActual() {
        return imagenActual;
    }

    public Rectangle getPosicionMenu() {
        return posicionMenu;
    }

    public void setPosicionMenu(Rectangle posicionMenu) {
        this.posicionMenu = posicionMenu;
    }

    public Rectangle getPosicionFlotante() {
        return posicionFlotante;
    }

    public void setPosicionFlotante(Rectangle posicionFlotante) {
        this.posicionFlotante = posicionFlotante;
    }

    public TipoObjeto getTipoHabilidad() {
        return tipoHabilidad;
    }

    public void setTipoHabilidad(TipoObjeto tipoHabilidad) {
        this.tipoHabilidad = tipoHabilidad;
    }

    public int getTiempoRestante() {
        int tiempoRestante = (int) ((getTiempoReutilizacion() - cronometro.getTiempoTranscurrido()));
        if (tiempoRestante > 0) {
            return tiempoRestante;
        }
        return 0;
    }

    public Cronometro getCronometro() {
        return cronometro;
    }

    public int getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }

    public boolean isEfectoActivado() {
        return efectoActivado;
    }

    public void setEfectoActivado(boolean efectoActivado) {
        this.efectoActivado = efectoActivado;
    }

    public TipoObjeto getActivaPasiva() {
        return activaPasiva;
    }

    public void setActivaPasiva(TipoObjeto activaPasiva) {
        this.activaPasiva = activaPasiva;
    }

}
