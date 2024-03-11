/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author GAMER ARRAX
 */
public class DatosDebug {

    private static ArrayList<String> datos = new ArrayList<String>();

    public static void recogerDatos(final String dato) {
        datos.add(dato);
    }

    public static void dibujarDatos(final Graphics g) {
        g.setColor(Color.white);

        for (int i = 0; i < datos.size(); i++) {
            DibujoDebug.dibujarString(g, datos.get(i), 20, 40 + i * 10);
        }
        datos.clear();
    }
    
    public static void vaciarDatos(){
        datos.clear();
    }

}
