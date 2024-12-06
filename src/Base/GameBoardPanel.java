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

    private Cell selectedCell;
    private JTextField statusBar; // Status bar to display messages

    public GameBoardPanel() {
        super.setLayout(new BorderLayout());
        super.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));
        gridPanel.setBackground(Color.WHITE);
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col, this);
                cells[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectedCell = (Cell) e.getSource();
                    }
                });

                // Set blue borders for 3x3 grid outlines
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == SudokuConstants.GRID_SIZE - 1 || (row + 1) % 3 == 0) ? 3 : 1;
                int right = (col == SudokuConstants.GRID_SIZE - 1 || (col + 1) % 3 == 0) ? 3 : 1;

                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLUE));
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
        add(controlPanel, BorderLayout.NORTH);

        // Add the number pad panel
        JPanel numberPadPanel = createNumberPadPanel();
        add(numberPadPanel, BorderLayout.EAST);

        // Create and add the status bar
        statusBar = new JTextField("Cells remaining: " + countRemainingCells());
        statusBar.setEditable(false);
        statusBar.setHorizontalAlignment(JTextField.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(BOARD_WIDTH + 100, BOARD_HEIGHT + 30)); // Adjust height for status bar

        timer = new Timer(1000, e -> updateTimer());
    }

    private JPanel createNumberPadPanel() {
        JPanel numberPadPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        numberPadPanel.setBackground(Color.LIGHT_GRAY);
        for (int i = 1; i <= 9; i++) {
            JButton button = createNumberButton(i);
            numberPadPanel.add(button);
        }
        return numberPadPanel;
    }

    private JButton createNumberButton(int number) {
        JButton button = new JButton(String.valueOf(number));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.addActionListener(e -> {
            if (selectedCell != null && selectedCell.isEditable()) {
                selectedCell.setText(button.getText());
                selectedCell.processInput();
                updateStatusBar();
            }
        });
        return button;
    }

    private int countRemainingCells() {
        int remaining = 0;
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!cells[row][col].isGiven() && cells[row][col].status != CellStatus.CORRECT_GUESS) {
                    remaining++;
                }
            }
        }
        return remaining;
    }

    private void updateStatusBar() {
        statusBar.setText("Cells remaining: " + countRemainingCells());
    }

    public void newGame(int level) {
        int cellsToGuess;
        switch (level) {
            case 1:
                cellsToGuess = 3;
                break;
            case 2:
                cellsToGuess = 5;
                break;
            case 3:
                cellsToGuess = 10;
                break;
            case 4:
                cellsToGuess = 20;
                break;
            case 5:
                cellsToGuess = 30;
                break;
            default:
                cellsToGuess = 3; // Default to level 1 if an invalid level is provided
                break;
        }

        puzzle.newPuzzle(cellsToGuess);
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
        currentLevel = level;
        elapsedTime = 0;
        score = 0;
        updateScore(0);
        updateStatusBar();
        timer.start();
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
            timer.stop();
            int option = JOptionPane.showOptionDialog(
                    this,
                    "Congratulations! You've solved the puzzle.",
                    "Puzzle Solved",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"New Game", "Next Level", "Quit Game"},
                    "New Game"
            );

            switch (option) {
                case 0: // New Game
                    newGame(currentLevel);
                    break;
                case 1: // Next Level
                    if (currentLevel < 5) {
                        newGame(currentLevel + 1);
                    } else {
                        JOptionPane.showMessageDialog(this, "You are already at the highest level.");
                    }
                    break;
                case 2: // Quit Game
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }
}