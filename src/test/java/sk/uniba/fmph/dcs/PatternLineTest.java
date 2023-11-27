package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
    @BeforeEach
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
        assertEquals(1, floor.tiles.size(), "Floor should contains exactly 1 Tile");
        assertEquals(Tile.STARTING_PLAYER, floor.tiles.get(0), "Floor should contains Tile.STARTING_PLAYER");
        Points points = patternLine.finishRound();
        assertEquals(0, points.getValue(), "PatternLine is not full, should returns 0 points");
        assertEquals(0, usedTiles.tiles.size(), "UsedTiles should be empty");

        tiles = new Tile[] {Tile.RED, Tile.RED};
        patternLine.put(tiles);
        assertEquals(2, floor.tiles.size(), "Floor should contains exactly 2 Tiles");
        assertEquals(Tile.RED, floor.tiles.get(1), "Floor[1] should be RED");
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
        assertEquals(2, floor.tiles.size(), "Floor should contains exactly 2 Tiles");
        assertEquals(Tile.BLUE, floor.tiles.get(1), "Floor[0] should be BLUE");
        assertEquals(Tile.BLUE, floor.tiles.get(1), "Floor[1] should be BLUE");
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
        assertEquals(1, floor.tiles.size(), "Floor should contains exactly 1 Tile");
        assertEquals(Tile.RED, floor.tiles.get(0), "Floor should contains Tile.RED");

        patternLine.put(new Tile[] {Tile.BLUE, Tile.BLUE});
        assertEquals(1, floor.tiles.size(), "Floor should contains 1 Tile");
    }
    @Test
    public void test_wallLineCantPut() {
        patternLine.put(new Tile[] {Tile.YELLOW});
        assertEquals(1, floor.tiles.size(), "Floor should contains exactly 1 Tile");
        assertEquals(Tile.YELLOW, floor.tiles.get(0), "Floor should contains YELLOW");

        patternLine.put(new Tile[] {Tile.BLUE, Tile.BLUE, Tile.BLUE});
        assertEquals(1, floor.tiles.size(), "Floor should contains 1 Tile");
    }
}
