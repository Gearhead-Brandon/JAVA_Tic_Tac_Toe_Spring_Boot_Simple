package game.tictactoe.domain.service.gameService.impl;

import game.tictactoe.datasource.repository.GameRepository;
import game.tictactoe.domain.model.*;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.domain.service.WinState;
import game.tictactoe.domain.service.minimax.MinimaxService;
import game.tictactoe.exception.InvalidRequestBodyException;
import game.tictactoe.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private MinimaxService minimaxService;

    @Mock
    private GameRepository gameRepository;

    @Test
    void testCreateGameAndMakeFirstMoveX() {
        // Arrange
        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);

        when(gameRepository.save(any(Game.class))).thenReturn(null);

        when(minimaxService.findBestMove(any(GameField.class), eq(CellType.O)))
                .thenReturn(new Position(1, 1));

        GameField expectedGameField = new GameField();
        expectedGameField.setCell(0, 0, CellType.X);
        expectedGameField.setCell(1, 1, CellType.O);

        // Act
        GameCreationResult result = gameService.createGameAndMakeFirstMove(CellType.X, gameField);

        // Assert
        assertThat(result.uuid())
                .isNotNull();
        assertThat(result.moveResult().status())
                .isEmpty();

        assertThat(result.moveResult().gameField())
                .isEqualTo(expectedGameField);

        verify(gameRepository, times(1))
                .save(any(Game.class));
        verify(minimaxService, times(1))
                .findBestMove(any(GameField.class), eq(CellType.O));
    }

    @Test
    void testCreateGameAndMakeFirstMoveO() {
        // Arrange
        GameField gameField = new GameField();

        when(gameRepository.save(any(Game.class))).thenReturn(null);

        final int EXPECTED_COUNT_OF_X = 1;

        // Act
        GameCreationResult result = gameService.createGameAndMakeFirstMove(CellType.O, gameField);

        // Assert
        assertThat(result.uuid())
                .isNotNull();
        assertThat(result.moveResult().status())
                .isEmpty();

        final GameField gameFieldResult = result.moveResult().gameField();

        int countOfX = 0;

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++)
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++)
                if(gameFieldResult.getCell(i, j) == CellType.X)
                    countOfX++;

        assertThat(countOfX)
                .isEqualTo(EXPECTED_COUNT_OF_X);

        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void testCreateGameAndMakeFirstMove_EmptyPlayerSide() {
        // Assert
        assertThatThrownBy(() -> gameService.createGameAndMakeFirstMove(CellType.EMPTY, null))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("Invalid player side, it should be X or O");
    }

    @Test
    void testCreateGameAndMakeFirstMove_InvalidGameField() {
        // Arrange
        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.O);

        // Assert
        assertThatThrownBy(() -> gameService.createGameAndMakeFirstMove(CellType.X, gameField))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("Invalid game field, it should be matrix with 3 rows and 3 columns of X or O or empty cells");

        // Arrange
        GameField gameField2 = new GameField();
        gameField2.setCell(0, 0, CellType.X);

        // Assert
        assertThatThrownBy(() -> gameService.createGameAndMakeFirstMove(CellType.O, gameField2))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("Invalid game field, it should be matrix with 3 rows and 3 columns of X or O or empty cells");

        // Arrange
        GameField gameField3 = new GameField();
        gameField3.setCell(0, 0, CellType.O);

        // Assert
        assertThatThrownBy(() -> gameService.createGameAndMakeFirstMove(CellType.O, gameField3))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("With player O the field must be empty");


        // Arrange
        GameField gameField4 = new GameField();

        // Assert
        assertThatThrownBy(() -> gameService.createGameAndMakeFirstMove(CellType.X, gameField4))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("With player X the field must have one X");
    }

    @Test
    void testValidateGameField() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);
        gameField.setCell(0, 1, CellType.O);
        gameField.setCell(0, 2, CellType.X);

        when(gameRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(game));

        Game expectedGame = new Game(uuid, playerSide);
        expectedGame.getGameField().setCell(0, 0, CellType.X);
        expectedGame.getGameField().setCell(0, 1, CellType.O);
        expectedGame.getGameField().setCell(0, 2, CellType.X);

        // Act
        Game result = gameService.validateGameField(uuid, gameField);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedGame);

        verify(gameRepository, times(1)).findByUuid(any(UUID.class));
    }

    @Test
    void testValidateGameField_InvalidChangedGameField() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);
        gameField.setCell(0, 1, CellType.O);
        gameField.setCell(0, 2, CellType.O);

        when(gameRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(game));

        // Assert & Act
        assertThatThrownBy(() -> gameService.validateGameField(uuid, gameField))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("Invalid game field");

        verify(gameRepository, times(1)).findByUuid(any(UUID.class));
    }

    @Test
    void testValidateGameField_ZeroChangedGameField() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);
        gameField.setCell(0, 1, CellType.O);

        when(gameRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(game));

        // Assert & Act
        assertThatThrownBy(() -> gameService.validateGameField(uuid, gameField))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("Invalid game field");

        verify(gameRepository, times(1)).findByUuid(any(UUID.class));
    }

    @Test
    void testValidateGameField_TooManyInvalidChanges() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        CellType playerSide = CellType.X;
        Game game = new Game(uuid, playerSide);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);
        gameField.setCell(0, 1, CellType.O);
        gameField.setCell(0, 2, CellType.X);
        gameField.setCell(1, 0, CellType.X);

        when(gameRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(game));

        // Assert & Act
        assertThatThrownBy(() -> gameService.validateGameField(uuid, gameField))
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessage("Invalid game field");

        verify(gameRepository, times(1)).findByUuid(any(UUID.class));
    }

    @Test
    void testValidateGameField_NotExistGame() {
        // Arrange
        when(gameRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Throwable exception = catchThrowable(() -> gameService.validateGameField(UUID.randomUUID(), null));

        // Assert
        assertThat(exception)
                .as("Game not found")
                .isInstanceOf(ResourceNotFoundException.class);

        verify(gameRepository, times(1)).findByUuid(any(UUID.class));
    }

    @Test
    void testNextMove() {
        // Arrange
        Game game = new Game(UUID.randomUUID(), CellType.O);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        when(gameRepository.save(any(Game.class))).thenReturn(null);

        when(minimaxService.findBestMove(any(GameField.class), eq(CellType.X)))
                .thenReturn(new Position(1, 1));

        // Act
        gameService.nextMove(game);

        // Assert
        assertThat(game.getGameField().getCell(1, 1))
                .isEqualTo(CellType.X);

        verify(gameRepository, times(1)).save(any(Game.class));
        verify(minimaxService, times(1)).findBestMove(any(GameField.class), eq(CellType.X));
    }

    @Test
    void testNextMove_InvalidNewPosition() {
        // Arrange
        Game game = new Game(UUID.randomUUID(), CellType.O);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        when(gameRepository.save(any(Game.class))).thenReturn(null);

        when(minimaxService.findBestMove(any(GameField.class), eq(CellType.X)))
                .thenReturn(new Position(-1, -1));

        // Act
        gameService.nextMove(game);

        // Assert
        assertThat(game.getGameField().getCell(1, 1))
                .isEqualTo(CellType.EMPTY);

        verify(gameRepository, times(1)).save(any(Game.class));
        verify(minimaxService, times(1)).findBestMove(any(GameField.class), eq(CellType.X));
    }

    @Test
    void testNextMove_HalfInvalidNewPosition() {
        // Arrange
        Game game = new Game(UUID.randomUUID(), CellType.O);
        game.getGameField().setCell(0, 0, CellType.X);
        game.getGameField().setCell(0, 1, CellType.O);

        when(gameRepository.save(any(Game.class))).thenReturn(null);

//        when(minimaxService.findBestMove(any(GameField.class), eq(CellType.X)))
//                .thenReturn(new Position(2, -1));

        doReturn(new Position(2, -1))
                .when(minimaxService)
                .findBestMove(any(GameField.class), eq(CellType.X));

        // Act
        gameService.nextMove(game);

        // Assert
        assertThat(game.getGameField().getCell(1, 1))
                .isEqualTo(CellType.EMPTY);

        verify(gameRepository, times(1)).save(any(Game.class));
        verify(minimaxService, times(1)).findBestMove(any(GameField.class), eq(CellType.X));
    }

    @Test
    void testCheckWin_NotWin(){
        // Arrange
        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);
        gameField.setCell(0, 1, CellType.O);
        gameField.setCell(0, 2, CellType.X);

        Game game = new Game(UUID.randomUUID(), CellType.X, gameField);

        // Act
        MoveResult moveResult = gameService.checkWin(game);

        // Assert
        assertThat(moveResult.status())
                .isEqualTo(WinState.CONTINUE.name());

        verify(gameRepository, never()).deleteByUuid(game.getUuid());
    }

    @Test
    void testCheckWin_XWin(){
        // Arrange
        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.X);
        gameField.setCell(0, 1, CellType.X);
        gameField.setCell(0, 2, CellType.X);

        Game game = new Game(UUID.randomUUID(), CellType.X, gameField);

        when(gameRepository.deleteByUuid(any(UUID.class))).thenReturn(null);

        // Act
        MoveResult moveResult = gameService.checkWin(game);

        // Assert
        assertThat(moveResult.status())
                .isEqualTo(WinState.X_WON.name());

        verify(gameRepository, times(1)).deleteByUuid(game.getUuid());
    }

    @Test
    void testCheckWin_OWin(){
        // Arrange
        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.O);
        gameField.setCell(0, 1, CellType.O);
        gameField.setCell(0, 2, CellType.O);

        Game game = new Game(UUID.randomUUID(), CellType.X, gameField);

        when(gameRepository.deleteByUuid(any(UUID.class))).thenReturn(null);

        // Act
        MoveResult moveResult = gameService.checkWin(game);

        // Assert
        assertThat(moveResult.status())
                .isEqualTo(WinState.O_WON.name());

        verify(gameRepository, times(1)).deleteByUuid(game.getUuid());
    }

    @Test
    void testCheckWin_DRAW(){
        // Arrange
        GameField gameField = new GameField();
        gameField.setCell(0, 0, CellType.O);
        gameField.setCell(0, 1, CellType.X);
        gameField.setCell(0, 2, CellType.O);

        gameField.setCell(1, 0, CellType.X);
        gameField.setCell(1, 1, CellType.O);
        gameField.setCell(1, 2, CellType.X);

        gameField.setCell(2, 0, CellType.X);
        gameField.setCell(2, 1, CellType.O);
        gameField.setCell(2, 2, CellType.X);

        Game game = new Game(UUID.randomUUID(), CellType.X, gameField);

        when(gameRepository.deleteByUuid(any(UUID.class))).thenReturn(null);

        // Act
        MoveResult moveResult = gameService.checkWin(game);

        // Assert
        assertThat(moveResult.status())
                .isEqualTo(WinState.DRAW.name());

        verify(gameRepository, times(1)).deleteByUuid(game.getUuid());
    }
}








