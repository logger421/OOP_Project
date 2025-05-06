package gomoku.strategy;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import gomoku.domain.Board;

import java.util.Optional;

public interface MoveStrategy {
    /**
     * Finds the best move for the given player on the provided board.
     *
     * @param board The game board.
     * @param mark  The player's mark (X or O).
     * @return An Optional containing the best move if found, otherwise an empty Optional.
     */
    Optional<Move> findMove(Board board, Mark mark);
}
