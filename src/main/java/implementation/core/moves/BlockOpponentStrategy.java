package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;

public class BlockOpponentStrategy implements MoveStrategy {
    private final BoardConfig boardConfig = BoardConfig.getInstance();

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;

        // Natychmiastowa blokada, jeśli przeciwnik wygrywa w następnym ruchu
        for (Position pos : board.getEmptyPositions()) {
            if (board.isWinningMove(pos, opponent)) {
                return Optional.of(new Move(pos, mark));
            }
        }

        // Sprawdź potencjał przeciwnika (jeśli zaczął pierwszy lub jest bliżej wygranej niż my)
        int opponentBest = board.bestLine(opponent);
        int ourBest = board.bestLine(mark);

        if (boardConfig.getFirstMark() == opponent || opponentBest > ourBest) {
            for (Position pos : board.getEmptyPositions()) {
                if (board.createsOpenFour(pos, opponent)) {
                    return Optional.of(new Move(pos, mark));
                }
            }

            for (Position pos : board.getEmptyPositions()) {
                Board simulated = board.clone();
                simulated.placeMark(pos, opponent);

                int threats = 0;
                for (Position next : simulated.getEmptyPositions()) {
                    if (simulated.isWinningMove(next, opponent) || simulated.createsOpenFour(next, opponent)) threats++;
                }
                if (threats >= 2) {
                    return Optional.of(new Move(pos, mark));
                }
            }
        }

        return Optional.empty();
    }
}
