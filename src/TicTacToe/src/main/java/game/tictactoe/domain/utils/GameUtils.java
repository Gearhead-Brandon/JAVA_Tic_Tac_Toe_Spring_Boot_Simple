package game.tictactoe.domain.utils;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.domain.service.WinState;

public class GameUtils {

    private GameUtils(){}

    /**
     * Checks if the game is over and determines the winner or draw.
     *
     * <p> This method analyzes the game field to determine if a player has won,
     * the game is a draw, or the game is still ongoing.
     *
     * @param field The current state of the game field.
     * @return The current win state of the game:
     *         <ul>
     *             <li>{@link WinState#X_WON}: Player X has won.</li>
     *             <li>{@link WinState#O_WON}: Player O has won.</li>
     *             <li>{@link WinState#DRAW}: The game is a draw.</li>
     *             <li>{@link WinState#CONTINUE}: The game is still ongoing.</li>
     *         </ul>
     */
    public static WinState isGameOver(GameField field) {
        // Check rows and columns
        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            if (checkRow(field, i))
                return map(field.getCell(i, 0));

            if (checkColumn(field, i))
                return map(field.getCell(0, i));
        }

        // Check diagonals
        final WinState diagonalState = checkDiagonal(field);
        if(diagonalState != WinState.CONTINUE)
            return diagonalState;

        // Check if field is full
        if(isFieldFull(field))
            return WinState.DRAW;

        return WinState.CONTINUE;
    }

    /**
     * Checks if a row contains three identical non-empty cells.
     *
     * @param field The game field.
     * @param row The row index to check.
     * @return true if the row contains three identical non-empty cells, false otherwise.
     */
    private static boolean checkRow(GameField field, int row) {
        CellType cell = field.getCell(row, 0);
        return cell != CellType.EMPTY &&
                cell == field.getCell(row, 1) &&
                cell == field.getCell(row, 2);
    }

    /**
     * Checks if a column contains three identical non-empty cells.
     *
     * @param field The game field.
     * @param col The column index to check.
     * @return true if the column contains three identical non-empty cells, false otherwise.
     */
    private static boolean checkColumn(GameField field, int col) {
        CellType cell = field.getCell(0, col);
        return cell != CellType.EMPTY &&
                cell == field.getCell(1, col) &&
                cell == field.getCell(2, col);
    }

    /**
     * Checks if either diagonal contains three identical non-empty cells.
     *
     * @param field The game field.
     * @return The win state if a diagonal win is found, otherwise {@link WinState#CONTINUE}.
     */
    private static WinState checkDiagonal(GameField field) {
        if (field.getCell(0, 0) == field.getCell(1, 1) && field.getCell(1, 1) == field.getCell(2, 2)
                && field.getCell(0, 0) != CellType.EMPTY) {
            return map(field.getCell(0, 0));
        }

        if (field.getCell(0, 2) == field.getCell(1, 1) && field.getCell(1, 1) == field.getCell(2, 0)
                && field.getCell(0, 2) != CellType.EMPTY) {
            return map(field.getCell(0, 2));
        }

        return WinState.CONTINUE;
    }

    /**
     * Checks if all cells on the game field are filled.
     *
     * @param field The game field.
     * @return true if the field is full, false otherwise.
     */
    private static boolean isFieldFull(GameField field) {
        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            for (int j = 0; j < GameAttribute.COLS.getValue(); j++) {
                if (field.getCell(i, j) == CellType.EMPTY)
                    return false;
            }
        }
        return true;
    }

    /**
     * Maps a {@link CellType} to its corresponding {@link WinState}.
     *
     * @param cellType The {@link CellType} to map.
     * @return The corresponding {@link WinState}.
     */
    private static WinState map(CellType cellType){
        return cellType == CellType.X ? WinState.X_WON : WinState.O_WON;
    }
}
