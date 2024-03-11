/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.mapas;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author GAMER ARRAX
 */
public class Salida {

    private static ArrayList<Salida> salidas = new ArrayList<>();

    private Point puntoInicioSiguienteMapa;
    public static Point puntoInicialSiguiente = new Point(0,0);
    public static String mapaSiguiente;
    private static Point puntoSalida;
    private String nombreSiguienteMapa;

    public Salida(Point puntoInicioSiguienteMapa, Point puntoSalida ,String siguienteMapa) {
        this.puntoInicioSiguienteMapa = puntoInicioSiguienteMapa;
        this.puntoSalida = puntoSalida;
        this.nombreSiguienteMapa = siguienteMapa;

    }

    public Salida() {
    }

    public Point getPuntoInicioSiguienteMapa() {
        return puntoInicioSiguienteMapa;
    }

    public Point getPuntoSalida() {
        return puntoSalida;
    }

    public void setPuntoSalida(Point puntoSalida) {
        this.puntoSalida = puntoSalida;
    }

    public  static ArrayList<Salida> getSalidas() {
        return salidas;
    }

    public static Point getPuntoInicialSiguiente() {
        return puntoInicialSiguiente;
    }

    public String getNombreSiguienteMapa() {
        return nombreSiguienteMapa;
    }
    
    
    
    
    
    
    

}
