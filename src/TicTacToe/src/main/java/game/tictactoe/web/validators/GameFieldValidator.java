package game.tictactoe.web.validators;

import game.tictactoe.web.annotation.ValidGameField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

/**
 * This class implements the {@link ConstraintValidator} interface to validate the
 * structure and content of a game field represented as a 2D list of characters.
 *
 * <p> The game field is considered valid if:
 * <ul>
 * <li> It is a 3x3 matrix (list of 3 lists, each containing 3 characters). </li>
 * <li> Each cell in the matrix contains only valid characters ('X', 'O', or ' '). </li>
 * </ul>
 *
 * <p> This validator is intended to be used with the {@link ValidGameField} annotation
 * to ensure the integrity of game data.
 */
public class GameFieldValidator implements ConstraintValidator<ValidGameField, List<List<Character>>> {
    private static final Set<Character> VALID_CHARACTERS = Set.of('X', 'O', ' ');

    @Override
    public boolean isValid(List<List<Character>> gameField, ConstraintValidatorContext context) {
        if (gameField == null || gameField.size() != 3) {
            return false;
        }

        for (List<Character> row : gameField) {
            if (row == null || row.size() != 3) {
                return false;
            }

            for (Character cell : row) {
                if (!VALID_CHARACTERS.contains(cell)) {
                    return false;
                }
            }
        }

        return true;
    }
}
