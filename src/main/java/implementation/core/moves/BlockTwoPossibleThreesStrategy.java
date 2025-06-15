package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;
import implementation.core.LinesCounter;
import implementation.core.ThreatPositionsCalculator;

import java.util.Optional;
import java.util.Set;

public class BlockTwoPossibleThreesStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = board.getOpponentMark();
        Set<Position> openThreeThreatPositions = ThreatPositionsCalculator.getOpenThreeThreatPositions(board, opponent);

        int openThreeCounter = 0;
        Position bestPosition = null;
        if (!openThreeThreatPositions.isEmpty()) {
            for (Position position : openThreeThreatPositions) {
                int current = LinesCounter.countPotentialOpenLinesFormed(board, position, opponent, 3);
                if (current > openThreeCounter) {
                    bestPosition = position;
                    openThreeCounter = current;
                }

            }
        }

        if (openThreeCounter >= 2) {
            return Optional.of(new Move(bestPosition, mark));
        }

        return Optional.empty();
    }
}
