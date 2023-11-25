package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TableCenterTest {
    private TableCenter tableCenter;

    @Before
    public void setUp() {
        tableCenter = new TableCenter();
    }

    @Test
    public void test_table_centre() {
        ArrayList<Tile> redRed = new ArrayList<>();
        redRed.add(Tile.RED);
        redRed.add(Tile.RED);
        ArrayList<Tile> blueBlue = new ArrayList<>();
        blueBlue.add(Tile.BLUE);
        blueBlue.add(Tile.BLUE);
        ArrayList<Tile> black = new ArrayList<>();
        blueBlue.add(Tile.BLACK);
        for (int i = 0; i < 2; i++) {
            assertFalse("There should be the STARTING_PLAYER tile", tableCenter.isEmpty());
            assertEquals("There should be the STARTING_PLAYER tile", "S", tableCenter.state());
            tableCenter.add(redRed);
            assertEquals("We expect SRR", "SRR", tableCenter.state());
            Tile[] takenTiles = tableCenter.take(Tile.RED.ordinal());
            assertEquals("[STARTING_PLAYER, RED, RED]", new Tile[]{Tile.STARTING_PLAYER, Tile.RED, Tile.RED}, takenTiles);
            assertTrue("Now the table center is empty", tableCenter.isEmpty());
            assertEquals("Now the table center is empty", "", tableCenter.state());
            tableCenter.add(redRed);
            tableCenter.add(blueBlue);
            tableCenter.add(black);
            tableCenter.add(redRed);
            assertEquals("RRBBLRR", tableCenter.state());
            assertEquals(new Tile[]{Tile.BLACK}, tableCenter.take(Tile.BLACK.ordinal()));
            assertEquals("We expect RRBBRR", "RRBBRR", tableCenter.state());
            assertEquals(new Tile[]{Tile.RED, Tile.RED, Tile.RED, Tile.RED}, tableCenter.take(Tile.RED.ordinal()));
            assertEquals("We expect BB", "BB", tableCenter.state());

            tableCenter.startNewRound();
        }
    }

}
