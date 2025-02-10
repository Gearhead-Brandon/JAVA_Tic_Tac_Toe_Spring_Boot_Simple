package game.tictactoe.domain.service.gameService.impl;

import game.tictactoe.datasource.repository.GameRepository;
import game.tictactoe.domain.model.CellType;
import game.tictactoe.domain.service.GameAttribute;
import game.tictactoe.domain.service.gameService.GameService;
import game.tictactoe.domain.service.WinState;
import game.tictactoe.domain.model.*;
import game.tictactoe.domain.service.minimax.MinimaxService;
import game.tictactoe.domain.utils.GameUtils;
import game.tictactoe.exception.ResourceNotFoundException;
import game.tictactoe.exception.InvalidRequestBodyException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final MinimaxService minimaxService;
    private final GameRepository gameRepository;

    @Override
    public GameCreationResult createGameAndMakeFirstMove(@NotNull final CellType playerSide, @NotNull final GameField gameField) {
        if (playerSide == CellType.EMPTY)
            throw new InvalidRequestBodyException("Invalid player side, it should be X or O");

        initialGameFieldValidation(playerSide, gameField);

        Game game = new Game(
                UUID.randomUUID(),
                playerSide,
                gameField
        );

        if(playerSide == CellType.O) {
            randomMove(game, CellType.X);
            gameRepository.save(game);
        }else {
            nextMove(game);
        }

        log.info("Created game: {}", game);

        return new GameCreationResult(game.getUuid(), new MoveResult("", game.getGameField()));
    }

    /**
     * Performs initial validation on the provided game field.
     *
     * <p> This method checks if the initial game field is valid based on the following rules:
     * <ul>
     * <li> Only the player's side or empty cells are allowed in the initial field. </li>
     * <li> If the player's side is 'O', there should be no 'O' cells in the initial field. </li>
     * <li> If the player's side is 'X', there should be exactly one 'X' cell in the initial field. </li>
     * </ul>
     *
     * @param playerSide The player's side (X or O).
     * @param gameField The initial state of the game field.
     */
    private void initialGameFieldValidation(final CellType playerSide, final GameField gameField) {
        int counter = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final CellType cellType = gameField.getCell(i, j);
                if (cellType != CellType.EMPTY && (cellType != playerSide || counter++ > 1))
                    throw new InvalidRequestBodyException("Invalid game field, it should be matrix with 3 rows and 3 columns of X or O or empty cells");
            }
        }

        if(playerSide == CellType.O && counter > 0)
            throw new InvalidRequestBodyException("With player O the field must be empty");

        if(playerSide == CellType.X && counter != 1)
            throw new InvalidRequestBodyException("With player X the field must have one X");
    }

    /**
     * Makes a random move on the game field.
     *
     * <p> This method selects a random empty cell on the game field and places the specified symbol
     * (X or O) on that cell.
     *
     * @param game The current game instance.
     * @param symbol The symbol to place on the game field.
     */
    @SuppressWarnings("SameParameterValue")
    private void randomMove(Game game, CellType symbol) {
        int i = ThreadLocalRandom.current().nextInt(GameAttribute.ROWS.getValue());
        int j = ThreadLocalRandom.current().nextInt(GameAttribute.ROWS.getValue());
        game.getGameField().setCell(i, j, symbol);
    }

    @Override
    public Game validateGameField(@NotNull final UUID uuid, @NotNull final GameField newField) {
        final Game repGame = gameRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        validateFieldChanges(repGame, newField);

        mergeField(repGame.getGameField(), newField);

        return repGame;
    }

    /**
     * Validates the changes made to the game field.
     *
     * <p> This method checks if the changes made to the game field are valid
     * according to the game rules.
     *
     * @param game The current game instance.
     * @param newField The updated game field.
     * @throws InvalidRequestBodyException If the changes to the game field are invalid.
     */
    private void validateFieldChanges(final Game game, final GameField newField) {
        final CellType playerSide = game.getPlayerSide();
        final GameField oldField = game.getGameField();
        int numberOfChangedCells = 0;

        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++) {

                final CellType cellType = oldField.getCell(i, j);
                final CellType newCellType = newField.getCell(i, j);

                if(cellType != newCellType) {
                    numberOfChangedCells++;
                    if(newCellType != playerSide || numberOfChangedCells > 1)
                        throw new InvalidRequestBodyException("Invalid game field");
                }
            }
        }

        if(numberOfChangedCells == 0)
            throw new InvalidRequestBodyException("Invalid game field");
    }

    /**
     * Merges the changes from the new game field into the old game field.
     *
     * <p> This method updates the old game field with the changes from the new game field.
     *
     * @param oldField The original {@link GameField}.
     * @param newField The updated {@link GameField}.
     */
    private void mergeField(final GameField oldField, final GameField newField) {
        for(int i = 0; i < GameAttribute.ROWS.getValue(); i++) {
            for(int j = 0; j < GameAttribute.COLS.getValue(); j++) {
                final CellType oldFieldCell = oldField.getCell(i, j);
                final CellType newFieldCell = newField.getCell(i, j);

                if(oldFieldCell != newFieldCell)
                    oldField.setCell(i, j, newFieldCell);
            }
        }
    }

    @Override
    public void nextMove(@NotNull final Game game) {
        final CellType side = game.getPlayerSide() == CellType.X ? CellType.O : CellType.X;

        final Position p = minimaxService.findBestMove(game.getGameField(), side);

        if(p.row() != Position.NOT_VALID_POS && p.col() != Position.NOT_VALID_POS)
            game.getGameField().setCell(p.row(), p.col(), side);

        gameRepository.save(game);
    }

    @Override
    public MoveResult checkWin(@NotNull final Game game) {
        final WinState winState = GameUtils.isGameOver(game.getGameField());

        if(winState == WinState.X_WON || winState == WinState.O_WON || winState == WinState.DRAW)
            gameRepository.deleteByUuid(game.getUuid());

        return new MoveResult(winState.name(), game.getGameField());
    }
}
