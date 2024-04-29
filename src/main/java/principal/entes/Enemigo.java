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

    private final int indiceContenedor; // Índice del contenedor de objetos al que pertenece el enemigo
    private final int idEnemigo; // Identificador único del enemigo
    protected double posicionInicialX; // Coordenada X inicial del enemigo
    protected double posicionInicialY; // Coordenada Y inicial del enemigo
    protected double posicionX; // Coordenada X actual del enemigo
    protected double posicionY; // Coordenada Y actual del enemigo
    protected Rectangle posicionMenu; // Área de la pantalla donde se muestra el menú del enemigo
    protected double distanciaParaMov; // Distancia mínima para que el enemigo comience a moverse
    protected boolean mostrarDanho; // Indica si se debe mostrar daño recibido
    protected boolean mostrarCritico; // Indica si se debe mostrar un daño crítico recibido
    protected int danhoPorGolpe; // Daño recibido por golpe
    public float danhoRecibido; // Daño total recibido por el enemigo
    protected long tiempoInicioMostrarDanho; // Tiempo en que se inició la muestra del daño
    protected static final long DURACION_MOSTRAR_DANHO = 1000; // Duración en milisegundos de la muestra del daño
    protected static final float VELOCIDAD_SUBIDA_DANHO = 0.09f; // Velocidad de subida del texto de daño (ajustar según preferencia)

    private int animacion; // Índice de animación del enemigo
    private int estado; // Estado de animación del enemigo
    private int direccion; // Dirección de movimiento del enemigo (ajustar según necesidades)
    protected boolean enMovimiento = false; // Indica si el enemigo está en movimiento

    private HojaSprites hs; // Hoja de sprites del enemigo
    private BufferedImage imagenActual; // Imagen actual del enemigo

    protected String nombre; // Nombre del enemigo
    protected int vidaMaxima; // Vida máxima del enemigo
    protected float vidaActual; // Vida actual del enemigo
    public int ataque; // Poder de ataque del enemigo
    protected SoundThread lamento; // Sonido de lamento del enemigo
    protected long duracionLamento; // Duración del sonido de lamento
    protected long lamentoSiguiente = 0; // Tiempo para reproducir el siguiente lamento
    protected ContenedorObjetos co; // Contenedor de objetos asociado al enemigo
    protected String descripcion; // Descripción del enemigo

    protected Nodo siguienteNodo; // Siguiente nodo en el camino del enemigo

    private long tiempoUltimoAtaque; // Tiempo del último ataque del enemigo
    private static final long TIEMPO_ENTRE_ATAQUES = 1500; // Tiempo mínimo entre ataques del enemigo
    private long tiempoInicioMostrarCuracion; // Tiempo en que se inició la muestra de la curación
    private int montoRecuperado; // Monto de vida recuperado por curación
    private boolean mostrarCuracion; // Indica si se debe mostrar la curación recibida

    /**
     * Constructor de la clase Enemigo.
     *
     * @param idEnemigo Identificador único del enemigo.
     * @param nombre Nombre del enemigo.
     * @param vidaMaxima Vida máxima del enemigo.
     * @param rutaLamento Ruta del archivo de sonido de lamento del enemigo.
     * @param ataque Poder de ataque del enemigo.
     * @param hs Hoja de sprites del enemigo.
     * @param distanciaParaMov Distancia mínima para que el enemigo comience a moverse.
     * @param contenedor Contenedor de objetos asociado al enemigo.
     * @param idxContenedor Índice del contenedor de objetos al que pertenece el enemigo.
     */
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

    /**
     * Actualiza el estado del enemigo en el juego.
     *
     * @param enemigos Lista de enemigos en el juego.
     */
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

    /**
     * Realiza un ataque al jugador si está en el área de ataque del enemigo.
     *
     * @param jugador Jugador al que se realiza el ataque.
     */
    private void atacar(Jugador jugador) {

        if (getArea().intersects(ElementosPrincipales.jugador.areaPosicional)) {
            long tiempoActual = System.currentTimeMillis();

            if (tiempoActual - tiempoUltimoAtaque >= TIEMPO_ENTRE_ATAQUES && getArea().intersects(jugador.areaPosicional)) {
                jugador.perderVida(ataque);
                tiempoUltimoAtaque = tiempoActual; // Actualiza el tiempo del último ataque
            }
        }
    }

    /**
     * Cambia la animación y estado del enemigo en función de su movimiento.
     */
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

    /**
     * Comprueba si el enemigo colisiona con un objeto en una posición específica.
     *
     * @param nuevaX Nueva coordenada X del enemigo.
     * @param nuevaY Nueva coordenada Y del enemigo.
     * @param r Rectángulo que representa el área del objeto.
     * @return True si hay colisión, false en caso contrario.
     */
    public boolean colisionConObjeto(double nuevaX, double nuevaY, Rectangle r) {
        Rectangle areaEnemigo = new Rectangle((int) nuevaX, (int) nuevaY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
        Rectangle areaObjeto = r;
        return areaEnemigo.intersects(areaObjeto);
    }

    /**
     * Mueve al enemigo hacia el siguiente nodo en el camino.
     *
     * @param enemigos Lista de enemigos en el juego.
     */
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

    /**
     * Realiza la animación del enemigo.
     */
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

    /**
     * Dibuja al enemigo en pantalla.
     *
     * @param g Objeto Graphics para dibujar.
     * @param puntoX Coordenada X de la pantalla.
     * @param puntoY Coordenada Y de la pantalla.
     */
    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        //dibujarBarraVida(g, puntoX, puntoY);
        //DibujoDebug.dibujarRectanguloContorno(g, getArea());
        //dibujarVidaActual(g, puntoX, puntoY);
        // DibujoDebug.dibujarRectanguloContorno(GestorPrincipal.sd.getGraphics(), ElementosPrincipales.jugador.areaPosicional);
        dibujarDanhoRecibido(g, puntoX, puntoY + 20);

        DibujoDebug.dibujarImagen(g, imagenActual, puntoX, puntoY);
    }

    // Método para dibujar la vida actual del enemigo en la posición especificada
    private void dibujarVidaActual(final Graphics g, final int puntoX, final int puntoY) {
        DibujoDebug.dibujarString(g, "" + Float.toString(vidaActual), puntoX, puntoY - 8);
    }

