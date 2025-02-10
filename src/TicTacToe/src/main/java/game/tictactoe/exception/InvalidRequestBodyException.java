package game.tictactoe.exception;

/**
 * Represents an exception that occurs when processing a request that
 * contains invalid data or is malformed.
 *
 * <p> This exception is typically thrown when a client sends an invalid
 * request to the server, such as one that is missing required parameters
 * or has an invalid data type.
 */
public class InvalidRequestBodyException extends RuntimeException {
  public InvalidRequestBodyException(String message) {
    super(message);
  }
}
