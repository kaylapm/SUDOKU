package Base;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;

    private GameBoardPanel board = new GameBoardPanel();
    private JButton btnNewGame = new JButton("New Game");

    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout());
        panelSouth.add(btnNewGame);
        cp.add(panelSouth, BorderLayout.SOUTH);

        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Level 1", "Level 2", "Level 3"};
                int selected = JOptionPane.showOptionDialog(
                        Sudoku.this,
                        "Pilih level kesulitan:",
                        "New Game",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (selected != -1) {
                    board.newGame(selected + 1);
                }
            }
        });

        board.newGame(1);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Sudoku());
    }
}