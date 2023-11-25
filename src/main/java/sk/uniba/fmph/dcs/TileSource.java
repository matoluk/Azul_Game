package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

abstract class TileSource {
    /** STARTING_PLAYER=0, RED=1, GREEN=2, YELLOW=3, BLUE=4, BLACK=5 */
    final HashMap<Integer, Tile> idxToTile = new HashMap<>();
    final ArrayList<Tile> tiles = new ArrayList<>();
    TileSource(){
        for (Tile t: Tile.values())
            idxToTile.put(t.ordinal(), t);
    }
    public Tile[] take(int idx){
        if (idx < 0 || 5 < idx )
            return null;
        Tile color = idxToTile.get(idx);
        if (!tiles.contains(idxToTile.get(idx)))
            return null;

        ArrayList<Tile> toReturn = new ArrayList<>();
        for (Iterator<Tile> it = tiles.iterator(); it.hasNext();) {
            Tile tile = it.next();
            if (tile == color || tile == Tile.STARTING_PLAYER) {
                toReturn.add(tile);
                it.remove();
            }
        }
        return toReturn.toArray(new Tile[toReturn.size()]);
    }

    public boolean isEmpty(){
        return tiles.isEmpty();
    }

    public void startNewRound(){
        tiles.clear();
    }
    public String state() {
        StringBuilder toReturn = new StringBuilder();
        for (final Tile tile : tiles) {
            toReturn.append(tile.toString());
        }
        return toReturn.toString();
    }
}