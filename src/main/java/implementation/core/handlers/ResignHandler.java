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

        int opponentBest = board.bestLine(opponent);
        int ourBest = board.bestLine(mark);

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
                    if (simOppMove.isWinningMove(next, opponent) || simOppMove.createsOpenFour(next, opponent)) {
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


}
