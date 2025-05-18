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
            Board sim = board.clone();
            sim.placeMark(pos, mark);

            int lines = 0;
            for (int[] d : DIRS) {
                int count = 1 + countDirection(sim, pos, mark, d[0], d[1]) + countDirection(sim, pos, mark, -d[0], -d[1]);
                if (count >= 3) lines++;
                if (lines >= 2) return Optional.of(new Move(pos, mark));

            }
        }
        return Optional.empty();
    }

    private int countDirection(Board board, Position pos, Mark mark, int dx, int dy) {
        int cnt = 0;
        int r = pos.row(), c = pos.col();
        int size = boardConfig.getSize();
        boolean periodic = boardConfig.isPeriodic();

        while (true) {
            r += dx;
            c += dy;
            if (periodic) {
                r = (r + size) % size;
                c = (c + size) % size;
            } else {
                if (r < 0 || r >= size || c < 0 || c >= size) break;
            }
            if (board.getMarkAt(r, c) == mark) cnt++;
            else break;
        }
        return cnt;
    }
}
