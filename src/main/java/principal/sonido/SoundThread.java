package principal.sonido;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundThread extends Thread {

    private String filename;
    private Clip clip;
    private float volumeScale; // Variable para ajuste de volumen
    private boolean isPlaying;

    public SoundThread(String filename) {
        try {
            this.filename = filename;
            clip = AudioSystem.getClip();
        }
        catch (LineUnavailableException ex) {
            Logger.getLogger(SoundThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        isPlaying = true;
        try {
            InputStream in = getClass().getResourceAsStream("/sonidos/" + filename + ".wav");

            if (in != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();

                // Espera hasta que la reproducción del sonido termine
                while (clip.isActive()) {
                    Thread.sleep(80);
                }
            }
            else {
                System.err.println("No se pudo cargar el recurso de sonido: " + filename);
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            isPlaying = false;
        }
    }

    // Método para iniciar la reproducción del sonido
    public void reproducir(float volumen) {
        if (!isPlaying && clip != null && !clip.isActive()) {
            // Vuelve a cargar y reproducir el sonido
            new Thread(() -> {
                try {
                    InputStream in = getClass().getResourceAsStream("/sonidos/" + filename + ".wav");

                    if (in != null) {
                        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
                        clip = AudioSystem.getClip();
                        clip.open(ais);
                        // Ajustar el volumen antes de iniciar la reproducción
                        ajustarVolumen(clip, volumen);
                        // Iniciar la reproducción
                        clip.start();
                    }
                    else {
                        System.err.println("No se pudo cargar el recurso de sonido: " + filename);
                    }
                }
                catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // Método para repetir la reproducción del sonido de forma continua
    public void repetir(float volumen) {
        if (!isPlaying && clip != null && !clip.isActive()) {
            // Vuelve a cargar y reproducir el sonido
            new Thread(() -> {
                try {
                    InputStream in = getClass().getResourceAsStream("/sonidos/" + filename + ".wav");

                    if (in != null) {
                        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
                        clip = AudioSystem.getClip();
                        clip.open(ais);
                        // Ajustar el volumen antes de iniciar la reproducción
                        ajustarVolumen(clip, volumen);
                        // Iniciar la reproducción y el bucle
                        clip.start();
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                    else {
                        System.err.println("No se pudo cargar el recurso de sonido: " + filename);
                    }
                }
                catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // Método para ajustar el volumen en un Clip dado
    private void ajustarVolumen(Clip clip, float volumen) {
        if (clip != null) {
            // Obtener el control de volumen
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Ajustar el volumen (en dB)
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volumen) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    // Método para obtener la duración del sonido en microsegundos
    public long getDuracion() {
        return clip.getMicrosecondLength();
    }

    // Método para detener la reproducción del sonido
    public void detener() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Detiene la reproducción del sonido
            isPlaying = false;
        }
    }
    
    public void cambiarArchivo(String nuevoArchivo) {
        detener(); // Detener la reproducción actual antes de cambiar el archivo
        this.filename = nuevoArchivo;
    }
}
