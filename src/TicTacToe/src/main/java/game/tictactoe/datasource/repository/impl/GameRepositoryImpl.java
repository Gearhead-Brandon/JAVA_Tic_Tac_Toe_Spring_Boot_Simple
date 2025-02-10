package game.tictactoe.datasource.repository.impl;

import game.tictactoe.datasource.mapper.GameMapper;
import game.tictactoe.datasource.model.GameRepositoryModel;
import game.tictactoe.datasource.repository.GameRepository;
import game.tictactoe.domain.model.Game;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class GameRepositoryImpl implements GameRepository {

    /**
     * In-memory storage for games using a {@link ConcurrentHashMap} for thread-safe access.
     */
    private final Map<UUID, GameRepositoryModel> games;

    /**
     * Mapper for converting between {@link Game} and {@link GameRepositoryModel} entities.
     */
    private final GameMapper mapper;

    @Override
    public Game save(Game game){
        if(game == null) return null;

        games.put(
                game.getUuid(),
                mapper.toModel(game)
        );

        return game;
    }

    @Override
    public Optional<Game> deleteByUuid(UUID uuid) {
        if(uuid == null) return Optional.empty();

        var game = games.remove(uuid);

        if(game == null) return Optional.empty();

        return Optional.ofNullable(mapper.toEntity(game));
    }

    @Override
    public Optional<Game> findByUuid(UUID uuid) {
        var game = games.get(uuid);

        if(game == null)
            return Optional.empty();

        return Optional.ofNullable(mapper.toEntity(game));
    }
}
