package game.tictactoe.domain.service.minimax;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.model.Position;

public interface MinimaxService {

    /**
     * Finds the best possible move for the given player.
     *
     * @param field The current state of the game field.
     * @param side The player's side.
     * @return The best move as a Position object.
     */
    Position findBestMove(GameField field, CellType side);
}
