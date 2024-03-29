/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.inventario;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GAMER ARRAX
 */
public class RegistroTiendas {

    public static ArrayList<String> obtenerTienda(int idTienda) {
        ArrayList<String> listaIdsObjetos = new ArrayList<>();
        switch (idTienda) {
            case 1:
                listaIdsObjetos = new ArrayList<>(List.of("500", "502", "503", "504", "505",
                        "400", "401", "402", "600", "601", "616", "617",
                        "632", "648", "1", "2", "3", "4"));
                break;
            case 2:
                listaIdsObjetos = new ArrayList<>(List.of("600", "601", "616", "617", "632", "648"));
                break;
        }
        return listaIdsObjetos;
    }
}
