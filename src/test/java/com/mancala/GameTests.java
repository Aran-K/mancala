package com.mancala;

import com.mancala.exception.NotValidMoveException;
import com.mancala.persistence.model.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by aran on 15/07/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTests {

    @Test
    public void testBasicMove() throws Exception{
        Game mockGame = new Game(1, 6);

        mockGame.makeMove(0);

        // Player one validations
        assert Arrays.equals(mockGame.getPlayerOne().getPits(), new int[]{0, 2, 1, 1, 1, 1});
        assert mockGame.getPlayerOne().getHomePitCount() == 0;
        assert !mockGame.getPlayerOne().isActive();

        // Player two validations
        assert Arrays.equals(mockGame.getPlayerTwo().getPits(), new int[]{1, 1, 1, 1, 1, 1});
        assert mockGame.getPlayerTwo().getHomePitCount() == 0;
        assert mockGame.getPlayerTwo().isActive();

    }

    @Test
    public void testEndOnHomePit() throws Exception{
        Game mockGame = new Game(1, 6);

        mockGame.makeMove(5);

        // Player one validations
        assert Arrays.equals(mockGame.getPlayerOne().getPits(), new int[]{1, 1, 1, 1, 1, 0});
        assert mockGame.getPlayerOne().getHomePitCount() == 1;
        assert mockGame.getPlayerOne().isActive();

        // Player two validations
        assert Arrays.equals(mockGame.getPlayerTwo().getPits(), new int[]{1, 1, 1, 1, 1, 1});
        assert mockGame.getPlayerTwo().getHomePitCount() == 0;
        assert !mockGame.getPlayerTwo().isActive();
    }

    @Test
    public void testWrapAround() throws Exception{
        Game mockGame = new Game(1, 6);
        mockGame.getPlayerOne().setPitCount(5, 9);

        mockGame.makeMove(5);

        // Player one validations
        assert Arrays.equals(mockGame.getPlayerOne().getPits(), new int[]{2, 2, 1, 1, 1, 0});
        assert mockGame.getPlayerOne().getHomePitCount() == 1;
        assert !mockGame.getPlayerOne().isActive();

        // Player two validations
        assert Arrays.equals(mockGame.getPlayerTwo().getPits(), new int[]{2, 2, 2, 2, 2, 2});
        assert mockGame.getPlayerTwo().getHomePitCount() == 0;
        assert mockGame.getPlayerTwo().isActive();
    }

    @Test
    public void testCapture() throws Exception {
        Game mockGame = new Game(1, 6);
        mockGame.getPlayerOne().setPitCount(4, 0);
        mockGame.makeMove(3);

        // Player one validations
        assert Arrays.equals(mockGame.getPlayerOne().getPits(), new int[]{1, 1, 1, 0, 0, 1});
        assert mockGame.getPlayerOne().getHomePitCount() == 2;
        assert !mockGame.getPlayerOne().isActive();

        // Player two validations
        assert Arrays.equals(mockGame.getPlayerTwo().getPits(), new int[]{1, 0, 1, 1, 1, 1});
        assert mockGame.getPlayerTwo().getHomePitCount() == 0;
        assert mockGame.getPlayerTwo().isActive();

    }

    @Test
    public void testGameFinalize() throws Exception{
        Game mockGame = new Game(1, 6);
        IntStream.range(0, 5).forEach(i -> mockGame.getPlayerOne().setPitCount(i, 0));
        assert !mockGame.isGameOver();

        mockGame.makeMove(5);

        assert mockGame.isGameOver();
        mockGame.finalizeGame();

        assert Arrays.equals(mockGame.getPlayerOne().getPits(), new int[]{0, 0, 0, 0, 0, 0});
        assert Arrays.equals(mockGame.getPlayerTwo().getPits(), new int[]{0, 0, 0, 0, 0, 0});
        assert mockGame.getPlayerOne().getHomePitCount() == 1;
        assert mockGame.getPlayerTwo().getHomePitCount() == 6;
    }

    @Test(expected = NotValidMoveException.class)
    public void testInvalidMove() throws Exception{
        Game mockGame = new Game(1, 6);
        mockGame.getPlayerOne().setPitCount(0, 0);

        mockGame.makeMove(0);
    }


}
