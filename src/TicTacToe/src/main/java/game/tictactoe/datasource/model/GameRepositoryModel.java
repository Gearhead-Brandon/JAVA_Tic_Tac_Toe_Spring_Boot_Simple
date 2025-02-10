package game.tictactoe.datasource.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class GameRepositoryModel {
    /**
     * The unique identifier for the game.
     */
    private final UUID uuid;

    /**
     * The side of the player starting the game ('X' or 'O').
     */
    private final Character playerSide;

    /**
     *The game board, represented as a two-dimensional list of characters.
     * ' ' represents an empty cell, 'X' represents a cross, and 'O' represents a nought.
     */
    private final List<List<Character>> gameField = List.of(
            Arrays.asList(' ', ' ', ' '),
            Arrays.asList(' ', ' ', ' '),
            Arrays.asList(' ', ' ', ' ')
    );

    public GameRepositoryModel(UUID uuid, Character playerSide) {
        this.uuid = uuid;
        this.playerSide = playerSide;
    }

    /**
     * Sets the value of a cell on the game board.
     *
     * @param row The row index.
     * @param col The column index.
     * @param cell The value to set ('X', 'O', or ' ').
     */
    public void setCell(int row, int col, Character cell) { gameField.get(row).set(col, cell);}

    /**
     * Gets the value of a cell on the game board.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The value of the cell ('X', 'O', or ' ').
     */
    public Character getCell(int row, int col) { return gameField.get(row).get(col); }
}
