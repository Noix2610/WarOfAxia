/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.entes;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.control.GestorControles;
import principal.habilidades.Curacion;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.Cronometro;
import principal.herramientas.DibujoDebug;
import principal.inventario.Objeto;
import principal.inventario.TipoObjeto;
import principal.inventario.armaduras.Armadura;
import principal.inventario.armas.Arma;
import principal.inventario.armas.ArmaDosManos;
import principal.inventario.armas.SinArma;
import principal.inventario.joyas.Joya;
import principal.sonido.SoundThread;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 *
 * @author GAMER ARRAX
 */
public class Jugador implements EntidadCurable {

    // Variables de estado y atributos del jugador
    public long tiempo;
    public Curacion curacion;
    public boolean dibujarHabilidad = false;

    // Sonidos de pasos al caminar
    private SoundThread sonidoCaminar1;
    private SoundThread sonidoCaminar2;

    // Gestor de atributos del jugador
    private final GestorAtributos ga;
    public boolean estaVivo = true;

    // Posición del jugador en el mapa
    private double posicionX;
    private double posicionY;

    // Estado de animación y dirección del jugador
    private int estadoAnimacion;
    private int direccion;

    // Velocidades de movimiento del jugador
    private double velocidadCaminar = 1;
    private final double velocidadCorrer = velocidadCaminar * 2;

    // Estados del jugador
    private boolean enMovimiento = false;
    public boolean atacando = false;
    private boolean sobrepeso = false;
    public boolean preparado = false;
    private final Cronometro cronometro = new Cronometro();

    // Hojas de sprites del jugador
    private HojaSprites hs;
    private final HojaSprites ht;
    private final HojaSprites hCuracion;
    private BufferedImage imagenActual;

    // Dimensiones del jugador
    private final int ANCHO_JUGADOR = 16;
    private final int ALTO_JUGADOR = 16;

    // Área de colisión del jugador
    public Rectangle areaPosicional;

    // Límites del área de juego
    private final Rectangle LIMITE_ARRIBA = new Rectangle(Constantes.CENTRO_VENTANA_X - ANCHO_JUGADOR / 2,
            Constantes.CENTRO_VENTANA_Y, ANCHO_JUGADOR, 1);
    private final Rectangle LIMITE_ABAJO = new Rectangle(Constantes.CENTRO_VENTANA_X - ANCHO_JUGADOR / 2,
            Constantes.CENTRO_VENTANA_Y + ALTO_JUGADOR, ANCHO_JUGADOR, 1);
    private final Rectangle LIMITE_IZQUIERDA = new Rectangle(Constantes.CENTRO_VENTANA_X - ANCHO_JUGADOR / 2,
            Constantes.CENTRO_VENTANA_Y, 4, ALTO_JUGADOR);
    private final Rectangle LIMITE_DERECHA = new Rectangle(Constantes.CENTRO_VENTANA_X + ANCHO_JUGADOR / 2,
            Constantes.CENTRO_VENTANA_Y, 4, ALTO_JUGADOR);

    // Estado de animación y dirección del jugador
    private int animacion;
    public int estado;

    // Estado de subida de nivel del jugador
    private boolean subirNivel = false;
    private BufferedImage habilidad;

    // Tiempo de recuperación y estado de recuperación del jugador
    private int recuperacion = 60;
    private int recuperacionVida = 60;
    private boolean recuperado = true;

    // Almacén de equipo y acceso rápido del jugador
    private AlmacenEquipo ae;
    private final AccesoRapido ar;
    private ArrayList<Rectangle> alcanceActual;

    // Estados de muestra de eventos
    private boolean mostrarBloqueado = false;
    private boolean mostrarEvadido = false;
    private boolean mostrarDanho = false;
    public boolean mostrarCuracion = false;
    private int danhoPorGolpe;
    private int montoRecuperado;
    private float danhoRecibido;
    private long tiempoInicioMostrarDanho;
    private long tiempoInicioMostrarCuracion;
    protected static final long DURACION_MOSTRAR_DANHO = 500; // Duración en milisegundos
    protected static final float VELOCIDAD_SUBIDA_DANHO = 0.09f; // Ajusta según tu preferencia

    // Constructor del jugador
    public Jugador() {

        // Inicialización de posición, dirección, gestor de atributos, hojas de sprites, etc.
        posicionX = ElementosPrincipales.mapa.getPuntoInicial().getX();
        posicionY = ElementosPrincipales.mapa.getPuntoInicial().getY();
        direccion = 0;
        ga = new GestorAtributos(1, 6, 6, 6, 6, 6, 80, 100);
        hs = new HojaSprites(Constantes.RUTA_PERSONAJE, 32, 32, false);
        ht = new HojaSprites(Constantes.RUTA_PERSONAJE_TRANSPARENTE, Constantes.LADO_SPRITE, false);
        hCuracion = new HojaSprites("/personajes/enemigos/animacionesJugador/brilloCuracion.png", 32, 32, false);
        animacion = 0;
        estado = 1;
        imagenActual = hs.getSprites(estado, direccion).getImagen();
        ae = new AlmacenEquipo();
        ar = new AccesoRapido();
        alcanceActual = new ArrayList<>();
        areaPosicional = new Rectangle(Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2,
                Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2, 32, 32);

        // Actualización inicial de atributos
        actualizarAtributos();
        ga.setVida(ga.getVidaMaxima());
        ga.setMana(ga.getManaMaximo());
        ga.setResistencia(ga.getResistenciaMaxima());
        habilidad = CargadorRecursos.cargarImagenCompatibleTranslucida("/icons/habilidad1.png");
        sonidoCaminar1 = new SoundThread("Step_Grass");
        sonidoCaminar2 = new SoundThread("Step_Grass_2");
    }

