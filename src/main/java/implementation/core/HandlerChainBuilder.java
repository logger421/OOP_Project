package implementation.core;

import implementation.core.handlers.*;
import implementation.core.moves.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

public class HandlerChainBuilder {
    private final List<HandlerFactory> steps = new ArrayList<>();

    public HandlerChainBuilder addWinnerCheck() {
        steps.add(next -> new WinnerCheckHandler(next));
        return this;
    }

    public HandlerChainBuilder addStrategy(MoveStrategy strategy) {
        steps.add(next -> new StrategyHandler(strategy, next));
        return this;
    }

    public HandlerChainBuilder addDefault() {
        steps.add(next -> new DefaultHandler());
        return this;
    }

    public MoveHandler build() {
        MoveHandler chain = null;
        for (int i = steps.size() - 1; i >= 0; i--) {
            chain = steps.get(i).create(chain);
        }
        return chain;
    }

    public HandlerChainBuilder addResignCheck() {
        steps.add(next -> new ResignHandler(next));
        return this;
    }

    @FunctionalInterface
    private interface HandlerFactory {
        MoveHandler create(MoveHandler next);
    }
}