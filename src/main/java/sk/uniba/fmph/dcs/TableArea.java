package sk.uniba.fmph.dcs;
import java.util.ArrayList;

public class TableArea implements TableAreaInterface{
    private ArrayList<TileSource> tileSources;

    public TableArea(ArrayList<TileSource> tileSources) {
        this.tileSources = tileSources;
    }
    @Override
    public Tile[] take(int sourceIdx, int idx) {
        if (sourceIdx >= 0 && sourceIdx < tileSources.size()) {
            TileSource selectedSource = tileSources.get(sourceIdx);
            return selectedSource.take(idx);
        } else {
            return new Tile[0];
        }
    }

    @Override
    public boolean isRoundEnd() {
        for (TileSource tileSource : tileSources) {
            if (!tileSource.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void startNewRound() {
        for (TileSource tileSource : tileSources) {
            tileSource.startNewRound();
        }
    }

    @Override
    public String state() {
        StringBuilder stateBuilder = new StringBuilder();
        for (TileSource tileSource : tileSources) {
            stateBuilder.append(tileSource.state());
            stateBuilder.append("\n");
        }
        return stateBuilder.toString();
    }
}
