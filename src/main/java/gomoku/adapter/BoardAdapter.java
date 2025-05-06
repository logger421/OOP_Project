package gomoku.adapter;

import fais.zti.oramus.gomoku.Move;
import gomoku.config.BoardConfig;
import gomoku.domain.Board;

import java.util.Set;

public class BoardAdapter {
    private final Set<Move> rawMoves;
    private final BoardConfig config;

    public BoardAdapter(Set<Move> rawMoves, BoardConfig config) {
        this.rawMoves = rawMoves;
        this.config = config;
    }

    /**
     * Constructs and returns a Board initialized with all raw moves.
     */
    public Board toBoard() {
        Board board = new Board(config.getSize(), config.isPeriodic());
        for (Move mv : rawMoves) {
            board.placeMark(mv.position(), mv.mark());
        }
        return board;
    }
}
