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
    private int currentLevel = 1; // Default level

    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        CellInputListener listener = new CellInputListener();

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!cells[row][col].isGiven()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void newGame(int level) {
        this.currentLevel = level;
        int cellsToGuess;
        switch (level) {
            case 1 -> cellsToGuess = 3;  // Level mudah
            case 2 -> cellsToGuess = 5;  // Level menengah
            case 3 -> cellsToGuess = 10; // Level sulit
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        }
        puzzle.newPuzzle(cellsToGuess);

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            String input = sourceCell.getText().trim();

            if (input.matches("[1-9]")) {
                int number = Integer.parseInt(input);
                sourceCell.setNumber(number);
                sourceCell.status = puzzle.validate(sourceCell.getRow(), sourceCell.getCol(), number)
                        ? CellStatus.CORRECT_GUESS
                        : CellStatus.WRONG_GUESS;
            } else {
                sourceCell.setText("");
                sourceCell.status = CellStatus.TO_GUESS;
            }

            sourceCell.paint();

            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You've solved the puzzle!");
            }
        }
    }
}