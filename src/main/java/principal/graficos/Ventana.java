/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.graficos;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import principal.herramientas.CargadorRecursos;

/**
 *
 * @author GAMER ARRAX
 */
public class Ventana extends JFrame {

    private static final long serialVersionUID = 987654321L;
    private String titulo;

    private final ImageIcon icono;

    public Ventana(final String titulo, final SuperficieDibujo sd) {
        this.titulo = titulo;
        BufferedImage imagen = CargadorRecursos.cargarImagenCompatibleOpaca("/icons/icono.png");
        this.icono = new ImageIcon(imagen);

        configuracionVentana(sd);
    }

    private void configuracionVentana(final SuperficieDibujo sd) {
        setTitle(titulo);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(icono.getImage());
        setLayout(new BorderLayout());
        add(sd, BorderLayout.CENTER);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
