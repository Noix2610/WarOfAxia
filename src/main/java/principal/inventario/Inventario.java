/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario;

import java.util.ArrayList;
import principal.habilidades.Habilidad;
import principal.inventario.armaduras.Armadura;
import principal.inventario.armas.Arma;
import principal.inventario.armas.ArmaDosManos;
import principal.inventario.armas.ArmaUnaMano;
import principal.inventario.consumibles.Claves;
import principal.inventario.consumibles.Consumible;
import principal.inventario.joyas.Joya;

/**
 *
 * @author GAMER ARRAX
 */
public class Inventario {

    public final ArrayList<Objeto> objetos;
    public final ArrayList<Habilidad> habilidades;
    public final ArrayList<Objeto> objetosTienda;
    public int dinero;

    public Inventario() {

        objetos = new ArrayList<>();
        habilidades = new ArrayList<>();
        objetosTienda = new ArrayList<>();
        dinero = 20000;
        

    }

    public boolean incrementarObjeto(final Objeto objeto, final int cantidad) {
        boolean incrementado = false;

        for (Objeto objetoActual : objetos) {
            if (objetoActual.getId() == objeto.getId()) {
                objetoActual.incrementarCantidad(cantidad);
                incrementado = true;
                break;
            }
        }

        return incrementado;
    }

    public void recogerObjetos(final ObjetoUnicoTiled out) {
        if (objetoExiste(out.getObjeto())) {
            incrementarObjeto(out.getObjeto(), out.getObjeto().getCantidad());
        }
        if (!objetoExiste(out.getObjeto())) {
            objetos.add(out.getObjeto());
        }

    }

    /*public void recogerObjetos(final ContenedorObjetos contenedor) {
        for (Objeto objeto : contenedor.getObjetos()) {
            if (objetoExiste(objeto)) {
                incrementarObjeto(objeto, objeto.getCantidad());
            }
            if(!objetoExiste(objeto)){
                objetos.add(objeto);
            }
        }
    }*/
    public boolean objetoExiste(final Objeto objeto) {
        boolean existe = false;

        for (Objeto objetoActual : objetos) {
            if (objetoActual.getId() == objeto.getId()) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public ArrayList<Objeto> getConsumibles() {
        ArrayList<Objeto> consumibles = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof Consumible) {
                consumibles.add(objeto);
            }
        }
        return consumibles;
    }

    public ArrayList<Habilidad> getHabilidades() {
        return habilidades;
    }

    public ArrayList<Objeto> getClaves() {
        ArrayList<Objeto> claves = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof Claves) {
                claves.add(objeto);
            }
        }
        return claves;
    }

    public void setHabilidad(Habilidad habilidad) {
        habilidades.add(habilidad);
    }

    public ArrayList<Objeto> getEquipo() {
        ArrayList<Objeto> equipo = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof Arma) {
                equipo.add(objeto);
            }
            else if (objeto instanceof Armadura) {
                equipo.add(objeto);
            }
            else if (objeto instanceof Joya) {
                equipo.add(objeto);
            }
        }
        return equipo;
    }

    public ArrayList<Objeto> getArmas() {
        ArrayList<Objeto> armas = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof Arma) {
                armas.add(objeto);
            }
        }
        return armas;
    }
    
    public ArrayList<Objeto> getUnaMano(){
        ArrayList<Objeto> armas = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof ArmaUnaMano) {
                armas.add(objeto);
            }
        }
        return armas;
    }
    
    public ArrayList<Objeto> getDosManos(){
        ArrayList<Objeto> armas = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof ArmaDosManos) {
                armas.add(objeto);
            }
        }
        return armas;
    }

    public ArrayList<Objeto> getArmaduras() {
        ArrayList<Objeto> armaduras = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof Armadura) {
                armaduras.add(objeto);
            }
        }
        return armaduras;
    }

    public ArrayList<Objeto> getJoyas() {
        ArrayList<Objeto> joyas = new ArrayList<>();

        for (Objeto objeto : objetos) {
            if (objeto instanceof Joya) {
                joyas.add(objeto);
            }
        }
        return joyas;
    }

    public Objeto getObjeto(final int id) {
        for (Objeto objetoActual : objetos) {
            if (objetoActual.getId() == id) {
                return objetoActual;
            }
        }
        return null;
    }

    public ArrayList<Objeto> getListaObjetos() {
        return this.objetos;
    }

    public ArrayList<Objeto> getObjetosTienda() {
        return objetosTienda;
    }
    
    public void agregarObjeto(Objeto objeto){
        this.objetosTienda.add(objeto);
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }
    
    
    
    
    
    
}
