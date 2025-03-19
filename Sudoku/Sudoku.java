package Sudoku;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

/**
 * Class that holds and handles all the game logic
 */
public class Sudoku {

    /**
     * Class for tiles on the {@Link Sudoku} board
     */
    class Tile extends JButton {
        int row;
        int col;

        Tile(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    int boardWidth = 600;
    int boardHeight = 650;

    private static final int BOARD_SIZE = 9;
    private static final int MIN_NUM_TO_REMOVE = 30;

    int[][] puzzle;
    int[][] solution;

    JFrame frame = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JPanel actionsButtonsPanel = new JPanel();
    JButton solutionButton = new JButton("Solution");
    JButton newPuzzleButton = new JButton("New Puzzle");

    JButton numSelected = null;
    int errors = 0;
    int numOfMoves = 0;

    boolean solvedPuzzle = false;
    public boolean windowIsOpen = false;

    /**
     * Constructor that sets up the game, changes the {@Link frame} and all of the
     * other given panels
     */
    Sudoku() {
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        ImageIcon img = new ImageIcon(getClass().getResource("/img/images.png"));
        frame.setIconImage(img.getImage());

        windowClosingSettings(frame);

        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Mistakes: 0");

        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        setUpNewPuzzle();
        setUpTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setUpButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);

        actionsButtonsPanel.setLayout(new FlowLayout());

        solutionButton.setBackground(Color.lightGray);
        solutionButton.addActionListener(e -> {
            if (!solvedPuzzle && !windowIsOpen) {
                openConfirmationWindow(false);
                windowIsOpen = true;
            }
        });

        actionsButtonsPanel.add(solutionButton);

        newPuzzleButton.addActionListener(e -> {
            if (!windowIsOpen) {
                openConfirmationWindow(true);
                windowIsOpen = true;
            }
        });

        bottomPanel.add(actionsButtonsPanel, BorderLayout.SOUTH);

        actionsButtonsPanel.add(newPuzzleButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    /*
     * Causes that when leaving the game the player has to click a confirmation
     * window
     */
    private void windowClosingSettings(Frame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to exit?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });
    }

    /**
     * Open {@Link ConfirmationWindow} to either check if the user wants to be given
     * the solution or to check if the user wants to create a new puzzle
     * 
     * @param newPuzzle : boolean to check which of the two previously mentioned
     *                  options the user wants to access
     */
    private void openConfirmationWindow(boolean newPuzzle) {
        new ConfirmationWindow(this, newPuzzle);
    }

    /*
     * Opens a new Window to indicate, that the player won and to show their
     * accuracy in which they solved the puzzle
     */
    private void openVictoryWindow() {
        new VictoryWindow(this, errors, numOfMoves);
    }

    /**
     * Creates a new {@Link solution} and {@Link puzzle} array and sets them up on
     * the playing field
     */
    public void setUpNewPuzzle() {
        solution = generateSolvedSudoku();
        puzzle = removeNumbers(solution, MIN_NUM_TO_REMOVE);
        setUpTiles();
        errors = 0;
        numOfMoves = 0;
        solvedPuzzle = false;

    }

    /**
     * Solves the puzzle and marks all the unmarked {@Link Tile} in red
     */
    public void solvePuzzle() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Tile tile = (Tile) boardPanel.getComponent(r * BOARD_SIZE + c);
                tile.setText(Integer.toString(solution[r][c]));

