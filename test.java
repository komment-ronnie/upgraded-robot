package com.thealgorithms.backtracking;

import java.util.*;

/**
 * Is an implementation of the N-Queens problem, where the goal is to place N queens
 * on a board such that no queen attacks any other queen. The class has several methods
 * for solving this problem, including `size()` which returns the number of rows on
 * the board, `grid` which is the 2D array representing the board, and `total` which
 * is the total number of squares on the board. The `solve()` method takes a row and
 * column as input and checks if there is a queen that can be placed at that position
 * without attacking any other queen. If there is a solution, it prints the result
 * and returns true; otherwise, it returns false. The class also has several utility
 * methods for working with the grid, such as `neighbors()` which returns a list of
 * nearby cells, `countNeighbors()` which counts the number of non-zero neighbors of
 * a given cell, and `orphanDetected()` which checks if a node is an orphan (i.e.,
 * has no alive neighbors).
 */
public class KnightsTour {
    
    /**
     * Calculates the number of nodes in a circular linked list by iterating over the
     * list from its head and counting the number of non-null items. If the count reaches
     * the maximum value, the list is considered empty.
     * 
     * @returns the number of nodes in the circular buffer.
     */
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
    }; 
    
    private static int[][] grid; // chess grid
    private static int total; // total squares in chess

    /**
     * Generates a 2D grid with random values, checks for existence of a path between two
     * random coordinates using a recursive method `solve`, and prints the result.
     * 
     * @param args 0-or-more command line arguments passed to the program, which are
     * ignored in this case and have no effect on the function's execution.
     * 
     * 	- Length: `args.length` is equal to 0 or 1.
     * 	- Elements: If `args.length > 0`, then `args[0]` is a String representing the
     * command line argument. Otherwise, there are no command line arguments.
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
     * Determines if a given cell can be filled with a number greater than its current
     * value, based on the values of its neighbors and the total number of cells that can
     * be filled.
     * 
     * @param row 2D coordinate of the cell being checked for orphan status.
     * 
     * @param column 2nd dimension of the grid, which is used to determine the position
     * of the cell being checked for orphans and to update the count of orphans for each
     * cell.
     * 
     * @param count 2D coordinate of the cell that the function is trying to find the
     * value for, and it is used to determine if the function should continue searching
     * or stop when a valid value is found.
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
     * Calculates and returns a list of neighboring cells to a given cell in a grid, based
     * on the movement patterns of the grid's cells.
     * 
     * @param row 2D coordinate of the cell in the grid for which the neighbors are being
     * calculated.
     * 
     * @param column 2D coordinate of the cell in the grid that is being analyzed for neighbors.
     * 
     * @returns a list of triples containing the row and column of the current cell, and
     * the number of neighbors with the same value.
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
     * Counts the number of neighbors of a given cell in a grid that are occupied by the
     * game pieces.
     * 
     * @param row 2D coordinate of the cell for which the number of neighbors is being counted.
     * 
     * @param column 2D coordinate of the cell in the grid that the function is called
     * for, and is used to determine which cells to count as neighbors.
     * 
     * @returns the number of neighbors of a given cell in a grid.
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
     * Checks if a given cell is an orphan by examining its neighbors and determining if
     * any neighboring cells have a count of zero. If so, the function returns `true`.
     * 
     * @param count 2D coordinate of the cell being checked for orphan status, and is
     * used to determine if the cell has any neighbors with zero count.
     * 
     * @param row 1D coordinate of the current cell being evaluated in the grid.
     * 
     * @param column 2D coordinate of the cell being analyzed in the grid.
     * 
     * @returns a boolean value indicating whether an orphan cell is detected in the given
     * grid.
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
     * Iterates over a 2D array `grid` and prints each element in a compact format, with
     * leading zeroes and a final newline.
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
