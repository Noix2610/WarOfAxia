/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.inventario.Objeto;

/**
 *
 * @author GAMER ARRAX
 */
public abstract class SeccionMenu {

    protected int margenGeneral = 8;
    protected String nombreSeccion;
    protected Rectangle etiquetaMenu;
    protected Rectangle barraPeso;
    protected EstructuraMenu em;
    protected Objeto objetoSeleccionado;

    public SeccionMenu(final String nombreSeccion, final Rectangle etiquetaMenu, final EstructuraMenu em) {
        this.nombreSeccion = nombreSeccion;
        this.etiquetaMenu = etiquetaMenu;
        int anchoBarra = 100;
        this.em = em;
        barraPeso = new Rectangle(Constantes.ANCHO_JUEGO - anchoBarra + margenGeneral - 20, em.BANNER_SUPERIOR.height + margenGeneral,
                100, 8);
        
    }

    public SeccionMenu() {
    }

    public abstract void actualizar();

    public abstract void dibujar(final Graphics g, final SuperficieDibujo sd);

    public void dibujarEtiquetaInactiva(final Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);
        DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);

    }

    public void dibujarEtiquetaActiva(Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

        final Rectangle marcaActiva = new Rectangle(etiquetaMenu.x, etiquetaMenu.y, 5, etiquetaMenu.height);
        DibujoDebug.dibujarRectanguloRelleno(g, marcaActiva, new Color(0xff6700));

        DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);

    }

    public void dibujarEtiquetaInactResaltada(final Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

        final Rectangle etiquetaResaltada = new Rectangle(etiquetaMenu.x + etiquetaMenu.width - 10, etiquetaMenu.y + 5,
                5, etiquetaMenu.height - 10);
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaResaltada, new Color(0x2a2a2a));
        DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);

    }

    public void dibujarEtiquetaActivaResaltada(final Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

        final Rectangle marcaActiva = new Rectangle(etiquetaMenu.x, etiquetaMenu.y, 5, etiquetaMenu.height);
        DibujoDebug.dibujarRectanguloRelleno(g, marcaActiva, new Color(0xff6700));
        final Rectangle etiquetaResaltada = new Rectangle(etiquetaMenu.x + etiquetaMenu.width - 10, etiquetaMenu.y + 5,
                5, etiquetaMenu.height - 10);
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaResaltada, new Color(0x2a2a2a));

        DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);
    }

    public Rectangle getEtiquetaMenuEscalada() {
        final int x = (int) (etiquetaMenu.x * Constantes.FACTOR_ESCALADO_X);
        final int y = (int) (etiquetaMenu.y * Constantes.FACTOR_ESCALADO_Y);
        final int ancho = (int) (etiquetaMenu.width * Constantes.FACTOR_ESCALADO_X);
        final int alto = (int) (etiquetaMenu.height * Constantes.FACTOR_ESCALADO_Y);

        final Rectangle etiquetaEscalada = new Rectangle(x, y, ancho, alto);

        return etiquetaEscalada;
    }

    protected void dibujarLimitePeso(final Graphics g) {
        Color color = null;
        String carga = "CARGA";
        int x = barraPeso.x - 35;

        // Calcular el porcentaje de peso actual en relación con el límite de peso
        double porcentajePeso = (ElementosPrincipales.jugador.getGa().getPesoActual() * 100)
                / ElementosPrincipales.jugador.getGa().getLimitePeso();

        // Calcular la longitud de la parte coloreada de la barra
        int longitudColoreada = (int) ((porcentajePeso / 100) * (barraPeso.width - 2));
        if (ElementosPrincipales.jugador.isSobrepeso()) {
            longitudColoreada = 100;
        }

        final Rectangle contenidoBarra = new Rectangle(barraPeso.x + 1, barraPeso.y + 1, longitudColoreada, barraPeso.height - 2);

        if (porcentajePeso < 25) {
            color = Color.green;
        }
        else if (porcentajePeso >= 25 && porcentajePeso < 50) {
            color = Color.yellow;
        }
        else if (porcentajePeso >= 50 && porcentajePeso < 75) {
            color = Constantes.COLOR_NARANJA;
        }
        else if(porcentajePeso >= 100 ){
            color = Color.red;
            carga = "SOBREPESO";
            x = barraPeso.x - 60;
        }
        DibujoDebug.dibujarString(g, carga, x, barraPeso.y + 8, color);
        DibujoDebug.dibujarRectanguloRelleno(g, barraPeso, Color.gray);

        DibujoDebug.dibujarRectanguloRelleno(g, contenidoBarra, color);
    }

    public Rectangle getEtiquetaMenu() {
        return etiquetaMenu;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

}
