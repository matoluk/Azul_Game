package sk.uniba.fmph.dcs;

import java.util.List;

public class TableCenter extends TileSource implements TableCenterAddInterface {
    @Override
    public void startNewRound() {
        super.startNewRound();
        tiles.add(Tile.STARTING_PLAYER);
    }

    @Override
    public void add(List<Tile> tiles) {
        this.tiles.addAll(tiles);
    }
}