/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.dijkstra;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import principal.Constantes;
import principal.entes.Enemigo;

/**
 *
 * @author GAMER ARRAX
 */
public final class Dijkstra {
    
    private int anchoMapaTiles;
    private int altoMapaTiles;
    
    private ArrayList<Nodo> nodosMapa;
    private ArrayList<Nodo> pendientes;
    private ArrayList<Nodo> visitados;
    private boolean constructor = true;
    
    public Dijkstra(final Point centroCalculo, int anchoMapaTiles, int altoMapaTiles, ArrayList<Rectangle> zonasSolidas) {
        this.anchoMapaTiles = anchoMapaTiles;
        this.altoMapaTiles = altoMapaTiles;
        nodosMapa = new ArrayList<>();
        
        for (int y = 0; y < altoMapaTiles; y++) {
            for (int x = 0; x < anchoMapaTiles; x++) {
                final int lado = Constantes.LADO_SPRITE;
                final Rectangle ubicacionNodo = new Rectangle(x * lado, y * lado, lado, lado);
                
                boolean transitable = true;
                
                for (Rectangle area : zonasSolidas) {
                    if (ubicacionNodo.intersects(area)) {
                        transitable = false;
                        break;
                    }
                }
                
                if (!transitable) {
                    continue;
                }
                Nodo nodo = new Nodo(new Point(x, y), Double.MAX_VALUE);
                nodosMapa.add(nodo);
            }
        }
        pendientes = new ArrayList<>(nodosMapa);
        
        reiniciarYEvaluar(centroCalculo);
        constructor = false;
    }
    
    public Point getCoordenadasNodoCoincidente(final Point puntoJugador) {
        Rectangle rectanguloPuntoExacto = new Rectangle(puntoJugador.x / Constantes.LADO_SPRITE,
                puntoJugador.y / Constantes.LADO_SPRITE, 1, 1);
        Point puntoExacto = null;
        
        for (Nodo nodo : nodosMapa) {
            if (nodo.getArea().intersects(rectanguloPuntoExacto)) {
                puntoExacto = new Point(rectanguloPuntoExacto.x, rectanguloPuntoExacto.y);
                return puntoExacto;
            }
        }
        return puntoExacto;
    }
    
    private ArrayList<Nodo> clonarNodosMapaANodosPendientes() {
        ArrayList<Nodo> nodosClonados = new ArrayList<>();
        
        for (Nodo nodo : nodosMapa) {
            Point posicion = nodo.getPosicion();
            double distancia = nodo.getDistancia();
            
            Nodo nodoClonado = new Nodo(posicion, distancia);
            nodosClonados.add(nodoClonado);
        }
        return nodosClonados;
    }
    
    public void reiniciarYEvaluar(final Point centroCalculo) {
        if (!constructor) {
            if (visitados.isEmpty()) {
                clonarNodosMapaANodosPendientes();
            }
            else {
                pendientes = new ArrayList<>(visitados);
                for (Nodo nodo : pendientes) {
                    nodo.setDistancia(Double.MAX_VALUE);
                }
            }
        }
        definirCentroCalculoPendientes(centroCalculo);
        visitados = new ArrayList<>();
        evaluarEuristicaGlobal();
    }
    
    private void definirCentroCalculoPendientes(final Point centroCalculo) {
        for (Nodo nodo : pendientes) {
            if (nodo.getPosicion().equals(centroCalculo)) {
                nodo.setDistancia(0.0);
            }
        }
    }
    
    private void evaluarEuristicaGlobal() {
        while (!pendientes.isEmpty()) {
            int cambios = 0;
            
            for (Iterator<Nodo> iterador = pendientes.iterator(); iterador.hasNext();) {
                Nodo nodo = iterador.next();
                
                if (nodo.getDistancia() == Double.MAX_VALUE) {
                    continue;
                }
                else {
                    evaluarEuristicaVecinos(nodo);
                    visitados.add(nodo);
                    iterador.remove();
                    cambios++;
                }
            }
            if (cambios == 0) {
                break;
            }
        }
    }
    
