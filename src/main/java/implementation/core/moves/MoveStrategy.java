package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.ResignException;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;

public interface MoveStrategy {
    BoardConfig config = BoardConfig.getInstance();
    int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

    Optional<Move> findMove(Board board, Mark mark) throws ResignException;

    default void introduce(Mark mark) {
        if (config.isDebug())
            System.out.println("Handling move for mark: " + mark + ", in handler: " + this.getClass().getSimpleName());
    }

    default void printExit() {
        if (config.isDebug())
            System.out.println("Finalizing move strategy: " + this.getClass().getSimpleName());
    }
}
