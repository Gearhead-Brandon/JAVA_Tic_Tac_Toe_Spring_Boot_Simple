package game.tictactoe.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import game.tictactoe.web.annotation.ValidGameField;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Represents a request for creating a new game.
 * Contains information about the player's side and the initial game field.
 */
@Setter
@ToString
@AllArgsConstructor
public class GameCreationRequest {

    @NotNull
    @JsonProperty("playerSide")
    @Size(min = 1, max = 1, message = "Invalid player side, it should be 'X' or 'O'")
    @Pattern(regexp = "[XO]", message = "Invalid player side, it should be 'X' or 'O'")
    private String playerSide;

    @NotNull
    @ValidGameField
    @JsonProperty("gameField")
    private List<List<Character>> gameField;

    public GameCreationRequest() {
        this(null);
    }

    public GameCreationRequest(Character playerSide) {
        this.playerSide = String.valueOf(playerSide);
        this.gameField = List.of(
                List.of(' ', ' ', ' '),
                List.of(' ', ' ', ' '),
                List.of(' ', ' ', ' ')
        );
    }

    public Character getPlayerSide() {
        return playerSide.charAt(0);
    }

    public GameFieldDTO getGameField() {
        return new GameFieldDTO(gameField);
    }
}
