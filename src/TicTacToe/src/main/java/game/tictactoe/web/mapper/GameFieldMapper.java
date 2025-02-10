package game.tictactoe.web.mapper;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.web.model.GameFieldDTO;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;

/**
 * Mapper class that provides the conversion between {@link GameField} and {@link GameFieldDTO} objects.
 *
 * <p> Uses the MapStruct library to automatically generate the conversion code.
 * The {@link Mapper} annotation specifies that this class is a mapper, and the componentModel="spring" parameter
 * tells MapStruct to create a Spring bean for this mapper.
 */
@Mapper(componentModel = "spring")
public interface GameFieldMapper {

    /**
     * Maps a {@link GameField} entity to a {@link GameFieldDTO}.
     *
     * @param gameField The {@link GameField} entity to be mapped.
     * @return The corresponding {@link GameFieldDTO} object.
     */
    default GameFieldDTO toDTO(@NotNull final GameField gameField){
        GameFieldDTO gameFieldDTO = new GameFieldDTO();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameFieldDTO.setCell(i, j, gameField.getCell(i, j).getValue());

        return gameFieldDTO;
    }

    /**
     * Maps a {@link GameFieldDTO} to a {@link GameField} entity.
     *
     * @param gameFieldDTO The {@link GameFieldDTO} to be mapped.
     * @return The corresponding {@link GameField} entity.
     */
    default GameField toEntity(@NotNull final GameFieldDTO gameFieldDTO){
        GameField gameField = new GameField();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameField.setCell(i, j, CellType.valueOf(gameFieldDTO.getCell(i, j)));

        return gameField;
    }
}
