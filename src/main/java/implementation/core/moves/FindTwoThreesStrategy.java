package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;
import implementation.core.LinesCounter;
import implementation.core.ThreatPositionsCalculator;

import java.util.Optional;
import java.util.Set;

public class FindTwoThreesStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Set<Position> ourPossibleOpen3 = ThreatPositionsCalculator.getOpenThreeThreatPositions(board, mark);

        int ourBestOpen3Counter = 0;
        Position ourBestOpen3Position = null;
        if (!ourPossibleOpen3.isEmpty()) {
            for (Position position : ourPossibleOpen3) {
                int current = LinesCounter.countPotentialOpenLinesFormed(board, position, mark, 3);
                if (current > ourBestOpen3Counter) {
                    ourBestOpen3Counter = current;
                    ourBestOpen3Position = position;
                }
            }
        }

        int open3Counter = 0;
        if (!ourPossibleOpen3.isEmpty()) {
            for (Position position : ourPossibleOpen3) {
                board.placeMarkAt(position, mark);
                int current = LinesCounter.countUniqueOpenLines(board, mark, 3);
                if (current > open3Counter) {
                    open3Counter = current;
                    ourBestOpen3Position = position;
                }
                board.placeMarkAt(position, Mark.NULL);
            }
        }

        if (ourBestOpen3Counter >= 2 || open3Counter >= 2 && config.getFirstMark() == mark) {
            return Optional.of(new Move(ourBestOpen3Position, mark));
        }

        return Optional.empty();
    }
}
