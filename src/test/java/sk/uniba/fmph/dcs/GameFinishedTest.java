package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameFinishedTest {
    private TileField[][] wall;
    private int colours = Tile.values().length - 1;

    @BeforeEach
    public void setUp() {
        wall = new TileField[colours][colours];

        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                wall[i][j] = new TileField(Tile.values()[1 + ((i+colours-j) % colours)]);
            }
        }
    }

    @Test
    public void testGameFinishedWithCompleteHorizontalLine() {
        for (int i = 0; i < wall[0].length; i++) {
            wall[0][i].put();
        }

        FinishRoundResult result = GameFinished.gameFinished(wall);

        assertEquals(FinishRoundResult.GAME_FINISHED, result);
    }

    @Test
    public void testGameFinishedWithIncompleteHorizontalLine() {
        wall[0][0].put();
        wall[0][1].put();

        FinishRoundResult result = GameFinished.gameFinished(wall);
        assertEquals(FinishRoundResult.NORMAL, result);
    }

    @Test
    public void testGameFinishedWithEmptyWall() {
        FinishRoundResult result = GameFinished.gameFinished(wall);
        assertEquals(FinishRoundResult.NORMAL, result);
    }
}