    private void evaluarEuristicaVecinos(final Nodo nodo) {
        int inicialY = nodo.getPosicion().y;
        int inicialX = nodo.getPosicion().x;
        
        final double DISTANCIA_DIAGONAL = 1.42412;
        
        for (int y = inicialY - 1; y < inicialY + 2; y++) {
            for (int x = inicialX - 1; x < inicialX + 2; x++) {
                //dentro del rango del mapa
                if (x <= -1 || y <= -1 || x >= anchoMapaTiles || y >= altoMapaTiles) {
                    continue;
                }

                //omitimos el propio nodo
                if (inicialX == x && inicialY == y) {
                    continue;
                }

                //nodo existe en la posicion
                int indiceNodo = getIndiceNodoPorPosicionPendientes(new Point(x, y));
                if (indiceNodo == -1) {
                    continue;
                }

                //solo cambiamos distancia si es transitable y si no ha sido cambiada
                if (pendientes.get(indiceNodo).getDistancia() == Double.MAX_VALUE - 1) {
                    double distancia;
                    //distancia recta vs diagonal
                    if (inicialX != x && inicialY != y) {
                        distancia = DISTANCIA_DIAGONAL;
                    }
                    else {
                        distancia = 1;
                    }
                    
                    pendientes.get(indiceNodo).setDistancia(nodo.getDistancia() + distancia);
                }
            }
        }
    }
    
    private ArrayList<Nodo> getNodosVecinos(Nodo nodo) {
        int inicialY = nodo.getPosicion().y;
        int inicialX = nodo.getPosicion().x;
        
        ArrayList<Nodo> nodosVecinos = new ArrayList<>();
        
        for (int y = inicialY - 1; y < inicialY + 2; y++) {
            for (int x = inicialX - 1; x < inicialX + 2; x++) {
                if (x <= -1 || y <= -1 || x >= anchoMapaTiles || y >= altoMapaTiles) {
                    continue;
                }
                
                if (inicialX == x && inicialY == y) {
                    continue;
                }
                
                int indiceNodo = getIndiceNodoPorPosicionVisitados(new Point(x, y));
                
                if (indiceNodo == -1) {
                    continue;
                }
                
                nodosVecinos.add(visitados.get(indiceNodo));
                
            }
        }
        return nodosVecinos;
    }
    
    public Nodo enconcontrarSiguienteNodoParaEnemigo(Enemigo enemigo) {
        ArrayList<Nodo> nodosAfectados = new ArrayList<>();
        
        Nodo siguienteNodo = null;
        
        for (Nodo nodo : visitados) {
            if (enemigo.getAreaPosicional().intersects(nodo.getAreaPixeles())) {
                nodosAfectados.add(nodo);
            }
        }
        if (nodosAfectados.size() == 1) {
            Nodo nodoBase = nodosAfectados.get(0);
            nodosAfectados = getNodosVecinos(nodoBase);
        }
        
        for (int i = 0; i < nodosAfectados.size(); i++) {
            if (i == 0) {
                siguienteNodo = nodosAfectados.get(0);
            }
            else {
                if (siguienteNodo.getDistancia() > nodosAfectados.get(i).getDistancia()) {
                    siguienteNodo = nodosAfectados.get(i);
                }
            }
        }
        return siguienteNodo;
    }
    
    private int getIndiceNodoPorPosicionPendientes(final Point posicion) {
        for (Nodo nodo : pendientes) {
            if (nodo.getPosicion().equals(posicion)) {
                return pendientes.indexOf(nodo);
            }
        }
        return -1;
    }
    
    private int getIndiceNodoPorPosicionVisitados(final Point posicion) {
        for (Nodo nodo : visitados) {
            if (nodo.getPosicion().equals(posicion)) {
                return visitados.indexOf(nodo);
            }
        }
        return -1;
    }
    
    public ArrayList<Nodo> getVisitados() {
        return visitados;
    }
    
    public ArrayList<Nodo> getPendientes() {
        return pendientes;
    }

    public ArrayList<Nodo> getNodosMapa() {
        return nodosMapa;
    }
    
    
    
}
