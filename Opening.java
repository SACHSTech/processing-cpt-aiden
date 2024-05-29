import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Image;


public class Opening {
    private JFrame OpeningFrame;

    // Constructor to initialize TitleScreen
    public Opening() {
        this.OpeningFrame = new JFrame();

        // Load the GIF image for the title screen
        ImageIcon openingGif = new ImageIcon("Photos, GIFs, Videos, Music/202405271549-ezgif.com-animated-gif-maker.gif");
        Image scaledImage = openingGif.getImage().getScaledInstance(900, 500, Image.SCALE_DEFAULT);
        ImageIcon scaledGif = new ImageIcon(scaledImage);
        JLabel gifLabel = new JLabel(scaledGif);

        // Add the GIF to the frame
        OpeningFrame.add(gifLabel);

        // Set frame properties
        OpeningFrame.setSize(scaledGif.getIconWidth(), scaledGif.getIconHeight()); // Adjust the frame size to the GIF size
        OpeningFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        OpeningFrame.setLocationRelativeTo(null); // Center the frame on the screen
        OpeningFrame.setVisible(true);
    }

    public void close() {
        OpeningFrame.setVisible(false);
        OpeningFrame.dispose();
    }
}
