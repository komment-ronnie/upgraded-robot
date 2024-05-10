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
     * Generates a grid with random values, selects a random row and column within the
     * grid, and checks if there is a path from the top-left corner to the randomly
     * selected point using a recursive algorithm. If a path exists, it prints the result,
     * otherwise, it displays "no result".
     * 
     * @param args 0 or more command-line arguments passed to the program when it is
     * executed, and its values are ignored in this context.
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
     * Determines whether a given cell can be filled with a specific number of "clicks"
     * or not based on its neighboring cells and the total number of clicks available.
     * It iterates over neighboring cells, checks if they are already filled or not, and
     * recursively calls itself to check if the remaining neighbors can fill the cell.
     * 
     * @param row 2D coordinate of the current cell being checked for orphan detection
     * and solving of the Sudoku puzzle.
     * 
     * @param column 2D position of the cell in the grid where the algorithm is trying
     * to find a matching cell for the given `count`.
     * 
     * @param count 2-dimensional position of a cell in the grid that is being searched
     * for an orphan.
     * 
     * @returns a boolean value indicating whether the Sudoku puzzle has been solved.
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
     * Calculates and returns a list of neighboring cell indices for a given cell (row,
     * column) in a 2D grid, based on moves (row and column changes). It counts the number
     * of neighbors at each index and stores it in an array along with the cell index.
     * 
     * @param row 2D coordinate of the cell in the grid that the neighbor search is being
     * performed for.
     * 
     * @param column 2D position of the cell being checked for neighbors within the grid.
     * 
     * @returns a list of integer arrays representing the neighbors of a given cell in a
     * 2D grid, along with their count of surrounding cells of the same value.
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
     * Counts the number of neighbors of a given cell (row, column) in a grid, based on
     * the moves array.
     * 
     * @param row 2D coordinate of the grid cell whose neighbors are being counted.
     * 
     * @param column 2D coordinate of the grid position where the neighbors are counted.
     * 
     * @returns the number of neighbors of a given cell in the grid.
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
     * Checks if a given cell is an orphan by examining its neighbors and verifying if
     * any of them have zero count neighbors.
     * 
     * @param count number of occupied cells in the grid at row `row` and column `column`,
     * which is used to determine whether an orphan cell exists at that location.
     * 
     * @param row 1D coordinate of the cell being analyzed within the 2D grid.
     * 
     * @param column 2D position of the current cell in the grid, which is used to determine
     * the neighbors of the cell and check if any of them are orphans.
     * 
     * @returns a boolean value indicating whether an orphan block exists at the specified
     * position.
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
