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
}
