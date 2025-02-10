package game.tictactoe.web.controller;

import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.model.Game;
import game.tictactoe.domain.model.GameCreationResult;
import game.tictactoe.domain.service.gameService.GameService;
import game.tictactoe.exception.InvalidRequestBodyException;
import game.tictactoe.exception.ResourceNotFoundException;
import game.tictactoe.web.annotation.GameExceptionHandler;
import game.tictactoe.web.mapper.GameFieldMapper;
import game.tictactoe.web.mapper.MoveResultMapper;
import game.tictactoe.web.model.GameCreationRequest;
import game.tictactoe.web.model.GameFieldDTO;
import game.tictactoe.web.model.MoveResultDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * This class defines the REST API endpoints for Tic-Tac-Toe game management.
 * It handles requests for creating new games, making moves, and retrieving game state.
 */
@Slf4j
@Validated
@RestController
@GameExceptionHandler
@RequestMapping("/api")
@Tag(name = "TicTacToe")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final GameFieldMapper gameFieldMapper;
    private final MoveResultMapper moveResultMapper;

    /**
     * Creates a new game and makes the first move for the specified player side.
     *
     * @param request The request body containing game creation details.
     * @return A {@link ResponseEntity} containing the created game's location URI and the first move result.
     * @throws IllegalArgumentException if the playerSide or gameField is invalid.
     */
    @PostMapping(value = "/v1/game")
    public ResponseEntity<MoveResultDto> createGameAndMakeFirstMove(
            @Valid @RequestBody GameCreationRequest request
    ) {
        log.info("Request received: POST /api/v1/game, GameCreationRequest = {}", request);

        GameCreationResult gcr = gameService.createGameAndMakeFirstMove(
                CellType.valueOf(request.getPlayerSide()), gameFieldMapper.toEntity(request.getGameField())
        );

        URI location = UriComponentsBuilder.fromUriString("/api/v1/game/{uuid}")
                .buildAndExpand(gcr.uuid())
                .toUri();

        return ResponseEntity.created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .body(moveResultMapper.toDTO(gcr.moveResult()));
    }

    /**
     * Updates the game state by making a move on the provided game board.
     *
     * @param uuid The unique identifier of the game.
     * @param gameDTO The DTO containing the updated game board.
     * @return A ResponseEntity containing the updated game state after the move.
     * @throws ResourceNotFoundException if the game with the specified UUID is not found.
     * @throws InvalidRequestBodyException if the move is invalid.
     */
    @PutMapping(value = "/v1/game/{uuid}")
    public ResponseEntity<MoveResultDto> updateGame(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody GameFieldDTO gameDTO
    ) {
        log.info("Request received: PUT /api/v1/game/{uuid}, UUID = {}, NewGameField = {}", uuid, gameDTO);

        final Game game = gameService.validateGameField(
                uuid,
                gameFieldMapper.toEntity(gameDTO)
        );

        gameService.nextMove(game);

        log.info("Updated game: {}", game);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(moveResultMapper.toDTO(gameService.checkWin(game)));
    }
}
