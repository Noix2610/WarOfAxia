/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario.armas;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.entes.Enemigo;
import principal.entes.Jugador;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.sonido.SoundThread;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public abstract class Arma extends Objeto {

    protected int alcanceFrontal;
    protected int alcanceLateral;
    public SoundThread disparo;
    public static HojaSprites hojaArmas = new HojaSprites(Constantes.RUTA_HOJA_ARCOS, 32, false);
    public int ataqueMin;
    public int ataqueMax;
    protected boolean automatica;
    protected boolean penetrante;
    protected double ataqueXSegundo;
    protected int actualizacionesParaSgteAtaque;
    protected HojaSprites hojaArma;
    // Duración en milisegundos

    public Arma(int id, String nombre, String descripcion, int peso, int ataqueMin, int ataqueMax, int alcanceFrontal,
            int alcanceLateral, final TipoObjeto tipoObjeto,
            final boolean automatica, final boolean penetrante, final double ataquesXSegundo, final String rutaDisparo,
            String rutaPersonaje, int precioCompra, int precioVenta
    ) {
        super(id, nombre, peso, descripcion, tipoObjeto, precioCompra, precioVenta);
        this.ataqueMin = ataqueMin;
        this.ataqueMax = ataqueMax;
        this.alcanceFrontal = alcanceFrontal;
        this.alcanceLateral = alcanceLateral;
        this.automatica = automatica;
        this.penetrante = penetrante;
        this.ataqueXSegundo = ataquesXSegundo;
        this.actualizacionesParaSgteAtaque = 0;
        this.disparo = new SoundThread(rutaDisparo);
        this.hojaArma = new HojaSprites(rutaPersonaje, 32, false);
    }

    public abstract ArrayList<Rectangle> getAlcance(final Jugador jugador);

    public void actualizar() {
        if (this != null) {
            if (actualizacionesParaSgteAtaque > 0) {
                actualizacionesParaSgteAtaque--;
            }
        }

    }

    public void atacar(final ArrayList<Enemigo> enemigos, int atributo) {

        if (actualizacionesParaSgteAtaque > 0) {
            return;
        }
        actualizacionesParaSgteAtaque = (int) (ataqueXSegundo * 60);
        disparo.reproducir(0.8f);

        if (enemigos.isEmpty()) {
            return;
        }
        ElementosPrincipales.jugador.getCronometro().reiniciar();
        ElementosPrincipales.jugador.preparado = true;
        // Generar un número aleatorio entre 1 y 100
        double numeroAleatorio = new Random().nextDouble(100) + 1;
        int multiplicadorCritico = 1;

        // Verificar si es un golpe crítico
        boolean esCritico = numeroAleatorio <= ElementosPrincipales.jugador.getGa().getCritico();
        int rango = this.getAtaqueMax() - this.getAtaqueMin() + 1;
        int ataquealeatorio = new Random().nextInt(rango) + this.getAtaqueMin();
        // Calcular el daño base (ajusta según tus necesidades)
        int danioBase = ataquealeatorio + atributo;

        // Aplicar multiplicador si es un golpe crítico
        if (esCritico) {
            multiplicadorCritico = 2;
        }

        float danioTotal = danioBase * multiplicadorCritico;

        for (Enemigo enemigo : enemigos) {
            enemigo.perderVida(danioTotal, esCritico);
        }

    }

    public boolean isAutomatica() {
        return automatica;
    }

    public void setAutomatica(boolean automatica) {
        this.automatica = automatica;
    }

    public boolean isPenetrante() {
        return penetrante;
    }

    public void setPenetrante(boolean penetrante) {
        this.penetrante = penetrante;
    }

    public int getAtaquemedio() {
        return new Random().nextInt(ataqueMax - ataqueMin + 1) + ataqueMin;
    }

    @Override
    public Sprite getSprite() {

        return hojaArmas.getSprites(id - 500);

    }

    public int getAtaque() {
        if (this != null) {
            return (int) (ataqueMin + ataqueMax) / 2;
        }
        else {
            return 0;
        }
    }

    public int getAlcanceInt() {
        return alcanceFrontal;
    }

    public HojaSprites getHojaArma() {
        return hojaArma;
    }

    public int getAtaqueMin() {
        return ataqueMin;
    }

    public int getAtaqueMax() {
        return ataqueMax;
    }
    
    

}
