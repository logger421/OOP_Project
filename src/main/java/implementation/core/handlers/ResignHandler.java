package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;
import implementation.core.moves.WinImmediatelyStrategy;

import java.util.Optional;

public class ResignHandler extends MoveHandler {
    public ResignHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws ResignException {
        // Determine the opponent's mark
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;
        Board simulated = board.clone();

        Optional<Move> move = new WinImmediatelyStrategy().findMove(simulated, opponent);

        // If the opponent can win immediately, throw a ResignException
        if (move.isPresent()) {
            throw new ResignException();
        }
        // Otherwise, return empty to continue the chain
        return Optional.empty();
    }

}
