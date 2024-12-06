/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #5
 * 1 - 5026231158 - Kayla Putri Maharani
 * 2 - 5026231170 - Tahiyyah Mufhimah
 * 3 - 5026231206 - Rafael Dimas K
 */

package Base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;

    private GameBoardPanel board = new GameBoardPanel();
    private Clip clip;

    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Sudoku", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);


        ImageIcon icon = new ImageIcon("src/Base/background.jpeg");
        JLabel imageLabel = new JLabel(icon);
        welcomePanel.add(imageLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("src/Base/lagu.wav");
                showLevelSelection();
            }
        });


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
        String[] options = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
        int selected = JOptionPane.showOptionDialog(
                this,
                "Pilih level kesulitan:",
                "Select Level",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
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
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}