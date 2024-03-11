/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.Point;

/**
 *
 * @author GAMER ARRAX
 */
public class CalculadoraDistancia {

    public static double getDistanciaEntrePuntos(final Point p1, final Point p2) {

        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }
}