// Método para dibujar la distancia entre el jugador y el enemigo en la posición especificada
    private void dibujarDistancia(final Graphics g, final int puntoX, final int puntoY) {
        // Obtiene las coordenadas del jugador y del enemigo
        Point puntoJugador = new Point(
                (int) ElementosPrincipales.jugador.getPosicionXInt(),
                (int) ElementosPrincipales.jugador.getPosicionYInt());
        Point puntoEnemigo = new Point((int) posicionX, (int) posicionY);

        // Calcula la distancia entre el jugador y el enemigo
        Double distancia = CalculadoraDistancia.getDistanciaEntrePuntos(puntoEnemigo, puntoJugador);

        // Dibuja la distancia con formato de dos decimales
        DibujoDebug.dibujarString(g, String.format("%.2f", distancia), puntoX, puntoY - 8);
    }

// Método para dibujar la barra de vida del enemigo en la posición especificada
    private void dibujarBarraVida(final Graphics g, final int puntoX, final int puntoY) {
        g.setColor(Color.green); // Establece el color verde para la barra de vida
        // Dibuja un rectángulo relleno que representa la cantidad de vida actual del enemigo
        DibujoDebug.dibujarRectanguloRelleno(g, puntoX, puntoY - 5, Constantes.LADO_SPRITE * (int) vidaActual / vidaMaxima, 2);
    }

