/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.maquinaestado.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.herramientas.MedidorString;

/**
 *
 * @author GAMER ARRAX
 */
public class MenuBestiario extends SeccionMenu {

    final Rectangle panelBestiario = new Rectangle(em.FONDO.x + margenGeneral*3,
            barraPeso.y + barraPeso.height + margenGeneral,
            Constantes.ANCHO_JUEGO / 3, Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);

    final Rectangle panelDescripcion = new Rectangle(panelBestiario.x + panelBestiario.width + margenGeneral*3,
            panelBestiario.y, panelBestiario.width, panelBestiario.height);

    final Rectangle panelAtributos = new Rectangle(panelDescripcion.x + panelDescripcion.width + margenGeneral,
            panelBestiario.y, 132, panelDescripcion.height);
    final Rectangle titularPanelBestiario = new Rectangle(panelBestiario.x, panelBestiario.y,
            panelBestiario.width, 24);

    final Rectangle titularPanelDescripcion = new Rectangle(panelDescripcion.x, panelDescripcion.y,
            panelDescripcion.width, 24);

    final Rectangle titularPanelAtributos = new Rectangle(panelAtributos.x, panelAtributos.y,
            panelAtributos.width, 24);

    public MenuBestiario(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
        super(nombreSeccion, etiquetaMenu, em);
    }

    @Override
    public void actualizar() {
    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        dibujarLimitePeso(g);
        dibujarPaneles(g);

    }

    private void dibujarPaneles(final Graphics g) {
        dibujarPanelBestiario(g, panelBestiario, titularPanelBestiario, "BESTIARIO");
        dibujarPanelDescripcion(g, panelDescripcion, titularPanelDescripcion, "DESCRIPCION");

    }

    private void dibujarPanelBestiario(Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);

    }

    private void dibujarPanelDescripcion(Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        dibujarPanel(g, panel, titularPanel, nombrePanel);
    }

    private void dibujarPanel(final Graphics g, final Rectangle panel, final Rectangle titularPanel,
            final String nombrePanel) {
        g.setColor(Color.DARK_GRAY);
        DibujoDebug.dibujarRectanguloContorno(g, panel);
        DibujoDebug.dibujarRectanguloRelleno(g, titularPanel);
        g.setColor(Color.white);
        DibujoDebug.dibujarString(g, nombrePanel, new Point(
                panel.x + titularPanel.width / 2 - MedidorString.medirAnchoPixeles(g, nombrePanel) / 2,
                panel.y + titularPanel.height - MedidorString.medirAltoPixeles(g, nombrePanel) - 4));
    }
    private void dibujarElementosBestiario(final Graphics g,final Rectangle panel){
        
    }

    @Override
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
        else if (porcentajePeso >= 100) {
            color = Color.red;
            carga = "SOBREPESO";
            x = barraPeso.x - 60;
        }
        DibujoDebug.dibujarString(g, carga, x, barraPeso.y + 8, color);
        DibujoDebug.dibujarRectanguloRelleno(g, barraPeso, Color.gray);

        DibujoDebug.dibujarRectanguloRelleno(g, contenidoBarra, color);
    }

}
