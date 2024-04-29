/*
 * SeccionTienda.java
 * Clase abstracta que representa una sección de la tienda en el juego.
 */
package principal.maquinaestado.juego.menu_tienda;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.inventario.Objeto;

public abstract class SeccionTienda {

    protected int margenGeneral = 8;
    protected String nombreSeccion;
    protected Rectangle etiquetaMenu;
    protected Rectangle barraPeso;
    protected EstructuraTienda et;
    protected Objeto objetoSeleccionado;

    // Constructor
    public SeccionTienda(final String nombreSeccion, final Rectangle etiquetaMenu, final EstructuraTienda et) {
        this.nombreSeccion = nombreSeccion;
        this.etiquetaMenu = etiquetaMenu;
        int anchoBarra = 100;
        this.et = et;
        barraPeso = new Rectangle(Constantes.ANCHO_JUEGO - anchoBarra + margenGeneral - 20, et.BANNER_SUPERIOR.height + margenGeneral,
                100, 8);
    }

    public SeccionTienda() {
    }

    // Métodos abstractos que deben ser implementados por las clases hijas
    public abstract void actualizar();

    public abstract void dibujar(final Graphics g, final SuperficieDibujo sd);

    // Métodos de dibujo de etiquetas de secciones
    public void dibujarEtiquetaInactiva(final Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);
        DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 16, etiquetaMenu.y + 12, Color.black);
    }

    public void dibujarEtiquetaActiva(Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

        final Rectangle marcaActiva = new Rectangle(etiquetaMenu.x, etiquetaMenu.y, 5, etiquetaMenu.height);
        DibujoDebug.dibujarRectanguloRelleno(g, marcaActiva, new Color(0xff6700));

        DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 16, etiquetaMenu.y + 12, Color.black);
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

    // Método para obtener la etiqueta de menú escalada
    public Rectangle getEtiquetaMenuEscalada() {
        final int x = (int) (etiquetaMenu.x * Constantes.FACTOR_ESCALADO_X);
        final int y = (int) (etiquetaMenu.y * Constantes.FACTOR_ESCALADO_Y);
        final int ancho = (int) (etiquetaMenu.width * Constantes.FACTOR_ESCALADO_X);
        final int alto = (int) (etiquetaMenu.height * Constantes.FACTOR_ESCALADO_Y);

        final Rectangle etiquetaEscalada = new Rectangle(x, y, ancho, alto);

        return etiquetaEscalada;
    }

    // Método para dibujar la barra de peso
    protected void dibujarLimitePeso(final Graphics g) {
        ElementosPrincipales.jugador.calcularPesoActual();

        Color color = new Color(255, 255, 255);
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
            color = new Color(0, 255, 0);
        }
        else if (porcentajePeso >= 25 && porcentajePeso < 50) {
            color = Color.yellow;
        }
        else if (porcentajePeso >= 50 && porcentajePeso < 100) {
            color = new Color(255, 100, 50);
        }
        else {
            color = Color.red;
            carga = "SOBREPESO";
            x = barraPeso.x - 60;
        }
        DibujoDebug.dibujarString(g, carga, x, barraPeso.y + 7, color);
        DibujoDebug.dibujarRectanguloRelleno(g, barraPeso, Color.gray);

        DibujoDebug.dibujarRectanguloRelleno(g, contenidoBarra, color);
    }

    // Método para verificar si un objeto es no vendible
    public boolean objetoNoVendible(int idObjeto) {
        boolean flag = false;
        for (Objeto objetoEquipado : ElementosPrincipales.jugador.getAe().getEquipoActual()) {
            if (objetoEquipado.getId() == idObjeto) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    // Getters
    public Rectangle getEtiquetaMenu() {
        return etiquetaMenu;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }
}
