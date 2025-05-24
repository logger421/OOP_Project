import fais.zti.oramus.gomoku.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GomokuTest {

    private Gomoku engine;
    private Set<Move> history;

    @BeforeEach
    void setUp() {
        history = new HashSet<>();
        engine = new Gomoku();
        engine.size(10);
        engine.firstMark(Mark.CROSS);
    }

    @Test
    void testEngine() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        // brak wcześniejszych ruchów → powinno wybrać pierwsze wolne pole (0,0)
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(0, 0), next.position(), "Pierwszy ruch powinien być na (0,0)");
        assertEquals(Mark.CROSS, next.mark());

    }

    @Test
    void testWinImmediatelyStrategy() throws Exception {
        // przygotuj sytuację 4 w linii dla X na wierszu 0: pola (0,0),(0,1),(0,2),(0,3)
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
        // sytuacja: przeciwnik O ma 4 w linii na kolumnie 2: (0,2),(1,2),(2,2),(3,2)
        history.add(new Move(new Position(0, 2), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 2), Mark.NOUGHT));
        history.add(new Move(new Position(3, 2), Mark.NOUGHT));

        history.add(new Move(new Position(4, 1), Mark.CROSS));
        history.add(new Move(new Position(4, 2), Mark.CROSS));
        history.add(new Move(new Position(4, 3), Mark.CROSS));


        // teraz X powinien zablokować na (4,2)
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(4, 2), next.position(), "Strategia BlockOpponent powinna wskazać (4,2)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testCreateForkStrategy() throws Exception {
        // Poziomo: X w (1,0), (1,1) i (1,3) – dziura na (1,2)
        history.add(new Move(new Position(1, 0), Mark.CROSS));
        history.add(new Move(new Position(1, 1), Mark.CROSS));
        history.add(new Move(new Position(1, 3), Mark.CROSS));

        // Pionowo: X w (0,2), (2,2) i (3,2) – dziura na (1,2)
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(2, 2), Mark.CROSS));
        history.add(new Move(new Position(3, 2), Mark.CROSS));

        // Dodajemy parę ruchów przeciwnika gdzie indziej, by nie przeszkadzał
        history.add(new Move(new Position(5, 0), Mark.NOUGHT));
        history.add(new Move(new Position(5, 1), Mark.NOUGHT));
        history.add(new Move(new Position(4, 2), Mark.NOUGHT));
        history.add(new Move(new Position(5, 3), Mark.NOUGHT));
        history.add(new Move(new Position(4, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 5), Mark.NOUGHT));

        // Teraz CreateForkStrategy powinien wybrać (1,2):
        //  • pozioma linia: (1,0),(1,1),(1,2),(1,3) – 4 w linii
        //  • pionowa linia:  (0,2),(1,2),(2,2),(3,2) – 4 w linii

        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(1, 2), next.position(), "CreateFork powinien wskazać (1,2)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testWrongBoardStateException() {
        engine.firstMark(Mark.CROSS);
        engine.size(10);
        history = new HashSet<>();
        history.add(new Move(new Position(4, 3), Mark.CROSS));
        history.add(new Move(new Position(5, 3), Mark.CROSS));
        history.add(new Move(new Position(6, 3), Mark.CROSS));
        history.add(new Move(new Position(7, 3), Mark.CROSS));
        history.add(new Move(new Position(8, 3), Mark.CROSS));

        history.add(new Move(new Position(4, 5), Mark.CROSS));
        history.add(new Move(new Position(5, 5), Mark.CROSS));
        history.add(new Move(new Position(6, 5), Mark.CROSS));
        history.add(new Move(new Position(7, 5), Mark.CROSS));
        history.add(new Move(new Position(8, 5), Mark.CROSS));

        history.add(new Move(new Position(3, 4), Mark.NOUGHT));
        history.add(new Move(new Position(4, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 4), Mark.NOUGHT));
        history.add(new Move(new Position(6, 4), Mark.NOUGHT));

        history.add(new Move(new Position(4, 2), Mark.NOUGHT));
        history.add(new Move(new Position(5, 2), Mark.NOUGHT));
        history.add(new Move(new Position(6, 2), Mark.NOUGHT));
        history.add(new Move(new Position(7, 2), Mark.NOUGHT));

        history.add(new Move(new Position(3, 3), Mark.NOUGHT));
        history.add(new Move(new Position(3, 5), Mark.NOUGHT));

        assertThrows(WrongBoardStateException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void testRotateBoardRight() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.firstMark(Mark.CROSS);
        engine.size(10);

        history = new HashSet<>();
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
        engine.size(10);

        history = new HashSet<>();
        history.add(new Move(new Position(2, 5), Mark.CROSS));
        history.add(new Move(new Position(1, 6), Mark.CROSS));
        history.add(new Move(new Position(3, 6), Mark.CROSS));
        history.add(new Move(new Position(2, 7), Mark.CROSS));

        history.add(new Move(new Position(4, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 5), Mark.NOUGHT));
        history.add(new Move(new Position(6, 6), Mark.NOUGHT));
        history.add(new Move(new Position(7, 5), Mark.NOUGHT));

        assertEquals(new Move(new Position(2, 6), Mark.CROSS),  engine.nextMove(history, Mark.CROSS));
    }

    @Test
    void testMiddleOfForkPosition() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        engine.firstMark(Mark.CROSS);
        engine.size(10);

        history = new HashSet<>();
        history.add(new Move(new Position(1, 2), Mark.CROSS));
        history.add(new Move(new Position(1, 6), Mark.CROSS));
        history.add(new Move(new Position(2, 3), Mark.CROSS));
        history.add(new Move(new Position(2, 5), Mark.CROSS));

        history.add(new Move(new Position(4, 4), Mark.NOUGHT));
        history.add(new Move(new Position(5, 5), Mark.NOUGHT));
        history.add(new Move(new Position(6, 6), Mark.NOUGHT));
        history.add(new Move(new Position(7, 5), Mark.NOUGHT));

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
        history.add(new Move(new Position(2, 3), Mark.NOUGHT));

        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.NOUGHT));
    }

    @Test
    void testResignException() {
        engine.firstMark(Mark.CROSS);
        engine.size(10);

        history = new HashSet<>();
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
}
