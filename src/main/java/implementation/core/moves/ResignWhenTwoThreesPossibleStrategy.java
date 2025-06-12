package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class ResignWhenTwoThreesPossibleStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {

        Set<Position> openThreeThreatPositions = board.getOpenThreeThreatPositions(board.getOpponentMark());

        int maxOpenThreeCounter = 0;
        if (!openThreeThreatPositions.isEmpty()) {
            for (Position position : openThreeThreatPositions) {
                if (board.countPotentialOpenLinesFormed(position, board.getOpponentMark(), 3) == 2) {
                    maxOpenThreeCounter += 1;
                }
            }
        }

        if(maxOpenThreeCounter >= 2) {
            System.out.println("End of ResignWhenTwoThreesPossibleStrategy0\n");
            throw new ResignException();
        }

        System.out.println("End of ResignWhenTwoThreesPossibleStrategy\n");
        return Optional.empty();
    }
}
