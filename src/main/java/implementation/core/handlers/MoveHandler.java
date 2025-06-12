package implementation.core.handlers;


import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import fais.zti.oramus.gomoku.TheWinnerIsException;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;

public abstract class MoveHandler {
    private final MoveHandler next;
    protected final BoardConfig config = BoardConfig.getInstance();

    protected MoveHandler(MoveHandler next) {
        this.next = next;
    }

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


    protected abstract Optional<Move> doHandle(Board board, Mark mark) throws TheWinnerIsException, ResignException;
}

