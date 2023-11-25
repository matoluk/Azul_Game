package sk.uniba.fmph.dcs;
import java.util.ArrayList;

public class TableArea {
    private ArrayList<TileSource> tyleSources;

    public TableArea(ArrayList<TileSource> tyleSources) {
        this.tyleSources = tyleSources;
    }
    public Tile[] take(int sourceIdx, int idx) {
        if (sourceIdx >= 0 && sourceIdx < tyleSources.size()) {
            TileSource selectedSource = tyleSources.get(sourceIdx);
            return selectedSource.take(idx);
        } else {
            return new Tile[0];
        }
    }

    public boolean isRoundEnd() {
        for (TileSource tyleSource : tyleSources) {
            if (!tyleSource.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void startNewRound() {
        for (TileSource tyleSource : tyleSources) {
            tyleSource.startNewRound();
        }
    }

    public String state() {
        StringBuilder stateBuilder = new StringBuilder();
        for (TileSource tyleSource : tyleSources) {
            stateBuilder.append(tyleSource.state());
            stateBuilder.append("\n");
        }
        return stateBuilder.toString();
    }
}
