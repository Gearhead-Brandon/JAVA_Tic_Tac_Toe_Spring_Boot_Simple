package game.tictactoe.web.annotation;

import game.tictactoe.web.validators.GameFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GameFieldValidator.class)
public @interface ValidGameField {
    String message() default "Invalid game field, it should be a 3x3 matrix of X or O or empty cells";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
