package com.mancala;

import com.mancala.manager.GameManager;
import com.mancala.persistence.dao.GameRepo;
import com.mancala.persistence.model.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by aran on 18/07/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameMangerTests {
    @InjectMocks
    GameManager gameManager;

    @Mock
    GameRepo mockGameRepo;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testMakeMove() throws Exception{
        Game mockGame = new Game(1, 6);
        when(mockGameRepo.findOne("fakeId")).thenReturn(mockGame);

        Game newGameState = gameManager.makeMove("fakeId", 4);

        // Player one validations
        assert Arrays.equals(newGameState.getPlayerOne().getPits(), new int[]{1, 1, 1, 1, 0, 2});
        assert newGameState.getPlayerOne().getHomePitCount() == 0;
        assert !newGameState.getPlayerOne().isActive();

        // Player two validations
        assert Arrays.equals(newGameState.getPlayerTwo().getPits(), new int[]{1, 1, 1, 1, 1, 1});
        assert newGameState.getPlayerTwo().getHomePitCount() == 0;
        assert newGameState.getPlayerTwo().isActive();

    }
}
