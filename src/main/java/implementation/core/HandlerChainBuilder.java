package implementation.core;

import implementation.core.handlers.*;
import implementation.core.moves.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for building the chain of MoveHandler based on configuration.
 * Implements the Factory Method pattern.
 */
public class HandlerChainBuilder {
    private final List<HandlerFactory> steps = new ArrayList<>();

    /**
     * Add a winner-check handler (throws TheWinnerIsException if someone already won).
     */
    public HandlerChainBuilder addWinnerCheck() {
        steps.add(next -> new WinnerCheckHandler(next));
        return this;
    }

    /**
     * Add a strategy handler wrapping a MoveStrategy.
     */
    public HandlerChainBuilder addStrategy(MoveStrategy strategy) {
        steps.add(next -> new StrategyHandler(strategy, next));
        return this;
    }

    /**
     * Add the default handler (returns first empty move).
     */
    public HandlerChainBuilder addDefault() {
        steps.add(next -> new DefaultHandler());
        return this;
    }

    /**
     * Build the chain by applying factories in reverse order.
     */
    public MoveHandler build() {
        MoveHandler chain = null;
        for (int i = steps.size() - 1; i >= 0; i--) {
            chain = steps.get(i).create(chain);
        }
        return chain;
    }

    /**
     * Add a resign-check handler (throws ResignException if no safe move exists).
     */
    public HandlerChainBuilder addResignCheck() {
        steps.add(next -> new ResignHandler(next));
        return this;
    }

    /**
     * Functional interface to create MoveHandler instances given the next handler.
     */
    @FunctionalInterface
    private interface HandlerFactory {
        MoveHandler create(MoveHandler next);
    }
}