package gomoku.chain;


import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import gomoku.domain.Board;

import java.util.Optional;

/**
 * Abstract handler implementing the Template Method for move computation and chaining.
 */
public abstract class MoveHandler {
    private final MoveHandler next;

    protected MoveHandler(MoveHandler next) {
        this.next = next;
    }

    /**
     * Template method: logs, delegates to subclass, or passes to next.
     */
    public final Optional<Move> handle(Board board, Mark mark) {
        preHandle(board, mark);
        Optional<Move> result = doHandle(board, mark);
        if (result.isPresent()) {
            return result;
        }
        if (next != null) {
            return next.handle(board, mark);
        }
        return Optional.empty();
    }

    /**
     * Hook for logging/timing before actual handling.
     */
    protected void preHandle(Board board, Mark mark) {
        // e.g., logging or performance metrics
    }

    /**
     * Concrete handlers implement this to propose a move or return empty.
     */
    protected abstract Optional<Move> doHandle(Board board, Mark mark);
}