                if (puzzle[r][c] == -1)
                    tile.setForeground(Color.RED);

            }
        }
        checkVictory(true);
        solvedPuzzle = true;
    }

    /**
     * Sets up all of the {@Link Tile} on the 9x9 playing field while also adding a
     * {@Link ActionListener} to each so they can be interacted with
     */
    private void setUpTiles() {
        boardPanel.removeAll();
        boardPanel.revalidate();
        boardPanel.repaint();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Tile tile = new Tile(r, c);
                if (puzzle[r][c] != -1) {
                    tile.setFont(new Font("Arial", Font.BOLD, 20));
                    tile.setText(Integer.toString(puzzle[r][c]));
                    tile.setBackground(Color.lightGray);
                } else {
                    tile.setFont(new Font("Arial", Font.PLAIN, 20));
                    tile.setBackground(Color.white);
                }
                if ((r == 2 && c == 2) || (r == 2 && c == 5) || (r == 5 && c == 2) || (r == 5 && c == 5))
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.black));
                else if (r == 2 || r == 5)
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.black));
                else if (c == 2 || c == 5)
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.black));
                else
                    tile.setBorder(BorderFactory.createLineBorder(Color.black));
                tile.setFocusable(false);
                boardPanel.add(tile);

                addListenerToTile(tile);
            }
        }
    }

    private void addListenerToTile(Tile tile) {
        tile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Get Source of tile you clicked on
                Tile tile = (Tile) e.getSource();
                int r = tile.row;
                int c = tile.col;
                if (numSelected != null) {
                    if (tile.getText() != "")
                        return;
                    String numSelText = numSelected.getText();
                    String tileSolution = Integer.toString(solution[r][c]);
                    numOfMoves++;
                    if (tileSolution.equals(numSelText)) {
                        tile.setText(numSelText);

                        updatePuzzle(r, c);

                        if (checkVictory(false))
                            openVictoryWindow();
                    } else {
                        errors++;
                        textLabel.setText("Mistakes: " + String.valueOf(errors));
                    }
                }
            }

        });
    }

    /**
     * check if solution equals puzzle, solved if the game was solved by clicking
     * the button
     * 
     * @param solved : boolean in case the player chose to manually solve the puzzle
     * @return : boolean to check if board has been solved correctly
     */
    private boolean checkVictory(boolean solved) {
        if (solved)
            return true;

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (puzzle[r][c] != solution[r][c] || puzzle[r][c] == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    /*
     * Update the {@Link puzzle} array
     */
    private void updatePuzzle(int r, int c) {
        puzzle[r][c] = solution[r][c];
    }

    /*
     * Sets up all of the buttons on the playing field
     */
    private void setUpButtons() {
        for (int i = 1; i < 10; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setText(String.valueOf(i));
            button.setBackground(Color.white);
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    if (numSelected != null)
                        numSelected.setBackground(Color.white);
                    numSelected = button;
                    numSelected.setBackground(Color.lightGray);

                }
            });
        }
    }

    private static final int SIZE = BOARD_SIZE;
    private static final int[][] board = new int[SIZE][SIZE];
    private static final Random random = new Random();

    /**
     * generates a solved sudoku board as a two dimensional int array which is later
     * on assigned a {@Link solution}
     * 
     * @return : solved sudoku board as a two dimensional int array
     */
    public static int[][] generateSolvedSudoku() {
        solve(0, 0);
        return board;
    }

    /**
     * Recursively solves the Sudoku puzzle using a backtracking algorithm.
     * It tries placing each number from 1 to 9 in each empty cell, ensuring no
     * number
     * is repeated in the row, column, or 3x3 subgrid.
     * 
     * @param row : current row to be filled
     * @param col : current column to be filled
     * @return : boolean indicating if the puzzle can be solved from the current
     *         position
     */
    private static boolean solve(int row, int col) {
        if (row == SIZE)
            return true;
        if (col == SIZE)
            return solve(row + 1, 0);

        int[] numbers = randomPermutation();
        for (int num : numbers) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                if (solve(row, col + 1))
                    return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    /**
     * Checks if placing a number at the specified row and column is valid.
     * It ensures the number does not already appear in the current row, column, or
     * 3x3 subgrid.
     * 
     * @param row : row to check
     * @param col : column to check
     * @param num : number to check for validity
     * @return : boolean indicating if placing the number is valid
     */

    private static boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num)
                return false;
        }
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num)
                    return false;
            }
        }
        return true;
    }

    /**
     * Generates a random permutation of numbers from 1 to 9 to be used in the
     * backtracking algorithm.
     * This randomization helps to avoid biases and makes the solving process less
     * predictable.
     * 
     * @return : a randomly shuffled array of numbers from 1 to 9
     */

    private static int[] randomPermutation() {
        int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int i = numbers.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
        return numbers;
    }

    /**
     * Removes a given amount of numbers from the board so the array generated by
     * generateSolvedSudoku can be assigned as {@Link puzzle}
     * 
     * @param solution
     * @param i
     * @return
     */
    public static int[][] removeNumbers(int[][] solution, int i) {
        Random ra = new Random();
        int[][] res = new int[SIZE][SIZE];
        for (int r = 0; r < solution.length; r++) {
            System.arraycopy(solution[r], 0, res[r], 0, solution[r].length);
        }

        int removed = 0;
        while (removed < i) {
            int r = ra.nextInt(9);
            int c = ra.nextInt(9);
            if (res[r][c] != -1) {
                res[r][c] = -1;
                removed++;
            }
        }
        return res;
    }

}
