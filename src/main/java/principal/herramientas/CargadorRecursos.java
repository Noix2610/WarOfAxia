/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.herramientas;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author GAMER ARRAX
 */
public class CargadorRecursos {

    public static BufferedImage cargarImagenCompatibleOpaca(final String ruta) {
        Image imagen = null;
        try {
            imagen = ImageIO.read(CargadorRecursos.class.getResource(ruta));
        }
        catch (IOException ex) {
            Logger.getLogger(CargadorRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage imagenAcelerada = gc.createCompatibleImage(imagen.getWidth(null),
                imagen.getHeight(null), Transparency.TRANSLUCENT);

        Graphics g = imagenAcelerada.getGraphics();
        g.drawImage(imagen, 0, 0, null);
        g.dispose();

        return imagenAcelerada;
    }

    public static BufferedImage cargarImagenCompatibleTranslucida(final String ruta) {

        try {
            // Usa ClassLoader para cargar el recurso desde el classpath
            InputStream entradaBytes = CargadorRecursos.class.getResourceAsStream(ruta);

            if (entradaBytes == null) {
                throw new IllegalArgumentException("No se pudo encontrar el recurso en la ruta: " + ruta);
            }

            Image imagen = ImageIO.read(entradaBytes);

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage imagenAcelerada = gc.createCompatibleImage(imagen.getWidth(null),
                    imagen.getHeight(null), Transparency.TRANSLUCENT);

            Graphics g = imagenAcelerada.getGraphics();
            g.drawImage(imagen, 0, 0, null);
            g.dispose();

            return imagenAcelerada;
        }
        catch (IOException ex) {
            // Loggear o imprimir un mensaje de error
            ex.printStackTrace();
            // Puedes lanzar una excepción personalizada si es necesario
            throw new RuntimeException("Error al cargar la imagen desde la ruta: " + ruta, ex);
        }
    }

    public static String leerArchivoTexto(final String ruta) {
        String contenido = "";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream entradaBytes = classLoader.getResourceAsStream(ruta);

        if (entradaBytes == null) {
            System.err.println("No se pudo abrir el flujo de entrada para el archivo: " + ruta);
            return contenido;
        }

        try (BufferedReader lector = new BufferedReader(new InputStreamReader(entradaBytes, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                contenido += linea + "\n";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return contenido;
    }

    public static Font cargarFuente(final String ruta, float tamanho) {
        Font fuente = null;
        InputStream entradaBytes = ClassLoader.getSystemResourceAsStream(ruta);
        if (entradaBytes == null) {
            System.err.println("No se pudo abrir el flujo de entrada para la fuente.");
            return null; // o manejar el error de alguna manera
        }

        try {
            // Cargar la fuente con un GraphicsEnvironment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fuente = Font.createFont(Font.TRUETYPE_FONT, entradaBytes);
            ge.registerFont(fuente);

            // Derivar la fuente con el tamaño proporcionado
            fuente = fuente.deriveFont(tamanho);
        }
        catch (FontFormatException | IOException ex) {
            Logger.getLogger(CargadorRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fuente;
    }

}
