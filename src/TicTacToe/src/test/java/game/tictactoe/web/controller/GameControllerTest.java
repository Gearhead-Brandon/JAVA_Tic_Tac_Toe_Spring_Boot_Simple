package game.tictactoe.web.controller;

import game.tictactoe.domain.model.*;
import game.tictactoe.domain.service.gameService.GameService;
import game.tictactoe.web.mapper.GameFieldMapper;
import game.tictactoe.web.mapper.MoveResultMapper;
import game.tictactoe.web.model.GameCreationRequest;
import game.tictactoe.web.model.GameFieldDTO;
import game.tictactoe.web.model.MoveResultDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @Mock
    private GameFieldMapper gameFieldMapper;

    @Mock
    private MoveResultMapper moveResultMapper;

    @Test
    void testCreateGameAndMakeFirstMove() {
        // Arrange
        GameCreationRequest request = new GameCreationRequest('O');

        GameField mappedGameField = new GameField();
        when(gameFieldMapper.toEntity(any(GameFieldDTO.class)))
                .thenReturn(mappedGameField);

        MoveResult moveResult = new MoveResult("", mappedGameField);
        moveResult.gameField().setCell(1, 1, CellType.X);

        UUID uuid = UUID.randomUUID();
        GameCreationResult gcr = new GameCreationResult(uuid, moveResult);
        when(gameService.createGameAndMakeFirstMove(
                CellType.valueOf(request.getPlayerSide()),
                mappedGameField
        )).thenReturn(gcr);

        GameFieldDTO responseGameFieldDTO = new GameFieldDTO();
        responseGameFieldDTO.setCell(1, 1, CellType.X.getValue());
        MoveResultDto responseGameField = new MoveResultDto(
                "",
                responseGameFieldDTO
        );

        when(moveResultMapper.toDTO(gcr.moveResult()))
                .thenReturn(responseGameField);

        MoveResultDto expectedMoveResult = new MoveResultDto(
                "",
                new GameFieldDTO(List.of(
                    List.of(' ', ' ', ' '),
                    List.of(' ', 'X', ' '),
                    List.of(' ', ' ', ' ')
                ))
        );

        // Act
        var responseEntity = gameController.createGameAndMakeFirstMove(request);

        // Assert
        assertThat(responseEntity)
                .isNotNull();

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getHeaders().getLocation())
                .isEqualTo(UriComponentsBuilder.fromUriString("/api/v1/game/{uuid}")
                        .buildAndExpand(uuid)
                        .toUri()
                );

        assertThat(responseEntity.getHeaders().getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(expectedMoveResult);

        verify(gameFieldMapper, times(1)).toEntity(any(GameFieldDTO.class));
        verify(gameService, times(1)).createGameAndMakeFirstMove(CellType.valueOf(request.getPlayerSide()), mappedGameField);
        verify(moveResultMapper, times(1)).toDTO(gcr.moveResult());
    }

    @Test
    void testUpdateGame() {
        // Arrange
        UUID requestUuid = UUID.randomUUID();
        GameFieldDTO requestGameFieldDto = new GameFieldDTO(List.of(
                List.of(' ', ' ', ' '),
                List.of(' ', 'X', ' '),
                List.of(' ', ' ', ' ')
        ));

        GameField gameField = new GameField();
        gameField.setCell(1, 1, CellType.X);
        when(gameFieldMapper.toEntity(requestGameFieldDto))
                .thenReturn(gameField);

        Game game = new Game(requestUuid, CellType.X, gameField);
        when(gameService.validateGameField(requestUuid, gameField))
                .thenReturn(game);

        gameField.setCell(0, 0, CellType.O);
        MoveResult moveResult = new MoveResult("CONTINUE", gameField);

        when(gameService.checkWin(game))
                .thenReturn(moveResult);

        MoveResultDto moveResultDto = new MoveResultDto("CONTINUE", new GameFieldDTO(List.of(
                List.of('O', ' ', ' '),
                List.of(' ', 'X', ' '),
                List.of(' ', ' ', ' ')
        )));

        when(moveResultMapper.toDTO(moveResult))
                .thenReturn(moveResultDto);

        // Act
        var responseEntity = gameController.updateGame(requestUuid, requestGameFieldDto);

        // Assert
        assertThat(responseEntity)
                .isNotNull();

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getHeaders().getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(moveResultDto);

        verify(gameFieldMapper, times(1)).toEntity(requestGameFieldDto);
        verify(gameService, times(1)).validateGameField(requestUuid, gameField);
        verify(gameService, times(1)).checkWin(game);
        verify(moveResultMapper, times(1)).toDTO(moveResult);
    }
}
