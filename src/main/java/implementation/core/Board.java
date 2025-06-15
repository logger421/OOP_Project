package implementation.core;

import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;

import java.util.List;
import java.util.Set;

public class Board implements Cloneable {
    private final Mark ourMark;
    private final Mark opponentMark;
    private final int size;
    private final boolean periodic;
    private final Mark[][] grid;

    public Board(int size, boolean periodic, Mark currentMark, Mark opponentMark) {
        this.size = size;
        this.periodic = periodic;
        this.grid = new Mark[size][size];
        this.ourMark = currentMark;
        this.opponentMark = opponentMark;
    }

    public void initialize(Set<Move> rawMoves) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Mark.NULL;
            }
        }

        for (Move mv : rawMoves)
            this.placeMarkAt(mv.position(), mv.mark());
    }

    public int getSize() {
        return size;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public Mark getMarkAt(Position position) {
        return getMarkAt(position.row(), position.col());
    }

    public Mark getMarkAt(int row, int col) {
        if (periodic) {
            row = (row + size) % size;
            col = (col + size) % size;
        }
        return grid[col][row];
    }

    public void placeMarkAt(Position pos, Mark mark) {
        if (periodic) pos = new Position((pos.col() + size) % size, (pos.row() + size) % size);
        grid[pos.col()][pos.row()] = mark;
    }

    public List<Position> getEmptyPositions() {
        return PositionUtils.getEmptyPositions(this);
    }

    public Mark getOpponentMark() {
        return opponentMark;
    }

    public int countDirection(Position pos, Mark mark, int dx, int dy) {
        int row = pos.row();
        int col = pos.col();
        int cnt = 0;

        while (true) {
            row += dx;
            col += dy;
            if (periodic) {
                row = (row + size) % size;
                col = (col + size) % size;
            } else {
                if (row < 0 || row >= size || col < 0 || col >= size) break;
            }
            if (getMarkAt(row, col) == mark) cnt++;
            else break;
        }

        return cnt;
    }

    @Override
    public Board clone() {
        Board copy = new Board(size, periodic, ourMark, opponentMark);
        for (int i = 0; i < size; i++) {
            System.arraycopy(this.grid[i], 0, copy.grid[i], 0, size);
        }
        return copy;
    }

    @Override
    public String toString() {
        return BoardPrinter.print(this);
    }
}
