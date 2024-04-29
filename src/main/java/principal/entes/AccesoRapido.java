/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal.entes;

/**
 * Clase que representa un conjunto de accesos rápidos para equipar objetos. Proporciona un conjunto de accesos rápidos
 * que permiten equipar objetos en el juego. Permite establecer y obtener objetos en índices específicos dentro del
 * conjunto, verificando si los índices son válidos y manejando casos de índices fuera de rango.
 */
public class AccesoRapido {

    public Object[] accesosEquipados; // Array para almacenar los objetos equipados

    // Constructor de la clase AccesoRapido
    public AccesoRapido() {
        // Inicializar el array con tamaño 10
        accesosEquipados = new Object[10];
    }

    // Método para obtener todos los objetos equipados
    public Object[] getAccesosEquipados() {
        return accesosEquipados;
    }

    // Método para establecer un objeto en un índice específico de los accesos rápidos
    public void setAccesosEquipados(Object objetoEquipar, int indice) {
        // Verificar si el índice es válido
        if (indice >= 0 && indice < accesosEquipados.length) {
            accesosEquipados[indice] = objetoEquipar;
        }
        else {
            // Manejar el caso cuando el índice no es válido (por ejemplo, lanzar una excepción)
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

    // Método para obtener un objeto equipado en un índice específico
    public Object getAccesoEquipado(int indice) {
        // Verificar si el índice es válido
        if (indice >= 0 && indice < accesosEquipados.length) {
            return accesosEquipados[indice];
        }
        else {
            // Manejar el caso cuando el índice no es válido (por ejemplo, lanzar una excepción)
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

}
