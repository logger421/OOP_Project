package implementation.core.handlers;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class ResignHandler extends MoveHandler {

    public ResignHandler(MoveHandler next) {
        super(next);
    }

    @Override
    protected Optional<Move> doHandle(Board board, Mark mark) throws ResignException {
        Mark opponent = board.getOpponentMark();

        Set<Position> immediateThreats = board.getOpponentImmediateWinningPositions();
        if (immediateThreats.size() > 1) throw new ResignException();

        var ourPossibleBestLine = board.getPossibleLineLength();

        var opponentClosed4 = board.countUniqueClosedLines(opponent, 4);
        if (opponentClosed4 >= 2 && ourPossibleBestLine < 4) throw new ResignException();

        var opponentOpen3 = board.countUniqueOpenLines(opponent, 3);
        if (opponentOpen3 > 2 && ourPossibleBestLine < 3) throw new ResignException();

        var opponentClosed3 = board.countUniqueClosedLines(opponent, 3);
        if (opponentClosed3 >= 4 && ourPossibleBestLine < 3) throw new ResignException();

        Set<Position> opponentPossibleOpen4 = board.getOpenFourThreatPositions(opponent);
        Set<Position> opponentPossibleClosed4 = board.getClosedFourThreatPositions(opponent);

        if ((!opponentPossibleOpen4.isEmpty() || opponentPossibleClosed4.size() >= 2) && ourPossibleBestLine < 4) {
            throw new ResignException();
        }

        Set<Position> opponentPossibleOpen3 = board.getOpenThreeThreatPositions(opponent);
        int opponentBestOpen3Counter = 0;
        if (!opponentPossibleOpen3.isEmpty()) {
            for (Position position : opponentPossibleOpen3) {
                int current = board.countPotentialLinesFormed(position, opponent, 3);
                if (current > opponentBestOpen3Counter) {
                    opponentBestOpen3Counter = current;
                }
            }
        }
        var opponentUniqueOpenLines2 = board.countUniqueOpenLines(opponent, 2);
        if (opponentBestOpen3Counter >= 2 && ourPossibleBestLine <= 3 && opponentUniqueOpenLines2 >= 4) {
            throw new ResignException();
        }

        if (opponentOpen3 >= 2 && ourPossibleBestLine < 3) throw new ResignException();

        return Optional.empty();
    }

}
