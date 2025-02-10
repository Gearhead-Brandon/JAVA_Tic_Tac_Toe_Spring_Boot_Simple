package game.tictactoe.domain.utils;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.service.WinState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameUtilsTest {

    @Test
    void testIsGameOver_WinsRow() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.X);
        field.setCell(0, 2, CellType.X);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.X_WON);
    }

    @Test
    void testIsGameOver_AlmostWinsRow() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.X);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.CONTINUE);
    }

    @Test
    void testIsGameOver_AlmostWinsColumn() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(1, 0, CellType.X);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.CONTINUE);
    }

    @Test
    void testIsGameOver_OWinsColumn() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.O);
        field.setCell(1, 0, CellType.O);
        field.setCell(2, 0, CellType.O);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.O_WON);
    }

    @Test
    void testIsGameOver_XWinsDiagonal() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(1, 1, CellType.X);
        field.setCell(2, 2, CellType.X);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.X_WON);
    }

    @Test
    void testEmptyField_LeftDiagonal() {
        // Arrange
        GameField field = new GameField();

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.CONTINUE);
    }

    @Test
    void testIsGameOver_OWinsDiagonal() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 2, CellType.O);
        field.setCell(1, 1, CellType.O);
        field.setCell(2, 0, CellType.O);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.O_WON);
    }

    @Test
    void testIsGameOver_AlmostOWinsDiagonal() {
        // Arrange
        GameField field = new GameField();
        field.setCell(2, 0, CellType.O);
        field.setCell(1, 1, CellType.O);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.CONTINUE);
    }

    @Test
    void testIsGameOver_Draw() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.O);
        field.setCell(0, 2, CellType.X);
        field.setCell(1, 0, CellType.O);
        field.setCell(1, 1, CellType.X);
        field.setCell(1, 2, CellType.O);
        field.setCell(2, 0, CellType.O);
        field.setCell(2, 1, CellType.X);
        field.setCell(2, 2, CellType.O);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.DRAW);
    }

    @Test
    void testIsGameOver_Continue() {
        // Arrange
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.O);

        // Act
        WinState result = GameUtils.isGameOver(field);

        // Assert
        assertThat(result)
                .isEqualTo(WinState.CONTINUE);
    }
}
