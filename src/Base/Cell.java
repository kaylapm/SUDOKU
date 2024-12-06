package Base;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Cell extends JTextField {
    private static final long serialVersionUID = 1L;

    public static final Color BG_GIVEN = new Color(240, 240, 240);
    public static final Color FG_GIVEN = Color.BLACK;
    public static final Color FG_NOT_GIVEN = Color.GRAY;
    public static final Color BG_TO_GUESS = Color.YELLOW;
    public static final Color BG_CORRECT_GUESS = new Color(0, 216, 0);
    public static final Color BG_WRONG_GUESS = new Color(216, 0, 0);
    public static final Font FONT_NUMBERS = new Font("OCR A Extended", Font.PLAIN, 28);

    int row, col;
    int number;
    CellStatus status;

    public Cell(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        super.setHorizontalAlignment(JTextField.CENTER);
        super.setFont(FONT_NUMBERS);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (status == CellStatus.TO_GUESS || status == CellStatus.WRONG_GUESS) {
                    String input = getText();
                    if (input.matches("\\d")) { // Check if input is a single digit
                        int enteredNumber = Integer.parseInt(input);
                        if (enteredNumber == number) {
                            status = CellStatus.CORRECT_GUESS;
                        } else {
                            status = CellStatus.WRONG_GUESS;
                        }
                        paint();
                    }
                }
            }
        });
    }

    public void newGame(int number, boolean isGiven) {
        this.number = number;
        this.status = isGiven ? CellStatus.GIVEN : CellStatus.TO_GUESS;
        paint();
    }

    public void paint() {
        switch (status) {
            case GIVEN -> {
                setText(String.valueOf(number));
                setEditable(false);
                setBackground(BG_GIVEN);
                setForeground(FG_GIVEN);
            }
            case TO_GUESS -> {
                setText("");
                setEditable(true);
                setBackground(BG_TO_GUESS);
                setForeground(FG_NOT_GIVEN);
            }
            case CORRECT_GUESS -> {
                setBackground(BG_CORRECT_GUESS);
                setForeground(FG_GIVEN);
            }
            case WRONG_GUESS -> {
                setBackground(BG_WRONG_GUESS);
                setForeground(FG_NOT_GIVEN);
            }
        }
    }

    public boolean isGiven() {
        return status == CellStatus.GIVEN;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}