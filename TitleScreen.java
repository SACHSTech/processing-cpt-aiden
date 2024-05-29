import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class TitleScreen {
    private JFrame TitleFrame;

    // Constructor to initialize TitleScreen
    public TitleScreen() {
        this.TitleFrame = new JFrame();

        // Load the GIF image for the title screen
        ImageIcon titleGif = new ImageIcon("Photos, GIFs, Videos, Music/202405271549-ezgif.com-animated-gif-maker.gif");
        JLabel gifLabel = new JLabel(titleGif);

        // Add the GIF to the frame
        TitleFrame.add(gifLabel);

        // Set frame properties
        TitleFrame.setSize(titleGif.getIconWidth(), titleGif.getIconHeight()); // Adjust the frame size to the GIF size
        TitleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TitleFrame.setLocationRelativeTo(null); // Center the frame on the screen
        TitleFrame.setVisible(true);
    }
}
