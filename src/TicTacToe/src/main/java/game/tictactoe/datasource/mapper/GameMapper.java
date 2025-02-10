package game.tictactoe.datasource.mapper;

import game.tictactoe.datasource.model.GameRepositoryModel;
import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.Game;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.service.GameAttribute;
import org.mapstruct.Mapper;

/**
 * Mapper class that provides the conversion between {@link Game} and {@link GameRepositoryModel} objects.
 *
 * <p> Uses the MapStruct library to automatically generate the conversion code.
 * The {@link Mapper} annotation specifies that this class is a mapper, and the componentModel="spring" parameter
 * tells MapStruct to create a Spring bean for this mapper.
 */
@Mapper
public interface GameMapper {

    /**
     * Maps a {@link Game} entity to a {@link GameRepositoryModel}.
     *
     * @param game The {@link Game} entity to be mapped.
     * @return The corresponding {@link GameRepositoryModel}.
     */
    default GameRepositoryModel toModel(final Game game){
        if(game == null) return null;

        final GameRepositoryModel gameModel = new GameRepositoryModel(
                game.getUuid(),
                game.getPlayerSide().getValue()
        );

        final GameField gameField = game.getGameField();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameModel.setCell(i, j, gameField.getCell(i, j).getValue());

        return gameModel;
    }

    /**
     * Maps a {@link GameRepositoryModel} to a {@link Game} entity.
     *
     * @param gameModel The {@link GameRepositoryModel} to be mapped.
     * @return The corresponding {@link Game} entity.
     */
    default Game toEntity(final GameRepositoryModel gameModel){
        if(gameModel == null) return null;

        final Game game = new Game(
                gameModel.getUuid(),
                CellType.valueOf(gameModel.getPlayerSide())
        );

        final GameField gameField = game.getGameField();

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameField.setCell(i, j, CellType.valueOf(gameModel.getCell(i, j)));

        return game;
    }
}
