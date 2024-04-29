/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.interfaz_usuario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.habilidades.Habilidad;
import principal.herramientas.DibujoDebug;
import principal.inventario.consumibles.Consumible;
import principal.sprites.HojaSprites;

/**
 *
 * @author GAMER ARRAX
 */
public class MenuInferior {

    int xActual;
    private final ArrayList<Rectangle> ranuraObjetos;
    private final Rectangle areaInventario;
    private final BufferedImage areaInvent;
    private final HojaSprites hojaMenuInferior;
    public static HojaSprites bordesRanuras;
    private Rectangle bordeAreaInventario;
    public static long tiempoReutilizacionCuracion;
    int altoMenu;
    Rectangle ranura1;

    private Color negroDesaturado;
    private Color rojoclaro;
    private Color rojoOscuro;
    private Color verdeClaro;
    private Color verdeOscuro;
    private Color azulClaro;
    private Color azulOscuro;
    private Color rosaOscuro;
    private Color rosaClaro;

    public MenuInferior() {
        altoMenu = Constantes.LADO_SPRITE * 2;
        negroDesaturado = new Color(23, 23, 23);
        rojoclaro = new Color(255, 0, 0);
        rojoOscuro = new Color(150, 0, 0);
        verdeClaro = new Color(0, 255, 0);
        verdeOscuro = new Color(150, 255, 0);
        azulClaro = new Color(0, 200, 255);
        azulOscuro = new Color(0, 132, 168);
        rosaClaro = new Color(255, 0, 150);
        rosaOscuro = new Color(150, 0, 50);

        areaInventario = new Rectangle(0, Constantes.ALTO_JUEGO/*360*/ - altoMenu/*64*/,
                Constantes.ANCHO_JUEGO/*720*/, altoMenu/*64*/);
        hojaMenuInferior = new HojaSprites("/fondos/bordeMenuInferior.png", 720, 64, true);
        bordesRanuras = new HojaSprites("/fondos/bordeSkills.png", 39, true);
        areaInvent = hojaMenuInferior.getSprites(0).getImagen();
        bordeAreaInventario = new Rectangle(areaInventario.x, areaInventario.y - 1, areaInventario.width, 1);
        tiempoReutilizacionCuracion = 0;
        xActual = Constantes.ANCHO_JUEGO / 3 - 42;
        ranura1 = new Rectangle(xActual, areaInventario.y + 16, 32, 32);
        ranuraObjetos = new ArrayList<>();
        llenarListaDeRanuras();
    }

    public void dibujar(final Graphics2D g) {

        dibujarAreaInventario(g);
        dibujarBarraVitalidad(g);
        dibujarBarraMana(g);
        dibujarBarraResistencia(g);
        dibujarBarraExperiencia(g);
        dibujarRanuraObjetos(g);
    }

    public void dibujarAreaInventario(final Graphics2D g) {

        g.drawImage(areaInvent, areaInventario.x, areaInventario.y, areaInventario.width, areaInventario.height, null);
        // Dibuja el fondo con el color calculado
        //DibujoDebug.dibujarRectanguloRelleno(g, areaInventario, Color.BLACK);

        //DibujoDebug.dibujarRectanguloRelleno(g, bordeAreaInventario, Color.white);
    }

