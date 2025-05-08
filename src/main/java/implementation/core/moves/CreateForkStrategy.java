package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

/**
 * Strategy that creates a "fork": a move after which the current player
 * has two or more separate immediate winning threats.
 */
public class CreateForkStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position bestPos = null;
        int bestThreats = 0;

        for (Position pos : board.getEmptyPositions()) {
            // Simulate placing the mark
            Board simulated = board.clone();
            simulated.placeMark(pos, mark);

            // Count how many winning moves exist after this placement
            int threats = 0;
            for (Position next : simulated.getEmptyPositions()) {
                if (simulated.isWinningMove(next, mark)) {
                    threats++;
                }
            }

            // Track the position with the highest threats
            if (threats > bestThreats) {
                bestThreats = threats;
                bestPos = pos;
            }
        }

        // Accept only if we get at least two threats
        if (bestThreats >= 2 && bestPos != null) {
            return Optional.of(new Move(bestPos, mark));
        }
        return Optional.empty();
    }
}
