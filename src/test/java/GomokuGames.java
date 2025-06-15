import fais.zti.oramus.gomoku.Mark;
import fais.zti.oramus.gomoku.Move;
import fais.zti.oramus.gomoku.Position;
import fais.zti.oramus.gomoku.ResignException;

import java.util.HashSet;
import java.util.Set;

public class GomokuGames {

    // Helper: Parse a board string into a set of moves
    // 'X' = CROSS, 'O' = NOUGHT, '.' or ' ' = empty
    public static Set<Move> parseBoardString(String board, int size) {
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

    // New helper: Print the board as represented by the actual moves
    public static void debugPrintMoves(Set<Move> moves, int size) {
        System.out.println("DEBUG: Board from moves (Position format: " +
                (moves.isEmpty() ? "unknown" : moves.iterator().next().position().getClass().getName()) + ")");

        char[][] board = new char[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = '.';
            }
        }

        for (Move move : moves) {
            Position pos = move.position();
            // Get coordinates from the Position object - assuming Position has getters
            int x = pos.col();  // Adjust these accessor methods as needed
            int y = pos.row();
            if (x >= 0 && x < size && y >= 0 && y < size) {
                board[y][x] = (move.mark() == Mark.CROSS) ? 'X' : 'O';
            }
        }

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println("Crosses: " + moves.stream().filter(m -> m.mark() == Mark.CROSS).count() +
                ", Noughts: " + moves.stream().filter(m -> m.mark() == Mark.NOUGHT).count());
    }

    // Helper: Print the board for visual check
    public static void printBoard(String board) {
        System.out.println(board.trim());
    }

