package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import implementation.core.Board;

import java.util.Optional;

public class DefaultHandler extends MoveHandler {
    public DefaultHandler() {
        super(null);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) {
        return board.getEmptyPositions().stream()
                .findFirst()
                .map(pos -> new Move(pos, mark));
    }
}