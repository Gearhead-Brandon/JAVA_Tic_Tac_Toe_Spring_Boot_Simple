package game.tictactoe.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that the class contains exception handlers specific to the Tic-Tac-Toe game.
 * Classes marked with this annotation can be automatically discovered and used to centrally handle exceptions related to the game.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameExceptionHandler {}
