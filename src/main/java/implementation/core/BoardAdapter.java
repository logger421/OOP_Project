package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.WrongBoardStateException;
import implementation.config.BoardConfig;

import java.util.HashSet;
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
        for (Move mv : rawMoves) {
            board.placeMark(mv.position(), mv.mark());
        }

        int xWins = countWinningLines(board, Mark.CROSS);
        int oWins = countWinningLines(board, Mark.NOUGHT);
        if (xWins + oWins > 1) {
            throw new WrongBoardStateException();
        }

        return board;
    }

    private void validateBoardState(Set<Move> moves) throws WrongBoardStateException {
        int size = config.getSize();

        Set<Position> seen = new HashSet<>();

        if (size > BoardConfig.MAX_SIZE || size < BoardConfig.MIN_SIZE) {
            throw new WrongBoardStateException();
        }

        long countX = 0, countO = 0;
        for (Move m : moves) {
            Position p = m.position();

            if (p.row() < 0 || p.row() >= size || p.col() < 0 || p.col() >= size) {
                throw new WrongBoardStateException();
            }

            if (!seen.add(p)) {
                throw new WrongBoardStateException();
            }

            if (m.mark() == Mark.CROSS) countX++;
            else if (m.mark() == Mark.NOUGHT) countO++;
        }

        if (Math.abs(countX - countO) > 1) {
            throw new WrongBoardStateException();
        }
    }

    private int countWinningLines(Board board, Mark mark) {
        int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        int lines = 0;
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.getMarkAt(r, c) != mark) continue;
                for (int[] d : dirs) {
                    int pr = r - d[0], pc = c - d[1];

                    if (board.isInside(pr, pc) && board.getMarkAt(pr, pc) == mark)
                        continue;

                    int len = 0, rr = r, cc = c;
                    while (board.isInside(rr, cc) && board.getMarkAt(rr, cc) == mark) {
                        len++;
                        rr += d[0];
                        cc += d[1];
                    }

                    if (len >= 5)
                        lines++;
                }
            }
        }
        return lines;
    }
}
