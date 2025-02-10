package game.tictactoe.web.model;

import game.tictactoe.web.annotation.ValidGameField;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the game field for a Tic-Tac-Toe game.
 *
 * <p> This class holds the current state of the game board,
 * represented as a two-dimensional list of characters:
 * ' ' for empty cells, 'X' for player X's moves, and 'O' for player O's moves.
 */
@Setter
@ToString
public class GameFieldDTO {
    @NotNull
    @ValidGameField
    private List<List<Character>> gameField;

    public GameFieldDTO(List<List<Character>> gameField) {
        this.gameField = gameField;
    }

    public GameFieldDTO() {
        this.gameField = List.of(
                Arrays.asList(' ', ' ', ' '),
                Arrays.asList(' ', ' ', ' '),
                Arrays.asList(' ', ' ', ' ')
        );
    }

    public List<List<Character>> getField() {
        return gameField.stream()
                .map(List::copyOf)
                .toList();
    }

    public Character getCell(int row, int col) { return gameField.get(row).get(col); }

    public void setCell(int row, int col, Character cell) { gameField.get(row).set(col, cell); }
}
