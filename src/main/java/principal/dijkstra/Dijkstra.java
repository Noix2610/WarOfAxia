/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal.dijkstra;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import principal.Constantes;
import principal.entes.Enemigo;

/**
 * Implementación del algoritmo de Dijkstra para encontrar el camino más corto en un mapa. Este código implementa el
 * algoritmo de Dijkstra para encontrar el camino más corto en un mapa. Utiliza nodos que representan cada posición en
 * el mapa, evaluando la distancia desde un punto de origen hasta todos los demás puntos en el mapa.
 */
public final class Dijkstra {

    // Variables para el tamaño del mapa en tiles
    private int anchoMapaTiles;
    private int altoMapaTiles;

    // Listas de nodos del mapa
    private ArrayList<Nodo> nodosMapa;
    private ArrayList<Nodo> pendientes;
    private ArrayList<Nodo> visitados;
    private boolean constructor = true;

    // Constructor de la clase
    public Dijkstra(final Point centroCalculo, int anchoMapaTiles, int altoMapaTiles, ArrayList<Rectangle> zonasSolidas) {
        this.anchoMapaTiles = anchoMapaTiles;
        this.altoMapaTiles = altoMapaTiles;
        nodosMapa = new ArrayList<>();

        // Crear nodos para cada posición en el mapa
        for (int y = 0; y < altoMapaTiles; y++) {
            for (int x = 0; x < anchoMapaTiles; x++) {
                final int lado = Constantes.LADO_SPRITE;
                final Rectangle ubicacionNodo = new Rectangle(x * lado, y * lado, lado, lado);

                boolean transitable = true;

                // Comprobar si la posición del nodo está dentro de una zona sólida
                for (Rectangle area : zonasSolidas) {
                    if (ubicacionNodo.intersects(area)) {
                        transitable = false;
                        break;
                    }
                }

                // Si el nodo es transitable, se añade a la lista de nodos del mapa
                if (!transitable) {
                    continue;
                }
                Nodo nodo = new Nodo(new Point(x, y), Double.MAX_VALUE);
                nodosMapa.add(nodo);
            }
        }

        // Inicializar listas de nodos pendientes y visitados
        pendientes = new ArrayList<>(nodosMapa);

        // Reiniciar y evaluar el algoritmo de Dijkstra
        reiniciarYEvaluar(centroCalculo);
        constructor = false;
    }

    // Método para obtener las coordenadas del nodo coincidente con un punto
    public Point getCoordenadasNodoCoincidente(final Point puntoJugador) {
        Rectangle rectanguloPuntoExacto = new Rectangle(puntoJugador.x / Constantes.LADO_SPRITE,
                puntoJugador.y / Constantes.LADO_SPRITE, 1, 1);
        Point puntoExacto = null;

        // Buscar el nodo cuya área intersecta con el punto
        for (Nodo nodo : nodosMapa) {
            if (nodo.getArea().intersects(rectanguloPuntoExacto)) {
                puntoExacto = new Point(rectanguloPuntoExacto.x, rectanguloPuntoExacto.y);
                return puntoExacto;
            }
        }
        return puntoExacto;
    }

    // Método para clonar los nodos del mapa a los nodos pendientes
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

    // Método para reiniciar y evaluar el algoritmo de Dijkstra
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

    // Método para definir el centro de cálculo en los nodos pendientes
    private void definirCentroCalculoPendientes(final Point centroCalculo) {
        for (Nodo nodo : pendientes) {
            if (nodo.getPosicion().equals(centroCalculo)) {
                nodo.setDistancia(0.0);
            }
        }
    }

    // Método para evaluar la heurística global del algoritmo de Dijkstra
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

    // Método para evaluar la heurística de los vecinos de un nodo dado
    private void evaluarEuristicaVecinos(final Nodo nodo) {
        int inicialY = nodo.getPosicion().y;
        int inicialX = nodo.getPosicion().x;

        final double DISTANCIA_DIAGONAL = 1.42412;

        for (int y = inicialY - 1; y < inicialY + 2; y++) {
            for (int x = inicialX - 1; x < inicialX + 2; x++) {
                // Dentro del rango del mapa
                if (x <= -1 || y <= -1 || x >= anchoMapaTiles || y >= altoMapaTiles) {
                    continue;
                }

                // Omitir el propio nodo
                if (inicialX == x && inicialY == y) {
                    continue;
                }

                // Nodo existe en la posición
                int indiceNodo = getIndiceNodoPorPosicionPendientes(new Point(x, y));
                if (indiceNodo == -1) {
                    continue;
                }

                // Solo cambiamos la distancia si es transitable y si no ha sido cambiada
                if (pendientes.get(indiceNodo).getDistancia() == Double.MAX_VALUE - 1) {
                    double distancia;
                    // Distancia recta vs diagonal
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

    // Método para obtener los nodos vecinos de un nodo dado
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

    // Método para encontrar el siguiente nodo para un enemigo
    public Nodo enconcontrarSiguienteNodoParaEnemigo(Enemigo enemigo) {
        ArrayList<Nodo> nodosAfectados = new ArrayList<>();

        Nodo siguienteNodo = null;

        // Buscar nodos afectados por la posición del enemigo
        for (Nodo nodo : visitados) {
            if (enemigo.getAreaPosicional().intersects(nodo.getAreaPixeles())) {
                nodosAfectados.add(nodo);
            }
        }

        // Si solo hay un nodo afectado, buscar sus nodos vecinos
        if (nodosAfectados.size() == 1) {
            Nodo nodoBase = nodosAfectados.get(0);
            nodosAfectados = getNodosVecinos(nodoBase);
        }

        // Encontrar el siguiente nodo basado en la distancia más corta
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

    // Método para obtener el índice de un nodo por su posición en la lista de pendientes
    private int getIndiceNodoPorPosicionPendientes(final Point posicion) {
        for (Nodo nodo : pendientes) {
            if (nodo.getPosicion().equals(posicion)) {
                return pendientes.indexOf(nodo);
            }
        }
        return -1;
    }

    // Método para obtener el índice de un nodo por su posición en la lista de visitados
    private int getIndiceNodoPorPosicionVisitados(final Point posicion) {
        for (Nodo nodo : visitados) {
            if (nodo.getPosicion().equals(posicion)) {
                return visitados.indexOf(nodo);
            }
        }
        return -1;
    }

    // Métodos para obtener las listas de nodos visitados, pendientes y del mapa
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
