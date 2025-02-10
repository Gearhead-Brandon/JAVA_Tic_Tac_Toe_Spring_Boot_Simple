package game.tictactoe.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * Encapsulates the outcome of a game move and the resulting game state.
 */
@Getter
@EqualsAndHashCode
public class MoveResultDto {
    private final String status;
    private final List<List<Character>> gameField;

    public MoveResultDto(String status, GameFieldDTO gameField) {
        this.status = status;
        this.gameField = gameField.getField();
    }
}
