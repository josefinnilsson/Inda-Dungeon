package Code;
import javax.sound.sampled.*;

/**
 * This class represents a music player that play sound files in .wav format
 * in the background of the game.
 * Created by Josefin on 2016-05-18.
 */
public class Sound
{
    private Clip clip;

    public Sound(String fileName) {
        try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
                clip = AudioSystem.getClip();
                clip.open(ais);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    /**
     * Starts to play the sound file
     */
    public void play() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops playing the sound file
     */
        public void stop() {
            if (clip == null) return;
            clip.stop();
        }

    /**
     * Loops the sound file continuously
     */
    public void loop() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

