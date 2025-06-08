import fais.zti.oramus.gomoku.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 2x wrong board state exception
// Gra nie przewiduje, że pierwsza wygra i broni się zamiast atakować
// Gra w jednym przypadku próbuje się bronić/ randomowo coś rzuca zamiast się poddać
//

public class GomokuTest {
    private Gomoku engine;
    private Set<Move> history;

    @BeforeEach
    void setUp() {
        history = new HashSet<>();
        engine = new Gomoku();
        engine.firstMark(Mark.CROSS);
        engine.size(10);
        engine.debugMode();
    }

    @Test
    void testWinImmediatelyStrategy() throws Exception {
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(0, 3), Mark.CROSS));

        history.add(new Move(new Position(2, 0), Mark.NOUGHT));
        history.add(new Move(new Position(2, 1), Mark.NOUGHT));
        history.add(new Move(new Position(2, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));

        // teraz X powinien dołożyć na (0,4) i wygrać
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(0, 4), next.position(), "Strategia WinImmediately powinna wskazać (0,4)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testWinImmediatelyStrategyWithThreeCrossOnBoard() throws Exception {
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(1, 1), Mark.CROSS));
        history.add(new Move(new Position(2, 2), Mark.CROSS));

        history.add(new Move(new Position(2, 0), Mark.NOUGHT));
        history.add(new Move(new Position(3, 1), Mark.NOUGHT));
        history.add(new Move(new Position(4, 2), Mark.NOUGHT));

        // teraz X powinien dołożyć na (0,4) i wygrać
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(3, 3), next.position(), "Strategia WinImmediately powinna wskazać (0,4)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testTheWinnerIsExceptionOnImmediateBoard() {
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(0, 3), Mark.CROSS));
        history.add(new Move(new Position(0, 4), Mark.CROSS));

        history.add(new Move(new Position(2, 0), Mark.NOUGHT));
        history.add(new Move(new Position(2, 1), Mark.NOUGHT));
        history.add(new Move(new Position(2, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));

        assertThrows(TheWinnerIsException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void testBlockOpponentStrategy() throws Exception {
        engine.firstMark(Mark.NOUGHT);
        history.add(new Move(new Position(0, 2), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 2), Mark.NOUGHT));
        history.add(new Move(new Position(3, 2), Mark.NOUGHT));

        history.add(new Move(new Position(4, 1), Mark.CROSS));
        history.add(new Move(new Position(4, 3), Mark.CROSS));
        history.add(new Move(new Position(4, 4), Mark.CROSS));

        // teraz X powinien zablokować na (4,2)
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(4, 2), next.position(), "Strategia BlockOpponent powinna wskazać (4,2)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testCreateForkStrategy() throws Exception {
        String board =
                ". X . . . O . . . .\n" +
                ". X . . . O . . . .\n" +
                "X . X X . . . . . .\n" +
                ". X . . . . . . . .\n" +
                ". . . O O . . . . .\n" +
                ". . . . O O . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(1, 2), next.position(), "CreateFork powinien wskazać (1,2)");
    }

    @Test
    void testRotateBoardRight() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        history.add(new Move(new Position(2, 0), Mark.CROSS));
        history.add(new Move(new Position(2, 1), Mark.CROSS));
        history.add(new Move(new Position(2, 3), Mark.CROSS));
        history.add(new Move(new Position(2, 4), Mark.CROSS));

        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(3, 2), Mark.NOUGHT));
        history.add(new Move(new Position(4, 2), Mark.NOUGHT));
        history.add(new Move(new Position(5, 2), Mark.NOUGHT));

        Move predicted = engine.nextMove(history, Mark.CROSS);

        // Obracamy planszę w prawo
        history = new HashSet<>();
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(1, 2), Mark.CROSS));
        history.add(new Move(new Position(3, 2), Mark.CROSS));
        history.add(new Move(new Position(4, 2), Mark.CROSS));

        history.add(new Move(new Position(2, 1), Mark.NOUGHT));
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));
        history.add(new Move(new Position(2, 4), Mark.NOUGHT));
        history.add(new Move(new Position(2, 5), Mark.NOUGHT));

        Move actual = engine.nextMove(history, Mark.CROSS);
        assertEquals(predicted.position(), actual.position(), "Ruch powinien być taki sam po obrocie planszy");
    }

    @Test
    void testMiddleOfCrossPosition() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.firstMark(Mark.CROSS);
        history.add(new Move(new Position(2, 5), Mark.CROSS));
        history.add(new Move(new Position(1, 6), Mark.CROSS));
        history.add(new Move(new Position(3, 6), Mark.CROSS));
        history.add(new Move(new Position(2, 7), Mark.CROSS));

        history.add(new Move(new Position(4, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 5), Mark.NOUGHT));
        history.add(new Move(new Position(6, 5), Mark.NOUGHT));
        history.add(new Move(new Position(7, 4), Mark.NOUGHT));

        assertEquals(new Move(new Position(2, 6), Mark.CROSS), engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void testMiddleOfForkPosition() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.firstMark(Mark.CROSS);
        history.add(new Move(new Position(1, 2), Mark.CROSS));
        history.add(new Move(new Position(1, 6), Mark.CROSS));
        history.add(new Move(new Position(2, 3), Mark.CROSS));
        history.add(new Move(new Position(2, 5), Mark.CROSS));

        history.add(new Move(new Position(5, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 5), Mark.NOUGHT));
        history.add(new Move(new Position(6, 6), Mark.NOUGHT));
        history.add(new Move(new Position(7, 6), Mark.NOUGHT));

        assertEquals(new Move(new Position(3, 4), Mark.CROSS), engine.nextMove(history, Mark.CROSS));
    }

    // Scenarios testing the ResignException
    @Test
    void testResignWhenNoMoves() {
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(0, 3), Mark.CROSS));
        history.add(new Move(new Position(0, 4), Mark.CROSS));

        history.add(new Move(new Position(2, 4), Mark.NOUGHT));
        history.add(new Move(new Position(1, 1), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));

        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void testResignException() {
        history.add(new Move(new Position(2, 5), Mark.CROSS));
        history.add(new Move(new Position(1, 6), Mark.CROSS));
        history.add(new Move(new Position(3, 6), Mark.CROSS));
        history.add(new Move(new Position(2, 7), Mark.CROSS));

        history.add(new Move(new Position(4, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 5), Mark.NOUGHT));
        history.add(new Move(new Position(6, 6), Mark.NOUGHT));
        history.add(new Move(new Position(7, 7), Mark.NOUGHT));

        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    // Test for the periodic boundary conditions
    @Test
    void testPeriodicBoundaryConditions() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.periodicBoundaryConditionsInUse();

        history.add(new Move(new Position(0, 9), Mark.CROSS));
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(0, 3), Mark.CROSS));

        history.add(new Move(new Position(2, 4), Mark.NOUGHT));
        history.add(new Move(new Position(1, 1), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));

        assertEquals(new Move(new Position(0, 0), Mark.CROSS), engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void testPeriodicBoundaryConditionsTheWInnerIsException() {
        engine.periodicBoundaryConditionsInUse();

        history.add(new Move(new Position(0, 9), Mark.CROSS));
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(0, 3), Mark.CROSS));

        history.add(new Move(new Position(2, 4), Mark.NOUGHT));
        history.add(new Move(new Position(1, 1), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));

        assertThrows(TheWinnerIsException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void testPeriodicBoundaryConditionsResignException() {
        engine.firstMark(Mark.NOUGHT);
        engine.periodicBoundaryConditionsInUse();

        history.add(new Move(new Position(0, 9), Mark.CROSS));
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));

        history.add(new Move(new Position(2, 4), Mark.NOUGHT));
        history.add(new Move(new Position(1, 1), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));

        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void testPeriodicBoundaryConditionsWithFork() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.periodicBoundaryConditionsInUse();

        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(1, 0), Mark.CROSS));
        history.add(new Move(new Position(2, 0), Mark.CROSS));
        history.add(new Move(new Position(3, 0), Mark.CROSS));

        history.add(new Move(new Position(1, 4), Mark.NOUGHT));
        history.add(new Move(new Position(1, 5), Mark.NOUGHT));
        history.add(new Move(new Position(1, 6), Mark.NOUGHT));
        history.add(new Move(new Position(1, 7), Mark.NOUGHT));

        // Fork at (0,4) and (0,5)
        assertEquals(new Move(new Position(4, 0), Mark.CROSS), engine.nextMove(history, Mark.CROSS));
    }

    // Test find good moves

    @Test
    void findGoodMove1() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . X . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . X X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . O O O .\n" +
                ". . . . . . . . . .\n" +
                ". . . O . . . . . .\n" +
                ". . . . . . . . . .";

        Set<Move> moves = parseBoardString(board);

        Move move = engine.nextMove(moves, Mark.CROSS);
        assertEquals(new Move(new Position(5, 6), Mark.CROSS), move);
    }

    @Test
    void findGoodMove2() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . X . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . X X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . O O O .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        Set<Move> moves = parseBoardString(board);

        Move move = engine.nextMove(moves, Mark.NOUGHT);
        assertEquals(new Move(new Position(5, 6), Mark.NOUGHT), move);

    }

    @Test
    void findGoodMove3() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                        ". . X . O O . O . X\n" +
                        ". . . . . . . . . X\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";

        engine.firstMark(Mark.NOUGHT);
        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.NOUGHT);
        assertEquals(new Move(new Position(6, 1), Mark.NOUGHT), move);

    }

    @Test
    void findGoodMove4() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                        ". . X . O O . O . X\n" +
                        ". . . . . . . . . X\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";

        engine.firstMark(Mark.NOUGHT);
        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.NOUGHT);
        assertEquals(new Move(new Position(6, 1), Mark.NOUGHT), move);

    }

    @Test
    void findGoodMove5() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                ". . X X X . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O O O . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.NOUGHT);
        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.NOUGHT);
        assertEquals(new Move(new Position(2, 3), Mark.NOUGHT), move);
    }

    @Test
    void findGoodMove6() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                ". X X X O . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O O O . . . .\n" +
                ". . . . . . . . . .\n" +
                ". X X X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O O . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.CROSS);
        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.CROSS);
        assertEquals(new Move(new Position(4, 5), Mark.CROSS), move);
    }

    @Test
    void findGoodMove7() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . X . .\n" +
                ". . . . . . . . . .\n" +
                ". . O O X O O . . .\n" +
                ". . . X . O O . . O\n" +
                ". . X X O . O . X .\n" +
                ". . . X . . . X O .\n" +
                "X . X O . . . . X .\n" +
                ". . . . . . . . . O\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.NOUGHT);
        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.NOUGHT);
        assertEquals(new Move(new Position(1, 5), Mark.NOUGHT), move);
    }

    @Test
    void findGoodMove8() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". X . . . O . . . .\n" +
                ". X . . O X . . . O\n" +
                ". X . O . . O X O X\n" +
                ". . . . . . . O . .\n" +
                ". . . . O X . . O .\n" +
                ". . . . . X . . . O\n" +
                ". . . . . . X X . .\n" +
                ". . . . . . . X . .\n" +
                ". . . . . . . . O .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.CROSS);
        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.CROSS);
        assertEquals(new Move(new Position(5, 6), Mark.CROSS), move);
    }

    @Test
    void findGoodMove9() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.firstMark(Mark.CROSS);

        String board =
                ". . . . . . . . . .\n" +
                "X . . . . . O . . .\n" +
                ". X . . . . . O . .\n" +
                ". . X . . . O . O .\n" +
                ". . . X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        Set<Move> moves = parseBoardString(board);
        Move move = engine.nextMove(moves, Mark.CROSS);
        assertEquals(new Move(new Position(4, 5), Mark.CROSS), move);

        board =
                ". . . . . . . . . .\n" +
                "X . . . . . O . . .\n" +
                ". X . . . . . O . .\n" +
                ". . . . . . O . O .\n" +
                ". . . X . . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        moves = parseBoardString(board);
        move = engine.nextMove(moves, Mark.CROSS);
        assertEquals(new Move(new Position(2, 3), Mark.CROSS), move);
    }

    // Test resign exception
    @Test
    void shouldThrowResignException1() {
        String board =
                ". . . O . . . O . .\n" +
                ". . . O . . . O . .\n" +
                ". . . O . . . O . .\n" +
                ". . . O . . . O . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". X X . X . X . . X\n" +
                ". . . . . . . . . .\n" +
                ". . . X . X . . X .";

        Set<Move> moves = parseBoardString(board);
        assertThrows(ResignException.class, () -> engine.nextMove(moves, Mark.CROSS));
    }

    @Test
    void shouldThrowResignException2() {
        String board =
                ". . . . . . . . . .\n" +
                ". . . O . . . O . .\n" +
                ". . . O . . . O . .\n" +
                ". . . O . . . O . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". X . . . . X . . X\n" +
                ". . . . . . . . . .\n" +
                ". . . X . X . . X .";
        Set<Move> moves = parseBoardString(board);
        assertThrows(ResignException.class, () -> engine.nextMove(moves, Mark.CROSS));
    }

    @Test
    void shouldThrowResignException3() {
        engine.firstMark(Mark.CROSS);
        String board =
                ". . . . . . . . . .\n" +
                ". . . O . . O . . .\n" +
                ". . . O . . O . . .\n" +
                ". O O . . . . O O .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". X . . X . X . . X\n" +
                ". X . . . . . . . .\n" +
                ". . . X . X . . X .";

        Set<Move> moves = parseBoardString(board);

        assertThrows(ResignException.class, () -> engine.nextMove(moves, Mark.CROSS));
    }

    // Wrong Board State Exception
    @Test
    void shouldThrowWrongBoardStateException1() {
        engine.firstMark(Mark.NOUGHT);
        String board =
                ". X O . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";

        history = parseBoardString(board);
        assertThrows(WrongBoardStateException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void shouldThrowWrongBoardStateException2() {
        engine.firstMark(Mark.CROSS);
        String board =
                ". X O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        assertThrows(WrongBoardStateException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void shouldThrowWrongBoardStateException3() {
        engine.firstMark(Mark.CROSS);


        history = new HashSet<>();
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 0), Mark.NOUGHT));

        assertThrows(WrongBoardStateException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    public static Set<Move> parseBoardString(String board) {
        Set<Move> moves = new HashSet<>();
        String[] rows = board.trim().split("\n");
        for (int r = 0; r < rows.length; r++) {
            String line = rows[r].replaceAll("\\s+", "");
            for (int c = 0; c < line.length(); c++) {
                char ch = line.charAt(c);
                if (ch == 'X') {
                    // In Gomoku, positions are often column, row - note the swap
                    moves.add(new Move(new Position(c, r), Mark.CROSS));
                } else if (ch == 'O') {
                    moves.add(new Move(new Position(c, r), Mark.NOUGHT));
                }
            }
        }
        return moves;
    }
}
