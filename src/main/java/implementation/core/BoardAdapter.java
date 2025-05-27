package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.WrongBoardStateException;
import implementation.config.BoardConfig;

import java.util.Set;

public class BoardAdapter {
    private final Set<Move> rawMoves;
    private final BoardConfig config;

    public BoardAdapter(Set<Move> rawMoves, BoardConfig config) {
        this.rawMoves = rawMoves;
        this.config = config;
    }

    public Board toBoard() throws WrongBoardStateException {
        validateBoardState(this.rawMoves);

        Board board = new Board(config.getSize(), config.isPeriodic());

        for (Move mv : rawMoves)
            board.placeMark(mv.position(), mv.mark());

        return board;
    }

    private void validateBoardState(Set<Move> moves) throws WrongBoardStateException {
        int size = config.getSize();

        if (size > BoardConfig.MAX_SIZE || size < BoardConfig.MIN_SIZE) throw new WrongBoardStateException();

        long countX = 0, countO = 0;
        for (Move m : moves) {
            Position p = m.position();
            if (!config.isPeriodic() && (p.row() < 0 || p.row() >= size || p.col() < 0 || p.col() >= size)) {
                throw new WrongBoardStateException();
            }
            if (m.mark() == Mark.CROSS) countX++;
            else if (m.mark() == Mark.NOUGHT) countO++;
        }

        if (Math.abs(countX - countO) > 1) throw new WrongBoardStateException();
    }
}
