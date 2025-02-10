package game.tictactoe.datasource.mapper;

import game.tictactoe.datasource.model.GameRepositoryModel;
import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.Game;
import game.tictactoe.domain.model.GameField;
import game.tictactoe.domain.service.GameAttribute;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameMapperTest {

        private final GameMapper gameMapper = new GameMapperImpl();

        @Test
        void testToDTO() {
                // Arrange
                final UUID uuid = UUID.randomUUID();
                final CellType playerSide = CellType.X;

                final Game game = new Game(uuid, playerSide);

                final GameField gameField = game.getGameField();

                for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
                        for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                                gameField.setCell(i, j, CellType.O);

                // Act
                GameRepositoryModel gameRepositoryModel = gameMapper.toModel(game);

                // Assert
                assertThat(gameRepositoryModel)
                        .isNotNull();

                assertThat(gameRepositoryModel.getUuid())
                        .isEqualTo(uuid);

                assertThat(gameRepositoryModel.getPlayerSide())
                        .isEqualTo(playerSide.getValue());

                assertThat(gameRepositoryModel.getGameField())
                        .isNotNull();

                List<List<Character>> gameFieldList = gameRepositoryModel.getGameField();

                for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
                        for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                                assertThat(gameFieldList.get(i).get(j))
                                        .isEqualTo(CellType.O.getValue());
        }

        @Test
        void testToEntity() {
                // Arrange
                final UUID uuid = UUID.randomUUID();
                final CellType playerSide = CellType.X;

                final GameRepositoryModel gameRepositoryModel = new GameRepositoryModel(uuid, playerSide.getValue());

                final List<List<Character>> gameFieldList = gameRepositoryModel.getGameField();

                for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
                        for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                                gameFieldList.get(i).set(j, CellType.X.getValue());

                // Act
                final Game game = gameMapper.toEntity(gameRepositoryModel);

                // Assert
                assertThat(game)
                        .isNotNull();

                assertThat(game.getUuid())
                        .isEqualTo(uuid);

                assertThat(game.getPlayerSide())
                        .isEqualTo(playerSide);

                assertThat(game.getGameField())
                        .isNotNull();

                GameField gameField = game.getGameField();

                for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
                        for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                                assertThat(gameField.getCell(i, j))
                                        .isEqualTo(CellType.X);
        }

        @Test
        void testNull() {
            // Act
            final GameRepositoryModel gameRepositoryModel = gameMapper.toModel(null);

            // Assert
            assertThat(gameRepositoryModel)
                    .isNull();

            // Act
            final Game game = gameMapper.toEntity(null);

            // Assert
            assertThat(game)
                    .isNull();
        }
}
