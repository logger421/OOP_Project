package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class CreateForkStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Position bestPos = null;
        int bestThreats = 0;

        for (Position pos : board.getEmptyPositions()) {
            Board simulated = board.clone();
            simulated.placeMark(pos, mark);

            int threats = 0;
            for (Position next : simulated.getEmptyPositions()) {
                if (simulated.isWinningMove(next, mark)) {
                    threats++;
                }
            }

            if (threats > bestThreats) {
                bestThreats = threats;
                bestPos = pos;
            }
        }

        if (bestThreats >= 2 && bestPos != null) {
            return Optional.of(new Move(bestPos, mark));
        }
        return Optional.empty();
    }
}
