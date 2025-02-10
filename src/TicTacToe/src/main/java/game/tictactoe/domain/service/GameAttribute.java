package game.tictactoe.domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration for storing game attributes.
 */
@Getter
@AllArgsConstructor
public enum GameAttribute {
    COLS(3),
    ROWS(3);

    private final int value;
}
