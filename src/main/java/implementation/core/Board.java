package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    public static final int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

    private final int size;
    private final boolean periodic;
    private final Mark[][] grid;

    public Board(int size, boolean periodic) {
        this.size = size;
        this.periodic = periodic;
        this.grid = new Mark[size][size];
        init();
    }

    public void placeMark(Position pos, Mark mark) {
        if (periodic)
            pos = new Position((pos.col() + size) % size, (pos.row() + size) % size);
        grid[pos.col()][pos.row()] = mark;
    }

    public List<Position> getEmptyPositions() {
        List<Position> empties = new ArrayList<>();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (getMarkAt(row, col) == Mark.NULL) {
                    empties.add(new Position(col, row));
                }
            }
        }
        return empties;
    }

    public boolean isWinningMove(Position pos, Mark mark) {
        for (int[] direction : directions) {
            int count = 1;
            count += countDirection(pos, mark, direction[0], direction[1]);
            count += countDirection(pos, mark, -direction[0], -direction[1]);
            if (count >= 5) return true;
        }
        return false;
    }

    public boolean hasAlreadyWon(Mark mark) {
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (getMarkAt(row, col) == mark) {
                    for (int[] direction : directions) {
                        int count = 1;
                        count += countDirection(new Position(col, row), mark, direction[0], direction[1]);
                        count += countDirection(new Position(col, row), mark, -direction[0], -direction[1]);
                        if (count >= 5) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isEndOpen(Position pos, int dx, int dy) {
        int row = pos.row() + dx, col = pos.col() + dy;
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        return getMarkAt(row, col) == Mark.CROSS;
    }

    public int countDirection(Position pos, Mark mark, int dx, int dy) {
        int row = pos.row();
        int col = pos.col();
        int cnt = 0;

        while (true) {
            row += dx;
            col += dy;
            if (periodic) {
                row = (row + size) % size;
                col = (col + size) % size;
            } else {
                if (row < 0 || row >= size || col < 0 || col >= size) break;
            }
            if (getMarkAt(row, col) == mark) cnt++;
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
        sb.append("--".repeat(Math.max(0, size)));
        sb.append("+\n");

        for (int i = 0; i < size; i++) {
            String rowNum = String.valueOf(i);
            sb.append(" ".repeat(rowNumWidth - rowNum.length())).append(rowNum).append("|");

            for (int j = 0; j < size; j++) {
                sb.append(grid[j][i] == Mark.NULL ? "." : grid[j][i].toString().toUpperCase()).append(" ");
            }
            sb.append("|\n");
        }

        sb.append(" ".repeat(rowNumWidth)).append("+");
        sb.append("--".repeat(Math.max(0, size)));
        sb.append("+\n");

        if (size > 10) {
            sb.append("Note: Column numbers shown are modulo 10\n");
        }

        return sb.toString();
    }

    private Mark getMarkAt(int row, int col) {
        if (periodic) {
            row = (row + size) % size;
            col = (col + size) % size;
        }
        return grid[col][row];
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Mark.NULL;
            }
        }
    }
}

