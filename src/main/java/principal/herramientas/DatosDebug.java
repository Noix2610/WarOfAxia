package principal.herramientas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Clase que gestiona los datos de depuración y los dibuja en pantalla.
 */
public class DatosDebug {

    private static ArrayList<String> datos = new ArrayList<String>(); // Lista que almacena los datos de depuración

    /**
     * Agrega un dato a la lista de datos de depuración.
     *
     * @param dato El dato a agregar.
     */
    public static void recogerDatos(final String dato) {
        datos.add(dato);
    }

    /**
     * Dibuja los datos de depuración en pantalla.
     *
     * @param g El contexto gráfico en el que se dibujarán los datos.
     */
    public static void dibujarDatos(final Graphics g) {
        g.setColor(Color.white); // Establece el color de los datos en blanco

        for (int i = 0; i < datos.size(); i++) {
            // Dibuja cada dato en una posición específica en pantalla
            DibujoDebug.dibujarString(g, datos.get(i), 20, 40 + i * 10);
        }

        // Una vez que se han dibujado los datos, se vacía la lista para la próxima actualización
        datos.clear();
    }

    /**
     * Vacía la lista de datos de depuración.
     */
    public static void vaciarDatos() {
        datos.clear();
    }

}
