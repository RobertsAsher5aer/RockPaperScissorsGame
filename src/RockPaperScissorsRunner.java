import javax.swing.*;

/**
 * RockPaperScissorsRunner class to launch the game.
 */
public class RockPaperScissorsRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RockPaperScissorsFrame());
    }
}
