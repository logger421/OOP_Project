import fais.zti.oramus.gomoku.*;
import implementation.config.BoardConfig;
import implementation.core.Board;
import implementation.core.BoardAdapter;
import implementation.core.HandlerChainBuilder;
import implementation.core.handlers.MoveHandler;
import implementation.core.moves.*;

import java.util.Optional;
import java.util.Set;

public class Gomoku implements Game {
    private final MoveHandler handlerChain;
    private final BoardConfig config;

    public Gomoku() {
        BoardConfig.initialize();
        this.config = BoardConfig.getInstance();
        this.handlerChain = new HandlerChainBuilder()
                .addWinnerCheck()
                .addStrategy(new WinImmediatelyStrategy())
                .addStrategy(new ResignWhenTwoFivesPossibleStrategy())
                .addStrategy(new BlockOnePossibleFiveStrategy())
                .addStrategy(new ResignWhenTwoOpenFoursPresentStrategy())
                .addStrategy(new FindOpenFourStrategy())
                .addStrategy(new ResignWhenTwoOpenFoursPossibleStrategy())
                .addStrategy(new BlockOnePossibleOpenFourStrategy())
                .addStrategy(new ResignWhenTwoThreesPresentStrategy())
                .addStrategy(new FindTwoThreesStrategy())
                .addStrategy(new ResignWhenTwoThreesPossibleStrategy())
                .addStrategy(new BlockTwoPossibleThreesStrategy())
                .addStrategy(new ExtendLineStrategy())
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

    public void debugMode() {
        config.setDebug();
    }

    @Override
    public Move nextMove(Set<Move> previousMoves, Mark myMark)
            throws WrongBoardStateException, TheWinnerIsException, ResignException {
        Board board = new BoardAdapter(previousMoves, config, myMark).toBoard();
        if (config.isDebug()) {
            System.out.println("Current board state:");
            System.out.println(board);
        }
        Optional<Move> opt = handlerChain.handle(board, myMark);
        if (config.isDebug()) {
            System.out.printf("Next move for %s: %s%n", myMark, opt);
        }
        return opt.orElseThrow(ResignException::new);
    }
}
