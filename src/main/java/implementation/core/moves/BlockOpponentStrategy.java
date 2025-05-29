package implementation.core.moves;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import implementation.core.Board;

import java.util.Optional;

public class BlockOpponentStrategy implements MoveStrategy {

    @Override
    public Optional<Move> findMove(Board board, Mark mark) {
        Mark opponent = (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;

        for (Position pos : board.getEmptyPositions()) {
            if (board.isWinningMove(pos, opponent)) {
                return Optional.of(new Move(pos, mark));
            }
        }

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

        return Optional.empty();
    }

    private boolean createsOpenFour(Board board, Position pos, Mark mark) {
        Board sim = board.clone();
        sim.placeMark(pos, mark);

        for (int[] d : DIRECTIONS) {
            int count = 1;
            count += sim.countDirection(pos, mark, d[0], d[1]);
            count += sim.countDirection(pos, mark, -d[0], -d[1]);
            if (count == 4) {
                if (sim.isEndOpen(pos, d[0], d[1]) && sim.isEndOpen(pos, -d[0], -d[1])) return true;
            }
        }

        return false;
    }
}
