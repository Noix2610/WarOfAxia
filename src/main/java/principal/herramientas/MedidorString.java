/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 * @author GAMER ARRAX
 */
public class MedidorString {

    public static int medirAnchoPixeles(Graphics g, String s) {

        FontMetrics fm = g.getFontMetrics();

        return fm.stringWidth(s);
    }

    public static int medirAltoPixeles(final Graphics g, final String s) {
        FontMetrics fm = g.getFontMetrics();

        return (int) fm.getLineMetrics(s, g).getHeight();
    }

    public static int medirAnchoPixeles(FontMetrics fm, String s) {
        return fm.stringWidth(s);
    }

    public static int medirAltoPixeles(FontMetrics fm) {
        return (int) fm.getHeight();
    }
    
    public static int medirAnchoTextoCentrado(Graphics g, String texto, int x, int ancho) {
        FontMetrics metrics = g.getFontMetrics();
        return x + (ancho - metrics.stringWidth(texto)) / 2;
    }

    public static int calcularAlturaTexto(Graphics g, String texto) {
        FontMetrics metrics = g.getFontMetrics();
        return metrics.getHeight();
    }
}
