package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;

public class CreateForkStrategy implements MoveStrategy {

    private final BoardConfig boardConfig = BoardConfig.getInstance();

    private static final int[][] DIRS = {
            {1, 0},
            {0, 1},
            {1, 1},
            {1, -1}
    };

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        for (Position pos : board.getEmptyPositions()) {
            Board simulation = board.clone();
            simulation.placeMark(pos, mark);

            int lines = 0;
            for (int[] direction : DIRS) {
                int count = 1 + countDirection(simulation, pos, mark, direction[0], direction[1]) + countDirection(simulation, pos, mark, -direction[0], -direction[1]);
                if (count >= 3) lines++;
                if (lines >= 2) return Optional.of(new Move(pos, mark));

            }
        }
        return Optional.empty();
    }

    private int countDirection(Board board, Position pos, Mark mark, int dx, int dy) {
        int cnt = 0;
        int row = pos.row(), col = pos.col();
        int size = boardConfig.getSize();
        boolean periodic = boardConfig.isPeriodic();

        while (true) {
            row += dx;
            col += dy;
            if (periodic) {
                row = (row + size) % size;
                col = (col + size) % size;
            } else {
                if (row < 0 || row >= size || col < 0 || col >= size) break;
            }
            if (board.getMarkAt(row, col) == mark) cnt++;
            else break;
        }
        return cnt;
    }
}
