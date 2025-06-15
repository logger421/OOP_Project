package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;
import implementation.core.LinesCounter;
import implementation.core.ThreatPositionsCalculator;

import java.util.Optional;
import java.util.Set;

public class BlockOnePossibleOpenFourStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = board.getOpponentMark();
        Set<Position> openFourThreatPositions = ThreatPositionsCalculator.getOpenFourThreatPositions(board, opponent);

        int openFourCounter = 0;
        Position best = null;
        if (!openFourThreatPositions.isEmpty()) {
            for (Position position : openFourThreatPositions) {
                int current = LinesCounter.countPotentialOpenLinesFormed(board, position, opponent, 4);
                if (current > openFourCounter) {
                    openFourCounter = current;
                    best = position;
                }
            }
        }

        Set<Position> openThreeThreatPositions = ThreatPositionsCalculator.getOpenThreeThreatPositions(board, opponent);
        int openThreeCounter = 0;
        if (!openThreeThreatPositions.isEmpty()) {
            for (Position position : openThreeThreatPositions) {
                int current = LinesCounter.countPotentialOpenLinesFormed(board, position, opponent, 3);
                if (current > openThreeCounter) {
                    openThreeCounter = current;
                }
            }
        }

        if (openThreeCounter > 1 && openFourCounter != 0) return Optional.empty();
        if (openFourCounter == 1) return Optional.of(new Move(best, mark));
        return Optional.empty();
    }
}
