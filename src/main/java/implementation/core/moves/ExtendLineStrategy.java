package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class ExtendLineStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position best = null;
        int maxLength = 0;

        Set<Position> open4 = board.getOpenFourThreatPositions(mark);
        if (!open4.isEmpty()) {
            Position position = open4.iterator().next();
            return Optional.of(new Move(position, mark));
        }

        Set<Position> ourPossibleClosed4 = board.getClosedFourThreatPositions(mark);

        int ourBestClosed4Counter = 0;
        Position ourBestClosed4Position = null;
        if (!ourPossibleClosed4.isEmpty()) {
            for (Position position : ourPossibleClosed4) {
                int current = board.countPotentialLinesFormed(position, mark, 4);
                if (current > ourBestClosed4Counter) {
                    ourBestClosed4Counter = current;
                    ourBestClosed4Position = position;
                }
            }
        }

        if (ourBestClosed4Counter >= 2) {
            return Optional.of(new Move(ourBestClosed4Position, mark));
        }

        Set<Position> ourPossibleOpen3 = board.getOpenThreeThreatPositions(mark);

        int ourBestOpen3Counter = 0;
        Position ourBestOpen3Position = null;
        if (!ourPossibleOpen3.isEmpty()) {
            for (Position position : ourPossibleOpen3) {
                int current = board.countPotentialLinesFormed(position, mark, 3);
                if (current > ourBestOpen3Counter) {
                    ourBestOpen3Counter = current;
                    ourBestOpen3Position = position;
                }
            }
        }

        if (ourBestClosed4Counter > 0 && ourBestOpen3Counter >= 2) {
            return Optional.of(new Move(ourBestOpen3Position, mark));
        }
        if (ourBestClosed4Counter == 0 && ourBestOpen3Counter > 0) {
            return Optional.of(new Move(ourBestOpen3Position, mark));
        }

        // randomly find the best position to extend a line
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
