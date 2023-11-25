package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

class FakeWallLinePut implements WallLineInterface{
    public boolean canPutTile(Tile tile) {
        assert (tile != null && tile != Tile.STARTING_PLAYER);
        return tile == Tile.RED || tile == Tile.BLUE;
    }
    public Optional<Tile>[] getTiles() {
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
class FakeFloorPut implements FloorInterface{
    public ArrayList<Tile> tiles = new ArrayList<>();
    public void put(Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }
    public String state() {
        return null;
    }
    public Points finishRound() {
        return null;
    }
}
class FakeUsedTilesGive implements UsedTilesGiveInterface {
    public ArrayList<Tile> tiles = new ArrayList<>();
    public void give(Collection<Tile> t) {
        tiles.addAll(t);
    }
}

public class PatternLineTest {
    private FakeWallLinePut wallLine;
    private FakeFloorPut floor;
    private FakeUsedTilesGive usedTiles;
    private PatternLine patternLine;
    @Before
    public void setUp() {
        wallLine = new FakeWallLinePut();
        floor = new FakeFloorPut();
        usedTiles = new FakeUsedTilesGive();
        patternLine = new PatternLine(3, wallLine, usedTiles, floor);
    }
    @Test
    public void test_main(){
        Tile[] tiles = {Tile.RED, Tile.RED, Tile.STARTING_PLAYER};
        patternLine.put(tiles);
        assertEquals("Floor should contains exactly 1 Tile", 1, floor.tiles.size());
        assertEquals("Floor should contains Tile.STARTING_PLAYER", Tile.STARTING_PLAYER, floor.tiles.get(0));
        Points points = patternLine.finishRound();
        assertEquals("PatternLine is not full, should returns 0 points", 0, points.getValue());
        assertEquals("UsedTiles should be empty", 0, usedTiles.tiles.size());

        tiles = new Tile[] {Tile.RED, Tile.RED};
        patternLine.put(tiles);
        assertEquals("Floor should contains exactly 2 Tiles", 2, floor.tiles.size());
        assertEquals("Floor[1] should be RED", Tile.RED, floor.tiles.get(1));
        points = patternLine.finishRound();
        assertEquals("PatternLine is full, should returns 7 points", 7, points.getValue());
        assertEquals("UsedTiles should contains 2 Tiles", 2, usedTiles.tiles.size());
        assertEquals("UsedTiles[0] should be RED", Tile.RED, usedTiles.tiles.get(0));
        assertEquals("UsedTiles[1] should be RED", Tile.RED, usedTiles.tiles.get(1));
    }
    @Test
    public void test_blue(){
        Tile[] tiles = {Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE};
        patternLine.put(tiles);
        assertEquals("Floor should contains exactly 2 Tiles", 2, floor.tiles.size());
        assertEquals("Floor[0] should be BLUE", Tile.BLUE, floor.tiles.get(1));
        assertEquals("Floor[1] should be BLUE", Tile.BLUE, floor.tiles.get(1));
        Points points = patternLine.finishRound();
        assertEquals("PatternLine is full, should returns 3 points", 3, points.getValue());
    }
    @Test(expected = AssertionError.class)
    public void test_capacity() {
        patternLine = new PatternLine(0, wallLine, usedTiles, floor);
    }
    @Test(expected = AssertionError.class)
    public void test_putNullTile() {
        patternLine.put(new Tile[] {null});
    }
    @Test(expected = AssertionError.class)
    public void test_putMoreColors() {
        patternLine.put(new Tile[] {Tile.BLUE, Tile.RED});
    }
    @Test
    public void test_putDifferentColors() {
        patternLine.put(new Tile[] {Tile.BLUE});
        patternLine.put(new Tile[] {Tile.RED});
        assertEquals("Floor should contains exactly 1 Tile", 1, floor.tiles.size());
        assertEquals("Floor should contains Tile.RED", Tile.RED, floor.tiles.get(0));

        patternLine.put(new Tile[] {Tile.BLUE, Tile.BLUE});
        assertEquals("Floor should contains 1 Tile", 1, floor.tiles.size());
    }
    @Test
    public void test_wallLineCantPut() {
        patternLine.put(new Tile[] {Tile.YELLOW});
        assertEquals("Floor should contains exactly 1 Tile", 1, floor.tiles.size());
        assertEquals("Floor should contains YELLOW", Tile.YELLOW, floor.tiles.get(0));

        patternLine.put(new Tile[] {Tile.BLUE, Tile.BLUE, Tile.BLUE});
        assertEquals("Floor should contains 1 Tile", 1, floor.tiles.size());
    }
}
