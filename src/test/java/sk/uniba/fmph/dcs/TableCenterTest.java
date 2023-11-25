package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TableCenterTest {
    private TableCenter tableCenter;

    @BeforeEach
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
            assertFalse(tableCenter.isEmpty(), "There should be the STARTING_PLAYER tile");
            assertEquals("S", tableCenter.state(), "There should be the STARTING_PLAYER tile");
            tableCenter.add(redRed);
            assertEquals("SRR", tableCenter.state(), "We expect SRR");
            Tile[] takenTiles = tableCenter.take(Tile.RED.ordinal());
            assertEquals(3, takenTiles.length);
            assertEquals(Tile.STARTING_PLAYER, takenTiles[0]);
            assertEquals(Tile.RED, takenTiles[1]);
            assertEquals(Tile.RED, takenTiles[2]);
            assertTrue(tableCenter.isEmpty(), "Now the table center is empty");
            assertEquals("", tableCenter.state(), "Now the table center is empty");
            tableCenter.add(redRed);
            tableCenter.add(blueBlue);
            tableCenter.add(black);
            tableCenter.add(redRed);
            assertEquals("RRBBLRR", tableCenter.state());
            takenTiles = tableCenter.take(Tile.BLACK.ordinal());
            assertEquals(1, takenTiles.length);
            assertEquals(Tile.BLACK, takenTiles[0]);
            assertEquals("RRBBRR", tableCenter.state(), "We expect RRBBRR");
            takenTiles = tableCenter.take(Tile.RED.ordinal());
            assertEquals(4, takenTiles.length);
            for(int j = 0; j < 4; j++)
                assertEquals(Tile.RED, takenTiles[j]);
            assertEquals("BB", tableCenter.state(), "We expect BB");

            tableCenter.startNewRound();
        }
    }

}
