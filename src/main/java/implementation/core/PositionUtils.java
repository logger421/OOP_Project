package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.ArrayList;
import java.util.List;

public class PositionUtils {
    public static final int MAX_SIZE = 15;
    public static final int MIN_SIZE = 10;
    public static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};


    public static boolean isWithinBounds(Board board, Position p) {
        int size = board.getSize();
        if (board.isPeriodic()) {
            return true;
        }
        return p.col() >= 0 && p.col() < size && p.row() >= 0 && p.row() < size;
    }

    public static List<Position> getEmptyPositions(Board board) {
        List<Position> empties = new ArrayList<>();
        int size = board.getSize();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (board.getMarkAt(row, col) == Mark.NULL) {
                    empties.add(new Position(col, row));
                }
            }
        }

        return empties;
    }

    public static boolean createsLineWithOpenEnds(Board board, Position pos, Mark mark, int length, int dx, int dy) {
        Board sim = board.clone();
        sim.placeMarkAt(pos, mark);

        int f = sim.countDirection(pos, mark, dx, dy);
        int b = sim.countDirection(pos, mark, -dx, -dy);
        if (1 + f + b != length) return false;

        Position endF = new Position(pos.col() + dy * (f + 1), pos.row() + dx * (f + 1));
        Position endB = new Position(pos.col() - dy * (b + 1), pos.row() - dx * (b + 1));

        boolean openF = isWithinBounds(board, endF) && sim.getMarkAt(endF) == Mark.NULL;
        boolean openB = isWithinBounds(board, endB) && sim.getMarkAt(endB) == Mark.NULL;

        return openF && openB;
    }
}
