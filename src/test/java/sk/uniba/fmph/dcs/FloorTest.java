package sk.uniba.fmph.dcs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FakeUsedTiles implements UsedTilesGiveInterface {
  public ArrayList<Tile> tiles;

  public FakeUsedTiles() {
    tiles = new ArrayList<Tile>();
  }

  public void give(Collection<Tile> t) {
    tiles.addAll(t);
  }
}

public class FloorTest {
  private FakeUsedTiles usedTiles;
  private Floor floor;

  @BeforeEach
  public void setUp() {
    usedTiles = new FakeUsedTiles();
    ArrayList<Points> pointPattern = new ArrayList<Points>();
    pointPattern.add(new Points(1));
    pointPattern.add(new Points(2));
    pointPattern.add(new Points(2));
    floor = new Floor(usedTiles, pointPattern);
  }

  @Test
  public void test_tiles() {
    ArrayList<Tile> tiles = new ArrayList<Tile>();
    tiles.add(Tile.STARTING_PLAYER);
    tiles.add(Tile.RED);
    tiles.add(Tile.GREEN);
    tiles.add(Tile.RED);
    assertEquals("", floor.state(), "Floor should be empty when created.");
    floor.put(tiles);
    assertEquals("SRGR", floor.state(), "Floor should contain tiles we put on it.");
    Points points = floor.finishRound();
    assertEquals("", floor.state(), "Floor should be empty after the round is finished.");
    assertEquals(7, points.getValue(),
            "Incorrect points calculation when there are more tiles than pattern size");
    assertArrayEquals(tiles.toArray(), usedTiles.tiles.toArray(),
            "Used tiles should get the tiles");

    floor.put(Arrays.asList(Tile.RED));
    floor.put(Arrays.asList(Tile.GREEN));
    floor.put(new ArrayList<Tile>());
    assertEquals("RG", floor.state(), "Floor should contain tiles we put on it.");
    Points points2 = floor.finishRound();
    assertEquals("", floor.state(), "Floor should be empty after the round is finished.");
    assertEquals(3, points2.getValue(),
            "Incorrect points calculation when there are less tiles than pattern size");
    tiles.add(Tile.RED);
    tiles.add(Tile.GREEN);
    assertArrayEquals(tiles.toArray(), usedTiles.tiles.toArray(),
            "Used tiles should get the tiles");
  }
}
