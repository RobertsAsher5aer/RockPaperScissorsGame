import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * RockPaperScissorsFrame class creates the GUI for the Rock-Paper-Scissors game.
 */
public class RockPaperScissorsFrame extends JFrame implements ActionListener {
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;
    private JTextArea resultsArea;
    private JTextField playerWinsField, computerWinsField, tiesField;
    private String lastPlayerMove = "";

    private final String[] choices = {"Rock", "Paper", "Scissors"};
    private final Random random = new Random();
    private final String[] strategies = {"Random", "Least Used", "Most Used", "Last Used", "Cheat"};
    private int[] playerChoiceCount = {0, 0, 0};

    /**
     * Constructor to set up the game frame.
     */
    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Choose your move"));
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout for better control

// Create buttons with larger dimensions
        JButton rockButton = new JButton("Rock", new ImageIcon("src/rock.png"));
        JButton paperButton = new JButton("Paper", new ImageIcon("src/paper.png"));
        JButton scissorsButton = new JButton("Scissors", new ImageIcon("src/scissors.png"));
        JButton quitButton = new JButton("Quit");

        rockButton.setActionCommand("Rock");
        paperButton.setActionCommand("Paper");
        scissorsButton.setActionCommand("Scissors");
        quitButton.setActionCommand("Quit");

        rockButton.addActionListener(this);
        paperButton.addActionListener(this);
        scissorsButton.addActionListener(this);
        quitButton.addActionListener(this);

// Set button sizes to ensure they are large enough
        Dimension buttonSize = new Dimension(120, 60); // Larger size
        rockButton.setPreferredSize(buttonSize);
        paperButton.setPreferredSize(buttonSize);
        scissorsButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(new Dimension(100, 50)); // Smaller size for Quit

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3, 2));
        statsPanel.add(new JLabel("Player Wins:"));
        playerWinsField = new JTextField("0");
        playerWinsField.setEditable(false);
        statsPanel.add(playerWinsField);
        statsPanel.add(new JLabel("Computer Wins:"));
        computerWinsField = new JTextField("0");
        computerWinsField.setEditable(false);
        statsPanel.add(computerWinsField);
        statsPanel.add(new JLabel("Ties:"));
        tiesField = new JTextField("0");
        tiesField.setEditable(false);
        statsPanel.add(tiesField);

        add(statsPanel, BorderLayout.CENTER);

        // Results area
        resultsArea = new JTextArea(10, 30);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        add(scrollPane, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * Action event handler for button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Quit")) {
            System.exit(0);
        } else {
            playGame(command);
        }
    }

    /**
     * Plays a game round based on the player's move.
     *
     * @param playerMove The player's move.
     */
    private void playGame(String playerMove) {
        lastPlayerMove = playerMove;
        int playerIndex = getPlayerIndex(playerMove);
        playerChoiceCount[playerIndex]++;

        String computerMove = determineComputerMove();
        String result = determineResult(playerMove, computerMove);
        updateStats(result);
        resultsArea.append(result + "\n");
    }

    /**
     * Determines the computer's move based on different strategies.
     *
     * @return The computer's move.
     */
    private String determineComputerMove() {
        String strategy = strategies[random.nextInt(strategies.length)];
        switch (strategy) {
            case "Least Used":
                return choices[leastUsedChoice()];
            case "Most Used":
                return choices[mostUsedChoice()];
            case "Last Used":
                return lastPlayerMove.isEmpty() ? choices[random.nextInt(3)] : lastPlayerMove;
            case "Cheat":
                return cheatMove();
            default:
                return choices[random.nextInt(3)];
        }
    }

    /**
     * Determines the least used choice of the player.
     *
     * @return Index of the least used choice.
     */
    private int leastUsedChoice() {
        int minIndex = 0;
        for (int i = 1; i < playerChoiceCount.length; i++) {
            if (playerChoiceCount[i] < playerChoiceCount[minIndex]) {
                minIndex = i;
            }
        }
        return (minIndex + 1) % 3; // Pick the winning move
    }

    /**
     * Determines the most used choice of the player.
     *
     * @return Index of the most used choice.
     */
    private int mostUsedChoice() {
        int maxIndex = 0;
        for (int i = 1; i < playerChoiceCount.length; i++) {
            if (playerChoiceCount[i] > playerChoiceCount[maxIndex]) {
                maxIndex = i;
            }
        }
        return (maxIndex + 2) % 3; // Pick the winning move
    }

    /**
     * Determines the computer's cheating move.
     *
     * @return The cheating move.
     */
    private String cheatMove() {
        if (random.nextDouble() < 0.1) { // 10% chance to cheat
            return lastPlayerMove.equals("Rock") ? "Paper" :
                    lastPlayerMove.equals("Paper") ? "Scissors" : "Rock";
        }
        return choices[random.nextInt(3)]; // Random otherwise
    }

    /**
     * Determines the result of the game.
     *
     * @param playerMove   The player's move.
     * @param computerMove The computer's move.
     * @return The result of the game.
     */
    private String determineResult(String playerMove, String computerMove) {
        if (playerMove.equals(computerMove)) {
            return playerMove + " ties " + computerMove + " (Tie)";
        }
        if ((playerMove.equals("Rock") && computerMove.equals("Scissors")) ||
                (playerMove.equals("Paper") && computerMove.equals("Rock")) ||
                (playerMove.equals("Scissors") && computerMove.equals("Paper"))) {
            return playerMove + " breaks " + computerMove + " (Player wins)";
        }
        return computerMove + " beats " + playerMove + " (Computer wins)";
    }

    /**
     * Updates the stats after each game.
     *
     * @param result The result string of the game.
     */
    private void updateStats(String result) {
        if (result.contains("(Player wins)")) {
            playerWins++;
        } else if (result.contains("(Computer wins)")) {
            computerWins++;
        } else {
            ties++;
        }
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    /**
     * Gets the index of the player's move.
     *
     * @param move The player's move.
     * @return The index of the move.
     */
    private int getPlayerIndex(String move) {
        switch (move) {
            case "Rock": return 0;
            case "Paper": return 1;
            case "Scissors": return 2;
            default: return -1;
        }
    }
}

