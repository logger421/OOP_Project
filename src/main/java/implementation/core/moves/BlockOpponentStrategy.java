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
        int opponentBest = bestLine(board, opponent);
        int ourBest = bestLine(board, mark);

        if (boardConfig.getFirstMark() == opponent || opponentBest > ourBest) {
            for (Position pos : board.getEmptyPositions()) {
                if (createsOpenFour(board, pos, opponent)) {
                    return Optional.of(new Move(pos, mark));
                }
            }

            for (Position pos : board.getEmptyPositions()) {
                Board simulated = board.clone();
                simulated.placeMark(pos, opponent);

                int threats = 0;
                for (Position next : simulated.getEmptyPositions()) {
                    if (simulated.isWinningMove(next, opponent) || createsOpenFour(simulated, next, opponent)) threats++;
                }
                if (threats >= 2) {
                    return Optional.of(new Move(pos, mark));
                }
            }
        }

        return Optional.empty();
    }

    private int bestLine(Board board, Mark mark) {
        int maxLen = 0;
        for (Position pos : board.getEmptyPositions()) {
            Board sim = board.clone();
            sim.placeMark(pos, mark);

            for (int[] d : DIRECTIONS) {
                int len = 1 + sim.countDirection(pos, mark, d[0], d[1]) + sim.countDirection(pos, mark, -d[0], -d[1]);
                if (len > maxLen) maxLen = len;
            }
        }
        return maxLen;
    }

    private boolean createsOpenFour(Board board, Position pos, Mark mark) {
        Board sim = board.clone();
        sim.placeMark(pos, mark);
        int size = boardConfig.getSize();

        for (int[] d : Board.directions) {
            int count = 1 + sim.countDirection(pos, mark, d[0], d[1]) + sim.countDirection(pos, mark, -d[0], -d[1]);

            if (count == 4) {
                int endRowFwd = pos.row() + d[0] * (sim.countDirection(pos, mark, d[0], d[1]) + 1);
                int endColFwd = pos.col() + d[1] * (sim.countDirection(pos, mark, d[0], d[1]) + 1);
                int endRowBwd = pos.row() - d[0] * (sim.countDirection(pos, mark, -d[0], -d[1]) + 1);
                int endColBwd = pos.col() - d[1] * (sim.countDirection(pos, mark, -d[0], -d[1]) + 1);

                if (boardConfig.isPeriodic()) {
                    endRowFwd = (endRowFwd + size) % size;
                    endColFwd = (endColFwd + size) % size;
                    endRowBwd = (endRowBwd + size) % size;
                    endColBwd = (endColBwd + size) % size;
                }

                if (!boardConfig.isPeriodic()) {
                    if (endRowFwd < 0 || endRowFwd >= size || endColFwd < 0 || endColFwd >= size) continue;
                    if (endRowBwd < 0 || endRowBwd >= size || endColBwd < 0 || endColBwd >= size) continue;
                }

                if (board.getMarkAt(endRowFwd, endColFwd) == Mark.NULL &&
                        board.getMarkAt(endRowBwd, endColBwd) == Mark.NULL) {
                    return true;
                }
            }
        }
        return false;
    }
}
