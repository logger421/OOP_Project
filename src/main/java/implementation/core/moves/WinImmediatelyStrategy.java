package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class WinImmediatelyStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        // Iterate through all empty cells and check if placing the mark results in a win
        for (Position pos : board.getEmptyPositions()) {
            if (board.isWinningMove(pos, mark)) {
                return Optional.of(new Move(pos, mark));
            }
        }
        return Optional.empty();
    }
}
