package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;
import implementation.core.WinningPositionsCalculator;

import java.util.Optional;
import java.util.Set;

public class WinImmediatelyStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Set<Position> immediateWinningPositions = WinningPositionsCalculator.findImmediateWinningPositions(board, mark);

        if (!immediateWinningPositions.isEmpty())
            for (Position pos : immediateWinningPositions)
                return Optional.of(new Move(pos, mark));

        return Optional.empty();
    }
}
