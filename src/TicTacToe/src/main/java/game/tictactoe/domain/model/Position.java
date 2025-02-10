package game.tictactoe.domain.model;

/**
 * Represents a position on a 2D grid, typically used for game boards.
 *
 * <p> This record encapsulates the row and column coordinates of a position.
 *
 * @param row The row index of the position (0-based).
 * @param col The column index of the position (0-based).
 */
public record Position(int row, int col) {

    /**
     * A constant representing an invalid position.
     */
    public static int NOT_VALID_POS = -1;
}
