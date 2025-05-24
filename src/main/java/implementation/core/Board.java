package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private final int size;
    private final boolean periodic;
    private final Mark[][] grid;

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


    public int getSize() {
        return size;
    }

    public boolean isInside(int row, int col) {
        if (periodic) {
            return true;
        }
        return row >= 0 && row < size
                && col >= 0 && col < size;
    }

    public Mark getMarkAt(int r, int c) {
        if (!isInside(r, c)) {
            throw new IndexOutOfBoundsException("Outside board: " + r + "," + c);
        }
        if (periodic) {
            r = (r + size) % size;
            c = (c + size) % size;
        }
        return grid[r][c];
    }

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

    public void placeMark(Position pos, Mark mark) {
        grid[pos.row()][pos.col()] = mark;
    }

    public boolean isWinningMove(Position pos, Mark mark) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] d : directions) {
            int count = 1;
            count += countDirection(pos, mark, d[0], d[1]);
            count += countDirection(pos, mark, -d[0], -d[1]);

            if (count >= 5) return true;
        }
        return false;
    }

    public boolean hasAlreadyWon(Mark m) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == m) {
                    for (int[] d : directions) {
                        int count = 1;
                        count += countDirection(new Position(i, j), m, d[0], d[1]);
                        count += countDirection(new Position(i, j), m, -d[0], -d[1]);

                        if (count >= 5) return true;
                    }
                }
            }
        }
        return false;
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int rowNumWidth = String.valueOf(size - 1).length();

        sb.append(" ".repeat(rowNumWidth + 1));

        for (int i = 0; i < size; i++) {
            sb.append(i % 10).append(" ");
        }
        sb.append("\n");

        sb.append(" ".repeat(rowNumWidth)).append("+");
        sb.append("--".repeat(Math.max(0, size))); // Two dashes per cell
        sb.append("+\n");

        for (int i = 0; i < size; i++) {
            String rowNum = String.valueOf(i);
            sb.append(" ".repeat(rowNumWidth - rowNum.length())).append(rowNum).append("|");

            for (int j = 0; j < size; j++) {
                sb.append(grid[i][j] == Mark.NULL ? "." : grid[i][j].toString().toUpperCase()).append(" ");
            }
            sb.append("|\n");
        }

        // Bottom border
        sb.append(" ".repeat(rowNumWidth)).append("+");
        sb.append("--".repeat(Math.max(0, size))); // Two dashes per cell
        sb.append("+\n");

        // Add a legend for larger boards
        if (size > 10) {
            sb.append("Note: Column numbers shown are modulo 10\n");
        }

        return sb.toString();
    }
}

