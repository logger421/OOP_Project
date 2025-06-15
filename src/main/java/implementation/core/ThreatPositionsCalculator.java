package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Position;

import java.util.HashSet;
import java.util.Set;

import static implementation.core.PositionUtils.DIRECTIONS;

public class ThreatPositionsCalculator {

    public static Set<Position> getOpenFourThreatPositions(Board board, Mark mark) {
        Set<Position> threats = new HashSet<>();
        for (Position pos : board.getEmptyPositions()) {
            for (int[] d : DIRECTIONS) {
                if (PositionUtils.createsLineWithOpenEnds(board, pos, mark, 4, d[0], d[1])) {
                    threats.add(pos);
                    break;
                }
            }
        }

        return threats;
    }

    public static Set<Position> getOpenThreeThreatPositions(Board board, Mark mark) {
        Set<Position> threats = new HashSet<>();
        for (Position pos : board.getEmptyPositions()) {
            for (int[] d : DIRECTIONS) {
                if (PositionUtils.createsLineWithOpenEnds(board, pos, mark, 3, d[0], d[1])) {
                    threats.add(pos);
                    break;
                }
            }
        }

        return threats;
    }
}