// Método para dibujar el daño recibido en la posición especificada
    public void dibujarDanhoRecibido(Graphics g, int puntoX, int puntoY) {
        // Verifica si se debe mostrar el daño recibido
        if (mostrarDanho) {
            // Calcula la opacidad del texto en función del tiempo transcurrido
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarDanho;
            float opacidad = 1.0f - (float) tiempoTranscurrido / DURACION_MOSTRAR_DANHO;
            opacidad = Math.max(0.0f, Math.min(1.0f, opacidad)); // Asegura que la opacidad esté en el rango [0, 1]

            // Calcula la posición Y del texto en función del tiempo transcurrido y la velocidad de subida
            int posY = puntoY - (int) (VELOCIDAD_SUBIDA_DANHO * tiempoTranscurrido);

            // Configura el color del texto con la opacidad calculada
            Color colorDanho = new Color(1.0f, 0.0f, 0.0f, opacidad);
            g.setColor(colorDanho);

            // Dibuja el texto del daño recibido
            DibujoDebug.dibujarString(g, Float.toString(danhoPorGolpe), puntoX, posY);

            // Si se muestra un golpe crítico, dibuja también el texto "CRITICO" con el mismo tratamiento de opacidad
            if (mostrarCritico) {
                colorDanho = new Color(1.0f, 1.0f, 1.0f, opacidad);
                g.setColor(colorDanho);
                DibujoDebug.dibujarString(g, "CRITICO", puntoX, posY);
            }

            // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar el daño
            if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
                mostrarDanho = false;
            }
        }
    }

// Método para dibujar la curación recibida en la posición especificada
    public void dibujarCuracionRecibida(Graphics g, int puntoX, int puntoY) {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarCuracion;

        // Calcula la posición Y en función de la velocidad de subida
        int posY = puntoY - (int) (VELOCIDAD_SUBIDA_DANHO * tiempoTranscurrido);

        // Asegura que la posición Y no sea menor que el límite inferior
        posY = (int) Math.max(posY, puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20);

        // Calcula la opacidad en función del tiempo transcurrido
        float opacidad = 1.0f - (float) tiempoTranscurrido / DURACION_MOSTRAR_DANHO;
        opacidad = Math.max(0.0f, Math.min(1.0f, opacidad)); // Asegura que la opacidad esté en el rango [0, 1]

        // Verifica si se debe mostrar la curación recibida
        if (mostrarCuracion) {
            // Configura el color de la curación con opacidad verde
            Color colorCuracion = new Color(0.0f, 1.0f, 0.0f, opacidad);
            g.setColor(colorCuracion);

            // Dibuja el texto de la curación recibida
            DibujoDebug.dibujarString(g, Float.toString(montoRecuperado), puntoX, posY);

            // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar la información
            if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
                mostrarCuracion = false;
            }
        }
    }

// Método para reducir la vida del enemigo y mostrar el daño recibido
    public void perderVida(float danhoRecibido, boolean critico) {
        lamento.reproducir(0.8f); // Reproduce un sonido de lamento

        // Establece el daño recibido y si fue un golpe crítico
        danhoPorGolpe = (int) danhoRecibido;
        mostrarDanho = true;
        mostrarCritico = critico;

        tiempoInicioMostrarDanho = System.currentTimeMillis(); // Registra el tiempo de inicio de mostrar daño

        // Actualiza la vida del enemigo después de recibir el daño
        if (vidaActual - (int) danhoRecibido < 0) {
            // Si la vida resulta ser menor que cero, establece la vida en cero y otorga experiencia al jugador
            vidaActual = 0;
            ElementosPrincipales.jugador.getGa().setExperiencia(ElementosPrincipales.jugador.getGa().getExperiencia() + 10);
        }
        else {
            // De lo contrario, reduce la vida del enemigo
            vidaActual -= (int) danhoRecibido;
        }
    }

