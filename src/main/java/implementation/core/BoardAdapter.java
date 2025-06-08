package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.WrongBoardStateException;
import implementation.config.BoardConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoardAdapter {
    private final Set<Move> rawMoves;
    private final Mark currentMark;
    private final BoardConfig config;

    public BoardAdapter(Set<Move> rawMoves, BoardConfig config, Mark currentMark) {
        this.rawMoves = rawMoves;
        this.currentMark = currentMark;
        this.config = config;
    }

    public Board toBoard() throws WrongBoardStateException {
        validateBoardState(this.rawMoves);

        Board board = new Board(config.getSize(), config.isPeriodic());

        for (Move mv : rawMoves)
            board.placeMarkAt(mv.position(), mv.mark());

        return board;
    }

    private void validateBoardState(Set<Move> moves) throws WrongBoardStateException {
        int size = config.getSize();

        if (size > BoardConfig.MAX_SIZE || size < BoardConfig.MIN_SIZE) throw new WrongBoardStateException();

        Map<Position, Mark> positionMarkMap = new HashMap<>();
        long countX = 0, countO = 0;
        for (Move m : moves) {
            Position p = m.position();
            if (!config.isPeriodic() && (p.row() < 0 || p.row() > size || p.col() < 0 || p.col() > size)) {
                throw new WrongBoardStateException();
            }

            // Check for duplicate positions with different marks
            if (positionMarkMap.containsKey(p) && positionMarkMap.get(p) != m.mark()) {
                throw new WrongBoardStateException();
            }

            positionMarkMap.put(p, m.mark());

            if (m.mark() == Mark.CROSS) countX++;
            else if (m.mark() == Mark.NOUGHT) countO++;
        }

        if (Math.abs(countX - countO) > 1) throw new WrongBoardStateException();

        if ((countX < countO && config.getFirstMark() == Mark.CROSS && currentMark == Mark.CROSS) ||
                (countO < countX && config.getFirstMark() == Mark.NOUGHT && currentMark == Mark.NOUGHT)) {
            throw new WrongBoardStateException();
        }
        if (countX == countO && (config.getFirstMark() == Mark.NOUGHT && currentMark == Mark.CROSS ||
                config.getFirstMark() == Mark.CROSS && currentMark == Mark.NOUGHT)) {
            throw new WrongBoardStateException();
        }
    }
}
