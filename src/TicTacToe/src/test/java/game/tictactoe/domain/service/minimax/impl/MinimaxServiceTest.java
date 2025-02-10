package game.tictactoe.domain.service.minimax.impl;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.model.Position;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.domain.service.minimax.MinimaxService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MinimaxServiceTest {

    private final MinimaxService minimaxService = new MinimaxServiceImpl();

    @Test
    void testFindBestMoveO() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);

        // Act
        Position bestMove = minimaxService.findBestMove(field, CellType.O);

        // Assert
        assertThat(bestMove.row()).isNotEqualTo(Position.NOT_VALID_POS);
        assertThat(bestMove.col()).isNotEqualTo(Position.NOT_VALID_POS);

        assertThat(bestMove.row()).isNotZero();
        assertThat(bestMove.col()).isNotZero();

        assertThat(bestMove.row())
                .isNotNegative()
                .isLessThan(GameAttribute.ROWS.getValue());

        assertThat(bestMove.col())
                .isNotNegative()
                .isLessThan(GameAttribute.COLS.getValue());
    }

    @Test
    void testFindBestMoveXFirstMove() {
        // Arrange
        GameField field = new GameField();

        // Act
        Position bestMove = minimaxService.findBestMove(field, CellType.X);

        // Assert
        assertThat(bestMove.row()).isZero();
        assertThat(bestMove.col()).isZero();
    }

    @Test
    void testFindBestMoveDraw() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.O);
        field.setCell(0, 2, CellType.X);

        field.setCell(1, 0, CellType.X);
        field.setCell(1, 1, CellType.O);
        field.setCell(1, 2, CellType.X);

        field.setCell(2, 0, CellType.O);
        field.setCell(2, 1, CellType.X);
        field.setCell(2, 2, CellType.O);

        // Act
        Position bestMove = minimaxService.findBestMove(field, CellType.O);

        // Assert
        assertThat(bestMove.row()).isEqualTo(Position.NOT_VALID_POS);
        assertThat(bestMove.col()).isEqualTo(Position.NOT_VALID_POS);
    }
}
