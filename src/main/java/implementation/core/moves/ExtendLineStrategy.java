package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class ExtendLineStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position best = null;
        int maxLength = 0;

        // randomly find the best position to extend a line
        for (Position position : board.getEmptyPositions()) {
            Board sim = board.clone();
            sim.placeMarkAt(position, mark);

            int singleMax = 0;
            for (int[] direction : DIRECTIONS) {
                int count = 1;
                count += sim.countDirection(position, mark, direction[0], direction[1]);
                count += sim.countDirection(position, mark, -direction[0], -direction[1]);
                if (count > singleMax) singleMax = count;
            }
            if (singleMax > maxLength) {
                maxLength = singleMax;
                best = position;
            }
        }

        return best != null && maxLength > 3 ? Optional.of(new Move(best, mark)) : Optional.empty();
    }
}