// Método para establecer la posición del enemigo
    public void setPosicion(final double posicionX, final double posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

// Método para obtener la coordenada X del enemigo
    public double getPosicionX() {
        return posicionX;
    }

// Método para obtener la coordenada Y del enemigo
    public double getPosicionY() {
        return posicionY;
    }

// Método para obtener el identificador del enemigo
    public int getIdEnemigo() {
        return idEnemigo;
    }

// Método para obtener la vida actual del enemigo
    @Override
    public int getVidaActual() {
        return (int) vidaActual;
    }

// Método para obtener el área del enemigo relativa a la posición del jugador
    public Rectangle getArea() {
        final int puntoX = (int) posicionX - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
        final int puntoY = (int) posicionY - (int) ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;

        return new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

// Método para obtener el área posicional del enemigo
    public Rectangle getAreaPosicional() {
        return new Rectangle((int) posicionX, (int) posicionY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

// Método para cambiar el siguiente nodo de movimiento del enemigo
    public void cambiarSiguienteNodo(Nodo nodo) {
        siguienteNodo = nodo;
    }

// Método para obtener el siguiente nodo de movimiento del enemigo
    public Nodo getSiguienteNodo() {
        return siguienteNodo;
    }

// Método para establecer el siguiente nodo de movimiento del enemigo
    public void setSiguienteNodo(Nodo siguienteNodo) {
        this.siguienteNodo = siguienteNodo;
    }

// Método para obtener el nombre del enemigo
    public String getNombre() {
        return nombre;
    }

// Método para obtener el valor de ataque del enemigo
    public int getAtaque() {
        return ataque;
    }

// Método para establecer la posición inicial en X del enemigo
    public void setPosicionInicialX(double posicionInicialX) {
        this.posicionInicialX = posicionInicialX;
    }

// Método para establecer la posición inicial en Y del enemigo
    public void setPosicionInicialY(double posicionInicialY) {
        this.posicionInicialY = posicionInicialY;
    }

// Método para obtener la vida máxima del enemigo
    @Override
    public int getVidaMaxima() {
        return (int) vidaMaxima;
    }

// Método para establecer la vida actual del enemigo
    @Override
    public void setVidaActual(int vidaActual) {
        this.vidaActual = (float) vidaActual;
    }

// Método para obtener el maná del enemigo (no implementado)
    @Override
    public int getMana() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

// Método para establecer el maná del enemigo (no implementado)
    @Override
    public void setMana(int mana) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

// Método para obtener la inteligencia del enemigo (no implementado)
    @Override
    public int getInteligencia() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

// Método para curar la vida del enemigo
    @Override
    public void curarVida(int montoCuracion) {
        // Registra el tiempo de inicio de mostrar curación
        tiempoInicioMostrarCuracion = System.currentTimeMillis();

        // Verifica si la vida actual es menor que la vida máxima
        if (vidaActual < vidaMaxima) {
            vidaActual += montoCuracion; // Aumenta la vida actual con el monto de curación

            // Si la vida actual supera la vida máxima, la ajusta a la máxima
            if (vidaActual > vidaMaxima) {
                vidaActual = vidaMaxima;
            }

            montoRecuperado = montoCuracion; // Establece el monto recuperado
            mostrarCuracion = true; // Habilita la visualización de la curación recibida
        }
    }

    // Método para obtener el contenedor de objetos del enemigo
    public ContenedorObjetos getCo() {
        return co;
    }

// Método para establecer el contenedor de objetos del enemigo
    public void setCo(ContenedorObjetos co) {
        this.co = co;
    }

// Método para obtener la posición del menú del enemigo
    public Rectangle getPosicionMenu() {
        return posicionMenu;
    }

// Método para establecer la posición del menú del enemigo
    public void setPosicionMenu(Rectangle posicionMenu) {
        this.posicionMenu = posicionMenu;
    }

// Método para obtener la imagen actual del enemigo
    public BufferedImage getImagenActual() {
        return imagenActual;
    }

// Método para establecer la imagen actual del enemigo
    public void setImagenActual(BufferedImage imagenActual) {
        this.imagenActual = imagenActual;
    }

// Método para obtener la hoja de sprites del enemigo
    public HojaSprites getHs() {
        return hs;
    }

// Método para establecer la hoja de sprites del enemigo
    public void setHs(HojaSprites hs) {
        this.hs = hs;
    }

// Método para obtener la descripción del enemigo
    public String getDescripcion() {
        return "Los Skeletons son esqueletos reanimados por la magia oscura. Armados con armas oxidadas, "
                + "acechan en tumbas y ruinas antiguas. Ágiles y resistentes, representan una amenaza para los intrusos temerarios.";
    }

// Método para recibir daño del jugador
    @Override
    public void recibirDanho(int danho, TipoObjeto tipoDeHabilidad) {
        this.setVidaActual((int) vidaActual - danho); // Reduce la vida del enemigo en función del daño recibido
    }

}
