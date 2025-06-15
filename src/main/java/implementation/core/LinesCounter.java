package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.HashSet;
import java.util.Set;

import static implementation.core.PositionUtils.DIRECTIONS;
import static implementation.core.PositionUtils.isWithinBounds;

public class LinesCounter {

    public static int countConsecutiveMarks(Board board, Position start, int rowDelta, int colDelta, Mark mark) {
        int size = board.getSize();
        int count = 0;
        int row = start.row() + rowDelta;
        int col = start.col() + colDelta;

        while (row >= 0 && row < size && col >= 0 && col < size && board.getMarkAt(row, col) == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }

        return count;
    }

    public static int countUniqueOpenLines(Board board, Mark mark, int length) {
        int size = board.getSize();
        boolean periodic = board.isPeriodic();
        Set<Set<Position>> uniqueLines = new HashSet<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position startPos = new Position(col, row);
                if (board.getMarkAt(startPos) != mark) continue;
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

                        if (board.getMarkAt(pos) != mark) {
                            validLine = false;
                            break;
                        }
                        linePositions.add(pos);
                    }

                    if (!validLine) continue;

                    Position beforePos = new Position(col - dir[1], row - dir[0]);
                    Position afterPos = new Position(col + length * dir[1], row + length * dir[0]);

                    boolean beforeOpen = isWithinBounds(board, beforePos) && board.getMarkAt(beforePos) == Mark.NULL;
                    boolean afterOpen = isWithinBounds(board, afterPos) && board.getMarkAt(afterPos) == Mark.NULL;

                    if (!periodic) {
                        beforeOpen = !isWithinBounds(board, beforePos) || beforeOpen;
                        afterOpen = !isWithinBounds(board, afterPos) || afterOpen;
                    }

                    if (beforeOpen && afterOpen) {
                        uniqueLines.add(linePositions);
                    }
                }
            }
        }

        return uniqueLines.size();
    }

    public static int countPotentialOpenLinesFormed(Board board, Position position, Mark mark, int length) {
        boolean periodic = board.isPeriodic();
        if (board.getMarkAt(position) != Mark.NULL) return 0;

        Board sim = board.clone();
        sim.placeMarkAt(position, mark);

        int lineCount = 0;
        for (int[] dir : DIRECTIONS) {
            int forwardCount = countConsecutiveMarks(board, position, dir[0], dir[1], mark);
            int backwardCount = countConsecutiveMarks(board, position, -dir[0], -dir[1], mark);
            int totalMarks = 1 + forwardCount + backwardCount;

            if (totalMarks == length) {
                Position forwardEnd = new Position(position.col() + dir[1] * (forwardCount + 1), position.row() + dir[0] * (forwardCount + 1));
                Position backwardEnd = new Position(position.col() - dir[1] * (backwardCount + 1), position.row() - dir[0] * (backwardCount + 1));

                boolean forwardOpen = periodic ? sim.getMarkAt(forwardEnd) == Mark.NULL : isWithinBounds(sim, forwardEnd) && sim.getMarkAt(forwardEnd) == Mark.NULL;
                boolean backwardOpen = periodic ? sim.getMarkAt(backwardEnd) == Mark.NULL : isWithinBounds(sim, backwardEnd) && sim.getMarkAt(backwardEnd) == Mark.NULL;

                if (forwardOpen && backwardOpen) lineCount++;
            }
        }

        return lineCount;
    }
}
