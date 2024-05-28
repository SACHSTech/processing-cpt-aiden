import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Aaron Chow
 * 05/15/24
 * Video Game Assignment
 * 
 * Upgrades List
Basic
Get player image to Face the Proper Direction as they move
Get player image to change if jumping, moving, and stopping 
Add a pause game button
Add a Menubar - with a number of options (New Game, Quit, etc.)
Add a separate Opening Screen before the game starts
Add Music
Add Sound Effects 
Add Doorways/Portals
Add New Levels (with different maps and settings)
Credit scene
Add additional character to chase the original character
Timing

    Advanced
Different levels leading to a Boss fight–actual fighting

 * Description 
 * This Zelda-inspired video game encourages exploration. As you progress through the levels,
 * you find yourself in a series of connected tunnels that lead you to the bossfight. Upon defeating the
 * boss, "The Calamity", you are taken to the end credits screen
 * 
 * Unsolved Problems
(it is 6am and i have not been able to solve these)
Capped movement speed (might get scrapped)
Alternating map switches (from 10 seconds onward)
Proper bone collision
Dialog box error upon collision
Improper credit screen
Go to Ending.java to see the proper credit screen
 */

//https://www.geeksforgeeks.org/play-audio-file-using-java/
//the purpose of this class is to play audio
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
            clip.loop(0); //play once
        } else if (loopCount == 0) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); //loop continuously
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