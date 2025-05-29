package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;

public class ResignHandler extends MoveHandler {
    public ResignHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws ResignException {
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;
        boolean haveSafeMove = false;

        for (Position myMove : board.getEmptyPositions()) {
            Board sim = board.clone();
            sim.placeMark(myMove, mark);

            boolean opponentCanAlwaysWin = false;

            for (Position oppMove : sim.getEmptyPositions()) {
                Board afterOppMove = sim.clone();
                afterOppMove.placeMark(oppMove, opponent);

                if (afterOppMove.isWinningMove(oppMove, opponent)) {
                    opponentCanAlwaysWin = true;
                    break;
                }

                int threats = 0;

                for (Position winMove : afterOppMove.getEmptyPositions()) {
                    if (afterOppMove.isWinningMove(winMove, opponent)) threats++;
                }
                if (threats >= 2) {
                    opponentCanAlwaysWin = true;
                    break;
                }
            }
            if (!opponentCanAlwaysWin) {
                haveSafeMove = true;
                break;
            }
        }

        if (!haveSafeMove) throw new ResignException();
        return Optional.empty();
    }
}
