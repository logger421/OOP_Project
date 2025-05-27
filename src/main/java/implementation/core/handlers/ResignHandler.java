package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.List;
import java.util.Optional;

public class ResignHandler extends MoveHandler {
    public ResignHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws ResignException {
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;
        List<Position> opponentThreats = board.getEmptyPositions().stream()
                .filter(p -> board.isWinningMove(p, opponent))
                .toList();

        if (opponentThreats.size() > 1) {
            throw new ResignException();
        }

        return Optional.empty();
    }
}
