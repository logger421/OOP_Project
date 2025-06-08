package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ExtendLineStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position best = null;
        int maxLength = 0;

        Set<Position> open4 = board.getOpenFourThreatPositions(mark);
        if(!open4.isEmpty()) {
            Position position = open4.iterator().next();
            return Optional.of(new Move(position, mark));
        }

        Set<Position> closed4 = board.getClosedFourThreatPositions(mark);
        if (!closed4.isEmpty()) {
            Position position = closed4.iterator().next();
            return Optional.of(new Move(position, mark));
        }

        for (Position position : board.getEmptyPositions()) {
            Board sim = board.clone();
            sim.placeMarkAt(position, mark);

            int singleMax = 0;
            for (int[] direction : DIRECTIONS) {
                int count = 1;
                count += sim.countDirection(position, mark, direction[0], direction[1]);
                count += sim.countDirection(position, mark, -direction[0], -direction[1]);
                if (count > singleMax) singleMax = count;
            }
            if (singleMax > maxLength) {
                maxLength = singleMax;
                best = position;
            }
        }

        return best != null && maxLength > 3 ? Optional.of(new Move(best, mark)) : Optional.empty();
    }
}
