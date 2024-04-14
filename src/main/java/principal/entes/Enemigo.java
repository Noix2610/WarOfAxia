/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.entes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.dijkstra.Nodo;
import principal.herramientas.CalculadoraDistancia;
import principal.herramientas.DibujoDebug;
import principal.inventario.ContenedorObjetos;
import principal.inventario.TipoObjeto;
import principal.sonido.SoundThread;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class Enemigo implements EntidadCurable {

    private final int indiceContenedor;
    private final int idEnemigo;
    protected double posicionInicialX;
    protected double posicionInicialY;
    protected double posicionX;
    protected double posicionY;
    protected Rectangle posicionMenu;
    protected double distanciaParaMov;
    protected boolean mostrarDanho;
    protected boolean mostrarCritico;
    protected int danhoPorGolpe;
    public float danhoRecibido;
    protected long tiempoInicioMostrarDanho;
    protected static final long DURACION_MOSTRAR_DANHO = 1000; // Duración en milisegundos
    protected static final float VELOCIDAD_SUBIDA_DANHO = 0.09f; // Ajusta según tu preferencia

    private int animacion;
    private int estado;
    private int direccion; // Ajusta según tus necesidades
    protected boolean enMovimiento = false;

    private HojaSprites hs;
    private BufferedImage imagenActual;

    protected String nombre;
    protected int vidaMaxima;
    protected float vidaActual;
    public int ataque;
    protected SoundThread lamento;
    protected long duracionLamento;
    protected long lamentoSiguiente = 0;
    protected ContenedorObjetos co;
    protected String descripcion;

    protected Nodo siguienteNodo;

    private long tiempoUltimoAtaque;
    private static final long TIEMPO_ENTRE_ATAQUES = 1500;
    private long tiempoInicioMostrarCuracion;
    private int montoRecuperado;
    private boolean mostrarCuracion;

    public Enemigo(int idEnemigo, String nombre, int vidaMaxima, final String rutaLamento, int ataque, HojaSprites hs,
            double distanciaParaMov, ContenedorObjetos contenedor, int idxContenedor) {
        this.idEnemigo = idEnemigo;
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.ataque = ataque;

        this.vidaActual = vidaMaxima;
        this.lamento = new SoundThread(rutaLamento);
        this.duracionLamento = lamento.getDuracion();

        this.animacion = 0;
        this.estado = 0;
        this.direccion = 0;
        tiempoUltimoAtaque = 0;
        this.distanciaParaMov = distanciaParaMov;

        this.hs = hs;
        imagenActual = hs.getSprites(0).getImagen();
        this.co = contenedor;
        this.indiceContenedor = idxContenedor;
        this.posicionMenu = new Rectangle();

    }

    public void actualizar(ArrayList<Enemigo> enemigos) {
        calcularDistanciaAlJugador();
        if (lamentoSiguiente > 0) {
            lamentoSiguiente -= 1000000 / 60;
        }
        moverHaciaSiguienteNodo(enemigos);
        cambiarAnimacionEstado();
        animar();
        atacar(ElementosPrincipales.jugador);
        //DibujoDebug.dibujarRectanguloContorno(GestorPrincipal.sd.getGraphics(), areaPosicional);

    }

    // Método para calcular la distancia entre el enemigo y el jugador
    private double calcularDistanciaAlJugador() {
        Point puntoJugador = new Point(
                (int) ElementosPrincipales.jugador.getPosicionXInt(),
                (int) ElementosPrincipales.jugador.getPosicionYInt());

        Point puntoEnemigo = new Point((int) posicionX, (int) posicionY);
        return CalculadoraDistancia.getDistanciaEntrePuntos(puntoEnemigo, puntoJugador);

    }

    private void atacar(Jugador jugador) {

        if (getArea().intersects(ElementosPrincipales.jugador.areaPosicional)) {
            long tiempoActual = System.currentTimeMillis();

            if (tiempoActual - tiempoUltimoAtaque >= TIEMPO_ENTRE_ATAQUES && getArea().intersects(jugador.areaPosicional)) {
                jugador.perderVida(ataque);
                tiempoUltimoAtaque = tiempoActual; // Actualiza el tiempo del último ataque
            }
        }
    }

    private void cambiarAnimacionEstado() {
        if (enMovimiento) {
            if (animacion < 90) {
                animacion++;
            }
            else {
                animacion = 0;
            }

            if (animacion <= 90 && animacion >= 80) {
                estado = 1; // Estado normal
            }
            else if (animacion < 80 && animacion > 70) {
                estado = 0; // Estado normal
            }
            else if (animacion < 70 && animacion > 60) {
                estado = 2; // Estado normal
            }
            else if (animacion < 60 && animacion > 50) {
                estado = 1; // Estado normal
            }
            else if (animacion < 50 && animacion > 40) {
                estado = 0; // Estado normal
            }
            else if (animacion < 40 && animacion > 30) {
                estado = 2; // Estado normal
            }
            else if (animacion < 30 && animacion > 20) {
                estado = 1; // Estado normal
            }
            else if (animacion < 20 && animacion > 10) {
                estado = 0; // Estado normal
            }
            else {
                estado = 1;
            }
        }
        else {
            estado = 1;
        }
    }

    public boolean colisionConObjeto(double nuevaX, double nuevaY, Rectangle r) {
        Rectangle areaEnemigo = new Rectangle((int) nuevaX, (int) nuevaY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
        Rectangle areaObjeto = r;
        return areaEnemigo.intersects(areaObjeto);
    }

    private void moverHaciaSiguienteNodo(ArrayList<Enemigo> enemigos) {

        double distanciaJugador = calcularDistanciaAlJugador();

        enMovimiento = true;
        if (siguienteNodo == null) {
            enMovimiento = false;
            return;
        }

        if (distanciaJugador > distanciaParaMov) {
            return;
        }

        int velocidad = 1;

        int xSiguienteNodo = siguienteNodo.getPosicion().x * Constantes.LADO_SPRITE;
        int ySiguienteNodo = siguienteNodo.getPosicion().y * Constantes.LADO_SPRITE;

        for (Enemigo enemigo : enemigos) {
            if (enemigo != this && enemigo.getAreaPosicional().intersects(siguienteNodo.getAreaPixeles())) {
                return;
            }
        }

        if (posicionX < xSiguienteNodo) {
            posicionX += velocidad;
            direccion = 2;
        }
        else if (posicionX > xSiguienteNodo) {
            posicionX -= velocidad;
            direccion = 1;
        }

        if (posicionY < ySiguienteNodo) {
            posicionY += velocidad;
            direccion = 0;
        }
        else if (posicionY > ySiguienteNodo) {
            posicionY -= velocidad;
            direccion = 3;
        }

    }

    public void animar() {

        // Resto del código para la animación cuando el enemigo está en movimiento
        Sprite sprite = hs.getSprites(estado, direccion);

        if (sprite != null) {
            imagenActual = sprite.getImagen();
        }
        else {
            imagenActual = null; // o imagen por defecto
        }
    }

    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        dibujarBarraVida(g, puntoX, puntoY);
        DibujoDebug.dibujarRectanguloContorno(g, getArea());
        dibujarVidaActual(g, puntoX, puntoY);
        // DibujoDebug.dibujarRectanguloContorno(GestorPrincipal.sd.getGraphics(), ElementosPrincipales.jugador.areaPosicional);
        dibujarDanhoRecibido(g, puntoX, puntoY + 20);

        DibujoDebug.dibujarImagen(g, imagenActual, puntoX, puntoY);
    }

    private void dibujarVidaActual(final Graphics g, final int puntoX, final int puntoY) {
        DibujoDebug.dibujarString(g, "" + Float.toString(vidaActual), puntoX, puntoY - 8);
    }

    private void dibujarDistancia(final Graphics g, final int puntoX, final int puntoY) {
        Point puntoJugador = new Point(
                (int) ElementosPrincipales.jugador.getPosicionXInt(),
                (int) ElementosPrincipales.jugador.getPosicionYInt());

        Point puntoEnemigo = new Point((int) posicionX, (int) posicionY);
        Double distancia = CalculadoraDistancia.getDistanciaEntrePuntos(puntoEnemigo, puntoJugador);
        DibujoDebug.dibujarString(g, String.format("%.2f", distancia), puntoX, puntoY - 8);
    }

    private void dibujarBarraVida(final Graphics g, final int puntoX, final int puntoY) {
        g.setColor(Color.green);

        DibujoDebug.dibujarRectanguloRelleno(g, puntoX, puntoY - 5, Constantes.LADO_SPRITE * (int) vidaActual / vidaMaxima, 2);
    }

    public void dibujarDanhoRecibido(Graphics g, int puntoX, int puntoY) {
        if (mostrarDanho) {

            // Calcula la opacidad en función del tiempo transcurrido
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarDanho;
            float opacidad = 1.0f - (float) tiempoTranscurrido / DURACION_MOSTRAR_DANHO;

            // Asegura que la opacidad esté en el rango [0, 1]
            opacidad = Math.max(0.0f, Math.min(1.0f, opacidad));

            // Calcula la posición Y en función de la velocidad de subida
            int posY = puntoY - (int) (VELOCIDAD_SUBIDA_DANHO * tiempoTranscurrido);

            // Configura el color con la opacidad
            Color colorDanho = new Color(1.0f, 0.0f, 0.0f, opacidad);
            g.setColor(colorDanho);

            // Dibuja el texto
            DibujoDebug.dibujarString(g, Float.toString(danhoPorGolpe), puntoX, posY);
            if (mostrarCritico) {
                colorDanho = new Color(1.0f, 1.0f, 1.0f, opacidad);
                g.setColor(colorDanho);

                // Dibuja el texto
                DibujoDebug.dibujarString(g, "CRITICO", puntoX, posY);
            }

            // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar el daño
            if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
                mostrarDanho = false;
            }

        }
    }

    public void dibujarCuracionRecibida(Graphics g, int puntoX, int puntoY) {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarCuracion;

        // Calcula la posición Y en función de la velocidad de subida
        int posY = puntoY - (int) (VELOCIDAD_SUBIDA_DANHO * tiempoTranscurrido);

        // Asegura que la posición Y no sea menor que el límite inferior
        posY = (int) Math.max(posY, puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20);

        // Calcula la opacidad en función del tiempo transcurrido
        float opacidad = 1.0f - (float) tiempoTranscurrido / DURACION_MOSTRAR_DANHO;

        // Asegura que la opacidad esté en el rango [0, 1]
        opacidad = Math.max(0.0f, Math.min(1.0f, opacidad));

        if (mostrarCuracion) {
            Color colorCuracion = new Color(0.0f, 1.0f, 0.0f, opacidad); // Cambiado a verde
            g.setColor(colorCuracion);
            DibujoDebug.dibujarString(g, Float.toString(montoRecuperado), puntoX, posY);

            // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar la información
            if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
                mostrarCuracion = false;
            }
        }
    }

    public void perderVida(float danhoRecibido, boolean critico) {
        //gestionar sonidos
        if (lamentoSiguiente <= 0) {
            lamento.reproducir(0.8f);
            lamentoSiguiente = duracionLamento;
        }
        danhoPorGolpe = (int) danhoRecibido;
        mostrarDanho = true;
        mostrarCritico = critico;

        tiempoInicioMostrarDanho = System.currentTimeMillis();
        this.danhoRecibido += danhoRecibido;

        if (vidaActual - (int) danhoRecibido < 0) {

            vidaActual = 0;
            ElementosPrincipales.jugador.getGa().setExperiencia(ElementosPrincipales.jugador.getGa().getExperiencia() + 10);

        }
        else {
            vidaActual -= (int) danhoRecibido;

        }

    }

    public void idlePosition(Graphics g, Rectangle posMenu, int id) {
        // Incrementa la animación para cambiar progresivamente entre los sprites de reposo

        // Cambia el estado para representar un estado de reposo
        if (this.getIdEnemigo() == id) {
            animacion++;
            int animacionBase = 500;

            if (animacion <= animacionBase / 2) {
                estado = 0;
            }
            else if (animacion > animacionBase / 2 && animacion <= animacionBase) {
                estado = 2;
            }

            else {
                animacion = 0;
            }

            // Obtiene la sprite correspondiente a la animación y el estado actual
            Sprite sprite = hs.getSprites(estado, 0);

            if (sprite != null) {
                imagenActual = sprite.getImagen();
            }
            else {
                imagenActual = null; // o imagen por defecto
            }
        }
        DibujoDebug.dibujarImagen(g, imagenActual, posMenu.x, posMenu.y);

    }

    public void setPosicion(final double posicionX, final double posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public int getIdEnemigo() {
        return idEnemigo;
    }

    @Override
    public int getVidaActual() {
        return (int) vidaActual;
    }

    public Rectangle getArea() {
        final int puntoX = (int) posicionX
                - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
        final int puntoY = (int) posicionY
                - (int) ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

        return new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    public Rectangle getAreaPosicional() {
        return new Rectangle((int) posicionX, (int) posicionY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    public void cambiarSiguienteNodo(Nodo nodo) {
        siguienteNodo = nodo;
    }

    public Nodo getSiguienteNodo() {
        return siguienteNodo;
    }

    public void setSiguienteNodo(Nodo siguienteNodo) {
        this.siguienteNodo = siguienteNodo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setPosicionInicialX(double posicionInicialX) {
        this.posicionInicialX = posicionInicialX;
    }

    public void setPosicionInicialY(double posicionInicialY) {
        this.posicionInicialY = posicionInicialY;
    }

    @Override
    public int getVidaMaxima() {
        return (int) vidaMaxima;
    }

    @Override
    public void setVidaActual(int vidaActual) {
        this.vidaActual = (float) vidaActual;
    }

    @Override
    public int getMana() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setMana(int mana) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getInteligencia() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void curarVida(int montoCuracion) {
        tiempoInicioMostrarCuracion = System.currentTimeMillis();

        if (vidaActual < vidaMaxima) {
            vidaActual += montoCuracion;
            if (vidaActual > vidaMaxima) {
                vidaActual = vidaMaxima;
            }
            montoRecuperado = montoCuracion;
            mostrarCuracion = true;
        }
    }

    public ContenedorObjetos getCo() {
        return co;
    }

    public void setCo(ContenedorObjetos co) {
        this.co = co;
    }

    public Rectangle getPosicionMenu() {
        return posicionMenu;
    }

    public void setPosicionMenu(Rectangle posicionMenu) {
        this.posicionMenu = posicionMenu;
    }

    public BufferedImage getImagenActual() {
        return imagenActual;
    }

    public void setImagenActual(BufferedImage imagenActual) {
        this.imagenActual = imagenActual;
    }

    public HojaSprites getHs() {
        return hs;
    }

    public void setHs(HojaSprites hs) {
        this.hs = hs;
    }

    public String getDescripcion() {
        return "Los Skeletons son esqueletos reanimados por la magia oscura. Armados con armas oxidadas, "
                + "acechan en tumbas y ruinas antiguas. Ágiles y resistentes, representan una amenaza para los intrusos temerarios.";
    }

    @Override
    public void recibirDanho(int danho, TipoObjeto tipoDeHabilidad) {
        this.setVidaActual((int)vidaActual - danho);
    }

}
