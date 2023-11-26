package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.STARTING_PLAYER, Tile.BLUE, Tile.GREEN}));
        List<Tile> tilesAns = new ArrayList<>(List.of(new Tile[]{Tile.BLUE, Tile.GREEN}));
        usedTiles.give(tiles);
        assertEquals(tilesAns, usedTiles.takeAll());
    }

    @Test
    public void testIfSPTIsNotInTwice(){
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.STARTING_PLAYER, Tile.BLUE, Tile.GREEN}));
        List<Tile> tiles2x = new ArrayList<>(List.of(new Tile[]{Tile.BLUE, Tile.GREEN, Tile.BLUE, Tile.GREEN}));
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertEquals(tiles2x, usedTiles.takeAll());
    }
    @Test
    public void testGiveEmptyArray() {
        List<Tile> tiles = new ArrayList<>();
        usedTiles.give(tiles);
        assertTrue(usedTiles.takeAll().isEmpty());
    }

    @Test
    public void testGiveSomeTiles() {
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.GREEN}));
        usedTiles.give(tiles);
        assertEquals(tiles, usedTiles.takeAll());
    }

    @Test
    public void testGiveTilesTwice() {
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE}));
        List<Tile> tiles2x = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.RED, Tile.BLUE}));
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertEquals(tiles2x, usedTiles.takeAll());
    }

    @Test
    public void testGiveMixedTiles() {
        List<Tile> tiles1 = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.GREEN}));
        List<Tile> tiles2 = new ArrayList<>(List.of(new Tile[]{Tile.YELLOW, Tile.BLUE}));
        List<Tile> tiles1_2 = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE}));
        usedTiles.give(tiles1);
        usedTiles.give(tiles2);
        assertEquals(tiles1_2, usedTiles.takeAll());
    }

    @Test
    public void testState() {
        assertEquals("UsedTiles{count=0, usedTiles=[]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGive() {
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.GREEN}));
        usedTiles.give(tiles);
        assertEquals("UsedTiles{count=3, usedTiles=[R, B, G]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGiveTwice() {
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.GREEN}));
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertEquals("UsedTiles{count=6, usedTiles=[R, B, G, R, B, G]}", usedTiles.state());
    }

    @Test
    public void testStateAfterGiveMixedTiles() {
        List<Tile> tiles1 = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.GREEN}));
        List<Tile> tiles2 = new ArrayList<>(List.of(new Tile[]{Tile.YELLOW, Tile.BLUE}));
        usedTiles.give(tiles1);
        usedTiles.give(tiles2);
        assertEquals("UsedTiles{count=4, usedTiles=[R, G, I, B]}", usedTiles.state());
    }

    @Test
    public void testTakeAllThenGive2xThanTakeAll() {
        List<Tile> tiles = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.GREEN}));
        List<Tile> tiles2x = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.GREEN, Tile.RED, Tile.BLUE, Tile.GREEN}));
        usedTiles.give(tiles);
        usedTiles.takeAll();
        usedTiles.give(tiles);
        usedTiles.give(tiles);
        assertEquals(tiles2x, usedTiles.takeAll());
    }

}
