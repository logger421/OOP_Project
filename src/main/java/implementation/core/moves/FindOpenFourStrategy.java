package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;
import implementation.core.ThreatPositionsCalculator;

import java.util.Optional;
import java.util.Set;

public class FindOpenFourStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Set<Position> open4 = ThreatPositionsCalculator.getOpenFourThreatPositions(board, mark);
        if (!open4.isEmpty()) {
            Position position = open4.iterator().next();
            return Optional.of(new Move(position, mark));
        }

        return Optional.empty();
    }
}
