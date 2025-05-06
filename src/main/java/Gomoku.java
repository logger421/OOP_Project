import fais.zti.oramus.gomoku.*;
import gomoku.adapter.BoardAdapter;
import gomoku.chain.MoveHandler;
import gomoku.config.BoardConfig;
import gomoku.config.StrategyFactory;
import gomoku.domain.Board;

import java.util.Optional;
import java.util.Set;

public class Gomoku implements Game {
    private final MoveHandler handlerChain;
    private final BoardConfig config;

    public Gomoku() {
        this.config = BoardConfig.getInstance();
        this.handlerChain = StrategyFactory.createHandlerChain(config);
    }

    @Override
    public void firstMark(Mark mark) {
        // optional: store the initial mark
    }

    @Override
    public void size(int size) {
        // configure board size dynamically if needed
        config.setSize(size);
    }

    @Override
    public void periodicBoundaryConditionsInUse() {
        config.setPeriodic(true);
    }

    @Override
    public Move nextMove(Set<Move> previousMoves, Mark myMark)
            throws WrongBoardStateException, TheWinnerIsException, ResignException {
        // 1. Validate board state (omitted for brevity)

        // 2. Adapt raw moves to our Board model
        Board board = new BoardAdapter(previousMoves, config).toBoard();

        // 3. Delegate move computation to chain of responsibility
        Optional<Move> opt = handlerChain.handle(board, myMark);

        return opt.orElseThrow(ResignException::new);
    }
}
