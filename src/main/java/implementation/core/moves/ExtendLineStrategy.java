package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class ExtendLineStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position bestPos = null;
        int maxLength = 0;

        for (Position pos : board.getEmptyPositions()) {
            Board sim = board.clone();
            sim.placeMark(pos, mark);

            int singleMax = 0;
            for (int[] direction : DIRECTIONS) {
                int count = 1;
                count += sim.countDirection(pos, mark, direction[0], direction[1]);
                count += sim.countDirection(pos, mark, -direction[0], -direction[1]);
                if (count > singleMax) singleMax = count;
            }
            if (singleMax > maxLength) {
                maxLength = singleMax;
                bestPos = pos;
            }
        }

        if (bestPos != null && maxLength >= 3) {
            return Optional.of(new Move(bestPos, mark));
        }
        return Optional.empty();
    }
}
