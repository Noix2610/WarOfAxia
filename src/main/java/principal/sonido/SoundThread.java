/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.sonido;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author GAMER ARRAX
 */
public class SoundThread implements Runnable {

    private String filename;
    private Clip clip;
    private float volumeScale; // Variable para ajuste de volumen

    public SoundThread(String filename) {
        try {
            this.filename = filename;
            clip = AudioSystem.getClip();
        }
        catch (LineUnavailableException ex) {
            Logger.getLogger(SoundThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
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
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void reproducir(float volumen) {
    if (clip != null && !clip.isActive()) {
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
                } else {
                    System.err.println("No se pudo cargar el recurso de sonido: " + filename);
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

    public void repetir(float volumen) {
        if (clip != null && !clip.isActive()) {
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

    public Long getDuracion() {
        return clip.getMicrosecondLength();
    }

    public void adjustVolume(AudioInputStream ais) throws IOException, LineUnavailableException {
        // Crear un control de volumen para el Clip
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Ajustar el volumen
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volumeScale) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
        else {
            System.out.println("El control de volumen no es compatible.");
        }
    }

    public void setVolumeScale(float volumeScale) {
        this.volumeScale = volumeScale;
    }

}
