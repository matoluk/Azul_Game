package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FakeBag2 implements BagInterface{
    private final ArrayList<Tile> tiles;
    private final Random random = new Random(1);
    FakeBag2(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }
    @Override
    public List<Tile> take(int count) {
        List<Tile> toReturn = new ArrayList<>();
        for (int i = 0; i < count; i++)
            toReturn.add(tiles.remove(random.nextInt(tiles.size())));
        return toReturn;
    }
    @Override
    public String state() {
        return null;
    }
}
public class IntegrationTableAreaTest {
    TableArea tableArea;
    Factory factory1;
    Factory factory2;
    TableCenter tableCenter;
    FakeBag2 bag;
    @BeforeEach
    public void setUp(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            tiles.addAll(List.of(new Tile[]{Tile.RED, Tile.BLUE, Tile.BLACK, Tile.YELLOW, Tile.GREEN}));
        bag = new FakeBag2(tiles);
        tableCenter = new TableCenter();
        factory1 = new Factory(4, bag, tableCenter);
        factory2 = new Factory(4, bag, tableCenter);

        ArrayList<TileSource> tileSources = new ArrayList<>();
        tileSources.addAll(List.of(new TileSource[]{factory1, factory2, tableCenter}));
        tableArea = new TableArea(tileSources);
    }
    @Test
    public void test_takeNull(){
        assertEquals("RGBI\nGLRG\nS\n", tableArea.state());
        assertNull(tableArea.take(3, Tile.RED.ordinal()));
        assertNull(tableArea.take(0, Tile.BLACK.ordinal()));
        assertNull(tableArea.take(1, Tile.YELLOW.ordinal()));
        assertNull(tableArea.take(2, Tile.YELLOW.ordinal()));
        assertEquals("RGBI\nGLRG\nS\n", tableArea.state());
    }
    @Test
    public void test_take(){
        assertFalse(tableArea.isRoundEnd());
        assertEquals("RGBI\nGLRG\nS\n", tableArea.state());
        assertArrayEquals(new Tile[]{Tile.RED}, tableArea.take(0, Tile.RED.ordinal()));
        assertEquals("\nGLRG\nSGBI\n", tableArea.state());
        assertArrayEquals(new Tile[]{Tile.STARTING_PLAYER, Tile.BLUE}, tableArea.take(2, Tile.BLUE.ordinal()));
        assertEquals("\nGLRG\nGI\n", tableArea.state());
        assertArrayEquals(new Tile[]{Tile.GREEN, Tile.GREEN}, tableArea.take(1, Tile.GREEN.ordinal()));
        assertEquals("\n\nGILR\n", tableArea.state());
        assertFalse(tableArea.isRoundEnd());
        assertArrayEquals(new Tile[]{Tile.GREEN}, tableArea.take(2, Tile.GREEN.ordinal()));
        assertArrayEquals(new Tile[]{Tile.RED}, tableArea.take(2, Tile.RED.ordinal()));
        assertArrayEquals(new Tile[]{Tile.BLACK}, tableArea.take(2, Tile.BLACK.ordinal()));
        assertArrayEquals(new Tile[]{Tile.YELLOW}, tableArea.take(2, Tile.YELLOW.ordinal()));
        assertEquals("\n\n\n", tableArea.state());
        assertTrue(tableArea.isRoundEnd());

        tableArea.startNewRound();
        assertFalse(tableArea.isRoundEnd());
        assertEquals("BGLI\nRBLL\nS\n", tableArea.state());
        assertArrayEquals(new Tile[]{Tile.BLUE}, tableArea.take(0, Tile.BLUE.ordinal()));
        assertArrayEquals(new Tile[]{Tile.BLUE}, tableArea.take(1, Tile.BLUE.ordinal()));
        assertEquals("\n\nSGLIRLL\n", tableArea.state());
        assertArrayEquals(new Tile[]{Tile.STARTING_PLAYER, Tile.BLACK, Tile.BLACK, Tile.BLACK},
                tableArea.take(2, Tile.BLACK.ordinal()));
        assertEquals("\n\nGIR\n", tableArea.state());
        assertFalse(tableArea.isRoundEnd());
        assertArrayEquals(new Tile[]{Tile.GREEN}, tableArea.take(2, Tile.GREEN.ordinal()));
        assertArrayEquals(new Tile[]{Tile.RED}, tableArea.take(2, Tile.RED.ordinal()));
        assertArrayEquals(new Tile[]{Tile.YELLOW}, tableArea.take(2, Tile.YELLOW.ordinal()));
        assertEquals("\n\n\n", tableArea.state());
        assertTrue(tableArea.isRoundEnd());
    }
}
