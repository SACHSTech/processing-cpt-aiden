import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * A class that will play the first cutscene
 * @author A. Razack
 *
 */

public class Cutscene {
    private JFrame CutsceneFrame;

    // Constructor to initialize Cutscene
    public Cutscene() {
        this.CutsceneFrame = new JFrame();

        // Load the GIF image for the cutscene
        ImageIcon cutsceneGif = new ImageIcon("Photos, GIFs, Videos, Music/ezgif-6-c9c2e1f52f.gif");
        Image scaledImage = cutsceneGif.getImage().getScaledInstance(900, 500, Image.SCALE_DEFAULT);
        ImageIcon scaledGif = new ImageIcon(scaledImage);
        JLabel gifLabel = new JLabel(scaledGif);

        // Add the GIF to the frame
        CutsceneFrame.add(gifLabel);

        // Set frame properties
        CutsceneFrame.setSize(scaledGif.getIconWidth(), scaledGif.getIconHeight()); 
        CutsceneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CutsceneFrame.setLocationRelativeTo(null); 
        CutsceneFrame.setVisible(true);
    }

    // Close the cutscene
    public void close() {
        CutsceneFrame.setVisible(false);
        CutsceneFrame.dispose();
    }
}
