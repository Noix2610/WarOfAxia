/*
 * Esta clase abstracta representa un arma en el juego.
 * Proporciona métodos y atributos comunes a todas las armas.
 */
package principal.inventario.armas;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import principal.ElementosPrincipales;
import principal.entes.Enemigo;
import principal.entes.Jugador;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.sonido.SoundThread;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

public abstract class Arma extends Objeto {

    // Atributos comunes a todas las armas
    protected int alcanceFrontal;
    protected int alcanceLateral;
    protected SoundThread disparo;
    protected HojaSprites hojaArmas;
    protected int ataqueMin;
    protected int ataqueMax;
    protected boolean automatica;
    protected boolean penetrante;
    protected double ataqueXSegundo;
    protected int actualizacionesParaSgteAtaque;
    protected HojaSprites hojaArma;

    // Constructor de la clase Arma
    public Arma(int id, String nombre, String descripcion, int peso, int ataqueMin, int ataqueMax, int alcanceFrontal,
            int alcanceLateral, final TipoObjeto tipoObjeto, final boolean automatica, final boolean penetrante, final double ataquesXSegundo, final String rutaDisparo,
            String rutaPersonaje, int precioCompra, int precioVenta) {
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

    // Método abstracto para obtener el alcance del arma
    public abstract ArrayList<Rectangle> getAlcance(final Jugador jugador);

    // Método para actualizar el arma
    public void actualizar() {
        if (this != null) {
            if (actualizacionesParaSgteAtaque > 0) {
                actualizacionesParaSgteAtaque--;
            }
        }
    }

    // Método para realizar un ataque con el arma
    public void atacar(final ArrayList<Enemigo> enemigos, int atributo) {
        if (!enemigos.isEmpty()) {
            if (actualizacionesParaSgteAtaque > 0) {
                return;
            }
            actualizacionesParaSgteAtaque = (int) (ataqueXSegundo * 60);
            disparo.reproducir(0.8f);

            ElementosPrincipales.jugador.getCronometro().reiniciar();
            ElementosPrincipales.jugador.preparado = true;

            double numeroAleatorio = new Random().nextDouble(100) + 1;
            int multiplicadorCritico = 1;
            boolean esCritico = numeroAleatorio <= ElementosPrincipales.jugador.getGa().getCritico();
            int rango = this.getAtaqueMax() - this.getAtaqueMin() + 1;
            int ataqueAleatorio = new Random().nextInt(rango) + this.getAtaqueMin();
            int danioBase = ataqueAleatorio + atributo;

            if (esCritico) {
                multiplicadorCritico = 2;
            }

            float danioTotal = danioBase * multiplicadorCritico;

            for (Enemigo enemigo : enemigos) {
                enemigo.perderVida(danioTotal, esCritico);
            }
        }
    }

    // Métodos getter y setter para los atributos del arma
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

    public int getAtaqueMedio() {
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
