package Sudoku;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmationWindow extends JFrame {

    JLabel textLabel = new JLabel();
    JPanel buttonPanel = new JPanel();
    JButton confirmButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");

    int width = 300;
    int height = 200;

    ConfirmationWindow(Sudoku sudoku, boolean newPuzzle) {
        this.setSize(width, height);
        this.setResizable(false);
        // cant close it
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);

        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText(newPuzzle ? "Create New Puzzle?" : "Show Solution?");

        this.add(textLabel, BorderLayout.CENTER);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                confirm(sudoku, newPuzzle);
            }

        });

        buttonPanel.add(confirmButton);

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }

        });

        buttonPanel.add(cancelButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void confirm(Sudoku sudoku, boolean newPuzzle) {
        if (newPuzzle)
            sudoku.setUpNewPuzzle();
        else
            sudoku.solvePuzzle();
        this.dispose();
    }

    private void cancel() {
        this.dispose();
    }

}
