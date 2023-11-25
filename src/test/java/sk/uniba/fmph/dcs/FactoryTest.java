package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FakeBag implements BagInterface{
    public ArrayList<Tile> tiles = new ArrayList<>();
    @Override
    public List<Tile> take(int count) {
        assert (count == 4);
        return tiles;
    }
}
class FakeTableCenter implements TableCenterAddInterface{
    public ArrayList<Tile> tiles = new ArrayList<>();
    @Override
    public void add(List<Tile> tiles) {
        this.tiles.addAll(tiles);
    }
}

public class FactoryTest {
    private Factory factory;
    private FakeBag bag;
    private FakeTableCenter tableCenter;
    @BeforeEach
    public void setUp() {
        bag = new FakeBag();
        bag.tiles.add(Tile.RED);
        bag.tiles.add(Tile.RED);
        bag.tiles.add(Tile.BLUE);
        bag.tiles.add(Tile.YELLOW);
        tableCenter = new FakeTableCenter();
        factory = new Factory(4, bag, tableCenter);
    }
    @Test
    public void test(){
        assertFalse(factory.isEmpty());
        assertEquals("RRBI", factory.state());

        Tile[] taken = factory.take(Tile.BLUE.ordinal());
        assertTrue(factory.isEmpty());
        assertEquals("", factory.state());
        assertEquals(1, taken.length);
        assertEquals(Tile.BLUE, taken[0]);
        assertEquals(3, tableCenter.tiles.size());
        assertTrue(tableCenter.tiles.remove(Tile.RED));
        assertTrue(tableCenter.tiles.remove(Tile.RED));
        assertTrue(tableCenter.tiles.remove(Tile.YELLOW));
        assertEquals(0, tableCenter.tiles.size());

        factory.startNewRound();
        assertEquals("RRBI", factory.state());

        taken = factory.take(Tile.RED.ordinal());
        assertTrue(factory.isEmpty());
        assertEquals(2, taken.length);
        assertEquals(Tile.RED, taken[0]);
        assertEquals(Tile.RED, taken[1]);
        assertEquals(2, tableCenter.tiles.size());
        assertTrue(tableCenter.tiles.remove(Tile.YELLOW));
        assertTrue(tableCenter.tiles.remove(Tile.BLUE));
        assertEquals(0, tableCenter.tiles.size());
    }
    @Test
    public void test_startNewRound(){
        assertEquals("RRBI", factory.state());
        factory.startNewRound();
        assertEquals("RRBI", factory.state());

        bag.tiles.clear();
        bag.tiles.add(Tile.YELLOW);
        bag.tiles.add(Tile.BLACK);
        bag.tiles.add(Tile.GREEN);
        bag.tiles.add(Tile.RED);
        factory.startNewRound();
        assertEquals("ILGR", factory.state());
    }
    @Test
    public void test_nullTake(){
        Tile[] taken = factory.take(Tile.GREEN.ordinal());
        assertNull(taken);
        taken = factory.take(Tile.GREEN.ordinal());
        assertNull(taken);

        assertFalse(factory.isEmpty());
        assertEquals("RRBI", factory.state());
        assertTrue(tableCenter.tiles.isEmpty());
    }
}
