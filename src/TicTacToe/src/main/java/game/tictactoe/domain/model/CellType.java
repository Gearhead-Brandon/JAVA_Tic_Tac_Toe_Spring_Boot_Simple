package game.tictactoe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the type of a cell in the game field.
 */
@Getter
@AllArgsConstructor
public enum CellType {
    X('X'),
    O('O'),
    EMPTY(' ');

    private final Character value;

    public static CellType valueOf(Character c) {
        return switch (Character.toUpperCase(c)) {
            case 'X' -> X;
            case 'O' -> O;
            default -> EMPTY;
        };
    }
}
