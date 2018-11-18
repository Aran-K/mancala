package com.mancala.manager;

import com.mancala.exception.NotValidMoveException;
import com.mancala.persistence.dao.GameRepo;
import com.mancala.persistence.model.Game;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Created by aran on 15/07/17.
 * Manages game life cycles and insures the correct game is returned to the controller when a move is made
 */


@Component
public class GameManager {

    @Autowired
    GameRepo gameRepo;

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(GameManager.class);

    @Value("${mancala.stonecount}")
    private int stoneCount;

    @Value("${mancala.boardlength}")
    private int boardLength;

    public Game startNewGame() {
        Game game = new Game(stoneCount, boardLength);
        gameRepo.save(game);

        LOG.info("New game started. Id: " + game.getId());

        return game;
    }

    /**
     * Performs a full move for a game and returns the new game state
     *
     * @param id gameId
     * @param x pit
     * @return game in new state
     */
    public Game makeMove(String id, int x) throws NotValidMoveException{
        Game game = gameRepo.findOne(id);
        game.makeMove(x);

        // check is game over and finalize
        if(game.isGameOver()) game.finalizeGame();

        // save game
        gameRepo.save(game);

        LOG.info("Move successfully made for gameId: {}", game.getId());

        return game;
    }

    public Game findGame(String id) {
        return gameRepo.findOne(id);
    }

    public Iterable<Game> getAllGames() {
        return gameRepo.findAll();
    }
}
