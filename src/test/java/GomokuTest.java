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
    void shouldThrowTheWinnerIsException0() {
        String board =
                "X . O . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                "X . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);

        assertThrows(TheWinnerIsException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void shouldThrowTheWinnerIsException1() {
        String board =
                "X . . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                "X . . . . . . . . .";

        history = parseBoardString(board);
        engine.periodicBoundaryConditionsInUse();

        assertThrows(TheWinnerIsException.class, () -> engine.nextMove(history, Mark.NOUGHT));
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
        String board =
                ". . X . . . . . . .\n" +
                ". . X . . . . . . .\n" +
                ". O . O O O . . . .\n" +
                ". . X . . . . . . .\n" +
                ". . X . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        history = parseBoardString(board);

        Move predicted = engine.nextMove(history, Mark.CROSS);

        // Obracamy planszę w prawo
        board =
                ". . . . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                "X X . X X . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        history = parseBoardString(board);

        Move actual = engine.nextMove(history, Mark.CROSS);

        assertEquals(predicted.position(), actual.position(), "Ruch powinien być taki sam po obrocie planszy");
    }

    @Test
    void testMiddleOfCrossPosition() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . O . . O . .\n" +
                ". . X . . O O . . .\n" +
                ". X . X . . . . . .\n" +
                ". . X . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        Move nextMove = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Move(new Position(2, 6), Mark.CROSS), nextMove);
    }

    @Test
    void testMiddleOfForkPosition() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". X . . . . . . . .\n" +
                ". . X . . . . . . .\n" +
                ". . . . . O . . . .\n" +
                ". . X . . O . . . .\n" +
                ". X . . . . O O . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        Move nextMove = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Move(new Position(3, 4), Mark.CROSS), nextMove);
    }

    // Test for the periodic boundary conditions
    @Test
    void testPeriodicBoundaryConditions0() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . X\n" +
                "X O . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        engine.periodicBoundaryConditionsInUse();
        Move nextMove = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Move(new Position(0, 0), Mark.CROSS), nextMove);
    }

    @Test
    void testPeriodicBoundaryConditions1() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                "X X X X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". O . . . . . . . .\n" +
                ". O . . . . . . . .\n" +
                ". O . . . . . . . .\n" +
                ". O . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        engine.periodicBoundaryConditionsInUse();
        Move nextMove = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Move(new Position(4, 0), Mark.CROSS), nextMove);
    }

    // Test find good history

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

        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);

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

        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.NOUGHT);

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
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.NOUGHT);

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
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.NOUGHT);

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
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.NOUGHT);

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
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);

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
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.NOUGHT);

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
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);

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

        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);
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
        history = parseBoardString(board);
        move = engine.nextMove(history, Mark.CROSS);
        assertEquals(new Move(new Position(2, 3), Mark.CROSS), move);
    }

    @Test
    void findGoodMove10() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                ". X X X O . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O O O . . . .\n" +
                ". . . . . . . . . .\n" +
                "X X X . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O O . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.CROSS);
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);
        assertEquals(new Move(new Position(3, 5), Mark.CROSS), move);
    }

    @Test
    void findGoodMove11() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . . . . .\n" +
                ". X . O . . X . . .\n" +
                ". X . O . . X . . .\n" +
                ". X . O . . X . . O\n" +
                ". O . . . . . . . O\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.CROSS);
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);
        assertEquals(new Move(new Position(6, 4), Mark.CROSS), move);
    }

    @Test
    void findGoodMove12() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        String board =
                ". . . . . . X . . .\n" +
                ". X . O . . X . . .\n" +
                ". X . O . . X . . .\n" +
                ". X . O . . . . . O\n" +
                ". O . . . . . . . O\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.CROSS);
        history = parseBoardString(board);
        Move move = engine.nextMove(history, Mark.CROSS);
        assertEquals(new Move(new Position(6, 3), Mark.CROSS), move);
    }

    @Test
    void findGoodMove13() throws Exception {
        String board =
                "X . O . . . . . . .\n" +
                ". X . O . . . . . .\n" +
                ". . X . O . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        // teraz X powinien dołożyć na (0,4) i wygrać
        history = parseBoardString(board);
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(3, 3), next.position(), "Strategia WinImmediately powinna wskazać (0,4)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void findGoodMove14() throws Exception {
        String board =
                "X . O . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        // teraz X powinien dołożyć na (0,4) i wygrać
        history = parseBoardString(board);
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(0, 4), next.position(), "Strategia WinImmediately powinna wskazać (0,4)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void findGoodMove15() throws Exception {
        String board =
                ". . . . . . . . . .\n" +
                ". . . . X . . . . .\n" +
                "O O O O . . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        engine.firstMark(Mark.NOUGHT);
        history = parseBoardString(board);
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(4, 2), next.position(), "Strategia BlockOpponent powinna wskazać (4,2)");
        assertEquals(Mark.CROSS, next.mark());
    }

    // Scenarios testing the ResignException

    @Test
    void shouldThrowResignException0() {
        String board =
                ". . . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                "X . . . . . . . . .\n" +
                "X . O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

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

        history = parseBoardString(board);
        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.CROSS));
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
        history = parseBoardString(board);
        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.CROSS));
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

        history = parseBoardString(board);

        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void shouldThrowResignException4() {
        String board =
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . O . . . . .\n" +
                ". . X . . O . . . .\n" +
                ". X . X . . O . . .\n" +
                ". . X . . . . O . .\n" +
                ". . . . . . . . . .";

        history = parseBoardString(board);
        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void shouldThrowResignException5() {
        String board =
                "X . . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                "X O . . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . O . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                "X . . . . . . . . .";

        engine.firstMark(Mark.NOUGHT);
        engine.periodicBoundaryConditionsInUse();
        history = parseBoardString(board);

        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.NOUGHT));
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

        engine.firstMark(Mark.CROSS);
        history = parseBoardString(board);
        assertThrows(WrongBoardStateException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void shouldThrowWrongBoardStateException3() {
        engine.firstMark(Mark.CROSS);

        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 0), Mark.NOUGHT));

        assertThrows(WrongBoardStateException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    private static Set<Move> parseBoardString(String board) {
        Set<Move> moves = new HashSet<>();
        String[] rows = board.trim().split("\n");
        for (int r = 0; r < rows.length; r++) {
            String line = rows[r].replaceAll("\\s+", "");
            for (int c = 0; c < line.length(); c++) {
                char ch = line.charAt(c);
                if (ch == 'X') {
                    moves.add(new Move(new Position(c, r), Mark.CROSS));
                } else if (ch == 'O') {
                    moves.add(new Move(new Position(c, r), Mark.NOUGHT));
                }
            }
        }
        return moves;
    }
}
