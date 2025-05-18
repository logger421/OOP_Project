import fais.zti.oramus.gomoku.*;
import implementation.config.BoardConfig;
import implementation.core.Board;
import implementation.core.BoardAdapter;
import implementation.core.HandlerChainBuilder;
import implementation.core.handlers.MoveHandler;
import implementation.core.moves.BlockOpponentStrategy;
import implementation.core.moves.CreateForkStrategy;
import implementation.core.moves.WinImmediatelyStrategy;

import java.util.Optional;
import java.util.Set;

public class Gomoku implements Game {
    private final MoveHandler handlerChain;
    private final BoardConfig config;

    public Gomoku() {
        this.config = BoardConfig.getInstance();
        this.handlerChain = new HandlerChainBuilder()
                .addWinnerCheck()
                .addResignCheck()
                .addStrategy(new WinImmediatelyStrategy())
                .addStrategy(new BlockOpponentStrategy())
                .addStrategy(new CreateForkStrategy())
                .addDefault()
                .build();
    }

    @Override
    public void firstMark(Mark mark) {
        config.setFirstMark(mark);
    }

    @Override
    public void size(int size) {
        config.setSize(size);
    }

    @Override
    public void periodicBoundaryConditionsInUse() {
        config.setPeriodic(true);
    }

    @Override
    public Move nextMove(Set<Move> previousMoves, Mark myMark)
            throws WrongBoardStateException, TheWinnerIsException, ResignException {
        Board board = new BoardAdapter(previousMoves, config).toBoard();
        Optional<Move> opt = handlerChain.handle(board, myMark);
        return opt.orElseThrow(ResignException::new);
    }
}
