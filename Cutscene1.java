import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Image;


public class Cutscene1 {
    private JFrame Cutscene1Frame;

    // Constructor to initialize TitleScreen
    public Cutscene1() {
        this.Cutscene1Frame = new JFrame();

        // Load the GIF image for the title screen
        ImageIcon cutscene1Gif = new ImageIcon("Photos, GIFs, Videos, Music/ezgif-6-c9c2e1f52f.gif");
        Image scaledImage = cutscene1Gif.getImage().getScaledInstance(900, 500, Image.SCALE_DEFAULT);
        ImageIcon scaledGif = new ImageIcon(scaledImage);
        JLabel gifLabel = new JLabel(scaledGif);

        // Add the GIF to the frame
        Cutscene1Frame.add(gifLabel);

        // Set frame properties
        Cutscene1Frame.setSize(scaledGif.getIconWidth(), scaledGif.getIconHeight()); // Adjust the frame size to the GIF size
        Cutscene1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Cutscene1Frame.setLocationRelativeTo(null); // Center the frame on the screen
        Cutscene1Frame.setVisible(true);
    }

    public void close() {
        Cutscene1Frame.setVisible(false);
        Cutscene1Frame.dispose();
    }
}
