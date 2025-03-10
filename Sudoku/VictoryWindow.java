package Sudoku;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class VictoryWindow extends JFrame {

    JPanel buttonPanel = new JPanel();
    JButton nextPuzzleButton = new JButton("Next Sudoku");
    JLabel titleLabel = new JLabel("You Won!");
    JLabel movesLabel = new JLabel();
    JLabel mistakesLabel = new JLabel();
    JLabel accuracyLabel = new JLabel();

    int width = 300;
    int height = 250;

    public VictoryWindow(Sudoku sudoku, final int mistakes, final int numberOfMoves) {
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel labelPanel = new JPanel(new GridLayout(3, 1));
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

    private void setUpLabel(JLabel textLabel, String text) {
        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText(text);
    }

    private List<String> grading(int mistakes, int numberOfMoves) {
        List<String> res = new ArrayList<>();
        res.add("Total Number of Moves: " + numberOfMoves);
        res.add("Total Number of Mistakes: " + mistakes);
        res.add(numberOfMoves != 0
                ? "Accuracy: " + String.format("%.2f", (100.0 * (numberOfMoves - mistakes) / numberOfMoves)) + "%"
                : "Accuracy: 0%");
        return res;
    }

    private void confirm(Sudoku sudoku) {
        sudoku.setUpNewPuzzle();
        this.dispose();
    }
}