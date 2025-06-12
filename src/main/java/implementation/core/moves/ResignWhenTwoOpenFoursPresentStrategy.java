package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;

public class ResignWhenTwoOpenFoursPresentStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        introduce(mark);
        var opponentOpen4 = board.countUniqueOpenLines(board.getOpponentMark(), 4);
        if (opponentOpen4 >= 2) throw new ResignException();
        return Optional.empty();
    }
}
