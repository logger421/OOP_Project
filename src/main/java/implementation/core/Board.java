package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Mark getMarkAt(int row, int col) {
        if (periodic) {
            row = (row + size) % size;
            col = (col + size) % size;
        }
        return grid[col][row];
    }

    public void placeMarkAt(Position pos, Mark mark) {
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

    public Set<Position> findImmediateWinningPositions(Mark mark) {
        Set<Position> winningPositions = new java.util.HashSet<>();
        for (Position pos : getEmptyPositions()) {
            if (isWinningMove(pos, mark)) {
                winningPositions.add(pos);
            }
        }
        return winningPositions;
    }

    public boolean isPositionOpen(Position pos, int dx, int dy, int steps) {
        int col = pos.col() + dx * (steps + 1);
        int row = pos.row() + dy * (steps + 1);

        if (!periodic) {
            if (col < 0 || col >= size || row < 0 || row >= size) {
                return false;
            }
        }
        return getMarkAt(row, col) == Mark.NULL;
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

    public int bestLine(Mark mark) {
        int maxLen = 0;
        for (Position pos : getEmptyPositions()) {
            Board sim = this.clone();
            sim.placeMarkAt(pos, mark);

            for (int[] d : Board.directions) {
                int len = 1 + sim.countDirection(pos, mark, d[0], d[1]) + sim.countDirection(pos, mark, -d[0], -d[1]);
                if (len > maxLen) maxLen = len;
            }
        }
        return maxLen;
    }

    public Set<Position> getOpenFourThreatPositions(Mark mark) {
        Set<Position> threats = new HashSet<>();
        for (Position pos : getEmptyPositions()) {
            for (int[] d : directions) {
                if (createsLineWithOpenEnds(pos, mark, 4, d[0], d[1])) {
                    threats.add(pos);
                    break;
                }
            }
        }
        return threats;
    }

    public Set<Position> getClosedFourThreatPositions(Mark mark) {
        Set<Position> threats = new HashSet<>();
        for (Position pos : getEmptyPositions()) {
            for (int[] d : directions) {
                if (createsLineWithOneOpenEnd(pos, mark, 4, d[0], d[1])) {
                    threats.add(pos);
                    break;
                }
            }
        }
        return threats;
    }

    public Set<Position> getOpenThreeThreatPositions(Mark mark) {
        Set<Position> threats = new HashSet<>();
        for (Position pos : getEmptyPositions()) {
            for (int[] d : directions) {
                if (createsLineWithOpenEnds(pos, mark, 3, d[0], d[1])) {
                    threats.add(pos);
                    break;
                }
            }
        }
        return threats;
    }

    private boolean createsLineWithOpenEnds(Position pos, Mark mark, int length, int dx, int dy) {
        Board sim = clone();
        sim.placeMarkAt(pos, mark);
        int f = sim.countDirection(pos, mark, dx, dy);
        int b = sim.countDirection(pos, mark, -dx, -dy);
        if (1 + f + b != length) return false;
        Position endF = new Position(pos.col() + dy * (f + 1), pos.row() + dx * (f + 1));
        Position endB = new Position(pos.col() - dy * (b + 1), pos.row() - dx * (b + 1));
        boolean openF = isWithinBounds(endF) && sim.getMarkAt(endF.row(), endF.col()) == Mark.NULL;
        boolean openB = isWithinBounds(endB) && sim.getMarkAt(endB.row(), endB.col()) == Mark.NULL;
        return openF && openB;
    }

    private boolean createsLineWithOneOpenEnd(Position pos, Mark mark, int length, int dx, int dy) {
        Board sim = clone();
        sim.placeMarkAt(pos, mark);
        int f = sim.countDirection(pos, mark, dx, dy);
        int b = sim.countDirection(pos, mark, -dx, -dy);
        if (1 + f + b != length) return false;
        Position endF = new Position(pos.col() + dy * (f + 1), pos.row() + dx * (f + 1));
        Position endB = new Position(pos.col() - dy * (b + 1), pos.row() - dx * (b + 1));
        boolean openF = isWithinBounds(endF) && sim.getMarkAt(endF.row(), endF.col()) == Mark.NULL;
        boolean openB = isWithinBounds(endB) && sim.getMarkAt(endB.row(), endB.col()) == Mark.NULL;
        return openF ^ openB;
    }

    private boolean isWithinBounds(Position p) {
        if (periodic) {
            return true;
        }
        return p.col() >= 0 && p.col() < size && p.row() >= 0 && p.row() < size;
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Mark.NULL;
            }
        }
    }
}

