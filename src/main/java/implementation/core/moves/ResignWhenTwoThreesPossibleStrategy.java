package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;
import implementation.core.LinesCounter;
import implementation.core.ThreatPositionsCalculator;

import java.util.Optional;
import java.util.Set;

public class ResignWhenTwoThreesPossibleStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        Mark opponent = board.getOpponentMark();
        Set<Position> openThreeThreatPositions = ThreatPositionsCalculator.getOpenThreeThreatPositions(board, opponent);

        int maxOpenThreeCounter = 0;
        if (!openThreeThreatPositions.isEmpty()) {
            for (Position position : openThreeThreatPositions) {
                if (LinesCounter.countPotentialOpenLinesFormed(board, position, opponent, 3) == 2) {
                    maxOpenThreeCounter += 1;
                }
            }
        }

        if (maxOpenThreeCounter >= 2) throw new ResignException();

        return Optional.empty();
    }
}
