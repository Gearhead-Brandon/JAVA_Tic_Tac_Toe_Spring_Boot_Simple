package game.tictactoe.web.mapper;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.web.model.GameFieldDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameFieldMapperTest {

    private final GameFieldMapper gameFieldMapper = new GameFieldMapperImpl();

    @Test
    void testToDTO() {
        // Arrange
        GameField gameField = new GameField();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameField.setCell(i, j, CellType.X);

        // Act
        GameFieldDTO gameFieldDTO = gameFieldMapper.toDTO(gameField);

        // Assert
        assertThat(gameFieldDTO).isNotNull();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                assertThat(gameFieldDTO.getCell(i, j)).isEqualTo(CellType.X.getValue());
    }

    @Test
    void testToEntity() {
        // Arrange
        GameFieldDTO gameFieldDTO = new GameFieldDTO();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameFieldDTO.setCell(i, j, CellType.O.getValue());

        // Act
        GameField gameField = gameFieldMapper.toEntity(gameFieldDTO);

        // Assert
        assertThat(gameField).isNotNull();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                assertThat(gameField.getCell(i, j)).isEqualTo(CellType.O);
    }
}
