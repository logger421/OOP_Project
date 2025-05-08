package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import implementation.core.Board;
import implementation.core.moves.MoveStrategy;

import java.util.Optional;

public class StrategyHandler extends MoveHandler {
    private final MoveStrategy strategy;

    public StrategyHandler(MoveStrategy strategy, MoveHandler next) {
        super(next);
        this.strategy = strategy;
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) {
        return strategy.findMove(board, mark);
    }
}
