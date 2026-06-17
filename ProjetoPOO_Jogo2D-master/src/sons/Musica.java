package sons;

import javax.sound.sampled.*;
import javax.swing.*;
import java.net.URL;

public class Musica {

    private Clip clip;
    private FloatControl volumeControl;
    private boolean musicaLigada = true;
    private int volumeAtual = 3;

    public void tocar(String caminho) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
            URL url = getClass().getResource(caminho);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audio);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            setVolume(volumeAtual);
            if (musicaLigada) {
                clip.start();
            } else {
                // Se estiver desligada, NÃO inicia o clip
                clip.stop();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o audio: " + e);
        }
    }

    public void tocarUmaVez(String caminho) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
            URL url = getClass().getResource(caminho);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audio);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(volumeAtual);
            clip.start();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o audio: " + e);
        }
    }

    public void ligar() {
        musicaLigada = true;
        if (clip != null) {
            clip.start();
        }
    }

    public void desligar() {
        musicaLigada = false;
        if (clip != null) {
            clip.stop();
        }
    }

    public void setVolume(int nivel) {
        this.volumeAtual = nivel;
        if (volumeControl == null) return;
        float volume;
        switch (nivel) {
            case 0:
                volume = -80f;
                break;
            case 1:
                volume = -30f;
                break;
            case 2:
                volume = -20f;
                break;
            case 3:
                volume = -12f;
                break;
            case 4:
                volume = -5f;
                break;
            default:
                volume = 0f;
                break;
        }
        volumeControl.setValue(volume);
    }

    public boolean isMusicaLigada() {
        return musicaLigada;
    }

    public int getVolumeAtual() {
        return volumeAtual;
    }

    public void parar() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}