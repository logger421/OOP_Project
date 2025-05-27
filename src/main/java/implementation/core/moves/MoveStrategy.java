package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import implementation.core.Board;

import java.util.Optional;

public interface MoveStrategy {
    int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

    Optional<Move> findMove(Board board, Mark mark);
}
