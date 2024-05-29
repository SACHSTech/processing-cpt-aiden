import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A class that's set up to help play music in other classes
 * @author A. Razack
 *
 */

// A class that plays music
public class Audio {
    Clip clip;
    AudioInputStream audioInputStream;

    public Audio(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public void play(int loopCount) {
        clip.start();
        if (loopCount == 1) {
            clip.loop(0);
        } else if (loopCount == 0) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }


    public void stop() {
        clip.stop();
        clip.close();
        System.out.println("Playback stopped.");
    }

    public static Audio[] initializeAudioArray(String[] audioPaths) {
        Audio[] audioArray = new Audio[audioPaths.length];
        for (int i = 0; i < audioPaths.length; i++) {
            try {
                audioArray[i] = new Audio(audioPaths[i]);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
                audioArray[i] = null;
            }
        }
        return audioArray;
    }
    
    public boolean isRunning() {
        return clip.isRunning();
    }
    
    public void setMicrosecondPosition(long microseconds) {
        clip.setMicrosecondPosition(microseconds);
    }
}