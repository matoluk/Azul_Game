package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsedTilesTest {

    private UsedTiles usedTiles;

    @BeforeEach
    public void setUp() {
        usedTiles = new UsedTiles();
    }

    @Test
    public void testGiveNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            usedTiles.give(null);
        });
    }

    @Test
    public void testIfSPTIsNotIn(){
        Tile[] tiles = {Tile.STARTING_PLAYER, Tile.BLUE, Tile.GREEN};
        usedTiles.give(tiles);
        assertArrayEquals(new Tile[]{Tile.BLUE, Tile.GREEN}, usedTiles.takeAll());
    }

    @Test
    public void testIfSPTIsNotInTwice(){
        Tile[] tiles = {Tile.STARTING_PLAYER, Tile.BLUE, Tile.GREEN};
        Tile[] tiles2x = {Tile.BLUE, Tile.GREEN, Tile.BLUE, Tile.GREEN};
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertArrayEquals(tiles2x, usedTiles.takeAll());
    }
    @Test
    public void testGiveEmptyArray() {
        Tile[] tiles = new Tile[0];
        usedTiles.give(tiles);
        usedTiles.takeAll();

        assertArrayEquals(tiles, usedTiles.takeAll());
    }

    @Test
    public void testGiveSomeTiles() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, Tile.GREEN};
        usedTiles.give(tiles);
        assertArrayEquals(tiles, usedTiles.takeAll());
    }

    @Test
    public void testGiveTilesTwice() {
        Tile[] tiles = {Tile.RED, Tile.BLUE};
        Tile[] tiles2x = {Tile.RED, Tile.BLUE, Tile.RED, Tile.BLUE};
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertArrayEquals(tiles2x, usedTiles.takeAll());
    }

    @Test
    public void testGiveMixedTiles() {
        Tile[] tiles1 = {Tile.RED, Tile.GREEN};
        Tile[] tiles2 = {Tile.YELLOW, Tile.BLUE};
        Tile[] tiles1_2 = {Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE};
        usedTiles.give(tiles1);
        usedTiles.give(tiles2);
        assertArrayEquals(tiles1_2, usedTiles.takeAll());
    }

    @Test
    public void testGiveTilesWithNull() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, null};
        usedTiles.give(tiles);
        assertArrayEquals(tiles, usedTiles.takeAll());
    }

    @Test
    public void testGiveTilesWithNullTwice() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, null};
        Tile[] tiles2x = {Tile.RED, Tile.BLUE, null, Tile.RED, Tile.BLUE, null};
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertArrayEquals(tiles2x, usedTiles.takeAll());
    }

    @Test
    public void testState() {
        assertEquals("UsedTiles{count=0, usedTiles=[]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGive() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, Tile.GREEN};
        usedTiles.give(tiles);
        assertEquals("UsedTiles{count=3, usedTiles=[R, B, G]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGiveTwice() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, Tile.GREEN};
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertEquals("UsedTiles{count=6, usedTiles=[R, B, G, R, B, G]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGiveMixedTiles() {
        Tile[] tiles1 = {Tile.RED, Tile.GREEN};
        Tile[] tiles2 = {Tile.YELLOW, Tile.BLUE};
        usedTiles.give(tiles1);
        usedTiles.give(tiles2);
        assertEquals("UsedTiles{count=4, usedTiles=[R, G, I, B]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGiveTilesWithNull() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, null};
        usedTiles.give(tiles);
        assertEquals("UsedTiles{count=3, usedTiles=[R, B, null]}", usedTiles.state());
    }

    @Test
    public void testTakeAllThenGive2xThanTakeAll() {
        Tile[] tiles = {Tile.RED, Tile.BLUE, Tile.GREEN};
        Tile[] tiles2x = {Tile.RED, Tile.BLUE, Tile.GREEN, Tile.RED, Tile.BLUE, Tile.GREEN};
        usedTiles.give(tiles);
        usedTiles.takeAll();
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertArrayEquals(tiles2x, usedTiles.takeAll());
    }

}
