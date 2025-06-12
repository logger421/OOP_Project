package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class FindOpenFourStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Set<Position> open4 = board.getOpenFourThreatPositions(mark);
        if (!open4.isEmpty()) {
            Position position = open4.iterator().next();
            System.out.println("End of FindOpenFourStrategy0\n");
            return Optional.of(new Move(position, mark));
        }

        System.out.println("End of FindOpenFourStrategy\n");
        return Optional.empty();
    }
}
