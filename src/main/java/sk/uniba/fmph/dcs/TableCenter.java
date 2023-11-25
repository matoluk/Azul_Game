package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Arrays;

public class TableCenter extends TileSource {
    @Override
    public void startNewRound() {
        super.startNewRound();
        tiles.add(Tile.STARTING_PLAYER);
    }

    public void add(Tile[] newTiles) {
        tiles.addAll(new ArrayList<>(Arrays.asList(newTiles)));
    }
}