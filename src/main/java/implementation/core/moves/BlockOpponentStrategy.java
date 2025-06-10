package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;
import java.util.Set;

public class BlockOpponentStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = board.getOpponentMark();
        Set<Position> immediateThreats = board.getOpponentImmediateWinningPositions();

        if (immediateThreats.size() == 1) {
            return Optional.of(new Move(immediateThreats.iterator().next(), mark));
        } else if (immediateThreats.size() > 1) {
            return Optional.empty();
        }

        var ourPossibleBestLineLength = board.getPossibleLineLength();
        var opponentPossibleBestLineLength = board.getOpponentPossibleLineLength();
        var uniqueOpenLines3 = board.countUniqueOpenLines(opponent, 3);

        var opponentOpenFour = board.getOpenFourThreatPositions(opponent);
        int opponentPossibleOpen4Counter = 0;
        if (!opponentOpenFour.isEmpty()) {
            for (Position position : opponentOpenFour) {
                int current = board.countPotentialLinesFormed(position, opponent, 4);
                if (current > opponentPossibleOpen4Counter) {
                    opponentPossibleOpen4Counter = current;
                }
            }
        }
        if (ourPossibleBestLineLength < opponentPossibleBestLineLength && opponentPossibleOpen4Counter >= 1 && uniqueOpenLines3 < 2) {
            for (Position position : opponentOpenFour) {
                return Optional.of(new Move(position, mark));
            }
        }

        if (opponentPossibleOpen4Counter == 2 && ourPossibleBestLineLength < 4) {
            for (Position position : opponentOpenFour) {
                return Optional.of(new Move(position, mark));
            }
        }

        var ourPossibleOpen4 = board.getOpenFourThreatPositions(mark);
        var ourPossibleClosed4 = board.getClosedFourThreatPositions(mark);
        if (ourPossibleOpen4.isEmpty() && !ourPossibleClosed4.isEmpty() && !opponentOpenFour.isEmpty()) {
            return Optional.of(new Move(opponentOpenFour.iterator().next(), mark));
        }

        var opponentPossibleClosed4 = board.getClosedFourThreatPositions(opponent);
        if (opponentPossibleClosed4.size() >= 2 && ourPossibleBestLineLength < 3) {
            for (Position position : opponentPossibleClosed4) {
                return Optional.of(new Move(position, mark));
            }
        }

        var ourPossibleOpen3 = board.getOpenThreeThreatPositions(mark);
        int ourBestOpen3Counter = 0;
        if (!ourPossibleOpen3.isEmpty()) {
            for (Position position : ourPossibleOpen3) {
                int current = board.countPotentialLinesFormed(position, mark, 3);
                if (current > ourBestOpen3Counter) {
                    ourBestOpen3Counter = current;
                }
            }
        }

        var opponentPossibleOpen3 = board.getOpenThreeThreatPositions(opponent);
        int opponentBestOpen3Counter = 0;
        Position opponentBestOpenThreePosition = null;
        if (!opponentPossibleOpen3.isEmpty()) {
            for (Position position : opponentPossibleOpen3) {
                int current = board.countPotentialLinesFormed(position, opponent, 3);
                if (current > opponentBestOpen3Counter) {
                    opponentBestOpen3Counter = current;
                    opponentBestOpenThreePosition = position;
                }
            }
        }

        if (opponentPossibleOpen3.size() >= 2 && opponentBestOpen3Counter >= 2) {
            if (ourPossibleOpen4.isEmpty() && ourPossibleBestLineLength < opponentPossibleBestLineLength) {
                return Optional.empty();
            }
        }

        var opponentUniqueOpenLines2 = board.countUniqueOpenLines(opponent, 2);
        if (ourPossibleBestLineLength <= 3 && opponentPossibleBestLineLength <= 3
                & ourBestOpen3Counter < opponentBestOpen3Counter && opponentUniqueOpenLines2 < 4) {
            return Optional.of(new Move(opponentBestOpenThreePosition, mark));
        }

        return Optional.empty();
    }
}
