package sk.uniba.fmph.dcs;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class TableAreaTest {


    @Test
    public void testTakeValidSource() {

        TableCenter tableCenter = new TableCenter();
        ArrayList<Tile> redRedRed = new ArrayList<>();
        redRedRed.add(Tile.RED);
        redRedRed.add(Tile.RED);
        redRedRed.add(Tile.RED);
        tableCenter.add(redRedRed);
        TableArea tableArea = new TableArea(new ArrayList<>(Collections.singletonList(tableCenter)));

        Tile[] takenTiles = tableArea.take(0, Tile.RED.ordinal());
        assertEquals(Tile.STARTING_PLAYER, takenTiles[0]);
        for (int i = 1; i < takenTiles.length; i++) {
            assertEquals(redRedRed.get(i-1), takenTiles[i]);
        }
        assertTrue(tableCenter.isEmpty());
    }



    @Test
    public void testTakeInvalidSource() {
        TableCenter tableCenter = new TableCenter();
        TableArea tableArea = new TableArea(new ArrayList<>(Collections.singletonList(tableCenter)));

        Tile[] takenTiles = tableArea.take(1, Tile.RED.ordinal());

        assertNull(takenTiles);
    }



    @Test
    public void testIsRoundEndEmptySources() {
        TableCenter source1 = new TableCenter();
        TableCenter source2 = new TableCenter();
        ArrayList<TileSource> sources = new ArrayList<>(Arrays.asList(source1, source2));
        TableArea tableArea = new TableArea(sources);

        assertFalse(tableArea.isRoundEnd());
        tableArea.take(0, Tile.STARTING_PLAYER.ordinal());
        tableArea.take(1, Tile.STARTING_PLAYER.ordinal());
        assertTrue(tableArea.isRoundEnd());
    }


    @Test
    public void testIsRoundEndNonEmptySources() {
        TableCenter tableCenter = new TableCenter();
        ArrayList<Tile> red = new ArrayList<>();
        red.add(Tile.RED);
        tableCenter.add(red);
        TableArea tableArea = new TableArea(new ArrayList<>(Collections.singletonList(tableCenter)));


        assertFalse(tableArea.isRoundEnd());
    }


    @Test
    public void testStartNewRound() {
        TableCenter source1 = new TableCenter();
        ArrayList<Tile> redRedRed = new ArrayList<>();
        redRedRed.add(Tile.RED);
        redRedRed.add(Tile.RED);
        redRedRed.add(Tile.RED);
        source1.add(redRedRed);
        TableCenter source2 = new TableCenter();
        ArrayList<TileSource> sources = new ArrayList<>(Arrays.asList(source1, source2));
        TableArea tableArea = new TableArea(sources);

        tableArea.startNewRound();

        assertFalse(source1.isEmpty());
        assertFalse(source2.isEmpty());

        tableArea.take(0, Tile.STARTING_PLAYER.ordinal());
        tableArea.take(1, Tile.STARTING_PLAYER.ordinal());

        assertTrue(source1.isEmpty());
        assertTrue(source2.isEmpty());
    }

    @Test
    public void testState() {
        ArrayList<Tile> redBlackRed = new ArrayList<>();
        redBlackRed.add(Tile.RED);
        redBlackRed.add(Tile.BLACK);
        redBlackRed.add(Tile.RED);
        ArrayList<Tile> green3x = new ArrayList<>();
        green3x.add(Tile.GREEN);
        green3x.add(Tile.GREEN);
        green3x.add(Tile.GREEN);

        TableCenter source1 = new TableCenter();
        source1.add(redBlackRed);
        TableCenter source2 = new TableCenter();
        source2.add(green3x);
        ArrayList<TileSource> sources = new ArrayList<>(Arrays.asList(source1, source2));
        TableArea tableArea = new TableArea(sources);

        String expectedState = source1.state() + "\n" + source2.state() + "\n";

        assertEquals(expectedState, tableArea.state());
    }
}
