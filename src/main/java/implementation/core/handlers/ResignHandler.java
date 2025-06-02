package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.config.BoardConfig;
import implementation.core.Board;

import java.util.Optional;

public class ResignHandler extends MoveHandler {
    private final BoardConfig boardConfig = BoardConfig.getInstance();

    public ResignHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws ResignException {
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;

        int opponentBest = bestLine(board, opponent);
        int ourBest = bestLine(board, mark);

        System.out.println("Opponent best line: " + opponentBest);
        System.out.println("Our best line: " + ourBest);
        if (ourBest >= opponentBest && boardConfig.getFirstMark() == mark) {
            return Optional.empty();
        }

        for (Position myMove : board.getEmptyPositions()) {
            Board simMyMove = board.clone();
            simMyMove.placeMark(myMove, mark);

            boolean safeMoveFound = true;

            for (Position oppMove : simMyMove.getEmptyPositions()) {
                Board simOppMove = simMyMove.clone();
                simOppMove.placeMark(oppMove, opponent);

                if (simOppMove.isWinningMove(oppMove, opponent)) {
                    safeMoveFound = false;
                    break;
                }

                int threats = 0;
                for (Position next : simOppMove.getEmptyPositions()) {
                    if (simOppMove.isWinningMove(next, opponent) || createsOpenFour(simOppMove, next, opponent)) {
                        threats++;
                    }
                }

                if (threats >= 2) {
                    safeMoveFound = false;
                    break;
                }
            }

            if (safeMoveFound) {
                return Optional.empty();
            }
        }

        throw new ResignException();
    }

    private int bestLine(Board board, Mark mark) {
        int maxLen = 0;
        for (Position pos : board.getEmptyPositions()) {
            Board sim = board.clone();
            sim.placeMark(pos, mark);

            for (int[] d : Board.directions) {
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
