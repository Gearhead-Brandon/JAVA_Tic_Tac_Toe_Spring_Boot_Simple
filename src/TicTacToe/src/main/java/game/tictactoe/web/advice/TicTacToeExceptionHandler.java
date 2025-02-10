package game.tictactoe.web.advice;

import game.tictactoe.exception.ResourceNotFoundException;
import game.tictactoe.exception.InvalidRequestBodyException;
import game.tictactoe.web.annotation.GameExceptionHandler;
import game.tictactoe.web.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The {@link TicTacToeExceptionHandler} class is designed to centrally handle exceptions
 * related to the Tic-Tac-Toe game. It uses the {@link RestControllerAdvice} annotation to
 * intercept exceptions that occur in application controllers.
 */
@RestControllerAdvice(annotations = GameExceptionHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TicTacToeExceptionHandler {

    /**
     * Handles the {@link ResourceNotFoundException} exception.
     * This exception occurs when a game with the specified UUID is not found.
     *
     * @param e The {@link ResourceNotFoundException} instance.
     * @return A {@link ResponseEntity} with a status of {@link ResponseEntity#notFound()}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(final ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles the {@link InvalidRequestBodyException}
     * This exception occurs when an invalid request is received by the server.
     *
     * @param e The {@link InvalidRequestBodyException} instance.
     * @return A {@link ResponseEntity} with a status of {@link ResponseEntity#badRequest()}
     *         and an error message including the current timestamp.
     */
    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBodyException(final InvalidRequestBodyException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    /**
     * This method handles the {@link MethodArgumentNotValidException} which is thrown when a controller method receives an invalid request body.
     *
     * <p> The method extracts the specific error details from the exception and constructs a corresponding error response.
     *
     * @param e The {@link MethodArgumentNotValidException} instance containing the validation errors.
     * @return A {@link ResponseEntity} with bad request status code and an {@link ErrorResponse} object containing the error message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

        final var fieldError = e.getBindingResult().getFieldError();

        final String message = fieldError.getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message));
    }
}
