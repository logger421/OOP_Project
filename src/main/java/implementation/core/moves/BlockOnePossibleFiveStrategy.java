package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class BlockOnePossibleFiveStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Set<Position> immediateThreats = board.getOpponentImmediateWinningPositions();
        if (immediateThreats.size() == 1)
            return Optional.of(new Move(immediateThreats.iterator().next(), mark));
        System.out.println("End of BlockOnePossibleFiveStrategy\n");
        return Optional.empty();
    }
}
