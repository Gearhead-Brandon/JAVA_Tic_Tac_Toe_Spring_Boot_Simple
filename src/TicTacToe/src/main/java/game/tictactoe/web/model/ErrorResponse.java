package game.tictactoe.web.model;

/**
 * Represents a response from the server to a client.
 *
 * @param message The response message.
 */
public record ErrorResponse(String message) {}
