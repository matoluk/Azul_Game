package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameFinishedTest {

    @Test
    public void testGameFinishedWithCompleteHorizontalLine() {
        Optional<Tile>[][] wall = createCompleteHorizontalLineWall();

        FinishRoundResult result = GameFinished.gameFinished(wall);

        assertEquals(FinishRoundResult.GAME_FINISHED, result);
    }

    @Test
    public void testGameFinishedWithIncompleteHorizontalLine() {
        Optional<Tile>[][] wall = createIncompleteHorizontalLineWall();

        FinishRoundResult result = GameFinished.gameFinished(wall);

        assertEquals(FinishRoundResult.NORMAL, result);
    }

    @Test
    public void testGameFinishedWithEmptyWall() {
        Optional<Tile>[][] wall = createEmptyWall();

        FinishRoundResult result = GameFinished.gameFinished(wall);

        assertEquals(FinishRoundResult.NORMAL, result);
    }

    private Optional<Tile>[][] createCompleteHorizontalLineWall() {
        Optional<Tile>[][] wall = new Optional[Tile.values().length - 1][Tile.values().length - 1];

        for (int i = 0; i < Tile.values().length - 1; i++) {
            wall[0][i] = Optional.of(Tile.values()[i]);
        }
        return wall;
    }

    private Optional<Tile>[][] createIncompleteHorizontalLineWall() {
        Optional<Tile>[][] wall = new Optional[Tile.values().length - 1][Tile.values().length - 1];

        wall[0][0] = Optional.of(Tile.values()[0]);
        wall[0][1] = Optional.of(Tile.values()[1]);
        return wall;
    }

    private Optional<Tile>[][] createEmptyWall() {
        return new Optional[Tile.values().length - 1][Tile.values().length - 1];
    }
}
