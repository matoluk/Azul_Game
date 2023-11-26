package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FakeUsedTilesTake implements UsedTilesTakeAllInterface{
    public List<Tile> tiles = new ArrayList<>();
    @Override
    public List<Tile> takeAll() {
        return tiles;
    }
}
public class BagTest {
    Bag bag;
    FakeUsedTilesTake usedTiles = new FakeUsedTilesTake();
    ArrayList<Tile> tiles = new ArrayList<>();
    @BeforeEach
    public void setUp(){
        for (int i = 0; i < 3; i++)
            tiles.addAll(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.GREEN}));
        bag = new Bag(tiles, usedTiles,0);
    }
    @Test
    public void test(){
        List<Tile> taken = bag.take(4);
        List<Tile> answer = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.BLUE, Tile.GREEN}));
        assertEquals(answer, taken);

        taken = bag.take(4);
        answer = new ArrayList<>(List.of(new Tile[]{Tile.RED, Tile.GREEN, Tile.GREEN, Tile.BLUE}));
        assertEquals(answer, taken);

        taken = bag.take(4);
        answer = new ArrayList<>(List.of(new Tile[]{Tile.RED}));
        assertEquals(answer, taken);

        usedTiles.tiles.addAll(List.of(new Tile[]{Tile.RED, Tile.GREEN, Tile.GREEN, Tile.BLUE}));
        taken = bag.take(4);
        answer = new ArrayList<>(List.of(new Tile[]{Tile.BLUE, Tile.GREEN, Tile.RED, Tile.GREEN}));
        assertEquals(answer, taken);

        usedTiles.tiles.clear();
        taken = bag.take(4);
        assertTrue(taken.isEmpty());
    }
    @Test
    public void test_state(){
        assertEquals("RBGRBGRBG", bag.state());
        ArrayList<Tile> taken = (ArrayList<Tile>) bag.take(9);
        assertEquals("[R, B, B, G, R, G, G, B, R]", taken.toString());
        bag = new Bag(taken, usedTiles);
        assertEquals("RBBGRGGBR", bag.state());
    }
    @Test
    public void test_negativeCount(){
        assertThrows(AssertionError.class, () -> {
           bag.take(-1);
        });
    }
}
