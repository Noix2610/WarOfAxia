/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.entes;


/**
 *
 * @author GAMER ARRAX
 */
public class AccesoRapido {

    public Object[] accesosEquipados;

    public AccesoRapido() {
        // Inicializar el array con tamaño 10
        accesosEquipados = new Object[10];
    }

    public Object[] getAccesosEquipados() {
        return accesosEquipados;
    }

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

    public Object getAccesoEquipado(int indice) {
        if (indice >= 0 && indice < accesosEquipados.length) {
            return accesosEquipados[indice];
        }
        else {
            // Manejar el caso cuando el índice no es válido (por ejemplo, lanzar una excepción)
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

}