    // Método para aumentar la experiencia del jugador
    public void ganarExperiencia(int puntos) {
        ga.setExperiencia(ga.getExperiencia() + puntos);
    }

    // Método para subir de nivel al jugador
    public void subirNivel() {
        if (ga.getExperiencia() >= ga.getExperienciaMaxima()) {
            this.ga.setNivel(ga.getNivel() + 1);
            ga.setVida(ga.getVidaMaxima());
            ga.setMana(ga.getManaMaximo());
            ga.setExperiencia(ga.getExperiencia() - ga.getExperienciaMaxima());
            ga.setExperienciaMaxima((int) (ga.getExperienciaMaxima() * 1.1));
            ga.setPuntosAtributos(ga.getPuntosAtributos() + 3);
            subirNivel = true;
        }
    }

    // Método para actualizar los atributos del jugador
    private void actualizarAtributos() {
        ga.setVidaMaxima(120 + ga.getNivel() + (int) (ga.getConstitucion() * 1.15));
        ga.setManaMaximo((int) (50 + ga.getNivel() / 2 + (ga.getInteligencia() * 1.15)));
        ga.setAtaque(ga.getFuerza() + ga.getNivel() + calcularAtqFisico());
        ga.setDefensaFisica((int) (ga.getVidaMaxima() * 0.01 + ga.getConstitucion() * 1.1) + calcularDefensaF());
        ga.setMagia(ga.getInteligencia() + ga.getNivel() + calcularAtqMagico());
        ga.setDefensaMagica((int) (ga.getManaMaximo() * 0.01 + ga.getInteligencia() * 1.1) + calcularDefensaM());
        ga.setEvasion(ga.getDestreza() * 1.2 - ga.getPesoActual() * 0.3);
        setVelocidad(ga.getDestreza() * 0.2 - ga.getPesoActual() * 0.30);
        ga.setCritico(ga.getSuerte() * 0.5);
        ga.setResistenciaMaxima(600 + (int) (ga.getConstitucion() * 0.1 + ga.getDestreza() * 0.1));
        ga.setLimitePeso(ga.getFuerza() * 2 + ga.getConstitucion() * 2);
    }