    // Detailed board printer with row and column indices
    public static void printBoardDetailed(Set<Move> moves, int size) {
        char[][] board = new char[size][size];

        // Initialize empty board
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = '.';
            }
        }

        // Place moves on board
        for (Move move : moves) {
            Position pos = move.position();
            int x = pos.col();
            int y = pos.row();
            if (x >= 0 && x < size && y >= 0 && y < size) {
                board[y][x] = (move.mark() == Mark.CROSS) ? 'X' : 'O';
            }
        }

        // Print column indices
        System.out.print("   ");
        for (int c = 0; c < size; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        // Print separator line
        System.out.print("  ");
        for (int c = 0; c < size; c++) {
            System.out.print("--");
        }
        System.out.println();

        // Print board with row indices
        for (int r = 0; r < size; r++) {
            System.out.print(r + "| ");
            for (int c = 0; c < size; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }

    // Common test execution method
    private static void runTest(String board, Mark player, Mark firstMark, int size, boolean periodicBoundary, boolean printBoard) {
        Set<Move> moves = parseBoardString(board, size);
        Gomoku game = new Gomoku();

        if (periodicBoundary) {
            game.periodicBoundaryConditionsInUse();
        }
        game.size(size);
        game.firstMark(firstMark);

        try {
            if (printBoard) {
                printBoardDetailed(moves, size);
            }
            Move move = game.nextMove(moves, player);
            System.out.println("Suggested move: " + move);
        } catch (ResignException e) {
            System.out.println("Resign: " + e);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    // Overloaded method with default parameters
    private static void runTest(String board, Mark player, Mark firstMark, int size) {
        runTest(board, player, firstMark, size, false, true);
    }

    private static void runTest(String board, Mark player, Mark firstMark, int size, boolean periodicBoundary) {
        runTest(board, player, firstMark, size, periodicBoundary, true);
    }

    // Test 1: Board from image 2 (one move to win for X)
    public static void testDiagonalWin() {
        String board =
                "X . . . . . . . . .\n" +
                "O X . . . . . . . .\n" +
                ". O X . . . . . . .\n" +
                ". . O X . . . . . .\n" +
                ". . . O . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    // Test 2: Board from image 3 (X can win in two places, test for resign)
    public static void testDoubleThreatResign() {
        String board =
                ". . . . . . . . . .\n" +
                ". . . . . . O . X .\n" +
                ". . O . . X X O . .\n" +
                ". . X O X . O . X .\n" +
                ". X X X O O O X . .\n" +
                ". O X O O O X . . .\n" +
                ". O X X . . . . . .\n" +
                ". . O X O . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    // Test 3: Board from image 1 (complex, no immediate win)
    public static void testComplexBoard() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". X . . . X . O . .\n" +
                        ". . X . X . O . . .\n" +
                        ". . . X . O . . . .\n" +
                        ". . X . O O . . . .\n" +
                        ". . . O O . . . . .\n" +
                        ". . . . X . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    // Test 4: Empty board
    public static void testBoard4() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10, false, false);
    }

    // Test 5: Block opponent's winning move
    public static void testBlockWin() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". X O O O O . . . .\n" +
                        ". . X X . X . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    // Test 6: Create a fork (two potential winning lines)
    public static void testInvalidBoard() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . X X . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . X . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    // Test 7: Mid-game position with both players having threats
    public static void testMidGameThreats() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . O . . . . . .\n" +
                        ". . X X X . . . . .\n" +
                        ". . O X O O . . . .\n" +
                        ". . X O X O . . . .\n" +
                        ". . X O X . . . . .\n" +
                        ". . O X O . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    // Test 9: Two moves to win
    public static void testTwoMovesToWin() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . X X . X . . . .\n" +
                        ". . O O . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    // Test 10: Edge case with pieces at board edges
    public static void testEdgeCase() {
        String board =
                "X . . . . . . . . X\n" +
                ". O . . . . . . O .\n" +
                ". . X . . . . X . .\n" +
                ". . . O . . O . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O . . O . . .\n" +
                ". . X . . . . X . .\n" +
                ". O . . . . . . O .\n" +
                "X . . . . . . . . X";
        runTest(board, Mark.CROSS, Mark.CROSS, 10, true);
    }

    // Test 11: Defensive play with opponent threatening
    public static void testDefensivePlay() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . X O . . . . .\n" +
                        ". . . O X O . . . .\n" +
                        ". . . X O X . . . .\n" +
                        ". . . . X . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    // Test 12: Test periodic boundary conditions - horizontal wrap (left to right)
    public static void testPeriodicLeftRight() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        "X X X . . . . . . X\n" +
                        "O . . . . . . . . .\n" +
                        "O . . . . . . . . .\n" +
                        "O . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10, true);
    }

    // Test 13: Test periodic boundary conditions - vertical wrap (top to bottom)
    public static void testPeriodicTopBottom() {
        String board =
                ". . . . X . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . O . . . . . .\n" +
                ". . . O . . . . . .\n" +
                ". . . O . . . . . .\n" +
                ". . . O X . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . X . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10, true);
    }

    // Test 14: Test periodic boundary conditions - diagonal wrap across corners
    public static void testPeriodicCorners() {
        String board =
                ". . . . . . . . . .\n" +
                ". X . . . . . . . .\n" +
                ". . X . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                "O . . . . . O O O O\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . X . . .\n" +
                ". . . . . . . X . .\n" +
                ". . . . . . . . X .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10, true);
    }

    // Test 15: Test blocking moves with periodic boundary conditions
    public static void testPeriodicBlock() {
        String board =
                "O O O . . . . . . O\n" +
                "X . . . . . . . . .\n" +
                ". X . . . . . . . .\n" +
                ". . X . . . . . . .\n" +
                ". . . X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.NOUGHT, 10, true);
    }

    public static void testEnemyBlock() {
        String board =
                ". . O O O . . . . .\n" +
                ". O X X X O O O . .\n" +
                ". X . . . . O . . .\n" +
                ". . X . . . X . . .\n" +
                ". . . X . X . . . .\n" +
                ". . . O . . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void testCantWin() {
        String board =
                ". . O O O . . . . .\n" +
                ". O X X X O O O . .\n" +
                ". X . . . . O . . .\n" +
                ". . X . . . X . . .\n" +
                ". . . X . X . . . .\n" +
                ". . . O X . . . . .\n" +
                ". . . . X . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    public static void continueGame() {
        String board =
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . X X . . . .\n" +
                ". . . . O O . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void whoisWinning() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . X X X X X .\n" +
                        ". . . . O O O O O .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void doubleWin() {
        String board =
                "X . . . O O . . X .\n" +
                        ". X . . O O O X . .\n" +
                        ". . X . O O X . . .\n" +
                        ". . . X O X . . . .\n" +
                        ". . . . X . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    public static void doubleWinv2() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . O O O O . .\n" +
                        ". . . O X X X X X .\n" +
                        ". . . O O O O . . .\n" +
                        ". . . O X X X X X .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void cantWinv3() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . O . . .\n" +
                        ". . . . . O O O . .\n" +
                        ". . . . . . O . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". X X . . . . . . .\n" +
                        ". X X . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.NOUGHT, 10);
    }

    public static void cantWinv4() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". X . . . . . . . .\n" +
                        ". . X . . . . . . .\n" +
                        ". . . X . . O O . .\n" +
                        ". . X . . . O O . .\n" +
                        ". X . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    public static void cantWinv5() {
        String board =
                ". . . . . . . O O .\n" +
                        ". . . . . . . O O .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . X . . . . . .\n" +
                        ". . X X . . . . . .\n" +
                        ". X . X . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.NOUGHT, Mark.CROSS, 10);
    }

    public static void fingoodmove() {
        String board =
                ". . . . . . . O O .\n" +
                ". . . . . . . O O .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". X . X . . . . . .\n" +
                ". . X X . . . . . .\n" +
                ". . . . . . . . . .\n" +
                ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void fingoodmoveV2() {
        String board =
                ". . . . . . . O O .\n" +
                        ". . . . . . . O O .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . X . . . . . . .\n" +
                        ". X . X . . . . . .\n" +
                        ". . X . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void fingoodmoveV3() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". X . . . . . . . .\n" +
                        ". . X . . . . . . .\n" +
                        ". . . . . . O O . .\n" +
                        ". . X . . . O O . .\n" +
                        ". X . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void fingoodmoveV4() {
        String board =
                ". . . . X . . . . .\n" +
                        ". . . . X . . . . .\n" +
                        ". . X X . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . O O .\n" +
                        ". . . . . . . O O .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void fingoodmoveV5() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". X X X . . . . . .\n" +
                        ". O . . . . . . . .\n" +
                        ". O . . . . . . . .\n" +
                        ". O . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void testabc() {
        String board =
                ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . O X X . X . O .\n" +
                        ". . O . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .\n" +
                        ". . . . . . . . . .";
        runTest(board, Mark.CROSS, Mark.CROSS, 10);
    }

    public static void main(String[] args) {
        System.out.println("Test 1: Diagonal Win");
        testDiagonalWin();
        System.out.println("\nTest 2: Double Threat Resign");
        testDoubleThreatResign();
        System.out.println("\nTest 3: Complex Board");
        testComplexBoard();
        System.out.println("\nTest 4: Empty Board");
        testBoard4();
        System.out.println("\nTest 5: Block Win");
        testBlockWin();
        System.out.println("\nTest 6: WrongBoardStateException");
        testInvalidBoard();
        System.out.println("\nTest 7: Mid-Game Threats");
        testMidGameThreats();
        System.out.println("\nTest 9: Two Moves to Win");
        testTwoMovesToWin();
        System.out.println("\nTest 10: Edge Case");
        testEdgeCase();
        System.out.println("\nTest 11: Defensive Play");
        testDefensivePlay();
        System.out.println("\nTest 12: Periodic Left-Right");
        testPeriodicLeftRight();
        System.out.println("\nTest 13: Periodic Top-Bottom");
        testPeriodicTopBottom();
        System.out.println("\nTest 14: Periodic Corners");
        testPeriodicCorners();
        System.out.println("\nTest 15: Periodic Block");
        testPeriodicBlock();
        System.out.println("\nTest 16: Enemy Block");
        testEnemyBlock();
        System.out.println("\nTest 17: Can't Win");
        testCantWin();
        System.out.println("\nTest 18: Continue Game");
        continueGame();
        System.out.println("\nTest 19: Who is Winning");
        whoisWinning();
        System.out.println("\nTest 20: Double Win");
        doubleWin();
        System.out.println("\nTest 21: Double Win v2");
        doubleWinv2();
        System.out.println("\nTest 22");
        cantWinv3();
        System.out.println("\nTest 23");
        cantWinv4();
        System.out.println("\nTest 24");
        cantWinv5();
        System.out.println("\nTest 25");
        fingoodmove();
        System.out.println("\nTest 26");
        fingoodmoveV2();
        System.out.println("\nTest 27");
        fingoodmoveV3();
        System.out.println("\nTest 28");
        fingoodmoveV4();
        System.out.println("\nTest 29");
        fingoodmoveV5();
        System.out.println("\nTest 30: Test abc");
        testabc();
    }
}