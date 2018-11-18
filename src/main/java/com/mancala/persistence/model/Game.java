package com.mancala.persistence.model;

import com.mancala.exception.NotValidMoveException;
import com.mancala.manager.GameManager;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Created by aran on 15/07/17.
 */

@Entity
@PropertySource("classpath:/application.properties")
public class Game {
    @Id
    private String id;

    @OneToOne(cascade= CascadeType.ALL)
    private Player playerOne;
    @OneToOne(cascade=CascadeType.ALL)
    private Player playerTwo;

    private int boardLength;

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Game.class);

    /*
    Constructor and getters for JPA
     */
    private Game(){}
    public Game(int stoneCount, int boardLength) {
        id = UUID.randomUUID().toString();
        this.boardLength = boardLength;
        playerOne = new Player("Player One", stoneCount, boardLength, true);
        playerTwo = new Player("Player Two", stoneCount, boardLength, false);
    }

    public String getId() {
        return id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }


    /*
    Game logic methods
     */
    /**
     * Makes a move on behalf of active player when a given location is picked
     *
     * @param x Location on board that player picked (0-5)
     */
    public void makeMove(int x) throws NotValidMoveException{
        // Check if game is over
        if (isGameOver()) {
            LOG.warn("Invalid move for gameID: {}: Game is over; cannot make move.", this.getId());
            throw new NotValidMoveException(String.format("Invalid move for gameID: {}: Game is over; cannot make move.", this.getId()));
        }

        // Get active and passive players to make move
        Player activePlayer = playerOne.isActive()? playerOne:playerTwo;
        Player passivePlayer = playerOne.isActive()? playerTwo:playerOne;

        // Get number of stones to redistribute
        int stones = activePlayer.getPitCount(x);
        if (stones == 0) {
            LOG.warn("Invalid move for gameID: {}: No stones in pit; cannot make move.", this.getId());
            throw new NotValidMoveException(String.format("Invalid move for gameID: {}: No stones in pit; cannot make move.", this.getId()));
        }

        // Set selected location to 0
        activePlayer.setPitCount(x, 0);

        // Distribute the stones that were in that location.
        // Do this in a while loop in case there are so many stones to
        // distribute it goes all the way around the board.
        while (stones > 0) {
            // Place one stone into each of the active players pits
            for (int i = x + 1; i < boardLength; i++) {
                if (stones == 1) {
                    // Last Stone - capture stones if needed
                    if (activePlayer.getPitCount(i) == 0) {
                        activePlayer.addStonesToStorePit(1);
                        activePlayer.addStonesToStorePit(passivePlayer.getPitCount(boardLength-1-i));
                        passivePlayer.setPitCount(boardLength-1-i, 0);
                    } else {
                        activePlayer.incrementPitCount(i);
                    }
                    stones--;
                    swapPlayers();
                    return;
                } else if (stones > 1) {
                    activePlayer.incrementPitCount(i);
                    stones--;
                } else {
                    swapPlayers();
                    return; // Out of stones
                }
            }

            // Place stone into active players 'home'
            if (stones == 1) {
                // Last Stone
                activePlayer.addStonesToStorePit(1);
                stones--;
                return; // without swapping players
            } else if (stones > 1) {
                    activePlayer.addStonesToStorePit(1);
                    stones--;
            }

            // Place stones into opponents pits
            for (int i = 0; i < boardLength; i++) {
                if (stones > 0) {
                    passivePlayer.incrementPitCount(i);
                    stones--;
                } else {
                    swapPlayers();
                    return;
                }
            }

            // If there's stones left we're going around the board so reset x
            if (stones > 0) {
                x = -1;
            }
        }
        // Out of stones. Move finished.
        swapPlayers();
    }

    /**
     * Private helper method
     * Swaps active and passive Player objects
     */
    private void swapPlayers() {
        playerOne.setActive(!playerOne.isActive());
        playerTwo.setActive(!playerTwo.isActive());
    }

    /**
     * Checks if the game is over
     * @return gameOver
     */
    public boolean isGameOver() {
        return (IntStream.of(playerOne.getPits()).sum() == 0 || IntStream.of(playerTwo.getPits()).sum() == 0);
    }

    /**
     * Moves all stones left on the board into the home pits to determine the winner
     */
    public void finalizeGame() {
        LOG.info("GameId: {} has ended", this.getId());
        // Move all stones on each board into the players home pit
        playerOne.addStonesToStorePit(IntStream.of(playerOne.getPits()).sum());
        playerTwo.addStonesToStorePit(IntStream.of(playerTwo.getPits()).sum());

        for (int i = 0; i < playerOne.getPits().length; i++) {
            playerOne.setPitCount(i, 0);
            playerTwo.setPitCount(i, 0);
        }
        playerOne.setActive(false);
        playerTwo.setActive(false);
    }

}
