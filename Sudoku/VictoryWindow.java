package Sudoku;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/*
 * JFrame Window that indicates that the player won the Sudoku game and shows 
 * the amount of mistakes the player made compared to the number of move, which 
 * is shown as the accuracyF
 */
public class VictoryWindow extends JFrame {

    JPanel buttonPanel = new JPanel();
    JButton nextPuzzleButton = new JButton("Next Sudoku");
    JLabel titleLabel = new JLabel("You Won!");
    JLabel movesLabel = new JLabel();
    JLabel mistakesLabel = new JLabel();
    JLabel accuracyLabel = new JLabel();
    JPanel labelPanel = new JPanel(new GridLayout(3, 1));

    int width = 300;
    int height = 250;

    public VictoryWindow(Sudoku sudoku, final int mistakes, final int numberOfMoves) {
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        ImageIcon img = new ImageIcon(getClass().getResource("/img/images.png"));
        this.setIconImage(img.getImage());

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(titleLabel, BorderLayout.NORTH);

        List<JLabel> labels = List.of(movesLabel, mistakesLabel, accuracyLabel);
        List<String> text = grading(mistakes, numberOfMoves);

        for (int i = 0; i < labels.size(); i++) {
            setUpLabel(labels.get(i), text.get(i));
            labelPanel.add(labels.get(i));
        }

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(nextPuzzleButton);

        nextPuzzleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm(sudoku);
            }
        });

        this.add(labelPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    /**
     * Gives each a label their attributes (in the constructor this is done for all
     * three given labels)
     * 
     * @param textLabel
     * @param text
     */
    private void setUpLabel(JLabel textLabel, String text) {
        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText(text);
    }

    /**
     * Returns three Strings as a List. The first is the number of moves the player
     * did overall to win the game of Sudoku they just finished. The second
     * is the number of mistakes they did. The third is the accuracy which is
     * calculated from the previous two.
     * 
     * @param mistakes
     * @param numberOfMoves
     * @return
     */
    private List<String> grading(int mistakes, int numberOfMoves) {
        List<String> res = new ArrayList<>();
        res.add("Total Number of Moves: " + numberOfMoves);
        res.add("Total Number of Mistakes: " + mistakes);
        res.add(numberOfMoves != 0
                ? "Accuracy: " + String.format("%.2f", (100.0 * (numberOfMoves - mistakes) / numberOfMoves)) + "%"
                : "Accuracy: 0%");
        return res;
    }

    /**
     * Closes the game after the player clicks the confirm button. Also starts a new
     * puzzle in the Sudoku class
     * 
     * @param sudoku
     */
    private void confirm(Sudoku sudoku) {
        sudoku.setUpNewPuzzle();
        sudoku.windowIsOpen = false;
        sudoku.resetParameters();
        this.dispose();
    }
}