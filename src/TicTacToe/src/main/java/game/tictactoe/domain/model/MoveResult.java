package game.tictactoe.domain.model;

/**
 * Represents the result of a move in the game.
 *
 * <p> This class encapsulates the status of a move and the updated game field.
 *
 * @param status The textual representation of the move status.
 * @param gameField The updated game field after the move.
 */
public record MoveResult(String status, GameField gameField) {}
