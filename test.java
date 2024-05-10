package com.thealgorithms.backtracking;

import java.util.*;

/**
 * is a Java program that solves the classic problem of finding the Knight's Tour on
 * an NxN chessboard. The class has several methods for determining the moves available
 * to the knight at each position, counting the number of neighbors of a given cell,
 * and checking if a cell is an orphan (i.e., has no alive neighbors). The `printResult()`
 * method prints the values stored in the grid array with each row represented as a
 * list of integers.
 */
public class KnightsTour {
    
    /**
     * iterates through a linked list's nodes, counting the number of non-null items and
     * breaking the loop when it reaches the maximum value or finds the end of the list.
     * It returns the count of non-null items.
     * 
     * @returns the number of elements in the collection.
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
     * generates a grid of size base x base, sets some cells to -1, and then solves the
     * game by finding the cell containing the single 1. If the solution is found, it
     * prints the result; otherwise, it prints "no result".
     * 
     * @param args 0 or more command-line arguments passed to the program, which are not
     * used in this case.
     * 
     * 	- `args`: An array of `String` objects, representing the command-line arguments
     * passed to the program.
     * 	- `base`: A constant integer representing the size of the grid.
     * 	- `total`: The total area of the grid, calculated as the product of `base` and `base`.
     * 
     * The code then proceeds to fill the grid with values, marking any cells that are
     * outside the boundaries of the grid as `-1`. Finally, it solves a particular instance
     * of the Sudoku puzzle using the `solve()` function, and prints the result if the
     * solution is found, or "no result" otherwise.
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
     * determines if a given cell can be filled with a specific value by iterating through
     * neighboring cells and updating their values based on a sorting and comparison algorithm.
     * 
     * @param row 2D coordinate of the cell being checked for orphans, and is used to
     * determine the corresponding value in the grid array and to perform the orphan
     * detection check.
     * 
     * @param column 2nd dimension of the grid, which is used to determine the position
     * of the cell to be examined and its neighbors in the grid.
     * 
     * @param count 2D position of a tile in the grid that is being searched for orphaned
     * tiles.
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
     * This function `neighbors(int row++, int column++)` returns a list of integer tuples
     * representing the neighbors of the current position (row and column) on the game
     * grid. It does so by iterating over a list of valid moves `moves` and counting the
     * number of neighbors for each position reached by moving from the current position
     * to the target position.
     * 
     * @param row The `row` input parameter specifies the row number of the current cell
     * being processed. It is used to index into the `grid` array to determine the values
     * of cells located above or to the left of the current cell.
     * 
     * @param column The `column` input parameter specifies the current position on the
     * grid that is being analyzed for possible moves.
     * 
     * @returns This function returns a List of integer arrays representing the neighbors
     * of a given row and column position on a grid. Each integer array has three elements:
     * the y-coordinate of the neighboring cell (second element), the x-coordinate of the
     * neighboring cell (first element), and the number of neurons that cell contains
     * (third element).
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
     * This function counts the number of non-zero cells that are located on the same row
     * and column as a given cell.
     * 
     * @param row The `row` input parameter represents the current cell being evaluated
     * for neighboring cells. It determines the starting point for the iteration over the
     * move array.
     * 
     * @param column The `column` input parameter is used to iterate through the rows of
     * the grid while counting the number of neighboring empty cells. It determines the
     * current column being examined.
     * 
     * @returns The output returned by this function is an integer value that represents
     * the number of non-zero neighbors of a given cell (row and column) on a grid with
     * size NxN.
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
     * This function checks if a given cell (row and column) has no alive neighbors (cells
     * with a value of 1). It does this by iterating over the neighbors of the given cell
     * and checking if all of their alive neighbors have been cleared. If such a cell is
     * found (i.e., a cell with no alive neighbors), the function returns true. Otherwise
     * (if no such cell is found), it returns false.
     * 
     * @param count The `count` input parameter represents the number of mines remaining
     * to be discovered.
     * 
     * @param row The `row` input parameter specifies the row number of the detected orphan.
     * 
     * @param column The `column` parameter represents the column index of the current
     * cell being evaluated for orphanhood.
     * 
     * @returns This function takes three integers `count`, `row`, and `column` as input
     * and returns a boolean value indicating whether an orphan node (i.e., a node that
     * has no neighbors) has been detected.
     * 
     * Here's a concise description of the output:
     * 
     * 	- If `count < total - 1` (i.e., there are still some neighboring nodes to be
     * checked), the function checks the neighboring nodes of the current node (`row`, `column`).
     * 	- If none of the neighbors have any neighbors themselves (i.e., `countNeighbors(nb[0],
     * nb[1]) == 0`), then an orphan node has been detected and the function returns `true`.
     * 	- Otherwise (i.e., there is at least one neighbor with neighbors), the function
     * returns `false`.
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
