package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.Set;

import static implementation.core.PositionUtils.DIRECTIONS;

public class WinningPositionsCalculator {

    public static Set<Position> findImmediateWinningPositions(Board board, Mark mark) {
        Set<Position> winningPositions = new java.util.HashSet<>();
        for (Position pos : board.getEmptyPositions())
            if (isWinningMove(board, pos, mark))
                winningPositions.add(pos);

        return winningPositions;
    }

    public static boolean hasAlreadyWon(Board board, Mark mark) {
        int size = board.getSize();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (board.getMarkAt(row, col) == mark) {
                    for (int[] direction : DIRECTIONS) {
                        int count = 1;
                        count += board.countDirection(new Position(col, row), mark, direction[0], direction[1]);
                        count += board.countDirection(new Position(col, row), mark, -direction[0], -direction[1]);
                        if (count >= 5) return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean isWinningMove(Board board, Position pos, Mark mark) {
        for (int[] direction : DIRECTIONS) {
            int count = 1;
            count += board.countDirection(pos, mark, direction[0], direction[1]);
            count += board.countDirection(pos, mark, -direction[0], -direction[1]);
            if (count >= 5) return true;
        }

        return false;
    }
}
