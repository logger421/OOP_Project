package gomoku.config;

import gomoku.chain.DefaultHandler;
import gomoku.chain.MoveHandler;
import gomoku.chain.StrategyHandler;
import gomoku.strategy.BlockOpponentStrategy;
import gomoku.strategy.CreateForkStrategy;
import gomoku.strategy.MoveStrategy;
import gomoku.strategy.WinImmediatelyStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory for building the chain of MoveHandler based on configuration.
 * Implements the Factory Method pattern.
 */
public class StrategyFactory {
    /**
     * Create a chain of MoveHandler nodes wrapping enabled strategies.
     */
    public static MoveHandler createHandlerChain(BoardConfig cfg) {
        // Collect enabled strategies in order of priority
        List<MoveStrategy> strategies = new ArrayList<>();
        if (cfg.isWinImmediatelyEnabled()) {
            strategies.add(new WinImmediatelyStrategy());
        }
        if (cfg.isBlockOpponentEnabled()) {
            strategies.add(new BlockOpponentStrategy());
        }
        if (cfg.isForkCreationEnabled()) {
            strategies.add(new CreateForkStrategy());
        }

        // Start with the default handler at the end of the chain
        MoveHandler handlerChain = new DefaultHandler();

        // Wrap each strategy in a StrategyHandler, in reverse order so first strategy is at head
        Collections.reverse(strategies);
        for (MoveStrategy strategy : strategies) {
            handlerChain = new StrategyHandler(strategy, handlerChain);
        }

        return handlerChain;
    }
}