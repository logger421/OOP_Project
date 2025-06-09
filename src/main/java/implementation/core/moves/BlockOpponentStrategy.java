package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class BlockOpponentStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = board.getOpponentMark();
        Set<Position> immediateThreats = board.getOpponentImmediateWinningPositions();

        if (immediateThreats.size() == 1) {
            return Optional.of(new Move(immediateThreats.iterator().next(), mark));
        } else if (immediateThreats.size() > 1) {
            return Optional.empty();
        }

        var opponentOpenFour = board.getOpenFourThreatPositions(opponent);
        var ourPossibleBest = board.getPossibleLineLength();
        if (opponentOpenFour.size() == 1 && ourPossibleBest < 4) {
            for (Position position : opponentOpenFour) {
                return Optional.of(new Move(position, mark));
            }
        }

        var closed4 = board.getClosedFourThreatPositions(opponent);
        if (closed4.size() >= 2 && ourPossibleBest < 3) {
            for (Position position : closed4) {
                return Optional.of(new Move(position, mark));
            }
        }

        return Optional.empty();
    }
}
