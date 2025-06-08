package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class BlockOpponentStrategy implements MoveStrategy {
    private final BoardConfig boardConfig = BoardConfig.getInstance();

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;

        Set<Position> immediateThreats = board.findImmediateWinningPositions(opponent);
        if (immediateThreats.size() == 1) {
            return Optional.of(new Move(immediateThreats.iterator().next(), mark));
        } else if (immediateThreats.size() > 1) {
            return Optional.empty();
        }

        var opponentOpenFour = board.getOpenFourThreatPositions(opponent);
        var ourBest = board.bestLine(mark);
        if (opponentOpenFour.size() == 1 && ourBest < 4) {
            for (Position position : opponentOpenFour) {
                return Optional.of(new Move(position, mark));
            }
        }

        var closed4 = board.getClosedFourThreatPositions(opponent);
        if (closed4.size() == 2 && ourBest < 3) {
            for (Position position : closed4) {
                return Optional.of(new Move(position, mark));
            }
        }

        return Optional.empty();
    }
}
