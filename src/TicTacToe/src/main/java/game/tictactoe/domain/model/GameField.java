package game.tictactoe.domain.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the game field for a Tic-Tac-Toe game.
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class GameField {
    /**
     * Internal representation of the game field as a 2D list of {@link CellType} objects.
     */
    private final List<List<CellType>> fieldMatrix = List.of(
            Arrays.asList(CellType.EMPTY, CellType.EMPTY, CellType.EMPTY),
            Arrays.asList(CellType.EMPTY, CellType.EMPTY, CellType.EMPTY),
            Arrays.asList(CellType.EMPTY, CellType.EMPTY, CellType.EMPTY)
    );

    public CellType getCell(int row, int col) { return fieldMatrix.get(row).get(col); }

    public void setCell(int row, int col, CellType cellType) { fieldMatrix.get(row).set(col, cellType);}
}
