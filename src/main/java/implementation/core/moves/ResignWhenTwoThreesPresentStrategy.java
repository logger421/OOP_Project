package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;

public class ResignWhenTwoThreesPresentStrategy implements MoveStrategy {
    @Override
    public Optional<Move> findMove(Board board, Mark mark) throws ResignException {
        System.out.println("End of ResignWhenTwoThreesPresentStrategy\n");
        return Optional.empty();
    }
}
