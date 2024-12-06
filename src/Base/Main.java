package Base;
import javax.swing.SwingUtilities;

public class Main {
    /** The entry main() entry method */
    public static void main(String[] args) {
        // Jalankan aplikasi di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Buat instansi utama Sudoku
            Sudoku sudoku = new Sudoku();
            sudoku.setVisible(true); // Tampilkan GUI
        });
    }
}