package game.tictactoe.datasource.repository.impl;

import game.tictactoe.datasource.mapper.GameMapper;
import game.tictactoe.datasource.model.GameRepositoryModel;
import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.Game;
import game.tictactoe.domain.service.GameAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameRepositoryImplTest {

    @Mock
    private GameMapper mapper;

    @InjectMocks
    private GameRepositoryImpl gameRepository;

    @BeforeEach
    void setUp() {
        gameRepository = new GameRepositoryImpl(new ConcurrentHashMap<>(), mapper);
    }

    @Test
    void shouldSaveGame() {
        // Arrange
        final UUID uuid = UUID.randomUUID();
        final CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide); // Assuming Game has a constructor

        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for (int j = 0; j < GameAttribute.COLS.getValue(); j++)
                game.getGameField().setCell(i, j, CellType.O);

        final GameRepositoryModel gameModel = new GameRepositoryModel(uuid, playerSide.getValue());

        for (int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for (int j = 0; j < GameAttribute.COLS.getValue(); j++)
                gameModel.setCell(i, j, CellType.O.getValue());

        when(mapper.toModel(game))
                .thenReturn(gameModel);

        when(mapper.toEntity(gameModel))
                .thenReturn(game);

        // Act
        gameRepository.save(game);

        // Assert
        Optional<Game> savedGameOptional = gameRepository.findByUuid(uuid);

        assertThat(savedGameOptional)
                .isPresent()
                .hasValue(game);

        verify(mapper, times(1)).toModel(game);
        verify(mapper, times(1)).toEntity(gameModel);
    }

    @Test
    void shouldNotSaveNullGame() {
        // Act
        Game savedGame = gameRepository.save(null);

        // Assert
        assertThat(savedGame)
                .isNull();
    }

    @Test
    void shouldDeleteGameByUuid() {
        // Arrange
        final UUID uuid = UUID.randomUUID();
        final CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide);

        GameRepositoryModel gameModel = new GameRepositoryModel(uuid, playerSide.getValue());

        when(mapper.toModel(game))
                .thenReturn(gameModel);

        when(mapper.toEntity(gameModel))
                .thenReturn(game);

        gameRepository.save(game);

        // Act
        Optional<Game> deletedGame = gameRepository.deleteByUuid(uuid);

        // Assert
        assertThat(deletedGame)
                .isPresent()
                .hasValue(game);

        Optional<Game> remainingGameOptional = gameRepository.findByUuid(uuid);
        assertThat(remainingGameOptional)
                .isEmpty();

        verify(mapper, times(1)).toModel(game);
        verify(mapper, times(1)).toEntity(gameModel);
    }

    @Test
    void shouldNotDeleteGameByNonexistentUuid() {
        // Arrange
        final UUID uuid = UUID.randomUUID();

        // Act
        Optional<Game> deletedGame = gameRepository.deleteByUuid(uuid);

        // Assert
        assertThat(deletedGame)
                .isEmpty();
    }

    @Test
    void shouldNotDeleteGameByNullUuid() {
        // Act
        Optional<Game> deletedGame = gameRepository.deleteByUuid(null);

        // Assert
        assertThat(deletedGame)
                .isEmpty();
    }

    @Test
    void shouldFindGameByUuid() {
        // Arrange
        final UUID uuid = UUID.randomUUID();
        final CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide);

        GameRepositoryModel gameModel = new GameRepositoryModel(uuid, playerSide.getValue());

        when(mapper.toModel(game))
                .thenReturn(gameModel);

        when(mapper.toEntity(gameModel))
                .thenReturn(game);

        gameRepository.save(game);

        // Act
        Optional<Game> foundGameOptional = gameRepository.findByUuid(uuid);

        // Assert
        assertThat(foundGameOptional)
                .isPresent()
                .hasValue(game);

        verify(mapper, times(1)).toModel(game);
        verify(mapper, times(1)).toEntity(gameModel);
    }
}
