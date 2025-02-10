package game.tictactoe.web.validators;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameFieldValidatorTest {

    private final GameFieldValidator validator = new GameFieldValidator();

    @Test
    void testValidGameField() {
        List<List<Character>> gameField = Arrays.asList(
                Arrays.asList('X', 'O', ' '),
                Arrays.asList('O', 'X', 'O'),
                Arrays.asList(' ', ' ', 'X')
        );

        assertThat(validator.isValid(gameField, null))
                .isTrue();
    }

    @Test
    void testInvalidNullGameField() {
        assertThat(validator.isValid(null, null))
                .isFalse();
    }

    @Test
    void testInvalidGameFieldSize() {
        List<List<Character>> gameField = Arrays.asList(
                Arrays.asList('X', 'O', ' '),
                Arrays.asList('O', 'X', 'O')
        );

        assertThat(validator.isValid(gameField, null))
                .isFalse();
    }

    @Test
    void testInvalidRowSize() {
        List<List<Character>> gameField = Arrays.asList(
                Arrays.asList('X', 'O', ' '),
                Arrays.asList('O', 'X'),
                Arrays.asList(' ', ' ', 'X')
        );

        assertThat(validator.isValid(gameField, null))
                .isFalse();
    }

    @Test
    void testInvalidCharacterInField() {
        List<List<Character>> gameField = Arrays.asList(
                Arrays.asList('X', 'O', ' '),
                Arrays.asList('O', 'X', 'A'), // Invalid character 'A'
                Arrays.asList(' ', ' ', 'X')
        );

        assertThat(validator.isValid(gameField, null))
                .isFalse();
    }

    @Test
    void testInvalidRowIsNull() {
        List<List<Character>> gameField = Arrays.asList(
                Arrays.asList('X', 'O', ' '),
                null,
                Arrays.asList(' ', ' ', 'X')
        );

        assertThat(validator.isValid(gameField, null))
                .isFalse();}
}
