package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class ResignWhenTwoFivesPossibleStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        introduce(mark);
        Set<Position> immediateThreats = board.getOpponentImmediateWinningPositions();
        if (immediateThreats.size() >= 2) throw new ResignException();
        return Optional.empty();
    }
}
