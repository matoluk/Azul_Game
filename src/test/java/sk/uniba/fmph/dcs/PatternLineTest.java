package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FakeWallLinePut implements WallLineInterface{
    public boolean canPutTile(Tile tile) {
        assert (tile != null && tile != Tile.STARTING_PLAYER);
        return tile == Tile.RED || tile == Tile.BLUE;
    }
    public TileField[] getTiles() {
        return null;
    }
    public Points putTile(Tile tile) {
        assert (tile == Tile.RED || tile == Tile.BLUE);
        return new Points(tile == Tile.RED ? 7 : 3); //7-RED; 3-BLUE
    }
    public String state() {
        return null;
    }
}

public class PatternLineTest {
    private FakeWallLinePut wallLine;
    private Floor floor;
    private FakeUsedTiles usedTiles;
    private PatternLine patternLine;
    @BeforeEach
    public void setUp() {
        wallLine = new FakeWallLinePut();
        usedTiles = new FakeUsedTiles();
        floor = new Floor(usedTiles, new ArrayList<>(Arrays.asList(new Points(-1), new Points(-1), new Points(-2))));
        patternLine = new PatternLine(3, wallLine, usedTiles, floor);
    }
    @Test
    public void test_main(){
        Tile[] tiles = {Tile.RED, Tile.RED, Tile.STARTING_PLAYER};
        patternLine.put(tiles);
        assertEquals("S", floor.state());
        Points points = patternLine.finishRound();
        assertEquals(0, points.getValue(), "PatternLine is not full, should returns 0 points");
        assertEquals(0, usedTiles.tiles.size(), "UsedTiles should be empty");

        tiles = new Tile[] {Tile.RED, Tile.RED};
        patternLine.put(tiles);
        assertEquals("SR", floor.state());
        points = patternLine.finishRound();
        assertEquals(7, points.getValue(), "PatternLine is full, should returns 7 points");
        assertEquals(2, usedTiles.tiles.size(), "UsedTiles should contains 2 Tiles");
        assertEquals(Tile.RED, usedTiles.tiles.get(0), "UsedTiles[0] should be RED");
        assertEquals(Tile.RED, usedTiles.tiles.get(1), "UsedTiles[1] should be RED");
    }
    @Test
    public void test_blue(){
        Tile[] tiles = {Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE};
        patternLine.put(tiles);
        assertEquals("BB", floor.state());
        Points points = patternLine.finishRound();
        assertEquals(3, points.getValue(), "PatternLine is full, should returns 3 points");
    }
    @Test
    public void test_capacity() {
        assertThrows(AssertionError.class, () -> {
            patternLine = new PatternLine(0, wallLine, usedTiles, floor);
        });
    }
    @Test
    public void test_putNullTile() {
        assertThrows(AssertionError.class, () -> {
            patternLine.put(new Tile[]{null});
        });
    }
    @Test
    public void test_putMoreColors() {
        assertThrows(AssertionError.class, () -> {
            patternLine.put(new Tile[] {Tile.BLUE, Tile.RED});
        });
    }
    @Test
    public void test_putDifferentColors() {
        patternLine.put(new Tile[] {Tile.BLUE});
        patternLine.put(new Tile[] {Tile.RED});
        assertEquals("R", floor.state());

        patternLine.put(new Tile[] {Tile.BLUE, Tile.BLUE});
        assertEquals("R", floor.state());
    }
    @Test
    public void test_wallLineCantPut() {
        patternLine.put(new Tile[] {Tile.YELLOW});
        assertEquals("I", floor.state());

        patternLine.put(new Tile[] {Tile.BLUE, Tile.BLUE, Tile.BLUE});
        assertEquals("I", floor.state());
    }
}
