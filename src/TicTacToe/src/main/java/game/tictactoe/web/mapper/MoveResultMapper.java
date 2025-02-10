package game.tictactoe.web.mapper;

import game.tictactoe.domain.model.MoveResult;
import game.tictactoe.web.model.MoveResultDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper class that provides the conversion between {@link MoveResult} and {@link MoveResultDto} objects.
 *
 * <p> Uses the MapStruct library to automatically generate the conversion code.
 * The {@link Mapper} annotation specifies that this class is a mapper, and the componentModel="spring" parameter
 * tells MapStruct to create a Spring bean for this mapper.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = GameFieldMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MoveResultMapper {

    /**
     * Maps a {@link MoveResult} object to a {@link MoveResultDto} object.
     *
     * @param moveResult The {@link MoveResult} object to be mapped.
     * @return The mapped {@link MoveResultDto} object.
     */
    MoveResultDto toDTO(@NotNull final MoveResult moveResult);
}
