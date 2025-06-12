package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board implements Cloneable {
    public static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

    private final Mark ourMark;
    private final Mark opponentMark;
    private final int size;
    private final boolean periodic;
    private final Mark[][] grid;

    private Set<Position> immediateWinningPositions;
    private Set<Position> opponentImmediateWinningPositions;

    public Board(int size, boolean periodic, Mark currentMark, Mark opponentMark) {
        this.size = size;
        this.periodic = periodic;
        this.grid = new Mark[size][size];
        this.ourMark = currentMark;
        this.opponentMark = opponentMark;
    }

    public void initialize(Set<Move> rawMoves) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Mark.NULL;
            }
        }

        for (Move mv : rawMoves)
            this.placeMarkAt(mv.position(), mv.mark());

        immediateWinningPositions = findImmediateWinningPositions(ourMark);
        opponentImmediateWinningPositions = findImmediateWinningPositions(opponentMark);
    }

    @Override
    public Board clone() {
        Board copy = new Board(size, periodic, ourMark, opponentMark);
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

    public Mark getMarkAt(Position position) {
        return getMarkAt(position.row(), position.col());
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

    public Mark getOpponentMark() {
        return opponentMark;
    }

    public Set<Position> getOpponentImmediateWinningPositions() {
        return opponentImmediateWinningPositions;
    }

    public Set<Position> getImmediateWinningPositions() {
        return immediateWinningPositions;
    }

    public boolean isWinningMove(Position pos, Mark mark) {
        for (int[] direction : DIRECTIONS) {
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
                    for (int[] direction : DIRECTIONS) {
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

    public Set<Position> getOpenFourThreatPositions(Mark mark) {
        Set<Position> threats = new HashSet<>();
        for (Position pos : getEmptyPositions()) {
            for (int[] d : DIRECTIONS) {
                if (createsLineWithOpenEnds(pos, mark, 4, d[0], d[1])) {
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
            for (int[] d : DIRECTIONS) {
                if (createsLineWithOpenEnds(pos, mark, 3, d[0], d[1])) {
                    threats.add(pos);
                    break;
                }
            }
        }
        return threats;
    }

    public int countUniqueOpenLines(Mark mark, int length) {
        Set<Set<Position>> uniqueLines = new HashSet<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position startPos = new Position(col, row);
                if (getMarkAt(startPos) != mark) continue;
                for (int[] dir : DIRECTIONS) {
                    if (!periodic) {
                        int endRow = row + (length - 1) * dir[0];
                        int endCol = col + (length - 1) * dir[1];
                        if (endRow < 0 || endRow >= size || endCol < 0 || endCol >= size) continue;
                    }

                    Set<Position> linePositions = new HashSet<>();
                    linePositions.add(startPos);
                    boolean validLine = true;

                    for (int i = 1; i < length; i++) {
                        int r = row + i * dir[0];
                        int c = col + i * dir[1];
                        Position pos = new Position(c, r);

                        if (getMarkAt(pos) != mark) {
                            validLine = false;
                            break;
                        }
                        linePositions.add(pos);
                    }

                    if (!validLine) continue;

                    Position beforePos = new Position(col - dir[1], row - dir[0]);
                    Position afterPos = new Position(col + length * dir[1], row + length * dir[0]);

                    boolean beforeOpen = isWithinBounds(beforePos) && getMarkAt(beforePos) == Mark.NULL;
                    boolean afterOpen = isWithinBounds(afterPos) && getMarkAt(afterPos) == Mark.NULL;

                    if (!periodic) {
                        beforeOpen = !isWithinBounds(beforePos) || beforeOpen;
                        afterOpen = !isWithinBounds(afterPos) || afterOpen;
                    }

                    if (beforeOpen && afterOpen) {
                        uniqueLines.add(linePositions);
                    }
                }
            }
        }

        return uniqueLines.size();
    }

    public int countPotentialOpenLinesFormed(Position position, Mark mark, int length) {
        if (getMarkAt(position) != Mark.NULL) return 0;

        placeMarkAt(position, mark);

        int lineCount = 0;
        for (int[] dir : DIRECTIONS) {
            int forwardCount = countConsecutiveMarks(position, dir[0], dir[1], mark);
            int backwardCount = countConsecutiveMarks(position, -dir[0], -dir[1], mark);
            int totalMarks = 1 + forwardCount + backwardCount;

            if (totalMarks == length) {
                Position forwardEnd = new Position(position.col() + dir[1] * (forwardCount + 1), position.row() + dir[0] * (forwardCount + 1));
                Position backwardEnd = new Position(position.col() - dir[1] * (backwardCount + 1), position.row() - dir[0] * (backwardCount + 1));

                boolean forwardOpen = periodic ? getMarkAt(forwardEnd) == Mark.NULL : isWithinBounds(forwardEnd) && getMarkAt(forwardEnd) == Mark.NULL;
                boolean backwardOpen = periodic ? getMarkAt(backwardEnd) == Mark.NULL : isWithinBounds(backwardEnd) && getMarkAt(backwardEnd) == Mark.NULL;

                if (forwardOpen && backwardOpen) lineCount++;
            }
        }

        placeMarkAt(position, Mark.NULL);
        return lineCount;
    }

    private boolean createsLineWithOpenEnds(Position pos, Mark mark, int length, int dx, int dy) {
        Board sim = this.clone();
        sim.placeMarkAt(pos, mark);

        int f = sim.countDirection(pos, mark, dx, dy);
        int b = sim.countDirection(pos, mark, -dx, -dy);
        if (1 + f + b != length) return false;
        Position endF = new Position(pos.col() + dy * (f + 1), pos.row() + dx * (f + 1));
        Position endB = new Position(pos.col() - dy * (b + 1), pos.row() - dx * (b + 1));
        boolean openF = isWithinBounds(endF) && sim.getMarkAt(endF) == Mark.NULL;
        boolean openB = isWithinBounds(endB) && sim.getMarkAt(endB) == Mark.NULL;
        return openF && openB;
    }

    private int countConsecutiveMarks(Position start, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        int row = start.row() + rowDelta;
        int col = start.col() + colDelta;

        while (row >= 0 && row < size && col >= 0 && col < size && getMarkAt(row, col) == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }

        return count;
    }

    private boolean isWithinBounds(Position p) {
        if (periodic) {
            return true;
        }
        return p.col() >= 0 && p.col() < size && p.row() >= 0 && p.row() < size;
    }

    private Set<Position> findImmediateWinningPositions(Mark mark) {
        Set<Position> winningPositions = new java.util.HashSet<>();
        for (Position pos : getEmptyPositions()) {
            if (isWinningMove(pos, mark)) {
                winningPositions.add(pos);
            }
        }
        return winningPositions;
    }
}

