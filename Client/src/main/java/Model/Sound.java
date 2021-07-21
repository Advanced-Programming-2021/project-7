package Model;

import javafx.scene.media.AudioClip;

import java.util.ArrayList;

public class Sound {
    private static ArrayList<Sound> sounds = new ArrayList<>();
    private static ArrayList<Sound> activeSounds = new ArrayList<>();
    private static boolean isSoundMuted = false;
    private AudioClip sound;
    private String soundName;

    public Sound(String path, String name) {
        this.sound = new AudioClip(getClass().getResource(path).toExternalForm());
        this.sound.setVolume(0.1);
        this.soundName = name;
        sounds.add(this);
    }

    public static Sound getSoundByName(String name) {
        for (Sound sound : sounds) {
            if (sound.soundName.equals(name)) return sound;
        }
        return null;
    }

    public void playSound() {
        sound.setCycleCount(-1);
        sound.setVolume(0);
        activeSounds.add(this);
        if (isSoundMuted) return;
        sound.play();

    }

    public void playSoundOnce() {
        if (isSoundMuted) return;
        sound.setCycleCount(1);
        sound.play();
    }

    public void pauseSound() {
        sound.stop();
        activeSounds.remove(this);
    }

    public AudioClip getSound() {
        return this.sound;
    }
}