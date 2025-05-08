package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class BlockOpponentStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        // Determine opponent's mark
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;

        // Iterate through empty positions to see if opponent can win there
        for (Position pos : board.getEmptyPositions()) {
            if (board.isWinningMove(pos, opponent)) {
                // Block by placing on that position
                return Optional.of(new Move(pos, mark));
            }
        }
        return Optional.empty();
    }
}
