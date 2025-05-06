import fais.zti.oramus.gomoku.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GomokuTest {

    private Gomoku engine;

    @BeforeEach
    void setUp() {
        engine = new Gomoku();
        engine.size(15);
    }

    @Test
    void testEngine() throws TheWinnerIsException, ResignException, WrongBoardStateException {
        Set<Move> history = new HashSet<>();
        // brak wcześniejszych ruchów → powinno wybrać pierwsze wolne pole (0,0)
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(0, 0), next.position(), "Pierwszy ruch powinien być na (0,0)");
        assertEquals(Mark.CROSS, next.mark());

    }

    @Test
    void testWinImmediatelyStrategy() throws Exception {
        Set<Move> history = new HashSet<>();
        // przygotuj sytuację 4 w linii dla X na wierszu 0: pola (0,0),(0,1),(0,2),(0,3)
        history.add(new Move(new Position(0, 0), Mark.CROSS));
        history.add(new Move(new Position(0, 1), Mark.CROSS));
        history.add(new Move(new Position(0, 2), Mark.CROSS));
        history.add(new Move(new Position(0, 3), Mark.CROSS));

        // teraz X powinien dołożyć na (0,4) i wygrać
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(0, 4), next.position(), "Strategia WinImmediately powinna wskazać (0,4)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testBlockOpponentStrategy() throws Exception {
        Set<Move> history = new HashSet<>();
        // sytuacja: przeciwnik O ma 4 w linii na kolumnie 2: (0,2),(1,2),(2,2),(3,2)
        history.add(new Move(new Position(0, 2), Mark.NOUGHT));
        history.add(new Move(new Position(1, 2), Mark.NOUGHT));
        history.add(new Move(new Position(2, 2), Mark.NOUGHT));
        history.add(new Move(new Position(3, 2), Mark.NOUGHT));

        // teraz X powinien zablokować na (4,2)
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(4, 2), next.position(), "Strategia BlockOpponent powinna wskazać (4,2)");
        assertEquals(Mark.CROSS, next.mark());
    }


    @Test
    void testCreateForkStrategy() throws Exception {
        Set<Move> history = new HashSet<>();
        // ustaw trzy w linii dla X, np. (1,0),(1,1),(1,3) z dziurą w środku i miejsce na drugą linię
        history.add(new Move(new Position(1, 0), Mark.CROSS));
        history.add(new Move(new Position(1, 1), Mark.CROSS));
        history.add(new Move(new Position(1, 3), Mark.CROSS));
        // przeciwnik gdzie indziej
        history.add(new Move(new Position(0, 0), Mark.NOUGHT));

        // w takiej sytuacji CreateFork powinien zasymulować ruch X na (1,2),
        // bo po nim będzie zagrożenie 5-w-linii w poziomie i np. pionie
        Move next = engine.nextMove(history, Mark.CROSS);

        assertEquals(new Position(1, 2), next.position(), "Strategia CreateFork powinna wskazać (1,2)");
        assertEquals(Mark.CROSS, next.mark());
    }

    @Test
    void testResignWhenNoMoves() {
        Set<Move> history = new HashSet<>();
        // zapełnij planszę do końca
        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 15; c++) {
                history.add(new Move(new Position(r, c), (r + c) % 2 == 0 ? Mark.CROSS : Mark.NOUGHT));
            }
        }
        // teraz nie ma pustego pola → powinno rzucić ResignException
        assertThrows(ResignException.class, () -> engine.nextMove(history, Mark.CROSS));
    }

}
