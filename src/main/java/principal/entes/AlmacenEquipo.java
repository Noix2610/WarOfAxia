/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal.entes;

import java.util.ArrayList;
import principal.inventario.Objeto;
import principal.inventario.armaduras.Armadura;
import principal.inventario.joyas.Joya;
import principal.inventario.armas.Arma;

public class AlmacenEquipo {

    // Variables para almacenar los objetos equipados
    private Arma arma1;
    private Arma arma2;
    private Armadura armadura;
    private Armadura casco;
    private Armadura guante;
    private Armadura bota;
    private Joya collar;
    private Joya accesorio;
    private Joya anillo1;
    private Joya anillo2;

    // Lista para almacenar el equipo actual
    public ArrayList<Objeto> equipoActual;

    // Constructor que inicializa el equipo con objetos pasados como parámetros
    public AlmacenEquipo(Arma arma1, Arma arma2, Armadura armadura, Armadura casco, Armadura guante, Armadura bota,
            Joya collar, Joya accesorio, Joya anillo1, Joya anillo2) {
        this.arma1 = arma1;
        this.arma2 = arma2;
        this.armadura = armadura;
        this.casco = casco;
        this.guante = guante;
        this.bota = bota;
        this.collar = collar;
        this.accesorio = accesorio;
        this.anillo1 = anillo1;
        this.anillo2 = anillo2;

        // Inicializar la lista de equipo actual
        equipoActual = new ArrayList<>();
    }

    // Constructor que inicializa el equipo sin objetos
    public AlmacenEquipo() {
        // Inicializar la lista de equipo actual
        equipoActual = new ArrayList<>();
    }

    // Métodos para obtener y cambiar los objetos equipados
    public Arma getArma1() {
        return arma1;
    }

    public void cambiarArma1(Arma arma1) {
        this.arma1 = arma1;
    }

    public Arma getArma2() {
        return arma2;
    }

    public void cambiarArma2(Arma arma2) {
        this.arma2 = arma2;
    }

    public Armadura getArmaduraMedia() {
        return armadura;
    }

    public void setArmaduraMedia(Armadura armadura) {
        this.armadura = armadura;
    }

    public Armadura getCasco() {
        return casco;
    }

    public void setCasco(Armadura casco) {
        this.casco = casco;
    }

    public Armadura getGuante() {
        return guante;
    }

    public void setGuante(Armadura guante) {
        this.guante = guante;
    }

    public Armadura getBota() {
        return bota;
    }

    public void setBota(Armadura bota) {
        this.bota = bota;
    }

    public Joya getCollar() {
        return collar;
    }

    public void setCollar(Joya collar) {
        this.collar = collar;
    }

    public Joya getAccesorio() {
        return accesorio;
    }

    public void setAccesorio(Joya accesorio) {
        this.accesorio = accesorio;
    }

    public Joya getAnillo1() {
        return anillo1;
    }

    public void setAnillo1(Joya anillo1) {
        this.anillo1 = anillo1;

    }

    public Joya getAnillo2() {
        return anillo2;
    }

    public void setAnillo2(Joya anillo2) {
        this.anillo2 = anillo2;
    }

    public ArrayList<Objeto> getEquipoActual() {
        return equipoActual;
    }

    public void setEquipoActual(ArrayList<Objeto> equipoActual) {
        this.equipoActual = equipoActual;
    }

}
