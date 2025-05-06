package gomoku.chain;

import java.util.Optional;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import gomoku.strategy.MoveStrategy;


public class StrategyHandler extends MoveHandler {
    private final MoveStrategy strategy;

    public StrategyHandler(MoveStrategy strategy, MoveHandler next) {
        super(next);
        this.strategy = strategy;
    }

    @Override
    protected Optional<Move> doHandle(gomoku.domain.Board board, Mark mark) {
        return strategy.findMove(board, mark);
    }
}
