package com.mancala.controller;

import com.mancala.exception.NotValidMoveException;
import com.mancala.manager.GameManager;
import com.mancala.persistence.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by aran on 15/07/17.
 */


@RestController
public class WebController {

    @Autowired
    GameManager gameManager;

    @RequestMapping(value="/newGame", method = RequestMethod.GET)
    public @ResponseBody Game getGameInJson() {
        return gameManager.startNewGame();

    }

    @RequestMapping(value = "/makeMove", method = RequestMethod.POST)
    public @ResponseBody Game makeMove(String gameId, int pit) throws NotValidMoveException{
        return gameManager.makeMove(gameId, pit);
    }

    @RequestMapping(value="/gameList", method = RequestMethod.GET)
    public @ResponseBody Iterable<Game> getGameList() {
        return gameManager.getAllGames();

    }

    @RequestMapping(value="/loadGame", method = RequestMethod.GET)
    public @ResponseBody Game loadGame(@PathVariable String gameId) {
        return gameManager.findGame(gameId);

    }
}