    private void dibujarBarraVitalidad(final Graphics g) {
        final int medidadVertical = 4;
        final int anchoTotal = 100;
        final int anchoInteractivo = anchoTotal * ElementosPrincipales.jugador.getVidaActual() / ElementosPrincipales.jugador.getVidaMaxima();

        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidadVertical * 3,
                anchoInteractivo, medidadVertical, rojoclaro);
        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidadVertical * 4,
                anchoInteractivo, medidadVertical, rojoclaro);
        g.setColor(Color.WHITE);

        DibujoDebug.dibujarString(g, "VIT: ", areaInventario.x + 10, areaInventario.y + medidadVertical * 5 - 1);
        DibujoDebug.dibujarString(g, "" + ElementosPrincipales.jugador.getVidaActual(), anchoTotal + 45, areaInventario.y + medidadVertical * 5 - 1);
    }

    public void dibujarBarraMana(final Graphics g) {
        final int medidaVertical = 4;
        final int anchoTotal = 100;
        final int anchoInteractivo = anchoTotal * ElementosPrincipales.jugador.getMana() / ElementosPrincipales.jugador.getManaMaximo();

        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 6,
                anchoInteractivo, medidaVertical, azulClaro);
        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 7,
                anchoInteractivo, medidaVertical, azulOscuro);
        g.setColor(Color.WHITE);
        DibujoDebug.dibujarString(g, "MNA: ", areaInventario.x + 10, areaInventario.y + medidaVertical * 8 - 1);
        DibujoDebug.dibujarString(g, "" + ElementosPrincipales.jugador.getMana(), anchoTotal + 45, areaInventario.y + medidaVertical * 8 - 1);
    }

    public void dibujarBarraResistencia(Graphics g) {
        final int medidaVertical = 4;
        final int anchoTotal = 100;
        final int anchoInteractivo = anchoTotal * ElementosPrincipales.jugador.getGa().getResistencia()
                / ElementosPrincipales.jugador.getGa().getResistenciaMaxima();

        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 9,
                anchoInteractivo, medidaVertical, verdeClaro);
        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 10,
                anchoInteractivo, medidaVertical, verdeOscuro);
        g.setColor(Color.WHITE);
        DibujoDebug.dibujarString(g, "RES: ", areaInventario.x + 10, areaInventario.y + medidaVertical * 11 - 1);
        DibujoDebug.dibujarString(g, "" + ElementosPrincipales.jugador.getGa().getResistencia(), anchoTotal + 45, areaInventario.y + medidaVertical * 11 - 1);
    }

    public void dibujarBarraExperiencia(final Graphics g) {
        final int medidaVertical = 4;
        final int anchoTotal = 100;

        // Asegurar que la experiencia máxima sea mayor que cero para evitar divisiones entre cero
        int experienciaMaxima = Math.max(1, ElementosPrincipales.jugador.getGa().getExperienciaMaxima());

        // Calcular el ancho de la barra en función de la experiencia actual y máxima
        final int anchoInteractivo = anchoTotal * ElementosPrincipales.jugador.getGa().getExperiencia() / experienciaMaxima;

        // Dibujar la barra de experiencia
        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 12,
                anchoInteractivo, medidaVertical, rosaClaro);
        DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 13,
                anchoInteractivo, medidaVertical, rosaOscuro);

        // Dibujar etiquetas y porcentaje
        g.setColor(Color.WHITE);
        DibujoDebug.dibujarString(g, "EXP: ", areaInventario.x + 10, areaInventario.y + medidaVertical * 14 - 1);
        DibujoDebug.dibujarString(g, "" + ElementosPrincipales.jugador.getGa().getExperiencia() * 100
                / ElementosPrincipales.jugador.getGa().getExperienciaMaxima()
                + "%", anchoTotal + 45, areaInventario.y + medidaVertical * 14 - 1);
    }

    public void dibujarRanuraObjetos(Graphics g) {

        g.setColor(Color.BLACK);
        int i = 1;
        for (Rectangle ranura : ranuraObjetos) {

            DibujoDebug.dibujarImagen(g, bordesRanuras.getSprites(0).getImagen(), ranura.x - 3, ranura.y - 4);
            if (i > 9) {
                DibujoDebug.dibujarString(g, "0", ranuraObjetos.get(i - 1).x + 13, areaInventario.height + areaInventario.y - 8, Color.BLACK);
            }
            else {
                DibujoDebug.dibujarString(g, "" + i, ranuraObjetos.get(i - 1).x + 13, areaInventario.height + areaInventario.y - 8, Color.BLACK);
            }
            i++;
        }
        for (int j = 0; j < ElementosPrincipales.jugador.getAr().getAccesosEquipados().length; j++) {
            Object objeto = ElementosPrincipales.jugador.getAr().getAccesoEquipado(j);
            if (objeto != null && (objeto instanceof Consumible)) {
                Consumible consumible = (Consumible) objeto;
                Rectangle ranura = ranuraObjetos.get(j); // Obtener la ranura correspondiente

                // Dibujar el consumible en la ranura
                DibujoDebug.dibujarImagen(g, consumible.getSprite().getImagen(), ranura.x, ranura.y);
                Color colorTexto = Color.white;
                if (consumible.getCantidad() < 4) {
                    colorTexto = Color.red;
                }
                DibujoDebug.dibujarString(g, "" + consumible.getCantidad(), ranura.x + ranura.width - 5,
                        ranura.y + ranura.height - 1, colorTexto);
            }
            else if (objeto != null && (objeto instanceof Habilidad)) {
                Habilidad habilidad = (Habilidad) objeto;
                Rectangle ranura = ranuraObjetos.get(j); // Obtener la ranura correspondiente

                // Dibujar el consumible en la ranura
                DibujoDebug.dibujarImagen(g, habilidad.getImagenActual(), ranura.x, ranura.y);
                Color colorTexto = Color.white;
                int tiempoRestante = habilidad.getTiempoRestante();
                if (tiempoRestante > 0) {
                    if (tiempoRestante < 10) {
                        DibujoDebug.dibujarString(g, "0" + tiempoRestante, ranura.x + 9, ranura.y + 20, colorTexto);
                    }
                    else {
                        DibujoDebug.dibujarString(g, "" + tiempoRestante, ranura.x + 10, ranura.y + 20, colorTexto);
                    }

                }
            }
        }

    }

    private void actualizarPantalla() {
        // Agrega aquí la lógica necesaria para actualizar la pantalla.
        // Esto podría incluir el repintado de componentes, dependiendo de cómo estés manejando la interfaz gráfica.
        // Por ejemplo, si estás usando Swing, puedes llamar a repaint().
        // Si estás usando algún otro framework o librería, la actualización podría ser diferente.
    }

    private void llenarListaDeRanuras() {
        for (int i = 0; i <= 9; i++) {
            // Ahora se incrementa para dibujar de izquierda a derecha
            Rectangle ranura = new Rectangle(xActual, areaInventario.y + 16, 32, 32);
            ranuraObjetos.add(ranura);
            xActual = xActual + 46;
        }
    }

    public static void setTiempoReutilizacionCuracion(long tiempoReutilizacionCuracion) {
        MenuInferior.tiempoReutilizacionCuracion = tiempoReutilizacionCuracion;
    }
}
