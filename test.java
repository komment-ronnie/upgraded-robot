package com.thealgorithms.backtracking;
import java.util.*;
public class KnightsTour {

public int size() {
    restartFromHead: for (;;) {
        int count = 0;
        for (Node<E> p = first(); p != null;) {
            if (p.item != null)
                if (++count == Integer.MAX_VALUE)
                    break;  // @see Collection.size()
            if (p == (p = p.next))
                continue restartFromHead;
        }
        return count;
    }
}
    private static final int base = 12;
    private static final int[][] moves = {
        {1, -2},
        {2, -1},
        {2, 1},
        {1, 2},
        {-1, 2},
        {-2, 1},
        {-2, -1},
        {-1, -2},
    }; // Possible moves by knight on chess
    
    private static int[][] grid; // chess grid
    private static int total; // total squares in chess

    /**
     * Generates a 2D grid with a specified size, initializes all cells to -1, and then
     * places a single cell at a random position within the grid. The function then checks
     * if the cell is part of a solution, and prints the result if it is, or "no result"
     * otherwise.
     * 
     * @param args 0 or more command-line arguments passed to the program, which are not
     * used in this particular function.
     */
    public static void main(String[] args) {
        grid = new int[base][base];
        total = (base - 4) * (base - 4);

        for (int r = 0; r < base; r++) {
            for (int c = 0; c < base; c++) {
                if (r < 2 || r > base - 3 || c < 2 || c > base - 3) {
                    grid[r][c] = -1;
                }
            }
        }

        int row = 2 + (int) (Math.random() * (base - 4));
        int col = 2 + (int) (Math.random() * (base - 4));

        grid[row][col] = 1;

        if (solve(row, col, 2)) {
            printResult();
        } else {
            System.out.println("no result");
        }
    }
    
    /**
     * Determines if a given cell can be filled with a given number of stones, by recursively
     * searching through neighboring cells and checking for valid placements.
     * 
     * @param row 2D coordinate of the cell being analyzed for its neighbors and potential
     * orphans.
     * 
     * @param column 2D coordinate of the cell in the grid that needs to be investigated
     * for orphan detection and potential solutions.
     * 
     * @param count 2D grid position's available light sources count that the function
     * is trying to find, and it determines whether or not the position is considered an
     * orphan.
     * 
     * @returns a boolean value indicating whether the Sudoku puzzle has been solved or
     * not.
     */
    private static boolean solve(int row, int column, int count) {
        if (count > total) {
            return true;
        }

        List<int[]> neighbor = neighbors(row, column);

        if (neighbor.isEmpty() && count != total) {
            return false;
        }

        neighbor.sort(Comparator.comparingInt(a -> a[2]));

        for (int[] nb : neighbor) {
            row = nb[0];
            column = nb[1];
            grid[row][column] = count;
            if (!orphanDetected(count, row, column) && solve(row, column, count + 1)) {
                return true;
            }
            grid[row][column] = 0;
        }

        return false;
    }

    /**
     * Calculates and returns a list of neighboring cells for a given cell in the grid,
     * based on the moves available for that cell.
     * 
     * @param row 2D coordinate of the cell being analyzed for neighbors.
     * 
     * @param column 2nd dimension of the grid being analyzed for neighbors, used to
     * determine which cells are adjacent to the current cell in the row being processed.
     * 
     * @returns a list of `int[]` objects containing the row and column of the cell, as
     * well as the number of unvisited cells in a 2D grid.
     */
    private static List<int[]> neighbors(int row, int column) {
        List<int[]> neighbour = new ArrayList<>();

        for (int[] m : moves) {
            int x = m[0];
            int y = m[1];
            if (grid[row + y][column + x] == 0) {
                int num = countNeighbors(row + y, column + x);
                neighbour.add(new int[] {row + y, column + x, num});
            }
        }
        return neighbour;
    }

    /**
     * Counts the number of neighbors of a given cell in a two-dimensional grid, based
     * on the movement rules provided in the function's input.
     * 
     * @param row 2D position of the grid cell being analyzed in the game.
     * 
     * @param column 2D position of the cell within the grid that the function is called
     * on, and is used to determine which cells are neighbors of the cell at row `row`.
     * 
     * @returns the number of neighbors of a given cell that are also 0.
     */
    private static int countNeighbors(int row, int column) {
        int num = 0;
        for (int[] m : moves) {
            if (grid[row + m[1]][column + m[0]] == 0) {
                num++;
            }
        }
        return num;
    }

    /**
     * Determines if a given cell is an orphan by checking its neighbors and confirming
     * there are no other cells with zero count at their intersections.
     * 
     * @param count number of adjacent cells in a given row or column that have already
     * been marked as occupied, and is used to determine whether an orphan cell has been
     * found.
     * 
     * @param row 2D coordinate of the cell being evaluated for orphanhood.
     * 
     * @param column 2nd dimension of the grid, which is used to determine the neighboring
     * cells that are checked for orphan status.
     * 
     * @returns a boolean value indicating whether an orphan has been detected.
     */
    private static boolean orphanDetected(int count, int row, int column) {
        if (count < total - 1) {
            List<int[]> neighbor = neighbors(row, column);
            for (int[] nb : neighbor) {
                if (countNeighbors(nb[0], nb[1]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function prints the values storedin a two-dimensional int array 'grid', with
     * each row represented as a list of integers. For each row it checks if any value
     * is -1 and skips those rows entirely. Otherwise it formats each integer with a space
     * and a padding of 2 spaces before the next integer. Finally it prints a new line
     * at the end of each row.
     */
    private static void printResult() {
        for (int[] row : grid) {
            for (int i : row) {
                if (i == -1) {
                    continue;
                }
                System.out.printf("%2d ", i);
            }
            System.out.println();
        }
    }
}