    // Método para dibujar el efecto de subir de nivel del jugador
    public void dibujarSubirNivel(Graphics g, int puntoX, int puntoY) {
        if (subirNivel) {

            // Calcula la opacidad en función del tiempo transcurrido
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarDanho;
            float opacidad = 1.0f - (float) tiempoTranscurrido / DURACION_MOSTRAR_DANHO;

            // Asegura que la opacidad esté en el rango [0, 1]
            opacidad = Math.max(0.0f, Math.min(1.0f, opacidad));

            // Calcula la posición Y en función de la velocidadCaminar de subida
            int posY = puntoY - (int) (VELOCIDAD_SUBIDA_DANHO * tiempoTranscurrido);

            // Configura el color con la opacidad
            Color colorBlanco = new Color(1.0f, 1.0f, 1.0f, opacidad);
            g.setColor(colorBlanco);

            // Dibuja el texto
            DibujoDebug.dibujarString(g, "LEVEL UP!", puntoX, posY);

            // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar el daño
            if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
                subirNivel = false;
            }
        }
    }

    // Implementa la lógica para reiniciar la experiencia, si es necesario
    private void gestionarVelocidadResistencia() {
        if (GestorControles.teclado.corriendo && ga.getResistencia() > 0) {
            velocidadCaminar = velocidadCorrer;
            recuperado = false;
            recuperacion = 0;

        }
        else if (isSobrepeso()) {
            velocidadCaminar = 0.5;
        }
        else {
            velocidadCaminar = 1;
            if (!recuperado && recuperacion < 120) {
                recuperacion++;

            }

            if (recuperacion == 120 && ga.getResistencia() < ga.getResistenciaMaxima()) {
                ga.setResistencia(ga.getResistencia() + 3);
                if (ga.getResistencia() > ga.getResistenciaMaxima()) {
                    ga.setResistencia(ga.getResistenciaMaxima());
                }
            }
        }
    }

    // Método para actualizar las defensas del jugador
    public void actualizarDefensas() {
        int def = (int) (ga.getVidaMaxima() * 0.01 + ga.getConstitucion() * 1.1) + calcularDefensaF();
        int defM = (int) (ga.getManaMaximo() * 0.01 + ga.getInteligencia() * 1.1) + calcularDefensaM();
        double eva = (ga.getDestreza() * 1.2 - ga.getPesoActual() * 0.3) + calcularEvasion();

        ga.setDefensaFisica(def);
        ga.setDefensaMagica(defM);
        ga.setEvasion(eva);
        ga.setResFisica(calcularResF());
        ga.setResMagica(calcularResM());
    }

    // Método para actualizar el ataque del jugador
    public void actualizarAtaque() {
        double crit = (ga.getSuerte() * 0.5) + calcularCrit();

        ga.setAtaque(ga.getFuerza() + ga.getNivel() + calcularAtqFisico());
        ga.setMagia(ga.getInteligencia() + ga.getNivel() + calcularAtqMagico());
        ga.setCritico(crit);

    }

    // Método principal de actualización del jugador
    public void actualizar() {

        enMovimiento = false;
        gestionarVelocidadResistencia();
        determinarDireccion();
        actualizarAnimacion();
        //transparentar();
        actualizarArmas();
        subirNivel();
        morir();
        isDead();
        actualizarDefensas();
        calcularPesoActual();
        actualizarAtaque();
        cambiarHojaSprites();

    }

    private void actualizarArmas() {
        // Calcula el alcance de ataque del jugador
        calcularAlcanceAtaque();

        // Actualiza el estado de la primera arma si existe
        if (ae.getArma1() != null) {
            ae.getArma1().actualizar();
        }

        // Actualiza el estado de la segunda arma si existe
        if (ae.getArma2() != null) {
            ae.getArma2().actualizar();
        }
    }

    public void dibujar(Graphics g) {
        // Define las coordenadas del centro de la pantalla
        final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
        final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;

        // Si el jugador no está preparado, dibuja su imagen actual en el centro de la pantalla
        if (!preparado) {
            DibujoDebug.dibujarImagen(g, imagenActual, centroX, centroY);
        }

        // Dibuja la vestimenta del jugador
        dibujarVestimenta(g, centroX, centroY);

        // Comentarios eliminados por brevedad
    }

    public void dibujarHabilidad(Graphics g) {
        // Obtiene el tiempo transcurrido desde el inicio de la animación
        int tiempoTranscurrido = (int) cronometro.getTiempoTranscurridoMili();

        // Verifica si han pasado menos de 0.5 segundos desde el inicio de la animación
        if (tiempoTranscurrido < 500) {
            // Obtiene el índice de la imagen actual basado en el tiempo transcurrido
            int indiceImagen = tiempoTranscurrido / 250;

            // Determina qué imagen mostrar según el índice
            HojaSprites hojaHabilidad;
            if (indiceImagen % 2 == 0) {
                hojaHabilidad = new HojaSprites("/icons/habilidad1.png", 640, 360, false);
            }
            else {
                hojaHabilidad = new HojaSprites("/icons/habilidad2.png", 640, 360, false);
            }

            // Obtiene la imagen correspondiente y la dibuja
            habilidad = hojaHabilidad.getSprites(0).getImagen();
            DibujoDebug.dibujarImagen(g, habilidad, 0, 0);
        }
        else {
            // Si han pasado más de 0.5 segundos, detiene la animación
            dibujarHabilidad = false;
        }
    }

    private void dibujarVestimenta(Graphics g, int centroX, int centroY) {

        /*if (getAe().getCasco() != null) {
            ProteccionAlta armadura = (ProteccionAlta) getAe().getCasco();
            DibujoDebug.dibujarImagen(g, armadura.getHojaCasco().getSprites(estado, direccion).getImagen(), centroX, centroY);
        }
        else {
            HojaSprites hojaCabello = new HojaSprites(Constantes.RUTA_PERSONAJE_CABELLO, 32, false);
            DibujoDebug.dibujarImagen(g, hojaCabello.getSprites(estado, direccion).getImagen(), centroX, centroY);
        }*/
        if (preparado) {

            int tiempoTranscurrido = (int) cronometro.getTiempoTranscurridoMili();

            // Verificar si han pasado menos de 1 segundo desde el inicio de la animación
            if (tiempoTranscurrido < 300) { // 1000 milisegundos = 1 segundo
                // Obtener el índice de la imagen actual
                // Cambiar la imagen cada 100 milisegundos

                // Determinar qué imagen mostrar basándose en el índice
                if (tiempoTranscurrido <= 100) {
                    estado = 3;
                    DibujoDebug.dibujarImagen(g, hs.getSprites(estado, direccion).getImagen(), centroX, centroY);

                }
                else if (tiempoTranscurrido > 100 && tiempoTranscurrido < 200) {
                    estado = 4;
                    DibujoDebug.dibujarImagen(g, hs.getSprites(estado, direccion).getImagen(), centroX, centroY);
                }
                else {
                    estado = 5;
                    DibujoDebug.dibujarImagen(g, hs.getSprites(estado, direccion).getImagen(), centroX, centroY);

                }

            }
            else {
                // Si han pasado 3 segundos o más, detener la animación
                preparado = false;

            }

        }

    }

    public boolean enCombate() {
        // Obtiene las armas equipadas por el jugador
        Arma arma1 = getAe().getArma1();
        Arma arma2 = getAe().getArma2();

        // Verifica si alguna de las armas no es nula
        if (arma1 != null || arma2 != null) {
            // Si al menos una de las armas no es nula, el jugador está preparado para el combate
            preparado = true;
        }
        // Devuelve el estado de preparación para el combate
        return preparado;
    }

    private void dibujarAlcance(Graphics g) {
        // Dibuja un rectángulo relleno que representa el alcance de ataque del jugador
        DibujoDebug.dibujarRectanguloRelleno(g, alcanceActual.get(0), Color.red);
    }

    private void calcularAlcanceAtaque() {
        // Calcula el alcance de ataque en función de las armas equipadas por el jugador
        if (ae.getArma1() != null) {
            // Si el jugador tiene equipada un arma en la primera ranura, calcula su alcance
            alcanceActual = ae.getArma1().getAlcance(this);
        }
        else if (ae.getArma1() == null && ae.getArma2() != null && !(ae.getArma2() instanceof SinArma)) {
            // Si el jugador no tiene un arma en la primera ranura pero sí en la segunda, y esta no es un arma sin alcance, calcula su alcance
            alcanceActual = ae.getArma2().getAlcance(this);
        }
    }

    public void cambiarHojaSprites() {
        // Cambia la hoja de sprites del jugador según el arma equipada en la primera ranura
        if (getAe().getArma1() != null) {
            hs = getAe().getArma1().getHojaArma();
        }
    }

    public void dibujarDanhoRecibido(Graphics g, int puntoX, int puntoY) {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarDanho;

        // Calcula la posición Y en función de la velocidadCaminar de subida
        int posY = puntoY - (int) (VELOCIDAD_SUBIDA_DANHO * tiempoTranscurrido);

        // Asegura que la posición Y no sea menor que el límite inferior
        posY = (int) Math.max(posY, puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20);

        // Calcula la opacidad en función del tiempo transcurrido
        float opacidad = 1.0f - (float) tiempoTranscurrido / DURACION_MOSTRAR_DANHO;

        // Asegura que la opacidad esté en el rango [0, 1]
        opacidad = Math.max(0.0f, Math.min(1.0f, opacidad));

        if (mostrarDanho) {

            Color colorDanho = new Color(1.0f, 0.0f, 0.0f, opacidad);

            if (danhoPorGolpe <= 0) {
                colorDanho = new Color(0.0f, 1.0f, 0.0f, opacidad);
            }
            g.setColor(colorDanho);
            DibujoDebug.dibujarString(g, Float.toString(danhoPorGolpe), puntoX, posY);
        }

        // Muestra el texto de "ESQUIVADO" si es necesario
        if (mostrarEvadido) {
            Color colorEvadido = new Color(0.0f, 1.0f, 0.0f, opacidad);  // Cambiado a verde
            g.setColor(colorEvadido);
            DibujoDebug.dibujarString(g, "ESQUIVADO!", puntoX, posY - 10);
        }

        // Muestra el texto de "BLOQUEADO" si es necesario
        if (mostrarBloqueado && !mostrarEvadido) {
            Color colorBloqueado = new Color(0.0f, 1.0f, 0.0f, opacidad);  // Cambiado a verde
            g.setColor(colorBloqueado);
            DibujoDebug.dibujarString(g, "BLOQUEADO!", puntoX, posY - 10);
        }

        // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar la información
        if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
            mostrarDanho = false;
            mostrarEvadido = false;
            mostrarBloqueado = false;
        }
    }

    public void dibujarCuracionRecibida(Graphics g, int puntoX, int puntoY) {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMostrarCuracion;

        long indiceImagen = tiempoTranscurrido / 250;

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

            if (indiceImagen % 2 == 0) {
                DibujoDebug.dibujarImagen(g, hCuracion.getSprites(0, 0).getImagen(), puntoX, puntoY - 20);
            }
            else if (indiceImagen % 2 == 1) {
                DibujoDebug.dibujarImagen(g, hCuracion.getSprites(1, 0).getImagen(), puntoX, puntoY - 20);
            }
            else {
                DibujoDebug.dibujarImagen(g, hCuracion.getSprites(2, 0).getImagen(), puntoX, puntoY - 20);
            }
            // Si ha pasado el tiempo de duración o el texto ha subido lo suficiente, deja de mostrar la información
            if (tiempoTranscurrido >= DURACION_MOSTRAR_DANHO || posY <= puntoY - DURACION_MOSTRAR_DANHO * VELOCIDAD_SUBIDA_DANHO - 20) {
                mostrarCuracion = false;
            }
        }
    }

    @Override
    public void curarVida(int montoCuracion) {
        // Registra el tiempo en que se mostró la curación
        tiempoInicioMostrarCuracion = System.currentTimeMillis();

        // Verifica si la vida actual es menor que la vida máxima
        if (ga.getVida() < ga.getVidaMaxima()) {
            // Incrementa la vida actual con el monto de curación
            ga.setVida(ga.getVida() + montoCuracion);
            // Asegura que la vida no supere el máximo
            if (ga.getVida() > ga.getVidaMaxima()) {
                ga.setVida(ga.getVidaMaxima());
            }
            // Registra el monto de vida recuperado y muestra la curación
            montoRecuperado = montoCuracion;
            mostrarCuracion = true;
        }
    }

    public void perderVida(int ataqueEnemigo) {
        // Genera un número aleatorio para determinar si el jugador evade el ataque
        double numeroAleatorio = new Random().nextDouble() * 100 + 1;
        int danho = 0;

        // Verifica si el jugador evade el ataque
        mostrarEvadido = numeroAleatorio <= ga.getEvasion();
        if (mostrarEvadido) {
            // Registra el tiempo en que se mostró la evasión
            tiempoInicioMostrarDanho = System.currentTimeMillis();
        }
        else {
            // Calcula el daño recibido por el jugador
            tiempoInicioMostrarDanho = System.currentTimeMillis();
            danho = (int) ((ataqueEnemigo - (ataqueEnemigo * ga.getResFisica() / 100.0)) - ga.getDefensaFisica());
            danho = Math.max(danho, 0); // Asegura que el daño no sea negativo

            // Reduce la vida del jugador según el daño recibido
            ga.setVida(ga.getVida() - danho);
            danhoPorGolpe = (int) danho;
        }

        // Verifica si el ataque fue bloqueado
        mostrarBloqueado = danho <= 0;
        if (mostrarBloqueado) {
            // Registra el tiempo en que se mostró el bloqueo
            tiempoInicioMostrarDanho = System.currentTimeMillis();
        }

        // Registra el daño recibido y muestra la información de daño
        mostrarDanho = true;
        this.danhoRecibido += danho;
    }

    private void actualizarAnimacion() {
        // Lógica para determinar la animación del jugador
        if (!enMovimiento) {
            animacion = 1;
        }
        else {
            // Ajusta la velocidad de la animación aquí
            int velocidadAnimacion = 1; // Ajusta este valor según sea necesario

            // Incrementa la animación en cada ciclo
            animacion += velocidadAnimacion;

            // Ajusta la animación para que esté dentro del rango adecuado
            animacion %= 60;

            // Determina el estado de la animación basado en la animación actual
            if (animacion <= 60 && animacion > 50) {
                estado = 0; // Estado normal
            }
            else if (animacion <= 50 && animacion > 40) {
                estado = 1; // Estado normal
            }
            else if (animacion <= 40 && animacion > 30) {
                estado = 2; // Estado normal
            }
            else if (animacion <= 30 && animacion > 20) {
                estado = 0;
            }
            else if (animacion <= 20 && animacion > 10) {
                estado = 1;
            }
            else {
                estado = 2;
            }

            // Obtiene el sprite correspondiente basado en el estado y la dirección
            Sprite sprite = hs.getSprites(estado, direccion); // Sprite normal

            // Actualiza la imagen actual del jugador
            if (sprite != null) {
                imagenActual = sprite.getImagen();
            }
            else {
                // Manejo de caso en el que sprite es null
                // Puedes asignar una imagen por defecto, lanzar una excepción, etc.
                // En este ejemplo, asignaremos una imagen nula
                imagenActual = null;
            }
        }
    }

    /*private void cambiarAnimacionEstado() {
        if (animacion < 60) {
            animacion++;
        }
        else {
            animacion = 0;
        }

        if (animacion <= 45 && animacion >= 30) {
            estado = 1; // Estado normal
        }
        else if (animacion < 30 && animacion > 15) {
            estado = 2; // Estado normal
        }
        else if (animacion <= 15) {
            estado = 1; // Estado normal
        }
        else {
            estado = 0; // Estado normal
        }

    }*/
    private void determinarDireccion() {
        // Evaluar la velocidad en los ejes X e Y
        final int velocidadX = evaluarVelocidadX();
        final int velocidadY = evaluarVelocidadY();

        // Si no hay movimiento en ningún eje, salir del método
        if (velocidadX == 0 && velocidadY == 0) {
            return;
        }

        // Si hay movimiento solo en un eje, mover en esa dirección
        if ((velocidadX != 0 && velocidadY == 0)
                || (velocidadX == 0 && velocidadY != 0)) {
            mover(velocidadX, velocidadY);
        }
        else {
            // Si hay movimiento en ambos ejes (diagonal), determinar la dirección basándose en la última tecla presionada

            // Diagonal izquierda arriba
            if (velocidadX == -1 && velocidadY == -1) {
                if (GestorControles.teclado.izquierda.getUltimaPulsacion()
                        > GestorControles.teclado.arriba.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                }
                else {
                    mover(0, velocidadY);
                }
            }
            // Diagonal izquierda abajo
            if (velocidadX == -1 && velocidadY == 1) {
                if (GestorControles.teclado.izquierda.getUltimaPulsacion()
                        > GestorControles.teclado.abajo.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                }
                else {
                    mover(0, velocidadY);
                }
            }
            // Diagonal derecha arriba
            if (velocidadX == 1 && velocidadY == -1) {
                if (GestorControles.teclado.derecha.getUltimaPulsacion()
                        > GestorControles.teclado.arriba.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                }
                else {
                    mover(0, velocidadY);
                }
            }
            // Diagonal derecha abajo
            if (velocidadX == 1 && velocidadY == 1) {
                if (GestorControles.teclado.derecha.getUltimaPulsacion()
                        > GestorControles.teclado.abajo.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                }
                else {
                    mover(0, velocidadY);
                }
            }
        }
    }

    private void mover(int velocidadX, int velocidadY) {
        // Indicar que el personaje está en movimiento
        enMovimiento = true;

        // Cambiar la dirección del personaje según la velocidad en los ejes X e Y
        cambiarDireccion(velocidadX, velocidadY);

        // Verificar si el movimiento está dentro del mapa
        if (!fueraMapa(velocidadX, velocidadY)) {
            // Mover el personaje si no hay colisión en la dirección indicada
            if (velocidadX == -1 && !enColisionIzquierda(velocidadX)) {
                posicionX += velocidadX * velocidadCaminar;
                // Restar resistencia si el personaje está corriendo
                restarResistencia();
                return;
            }
            if (velocidadX == 1 && !enColisionDerecha(velocidadX)) {
                posicionX += velocidadX * velocidadCaminar;
                // Restar resistencia si el personaje está corriendo
                restarResistencia();
                return;
            }

            if (velocidadY == -1 && !enColisionArriba(velocidadY)) {
                posicionY += velocidadY * velocidadCaminar;
                // Restar resistencia si el personaje está corriendo
                restarResistencia();
                return;
            }
            if (velocidadY == 1 && !enColisionAbajo(velocidadY)) {
                posicionY += velocidadY * velocidadCaminar;
                // Restar resistencia si el personaje está corriendo
                restarResistencia();
            }
        }
    }

    private void transparentar() {
        // Iterar sobre las áreas de transparencia del mapa
        for (Rectangle rectangulo : ElementosPrincipales.mapa.areasTransparenciaActualizadas) {
            // Verificar si alguna de las áreas de transparencia intersecta con los límites del personaje
            if (LIMITE_ARRIBA.intersects(rectangulo) || LIMITE_ABAJO.intersects(rectangulo)
                    || LIMITE_IZQUIERDA.intersects(rectangulo) || LIMITE_DERECHA.intersects(rectangulo)) {
                // Si hay intersección, establecer la imagen actual del personaje
                imagenActual = ht.getSprites(estado, direccion).getImagen();
            }
        }
    }

    private void restarResistencia() {
        // Restar resistencia si el personaje está corriendo y aún tiene resistencia disponible
        if (GestorControles.teclado.corriendo && ga.getResistencia() > 0) {
            ga.setResistencia(ga.getResistencia() - 1);
        }
    }

    private boolean fueraMapa(final int velocidadX, final int velocidadY) {
        // Calcular la posición futura del personaje después de aplicar la velocidad
        int posicionFuturaX = (int) posicionX + velocidadX * (int) velocidadCaminar;
        int posicionFuturaY = (int) posicionY + velocidadY * (int) velocidadCaminar;

        // Obtener los bordes del mapa en la posición futura
        final Rectangle bordesMapa = ElementosPrincipales.mapa.getBordes(posicionFuturaX, posicionFuturaY);

        // Inicialmente, establecer fuera en true por defecto
        boolean fuera = true;

        // Verificar si los límites del personaje intersectan con los bordes del mapa
        if (LIMITE_ARRIBA.intersects(bordesMapa) || LIMITE_ABAJO.intersects(bordesMapa)
                || LIMITE_DERECHA.intersects(bordesMapa) || LIMITE_IZQUIERDA.intersects(bordesMapa)) {
            fuera = false; // Si hay intersección, establecer fuera en false
        }

        return fuera;
    }

    private boolean enColisionArriba(int velocidadY) {
        // Iterar sobre las áreas de colisión del mapa
        for (int r = 0; r < ElementosPrincipales.mapa.areasColisionActualizadas.size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.areasColisionActualizadas.get(r);

            // Calcular la posición futura en el eje Y
            int origenX = area.x;
            int origenY = area.y + velocidadY * (int) velocidadCaminar + 3 * (int) velocidadCaminar;

            // Crear un rectángulo que representa la posición futura
            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            // Verificar si hay colisión con el límite superior del personaje
            if (LIMITE_ARRIBA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean enColisionAbajo(int velocidadY) {
        // Iterar sobre las áreas de colisión del mapa
        for (int r = 0; r < ElementosPrincipales.mapa.areasColisionActualizadas.size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.areasColisionActualizadas.get(r);

            // Calcular la posición futura en el eje Y
            int origenX = area.x;
            int origenY = area.y + velocidadY * (int) velocidadCaminar - 3 * (int) velocidadCaminar;

            // Crear un rectángulo que representa la posición futura
            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            // Verificar si hay colisión con el límite inferior del personaje
            if (LIMITE_ABAJO.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean enColisionIzquierda(int velocidadX) {
        // Iterar sobre las áreas de colisión del mapa
        for (int r = 0; r < ElementosPrincipales.mapa.areasColisionActualizadas.size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.areasColisionActualizadas.get(r);

            // Calcular la posición futura en el eje X
            int origenX = area.x + velocidadX * (int) velocidadCaminar + 3 * (int) velocidadCaminar;
            int origenY = area.y;

            // Crear un rectángulo que representa la posición futura
            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            // Verificar si hay colisión con el límite izquierdo del personaje
            if (LIMITE_IZQUIERDA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean enColisionDerecha(int velocidadX) {
        // Iterar sobre las áreas de colisión del mapa
        for (int r = 0; r < ElementosPrincipales.mapa.areasColisionActualizadas.size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.areasColisionActualizadas.get(r);

            // Calcular la posición futura en el eje X
            int origenX = area.x + velocidadX * (int) velocidadCaminar - 3 * (int) velocidadCaminar;
            int origenY = area.y;

            // Crear un rectángulo que representa la posición futura
            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            // Verificar si hay colisión con el límite derecho del personaje
            if (LIMITE_DERECHA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private void cambiarDireccion(int velocidadX, int velocidadY) {
        //mov derecha
        if (velocidadX == 1) {
            direccion = 2;
        }
        //mov izquierda
        else if (velocidadX == -1) {
            direccion = 1;
        }
        //mov abajo
        else if (velocidadY == 1) {
            direccion = 0;
        }
        //mov arriba
        else if (velocidadY == -1) {
            direccion = 3;
        }

        sonidoCaminar1.reproducir(0.7f);
    }

    private int evaluarVelocidadX() {
        int velocidadX = 0;

        // Si se presiona la tecla de movimiento hacia la izquierda y no hacia la derecha
        if (GestorControles.teclado.izquierda.estaPulsada() && !GestorControles.teclado.derecha.estaPulsada()) {
            velocidadX = -1; // Se establece la velocidad en -1 para mover hacia la izquierda
        }
        // Si se presiona la tecla de movimiento hacia la derecha y no hacia la izquierda
        else if (!GestorControles.teclado.izquierda.estaPulsada() && GestorControles.teclado.derecha.estaPulsada()) {
            velocidadX = 1; // Se establece la velocidad en 1 para mover hacia la derecha
        }
        return velocidadX;
    }

    private int evaluarVelocidadY() {
        int velocidadY = 0;

        // Si se presiona la tecla de movimiento hacia arriba y no hacia abajo
        if (GestorControles.teclado.arriba.estaPulsada() && !GestorControles.teclado.abajo.estaPulsada()) {
            velocidadY = -1; // Se establece la velocidad en -1 para mover hacia arriba
        }
        // Si se presiona la tecla de movimiento hacia abajo y no hacia arriba
        else if (!GestorControles.teclado.arriba.estaPulsada() && GestorControles.teclado.abajo.estaPulsada()) {
            velocidadY = 1; // Se establece la velocidad en 1 para mover hacia abajo
        }
        return velocidadY;
    }

    public void morir() {
        // Verificar si la vida actual es menor o igual a 0
        if (getVidaActual() <= 0) {
            estaVivo = false; // Establecer que el personaje ha muerto
        }
        else {
            estaVivo = true; // Establecer que el personaje está vivo
        }
    }

    public void isDead() {
        // Verificar si la vida actual es menor o igual a 0
        if (getVidaActual() <= 0) {
            estaVivo = false; // Establecer que el personaje ha muerto
            ga.setVida(0); // Establecer la vida del personaje en 0
        }
    }

    public void calcularPesoActual() {
        double peso = 0;
        // Iterar sobre los objetos del inventario
        for (Objeto objeto : ElementosPrincipales.inventario.getListaObjetos()) {
            if (objeto != null) {
                peso += objeto.getPeso() * objeto.getCantidad(); // Calcular el peso total del inventario
            }
        }
        ga.setPesoActual(peso); // Establecer el peso actual del personaje
        sobrepeso = ga.getPesoActual() >= ga.getLimitePeso(); // Verificar si el personaje está en sobrepeso
    }

    public int calcularAtqFisico() {
        int ataqueTotal = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Arma arma1 = ae.getArma1(); // Obtener el arma principal equipada por el personaje

        if (arma1 != null) {
            ataqueTotal = arma1.getAtaque(); // Obtener el valor de ataque del arma principal
        }

        Objeto[] objetosEquipados = {ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto instanceof Joya) {
                Joya joya = (Joya) objeto;
                ataqueTotal += joya.getAtkF(); // Sumar el valor de ataque físico de las joyas equipadas
            }
        }

        return ataqueTotal; // Devolver el ataque total del personaje
    }

    public int calcularAtqMagico() {
        int ataqueTotal = 0;
        int atqMagico = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Arma arma1 = ae.getArma1(); // Obtener el arma principal equipada por el personaje
        Arma arma2 = ae.getArma2(); // Obtener el arma secundaria equipada por el personaje

        // Verificar si el personaje tiene dos armas equipadas que no sean de dos manos
        if (arma1 != null && arma2 != null && !(arma1 instanceof ArmaDosManos) && !(arma2 instanceof ArmaDosManos)) {
            ataqueTotal = arma1.getAtaque() + arma2.getAtaque(); // Sumar el ataque de ambas armas
        }
        // Si solo tiene un arma equipada y es de dos manos
        else if (arma1 != null && arma1 instanceof ArmaDosManos) {
            ataqueTotal = arma1.getAtaque(); // Obtener el ataque del arma de dos manos
        }
        // Si solo tiene un arma secundaria equipada y es de dos manos
        else if (arma2 != null && arma2 instanceof ArmaDosManos) {
            ataqueTotal = arma2.getAtaque(); // Obtener el ataque del arma de dos manos
        }

        Objeto[] objetosEquipados = {ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto instanceof Joya) {
                Joya joya = (Joya) objeto;
                ataqueTotal += joya.getAtkF(); // Sumar el valor de ataque físico de las joyas equipadas
                atqMagico += joya.getAtkM(); // Sumar el valor de ataque mágico de las joyas equipadas
            }
        }

        return atqMagico + (int) (ataqueTotal / 3); // Devolver el ataque mágico total del personaje
    }

    public double calcularEvasion() {
        double eva = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Objeto[] objetosEquipados = {ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto != null) {
                if (objeto instanceof Joya) {
                    Joya joya = (Joya) objeto;
                    if (joya.getEva() > 0) {
                        eva += joya.getEva(); // Sumar la evasión proporcionada por las joyas equipadas
                    }
                }
            }
        }
        return eva; // Devolver la evasión total del personaje
    }

    public double calcularResM() {
        double resM = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Objeto[] objetosEquipados = {ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto != null) {
                if (objeto instanceof Joya) {
                    Joya joya = (Joya) objeto;
                    if (joya.getResM() > 0) {
                        resM += joya.getResM(); // Sumar la resistencia mágica proporcionada por las joyas equipadas
                    }
                }
            }
        }
        return resM; // Devolver la resistencia mágica total del personaje
    }

    public double calcularResF() {
        double resF = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Objeto[] objetosEquipados = {ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto != null) {
                if (objeto instanceof Joya) {
                    Joya joya = (Joya) objeto;
                    if (joya.getResF() > 0) {
                        resF += joya.getResF(); // Sumar la resistencia física proporcionada por las joyas equipadas
                    }
                }
            }
        }
        return resF; // Devolver la resistencia física total del personaje
    }

    public double calcularCrit() {
        double crit = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Objeto[] objetosEquipados = {ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto != null) {
                if (objeto instanceof Joya) {
                    Joya joya = (Joya) objeto;
                    if (joya.getCrit() > 0) {
                        crit += joya.getCrit(); // Sumar la probabilidad de crítico proporcionada por las joyas equipadas
                    }
                }
            }
        }
        return crit; // Devolver la probabilidad de crítico total del personaje
    }

    public int calcularDefensaF() {
        int defensaF = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Objeto[] objetosEquipados = {ae.getArmaduraMedia(), ae.getCasco(), ae.getGuante(), ae.getBota(),
            ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto != null) {
                if (objeto instanceof Joya) {
                    defensaF += ((Joya) objeto).getDefensaF(); // Sumar la defensa física proporcionada por las joyas equipadas
                }
                else if (objeto instanceof Armadura) {
                    defensaF += ((Armadura) objeto).getDefensaF(); // Sumar la defensa física proporcionada por las armaduras equipadas
                }
            }
        }

        return defensaF; // Devolver la defensa física total del personaje
    }

    public int calcularDefensaM() {
        int defensaM = 0;
        AlmacenEquipo ae = getAe(); // Obtener el almacenamiento de equipo del personaje

        Objeto[] objetosEquipados = {ae.getArmaduraMedia(), ae.getCasco(), ae.getGuante(), ae.getBota(),
            ae.getCollar(), ae.getAccesorio(), ae.getAnillo1(), ae.getAnillo2()};

        // Iterar sobre los objetos equipados por el personaje
        for (Objeto objeto : objetosEquipados) {
            if (objeto != null) {
                if (objeto instanceof Joya) {
                    defensaM += ((Joya) objeto).getDefensaM(); // Sumar la defensa mágica proporcionada por las joyas equipadas
                }
                else if (objeto instanceof Armadura) {
                    defensaM += ((Armadura) objeto).getDefensaM(); // Sumar la defensa mágica proporcionada por las armaduras equipadas
                }
            }
        }

        return defensaM; // Devolver la defensa mágica total del personaje
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public void setPosicionX(double posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicionY(double posicionY) {
        this.posicionY = posicionY;
    }

    public Rectangle getLIMITE_ARRIBA() {
        return LIMITE_ARRIBA;
    }

    public int getPosicionXInt() {
        return (int) posicionX;
    }

    public int getPosicionYInt() {
        return (int) posicionY;
    }

    public int getANCHO_JUGADOR() {
        return ANCHO_JUGADOR;
    }

    public int getALTO_JUGADOR() {
        return ALTO_JUGADOR;
    }

    @Override
    public void setMana(int mana) {
        ga.setMana(mana);
    }

    public void setEstadoAnimacion(int estadoAnimacion) {
        this.estadoAnimacion = estadoAnimacion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public void setVelocidad(double velocidad) {
        this.velocidadCaminar = velocidad;
        if (this.velocidadCaminar <= 0) {
            this.velocidadCaminar = 0.1;
        }
    }

    public void setEnMovimiento(boolean enMovimiento) {
        this.enMovimiento = enMovimiento;
    }

    public void setHs(HojaSprites hs) {
        this.hs = hs;
    }

    public void setImagenActual(BufferedImage imagenActual) {
        this.imagenActual = imagenActual;
    }

    public void setAnimacion(int animacion) {
        this.animacion = animacion;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setRecuperacion(int recuperacion) {
        this.recuperacion = recuperacion;
    }

    public void setRecuperacionVida(int recuperacionVida) {
        this.recuperacionVida = recuperacionVida;
    }

    public void setRecuperado(boolean recuperado) {
        this.recuperado = recuperado;
    }

    @Override
    public int getMana() {
        return ga.getMana();
    }

    public int getEstadoAnimacion() {
        return estadoAnimacion;
    }

    public int getDireccion() {
        return direccion;
    }

    public double getVelocidad() {
        return velocidadCaminar;
    }

    public boolean isEnMovimiento() {
        return enMovimiento;
    }

    public HojaSprites getHs() {
        return hs;
    }

    public BufferedImage getImagenActual() {
        return imagenActual;
    }

    public Rectangle getLIMITE_ABAJO() {
        return LIMITE_ABAJO;
    }

    public Rectangle getLIMITE_IZQUIERDA() {
        return LIMITE_IZQUIERDA;
    }

    public Rectangle getLIMITE_DERECHA() {
        return LIMITE_DERECHA;
    }

    public int getAnimacion() {
        return animacion;
    }

    public int getEstado() {
        return estado;
    }

    public int getRecuperacion() {
        return recuperacion;
    }

    public int getRecuperacionVida() {
        return recuperacionVida;
    }

    public boolean isRecuperado() {
        return recuperado;
    }

    public boolean isSobrepeso() {
        return sobrepeso;
    }

    public void setSobrepeso(boolean sobrepeso) {
        this.sobrepeso = sobrepeso;
    }

    public AlmacenEquipo getAe() {
        return ae;
    }

    public TipoObjeto getTipoObjetoFromEquipoActual(Objeto objeto) {
        for (Objeto equipo : getAe().getEquipoActual()) {
            if (equipo.equals(objeto)) {
                return equipo.getTipoObjeto();
            }
        }
        return TipoObjeto.NINGUNO; // Ajusta según tus necesidades
    }

    public ArrayList<Rectangle> getAlcanceActual() {
        return alcanceActual;
    }

    public void setAe(AlmacenEquipo ae) {
        this.ae = ae;
    }

    public void setAlcanceActual(ArrayList<Rectangle> alcanceActual) {
        this.alcanceActual = alcanceActual;
    }

    @Override
    public int getVidaMaxima() {
        return ga.getVidaMaxima();
    }

    public int getManaMaximo() {
        return ga.getManaMaximo();
    }

    public Rectangle getArea() {
        final int puntoX = (int) posicionX;

        final int puntoY = (int) posicionY;

        return new Rectangle(puntoX + 8, puntoY + 8, Constantes.LADO_SPRITE / 2 + 8, Constantes.LADO_SPRITE / 2 + 8);
    }

    @Override
    public int getInteligencia() {
        return ga.getInteligencia();
    }

    @Override
    public int getVidaActual() {
        return ga.getVida();
    }

    @Override
    public void setVidaActual(int vidaActual) {
        ga.setVida(vidaActual);
    }

    public AccesoRapido getAr() {
        return ar;
    }

    public boolean isMostrarCuracion() {
        return mostrarCuracion;
    }

    public void setMostrarCuracion(boolean mostrarCuracion) {
        this.mostrarCuracion = mostrarCuracion;
    }

    public int getMontoRecuperado() {
        return montoRecuperado;
    }

    public void setMontoRecuperado(int montoRecuperado) {
        this.montoRecuperado = montoRecuperado;
    }

    public GestorAtributos getGa() {
        return ga;
    }

    public Cronometro getCronometro() {
        return cronometro;
    }

    @Override
    public void recibirDanho(int danho, TipoObjeto tipoDeHabilidad) {
        // Verificar el tipo de habilidad que inflige el daño
        if (tipoDeHabilidad == TipoObjeto.FISICO) {
            // Calcular el daño recibido considerando la resistencia física y la defensa física del personaje
            danho = (int) (danho * (100 - ga.getResFisica()) / 100.0 - ga.getDefensaFisica());
        }
        else if (tipoDeHabilidad == TipoObjeto.MAGIA) {
            // Calcular el daño recibido considerando la resistencia mágica y la defensa mágica del personaje
            danho = (int) (danho * (100 - ga.getResMagica()) / 100.0 - ga.getDefensaMagica());
        }
        // Actualizar la vida actual del personaje restando el daño recibido
        this.setVidaActual(ga.getVida() - danho);
    }

}
