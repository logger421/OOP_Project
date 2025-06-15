package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.TheWinnerIsException;
import implementation.core.Board;
import implementation.core.WinningPositionsCalculator;

import java.util.Optional;

public class WinnerCheckHandler extends MoveHandler {

    public WinnerCheckHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws TheWinnerIsException {
        if (WinningPositionsCalculator.hasAlreadyWon(board, Mark.CROSS) || WinningPositionsCalculator.hasAlreadyWon(board, Mark.NOUGHT))
            throw new TheWinnerIsException(mark);
        return Optional.empty();
    }
}
