package com.simo.minesweeper.minesweeper;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

@Component
public class MineSweeper {
    private static char[][] mineField;
    private static boolean[][] hasMine;
    private static boolean[][] isClicked;
    private static final Random rand = new Random();
    private static StringBuilder stringBuilder = new StringBuilder();
    private static int mines = 0;
    private static int[] playerClicks;
    private static boolean hasMoves = true;
    private static boolean hasLost = false;


    public void init() throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));

        String initialMsg = "Enter the Difficulty Level" + System.lineSeparator() +
                "Press 0 for BEGINNER (9 * 9 Cells and 10 Mines)" + System.lineSeparator() +
                "Press 1 for INTERMEDIATE (16 * 16 Cells and 40 Mines)" + System.lineSeparator() +
                "Press 2 for ADVANCED (24 * 24 Cells and 99 Mines)" + System.lineSeparator();
        System.out.print(initialMsg);

        int difficultyLevel = Integer.parseInt(rd.readLine());

        switch (difficultyLevel) {
            case 0:
                createField(9);

                printBoardInitialState();

                firstMove(rd);

                populateFieldWithMines(9, 10);

                break;
            case 1:
                createField(16);

                printBoardInitialState();

                firstMove(rd);

                populateFieldWithMines(16, 40);

                break;
            case 2:
                createField(24);

                printBoardInitialState();

                firstMove(rd);

                populateFieldWithMines(24, 99);

                break;
        }

        isClicked[playerClicks[0]][playerClicks[1]] = false;
        onClick(playerClicks[0], playerClicks[1]);


        while (hasMoves && !hasLost) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Enter your move, (row, column)").append(System.lineSeparator())
                    .append("->");

            System.out.print(stringBuilder.toString());

            playerClicks = Arrays.stream(rd.readLine().split("\\s+"))
                    .mapToInt(Integer::parseInt).toArray();

            onClick(playerClicks[0], playerClicks[1]);

            checkForClickableCells();
        }

        if (!hasLost) {
            System.out.println("You won!");
        }
    }

    private static void checkForClickableCells() {
        hasMoves = false;
        for (int row = 0; row < mineField.length; row++) {
            for (int col = 0; col < mineField[row].length; col++) {
                if (!isClicked[row][col] && !hasMine[row][col]) {
                    hasMoves = true;
                    break;
                }
            }
        }
    }

    private static void onClick(int row, int col) {
        if (hasMine[row][col]) {
            isClicked[row][col] = true;
            hasLost = true;
            showAllMines();
            printBoardInitialState();
            System.out.println("You lost!");
        } else {
            sweepField(row, col);
            printBoardInitialState();
        }
    }

    private static void showAllMines() {
        for (int row = 0; row < hasMine.length; row++) {
            for (int col = 0; col < hasMine[row].length; col++) {
                if (hasMine[row][col]) {
                    mineField[row][col] = '*';
                }
            }
        }
    }

    private static void sweepField(int row, int col) {
        if (isClicked[row][col]) {
            return;
        }
        isClicked[row][col] = true;
        int adjacentMines = calculateAdjacentMines(row, col);
        if (adjacentMines > 0) {
            mineField[row][col] = Character.forDigit(adjacentMines, 10);
        } else {
            mineField[row][col] = ' ';

            if (isInFieldBounds(row, col + 1)) {
                sweepField(row, col + 1);
            }
            if (isInFieldBounds(row + 1, col + 1)) {
                sweepField(row + 1, col + 1);
            }
            if (isInFieldBounds(row + 1, col)) {
                sweepField(row + 1, col);
            }
            if (isInFieldBounds(row + 1, col - 1)) {
                sweepField(row + 1, col - 1);
            }
            if (isInFieldBounds(row, col - 1)) {
                sweepField(row, col - 1);
            }
            if (isInFieldBounds(row - 1, col - 1)) {
                sweepField(row - 1, col - 1);
            }
            if (isInFieldBounds(row - 1, col)) {
                sweepField(row - 1, col);
            }
            if (isInFieldBounds(row - 1, col + 1)) {
                sweepField(row - 1, col + 1);
            }

        }
    }

    private static int calculateAdjacentMines(int row, int col) {
        int adjacentMines = 0;
        if (isInFieldBounds(row, col + 1) && hasMine[row][col + 1]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row + 1, col + 1) && hasMine[row + 1][col + 1]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row + 1, col) && hasMine[row + 1][col]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row + 1, col - 1) && hasMine[row + 1][col - 1]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row, col - 1) && hasMine[row][col - 1]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row - 1, col - 1) && hasMine[row - 1][col - 1]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row - 1, col) && hasMine[row - 1][col]) {
            adjacentMines++;
        }
        if (isInFieldBounds(row - 1, col + 1) && hasMine[row - 1][col + 1]) {
            adjacentMines++;
        }

        return adjacentMines;
    }

    private static boolean isInFieldBounds(int row, int col) {
        return row >= 0 && row < mineField.length && col >= 0 && col < mineField.length;
    }


    private static void firstMove(BufferedReader rd) throws IOException {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Enter your move, (row, column)").append(System.lineSeparator())
                .append("->");

        System.out.print(stringBuilder.toString());

        playerClicks = Arrays.stream(rd.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt).toArray();

        if (isInFieldBounds(playerClicks[0], playerClicks[1])) {
            isClicked[playerClicks[0]][playerClicks[1]] = true;
        }
    }

    private static void populateFieldWithMines(int fieldSize, int mineCount) {
        hasMine = new boolean[fieldSize][fieldSize];
        while (mines < mineCount) {
            int row = rand.nextInt(fieldSize);
            int col = rand.nextInt(fieldSize);
            if (!hasMine[row][col] && !isClicked[row][col]) {
                hasMine[row][col] = true;
                mines++;
            }
        }
    }

    private static void printBoardInitialState() {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Current Status of Board :").append(System.lineSeparator());
        stringBuilder.append("    ");
        for (int colIndex = 0; colIndex < mineField.length; colIndex++) {
            stringBuilder.append(colIndex).append(" ");
        }
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        stringBuilder.append(System.lineSeparator());

        for (int row = 0; row < mineField.length; row++) {
            stringBuilder.append(row).append(row < 10 ? "   " : "  ");
            for (int col = 0; col < mineField[row].length; col++) {
                stringBuilder.append(mineField[row][col]).append(col < 10 ? " " : "  ");
            }
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
            stringBuilder.append(System.lineSeparator());
        }

        System.out.print(stringBuilder.toString());
    }

    private static void createField(int size) {
        mineField = new char[size][size];
        for (char[] row : mineField) {
            Arrays.fill(row, '-');
        }

        isClicked = new boolean[size][size];
    }
}
