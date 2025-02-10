package game.tictactoe.domain.model;

import lombok.*;

import java.util.UUID;

/**
 * Represents a single game of Tic-Tac-Toe.
 *
 * <p> This class encapsulates the game's state, including its unique identifier,
 * the player's assigned side (X or O), and the game board itself.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Game {
    @Setter
    private UUID uuid;
    @Setter
    private CellType playerSide;
    private final GameField gameField;

    public Game(UUID uuid, CellType playerSide) {
        this.uuid = uuid;
        this.playerSide = playerSide;
        this.gameField = new GameField();
    }
}
