package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class CreateForkStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position best = null;
        int maxForkLines = 0;
        int maxForkStrength = 0;

        for (Position position : board.getEmptyPositions()) {
            Board simulation = board.clone();
            simulation.placeMark(position, mark);

            int forkLines = 0;
            int forkStrength = 0;

            for (int[] direction : DIRECTIONS) {
                int count = 1;
                count += simulation.countDirection(position, mark, direction[0], direction[1]);
                count += simulation.countDirection(position, mark, -direction[0], -direction[1]);

                if (count >= 3) {
                    forkLines++;
                    forkStrength += count;
                }
            }

            if (forkLines >= 2) {
                if (forkLines > maxForkLines || (forkLines == maxForkLines && forkStrength > maxForkStrength)) {
                    maxForkLines = forkLines;
                    maxForkStrength = forkStrength;
                    best = position;
                }
            }
        }

        return best != null ? Optional.of(new Move(best, mark)) : Optional.empty();
    }
}
