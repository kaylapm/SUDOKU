package Base;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

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
            case CORRECT_GUESS -> setBackground(BG_CORRECT_GUESS);
            case WRONG_GUESS -> setBackground(BG_WRONG_GUESS);
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