package game.tictactoe.domain.service.minimax.impl;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.model.Position;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.domain.service.WinState;
import game.tictactoe.domain.service.minimax.MinimaxService;
import game.tictactoe.domain.utils.GameUtils;

public class MinimaxServiceImpl implements MinimaxService {

    /**
     * Maximum depth of the search tree for the MinimaxService algorithm.
     */
    private static final int DEPTH = 9;

    @Override
    public Position findBestMove(GameField field, CellType side) {
        int bestVal = Integer.MIN_VALUE;
        int row = Position.NOT_VALID_POS;
        int col = Position.NOT_VALID_POS;

        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            for (int j = 0; j < GameAttribute.COLS.getValue(); j++) {
                if (field.getCell(i, j) == CellType.EMPTY) {
                    field.setCell(i, j, side);
                    int moveVal = minimax(field, DEPTH - 1, false, side, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    field.setCell(i, j, CellType.EMPTY);

                    if (moveVal > bestVal) {
                        row = i;
                        col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return new Position(row, col);
    }

    /**
     * Returns the opponent's side in the game.
     *
     * @param side The current player's side.
     * @return The opponent's side.
     */
    private CellType getOpponentSide(CellType side) {
        return side == CellType.X ? CellType.O : CellType.X;
    }

    /**
     * Makes a move on the game field by setting the specified cell to the given player's side.
     *
     * @param field The game field.
     * @param i The row index of the cell.
     * @param j The column index of the cell.
     * @param playerSide The player's side.
     */
    private void makeMove(GameField field, int i, int j, CellType playerSide) {
        field.setCell(i, j, playerSide);
    }

    /**
     * Core MinimaxService algorithm implementation with alpha-beta pruning.
     *
     * @param field The current state of the game field.
     * @param depth The current depth of the search.
     * @param isMaximizing Whether the current player is maximizing or minimizing.
     * @param playerSide The current player's side.
     * @param alpha The current alpha value for alpha-beta pruning.
     * @param beta The current beta value for alpha-beta pruning.
     * @return The score of the current game state.
     */
    private int minimax(GameField field, int depth, boolean isMaximizing, CellType playerSide, int alpha, int beta) {
        final WinState winState = GameUtils.isGameOver(field);
        if (winState != WinState.CONTINUE)
            return evaluate(winState, playerSide, depth);

        if (isMaximizing) {
            return maximize(field, depth, playerSide, alpha, beta);
        } else {
            return minimize(field, depth, playerSide, alpha, beta);
        }
    }

    /**
     * Maximizing player's turn in the MinimaxService algorithm.
     *
     * @param field The current state of the game field.
     * @param depth The current depth of the search.
     * @param playerSide The current player's side.
     * @param alpha The current alpha value for alpha-beta pruning.
     * @param beta The current beta value for alpha-beta pruning.
     * @return The best score for the maximizing player.
     */
    private int maximize(GameField field, int depth, CellType playerSide, int alpha, int beta) {
        int bestValue = Integer.MIN_VALUE;

        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            for (int j = 0; j < GameAttribute.COLS.getValue(); j++) {
                if (field.getCell(i, j) == CellType.EMPTY) {
                    makeMove(field, i, j, playerSide);
                    int eval = minimax(field, depth - 1, false, playerSide, alpha, beta);
                    field.setCell(i, j, CellType.EMPTY);

                    bestValue = Math.max(bestValue, eval);
                    alpha = Math.max(alpha, bestValue);

                    if (beta <= alpha) {
                        break; // Alpha-beta pruning
                    }
                }
            }
        }

        return bestValue;
    }

    /**
     * Minimizing player's turn in the MinimaxService algorithm.
     *
     * @param field The current state of the game field.
     * @param depth The current depth of the search.
     * @param playerSide The current player's side.
     * @param alpha The current alpha value for alpha-beta pruning.
     * @param beta The current beta value for alpha-beta pruning.
     * @return The best score for the minimizing player.
     */
    private int minimize(GameField field, int depth, CellType playerSide, int alpha, int beta) {
        int bestValue = Integer.MAX_VALUE;

        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            for (int j = 0; j < GameAttribute.COLS.getValue(); j++) {
                if (field.getCell(i, j) == CellType.EMPTY) {
                    makeMove(field, i, j, getOpponentSide(playerSide));
                    int eval = minimax(field, depth - 1, true, playerSide, alpha, beta);
                    field.setCell(i, j, CellType.EMPTY);

                    bestValue = Math.min(bestValue, eval);
                    beta = Math.min(beta, bestValue);

                    if (beta <= alpha) {
                        break; // Alpha-beta pruning
                    }
                }
            }
        }

        return bestValue;
    }

    /**
     * Evaluates the score of a given game state.
     *
     * @param state The current {@link WinState} of the game.
     * @param side The current player's side.
     * @param depth The current depth of the search in the MinimaxService algorithm.
     * @return The score of the game state:
     *         <ul>
     *             <li>Positive score if the current player wins.</li>
     *             <li>Negative score if the opponent wins.</li>
     *             <li>0 if the game is still ongoing.</li>
     *         </ul>
     */
    private int evaluate(WinState state, CellType side, int depth) {
        final WinState ourSide = (side == CellType.X) ? WinState.X_WON : WinState.O_WON;
        final WinState theirSide = (getOpponentSide(side) == CellType.X) ? WinState.X_WON : WinState.O_WON;

        if(state == ourSide)
            return 10 - depth;
        else if(state == theirSide)
            return depth - 10;
        return 0;
    }
}
