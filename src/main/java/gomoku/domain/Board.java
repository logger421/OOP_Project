package gomoku.domain;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private final int size;
    private final boolean periodic;
    private final Mark[][] grid;

    /**
     * Initializes an empty board with given size and boundary mode.
     */
    public Board(int size, boolean periodic) {
        this.size = size;
        this.periodic = periodic;
        this.grid = new Mark[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Mark.NULL;
            }
        }
    }

    /**
     * Returns all positions on the board that are currently empty.
     */
    public List<Position> getEmptyPositions() {
        List<Position> empties = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == Mark.NULL) {
                    empties.add(new Position(i, j));
                }
            }
        }
        return empties;
    }

    /**
     * Places a mark at the given position.
     */
    public void placeMark(Position pos, Mark mark) {
        grid[pos.row()][pos.col()] = mark;
    }

    /**
     * Checks whether placing 'mark' at 'pos' would create a five-in-a-row.
     */
    public boolean isWinningMove(Position pos, Mark mark) {
        int[][] directions = {{1,0},{0,1},{1,1},{1,-1}};
        for (int[] d : directions) {
            int count = 1; // count this position
            count += countDirection(pos, mark, d[0], d[1]);
            count += countDirection(pos, mark, -d[0], -d[1]);
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts consecutive marks from 'pos' in direction (dx,dy).
     */
    private int countDirection(Position pos, Mark mark, int dx, int dy) {
        int r = pos.row();
        int c = pos.col();
        int cnt = 0;
        while (true) {
            r += dx;
            c += dy;
            if (periodic) {
                r = (r + size) % size;
                c = (c + size) % size;
            } else {
                if (r < 0 || r >= size || c < 0 || c >= size) break;
            }
            if (grid[r][c] == mark) cnt++;
            else break;
        }
        return cnt;
    }

    @Override
    public Board clone() {
        Board copy = new Board(size, periodic);
        for (int i = 0; i < size; i++) {
            System.arraycopy(this.grid[i], 0, copy.grid[i], 0, size);
        }
        return copy;
    }
}

