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

public class ResignWhenTwoOpenFoursPossibleStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        Mark opponent = board.getOpponentMark();
        Set<Position> openFourThreatPositions = ThreatPositionsCalculator.getOpenFourThreatPositions(board, opponent);

        int openFourCounter = 0;
        if (!openFourThreatPositions.isEmpty()) {
            for (Position position : openFourThreatPositions) {
                openFourCounter += LinesCounter.countPotentialOpenLinesFormed(board, position, opponent, 4);
            }
        }

        int possibleOpen4Counter = 0;
        if (!openFourThreatPositions.isEmpty()) {
            for (Position position : openFourThreatPositions) {
                board.placeMarkAt(position, opponent);
                possibleOpen4Counter += LinesCounter.countUniqueOpenLines(board, opponent, 4);
                board.placeMarkAt(position, Mark.NULL);
            }
        }

        int uniqueOpenThreeCounter = LinesCounter.countUniqueOpenLines(board, opponent, 3);
        if (openFourThreatPositions.size() >= 2 && openFourCounter > 1 && uniqueOpenThreeCounter > 1)
            throw new ResignException();
        if (openFourCounter > 1 && possibleOpen4Counter > 2) throw new ResignException();

        return Optional.empty();
    }
}
