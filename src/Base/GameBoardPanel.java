package Base;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();
    private int currentLevel = 1;

    private Timer timer;
    private int elapsedTime = 0;
    private JLabel timerLabel;

    private int score = 0;
    private JLabel scoreLabel;

    public GameBoardPanel() {
        super.setLayout(new BorderLayout());
        super.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));
        gridPanel.setBackground(Color.WHITE);
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col, this);

                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == SudokuConstants.GRID_SIZE - 1 || (row + 1) % 3 == 0) ? 3 : 1;
                int right = (col == SudokuConstants.GRID_SIZE - 1 || (col + 1) % 3 == 0) ? 3 : 1;

                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                gridPanel.add(cells[row][col]);
            }
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);

        JButton levelButton = new JButton("Select Level");
        levelButton.setForeground(Color.BLUE);
        levelButton.addActionListener(e -> {
            String[] options = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
            int selected = JOptionPane.showOptionDialog(
                    this,
                    "Pilih level kesulitan:",
                    "Select Level",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (selected != -1) {
                newGame(selected + 1);
            }
        });

        JButton resetButton = new JButton("Reset Game");
        resetButton.setForeground(Color.BLUE);
        resetButton.addActionListener(e -> newGame(currentLevel));

        timerLabel = new JLabel("Time: 0s");
        timerLabel.setForeground(Color.BLUE);
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.BLUE);

        controlPanel.add(timerLabel);
        controlPanel.add(scoreLabel);
        controlPanel.add(levelButton);
        controlPanel.add(resetButton);

        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        timer = new Timer(1000, e -> updateTimer());
    }

    public void newGame(int level) {
        this.currentLevel = level;
        int cellsToGuess;
        switch (level) {
            case 1 -> cellsToGuess = 3;
            case 2 -> cellsToGuess = 5;
            case 3 -> cellsToGuess = 10;
            case 4 -> cellsToGuess = 20;
            case 5 -> cellsToGuess = 30;
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        }
        puzzle.newPuzzle(cellsToGuess);

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }

        elapsedTime = 0;
        timerLabel.setText("Time: 0s");
        timer.start();

        score = 0;
        scoreLabel.setText("Score: 0");
    }

    public void updateScore(int delta) {
        score += delta;
        scoreLabel.setText("Score: " + score);
    }

    private void updateTimer() {
        elapsedTime++;
        timerLabel.setText("Time: " + elapsedTime + "s");
    }

    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!cells[row][col].isGiven() && cells[row][col].status != CellStatus.CORRECT_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkAndShowWinOptions() {
        if (isSolved()) {
            showWinOptions();
        }
    }

    private void showWinOptions() {
        timer.stop();
        int option = JOptionPane.showOptionDialog(
                this,
                "Congratulations! You solved the puzzle.",
                "Puzzle Solved",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"New Game", "Next Level"},
                "New Game"
        );

        if (option == JOptionPane.YES_OPTION) {
            newGame(currentLevel);
        } else if (option == JOptionPane.NO_OPTION && currentLevel < 5) {
            newGame(currentLevel + 1);
        }
    }
}