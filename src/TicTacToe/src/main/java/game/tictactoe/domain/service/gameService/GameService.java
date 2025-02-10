package game.tictactoe.domain.service.gameService;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.Game;
import game.tictactoe.domain.model.GameCreationResult;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.model.MoveResult;

import java.util.UUID;

/**
 * This interface defines the core operations for managing a game.
 *
 * <p> It provides methods for creating new games, validating game states,
 * processing player moves, and checking for win conditions.
 */
public interface GameService {
    /**
     * Creates a new game, assigns the specified player side,
     * and makes the initial move for the game.
     *
     * @param playerSide The player's side (X or O).
     * @param gameField The initial state of the game field.
     * @return A {@link GameCreationResult} object containing the game's {@link UUID} and the result of the initial move.
     */
    GameCreationResult createGameAndMakeFirstMove(final CellType playerSide, final GameField gameField);

    /**
     * Checks if the given game board matches the previous game state.
     *
     * @param uuid The UUID of the current game.
     * @param field The new field to check.
     * @return the Game object if the field is valid.
     */
    Game validateGameField(final UUID uuid, final GameField field);

    /**
     * Processes the next move in the game.
     *
     * <p> This method should update the game state based on the player's move.
     *
     * @param game The current state of the game.
     */
    void nextMove(final Game game);

    /**
     * Checks for a win condition in the game.
     *
     * @param game The current state of the game.
     * @return A {@link MoveResult} object indicating the outcome of the game:
     */
    MoveResult checkWin(final Game game);
}
