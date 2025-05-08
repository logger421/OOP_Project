package implementation.core.handlers;


import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import fais.zti.oramus.gomoku.TheWinnerIsException;
import implementation.core.Board;

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
     * Template method: delegates to subclass, or passes to next.
     */
    public final Optional<Move> handle(Board board, Mark mark) throws TheWinnerIsException, ResignException {

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
     * Concrete handlers implementing this to propose a move or return empty.
     */
    protected abstract Optional<Move> doHandle(Board board, Mark mark) throws TheWinnerIsException, ResignException;
}

