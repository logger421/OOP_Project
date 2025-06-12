package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class ResignWhenTwoOpenFoursPossibleStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        Set<Position> openFourThreatPositions = board.getOpenFourThreatPositions(board.getOpponentMark());

        int openFourCounter = 0;
        if (!openFourThreatPositions.isEmpty()) {
            for (Position position : openFourThreatPositions) {
                openFourCounter += board.countPotentialOpenLinesFormed(position, board.getOpponentMark(), 4);
            }
        }

        int uniqueOpenFourCounter = board.countUniqueOpenLines(board.getOpponentMark(), 3);
        if (openFourThreatPositions.size() >= 2 && openFourCounter >= 2 && uniqueOpenFourCounter >= 2) {
            System.out.println("End of ResignWhenTwoOpenFoursPossibleStrategy0\n");
            throw new ResignException();
        }

        System.out.println("End of ResignWhenTwoOpenFoursPossibleStrategy\n");
        return Optional.empty();
    }
}
