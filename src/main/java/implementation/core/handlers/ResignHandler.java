package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class ResignHandler extends MoveHandler {
    private final BoardConfig boardConfig = BoardConfig.getInstance();

    public ResignHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws ResignException {
        System.out.println();
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;

        Set<Position> immediateThreats = board.findImmediateWinningPositions(opponent);
        if (immediateThreats.size() > 1) throw new ResignException();

        Set<Position> open4 = board.getOpenFourThreatPositions(opponent);
        Set<Position> closed4 = board.getClosedFourThreatPositions(opponent);

        if (!open4.isEmpty() || closed4.size() >= 2) {
            throw new ResignException();
        }

        return Optional.empty();
    }
}
