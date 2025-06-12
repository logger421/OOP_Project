package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.TheWinnerIsException;
import implementation.core.Board;

import java.util.Optional;

public class WinnerCheckHandler extends MoveHandler {

    public WinnerCheckHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws TheWinnerIsException {
        if(config.isDebug())
            System.out.println("Checking for winner in handler: " + this.getClass().getSimpleName());

        if (board.hasAlreadyWon(Mark.CROSS) || board.hasAlreadyWon(Mark.NOUGHT)) throw new TheWinnerIsException(mark);
        return Optional.empty();
    }
}
