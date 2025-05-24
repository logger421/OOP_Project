package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private static final int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

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

    public boolean isInside(int row, int col) {
        if (periodic) return true;
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public Mark getMarkAt(int row, int col) {
        if (periodic) {
            row = (row + size) % size;
            col = (col + size) % size;
        }
        return grid[col][row];
    }

    public List<Position> getEmptyPositions() {
        List<Position> empties = new ArrayList<>();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (grid[col][row] == Mark.NULL) {
                    empties.add(new Position(col, row));
                }
            }
        }
        return empties;
    }

    public void placeMark(Position pos, Mark mark) {
        if (periodic)
            pos = new Position((pos.col() + size) % size, (pos.row() + size) % size);
        grid[pos.col()][pos.row()] = mark;
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
                if (grid[col][row] == mark) {
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

    private int countDirection(Position pos, Mark mark, int dx, int dy) {
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
            if (grid[col][row] == mark) cnt++;
            else break;
        }

        return cnt;
    }

    public int countWinningLines(Mark mark) {
        int lines = 0;
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (getMarkAt(col, row) != mark) continue;
                for (int[] direction : directions) {
                    int pr = col - direction[0], pc = row - direction[1];

                    if (isInside(pr, pc) && getMarkAt(pr, pc) == mark)
                        continue;

                    int len = 0, rr = col, cc = row;
                    while (isInside(rr, cc) && getMarkAt(rr, cc) == mark) {
                        len++;
                        rr += direction[0];
                        cc += direction[1];
                    }

                    if (len >= 5) lines++;
                }
            }
        }
        return lines;
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
}

