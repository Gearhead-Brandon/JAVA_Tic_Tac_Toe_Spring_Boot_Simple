package game.tictactoe.exception;

import java.util.UUID;

/**
 * This exception is thrown when a game with the specified {@link UUID} cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
