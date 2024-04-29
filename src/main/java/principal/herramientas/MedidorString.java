/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Clase que proporciona métodos para medir las dimensiones de una cadena de texto en píxeles.
 */
public class MedidorString {

    /**
     * Calcula el ancho en píxeles de una cadena de texto utilizando el objeto Graphics dado.
     *
     * @param g El objeto Graphics utilizado para medir el texto.
     * @param s La cadena de texto que se va a medir.
     * @return El ancho en píxeles de la cadena de texto.
     */
    public static int medirAnchoPixeles(Graphics g, String s) {
        FontMetrics fm = g.getFontMetrics();
        return fm.stringWidth(s);
    }

    /**
     * Calcula la altura en píxeles de una cadena de texto utilizando el objeto Graphics dado.
     *
     * @param g El objeto Graphics utilizado para medir el texto.
     * @param s La cadena de texto que se va a medir.
     * @return La altura en píxeles de la cadena de texto.
     */
    public static int medirAltoPixeles(final Graphics g, final String s) {
        FontMetrics fm = g.getFontMetrics();
        return (int) fm.getLineMetrics(s, g).getHeight();
    }

    /**
     * Calcula el ancho en píxeles de una cadena de texto utilizando el objeto FontMetrics dado.
     *
     * @param fm El objeto FontMetrics utilizado para medir el texto.
     * @param s La cadena de texto que se va a medir.
     * @return El ancho en píxeles de la cadena de texto.
     */
    public static int medirAnchoPixeles(FontMetrics fm, String s) {
        return fm.stringWidth(s);
    }

    /**
     * Calcula la altura en píxeles de una cadena de texto utilizando el objeto FontMetrics dado.
     *
     * @param fm El objeto FontMetrics utilizado para medir el texto.
     * @return La altura en píxeles de la cadena de texto.
     */
    public static int medirAltoPixeles(FontMetrics fm) {
        return (int) fm.getHeight();
    }

    /**
     * Calcula el ancho en píxeles de una cadena de texto centrada dentro de un área determinada.
     *
     * @param g El objeto Graphics utilizado para medir el texto.
     * @param texto La cadena de texto que se va a medir.
     * @param x La coordenada x del área donde se centrará el texto.
     * @param ancho El ancho del área donde se centrará el texto.
     * @return La coordenada x donde se debe dibujar el texto para que esté centrado.
     */
    public static int medirAnchoTextoCentrado(Graphics g, String texto, int x, int ancho) {
        FontMetrics metrics = g.getFontMetrics();
        return x + (ancho - metrics.stringWidth(texto)) / 2;
    }

    /**
     * Calcula la altura en píxeles de una cadena de texto utilizando el objeto Graphics dado.
     *
     * @param g El objeto Graphics utilizado para medir el texto.
     * @param texto La cadena de texto que se va a medir.
     * @return La altura en píxeles de la cadena de texto.
     */
    public static int calcularAlturaTexto(Graphics g, String texto) {
        FontMetrics metrics = g.getFontMetrics();
        return metrics.getHeight();
    }
}
