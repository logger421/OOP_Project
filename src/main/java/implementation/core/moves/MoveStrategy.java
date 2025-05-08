package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import implementation.core.Board;

import java.util.Optional;

public interface MoveStrategy {
    Optional<Move> findMove(Board board, Mark mark);
}
