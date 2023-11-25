package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TableCenterTest {
    private TableCenter tableCenter;

    @Before
    public void setUp() {
        tableCenter = new TableCenter();
    }

    @Test
    public void test_table_centre() {
        for (int i = 0; i < 2; i++) {
            assertFalse("There should be the STARTING_PLAYER tile", tableCenter.isEmpty());
            assertEquals("There should be the STARTING_PLAYER tile", "S", tableCenter.state());
            tableCenter.add(new Tile[]{Tile.RED, Tile.RED,});
            assertEquals("We expect SRR", "SRR", tableCenter.state());
            Tile[] takenTiles = tableCenter.take(Tile.RED.ordinal());
            assertEquals("[STARTING_PLAYER, RED, RED]", new Tile[]{Tile.STARTING_PLAYER, Tile.RED, Tile.RED}, takenTiles);
            assertTrue("Now the table center is empty", tableCenter.isEmpty());
            assertEquals("Now the table center is empty", "", tableCenter.state());
            tableCenter.add(new Tile[]{Tile.RED, Tile.RED,});
            tableCenter.add(new Tile[]{Tile.BLUE, Tile.BLUE,});
            tableCenter.add(new Tile[]{Tile.BLACK});
            tableCenter.add(new Tile[]{Tile.RED, Tile.RED,});
            assertEquals("RRBBLRR", tableCenter.state());
            assertEquals(new Tile[]{Tile.BLACK}, tableCenter.take(Tile.BLACK.ordinal()));
            assertEquals("We expect RRBBRR", "RRBBRR", tableCenter.state());
            assertEquals(new Tile[]{Tile.RED, Tile.RED, Tile.RED, Tile.RED}, tableCenter.take(Tile.RED.ordinal()));
            assertEquals("We expect BB", "BB", tableCenter.state());

            tableCenter.startNewRound();
        }
    }

}
