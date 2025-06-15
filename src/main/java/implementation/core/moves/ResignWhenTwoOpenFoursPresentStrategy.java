package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;
import implementation.core.LinesCounter;

import java.util.Optional;

public class ResignWhenTwoOpenFoursPresentStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        var opponentOpen4 = LinesCounter.countUniqueOpenLines(board, board.getOpponentMark(), 4);
        if (opponentOpen4 >= 2) throw new ResignException();
        return Optional.empty();
    }
}
