package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class BlockTwoPossibleThreesStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = board.getOpponentMark();
        Set<Position> openThreeThreatPositions = board.getOpenThreeThreatPositions(opponent);

        int openThreeCounter = 0;
        Position bestPosition = null;
        if (!openThreeThreatPositions.isEmpty()) {
            for (Position position : openThreeThreatPositions) {
                int current = board.countPotentialOpenLinesFormed(position, opponent, 3);
                if (current > openThreeCounter) {
                    bestPosition = position;
                    openThreeCounter = current;
                }

            }
        }

        if (openThreeCounter >= 2) {
            System.out.println("End of BlockTwoPossibleThreesStrategy0\n");
            return Optional.of(new Move(bestPosition, mark));
        }

        System.out.println("End of BlockTwoPossibleThreesStrategy\n");
        return Optional.empty();
    }
}
