package Base;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;

    private GameBoardPanel board = new GameBoardPanel();
    private Clip clip;

    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Create a panel for the welcome message, image, and start button
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Sudoku", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);

        // Add an image to the center of the welcome panel
        ImageIcon icon = new ImageIcon("src/Base/background.jpeg"); // Adjust the path to your image
        JLabel imageLabel = new JLabel(icon);
        welcomePanel.add(imageLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for larger button
        startButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set font size for better visibility
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("src/Base/forgotten-144543.wav"); // Play music when the game starts
                showLevelSelection();
            }
        });

        // Create a panel to center the button at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        welcomePanel.add(buttonPanel, BorderLayout.SOUTH);

        cp.add(welcomePanel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showLevelSelection() {
        String[] options = {"Level 5", "Level 4", "Level 3", "Level 2", "Level 1"};
        int selected = JOptionPane.showOptionDialog(
                this,
                "Pilih level kesulitan:",
                "Select Level",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[4] // Highlight default Level 1
        );

        if (selected != -1) {
            showGameBoard(selected + 1);
        }
    }


    public void showGameBoard(int level) {
        getContentPane().removeAll();
        getContentPane().add(board, BorderLayout.CENTER);
        board.newGame(level);
        revalidate();
        repaint();
    }

    private void playSound(String filePath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the audio indefinitely
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}