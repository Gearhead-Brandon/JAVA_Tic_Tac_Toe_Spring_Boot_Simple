package game.tictactoe.configuration;

import game.tictactoe.datasource.mapper.GameMapper;
import game.tictactoe.datasource.mapper.GameMapperImpl;
import game.tictactoe.datasource.model.GameRepositoryModel;
import game.tictactoe.datasource.repository.GameRepository;
import game.tictactoe.datasource.repository.impl.GameRepositoryImpl;
import game.tictactoe.domain.service.gameService.GameService;
import game.tictactoe.domain.service.gameService.impl.GameServiceImpl;
import game.tictactoe.domain.service.minimax.MinimaxService;
import game.tictactoe.domain.service.minimax.impl.MinimaxServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration class for the Tic-Tac-Toe application.
 *
 * <p> It provides beans for the storage, game mapper, game repository, and game service.
 */
@Configuration
public class TicTacToeConfig {

    @Bean
    public Map<UUID, GameRepositoryModel> getStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public GameMapper getGameMapperImpl() {
        return new GameMapperImpl();
    }

    @Bean
    public GameRepository getGameRepository() {
        return new GameRepositoryImpl(getStorage(), getGameMapperImpl());
    }

    @Bean
    public MinimaxService getMinimaxService() {
        return new MinimaxServiceImpl();
    }

    @Bean
    public GameService getGameService() {
        return new GameServiceImpl(getMinimaxService(), getGameRepository());
    }
}
