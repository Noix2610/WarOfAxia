/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.habilidades;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author GAMER ARRAX
 */
import principal.Constantes;
import principal.herramientas.Cronometro;
import principal.inventario.TipoObjeto;
import principal.sprites.HojaSprites;

public abstract class Habilidad {
    private boolean efectoActivado;
    Cronometro cronometro;
    private TipoObjeto tipoHabilidad;
    private TipoObjeto activaPasiva;
    protected Rectangle posicionMenu;
    protected Rectangle posicionFlotante;
    HojaSprites hojaHabilidad;
    private final BufferedImage imagenActual;
    private String nombre;
    private String descripcion;
    private int duracion;
    private int tiempoReutilizacion;
    private int manaUtilizado;
    private int vidaUtilizada;
    private final Object objetivo;
    private int montoTotal;

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
