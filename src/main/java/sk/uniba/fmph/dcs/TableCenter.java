package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.HashMap;

public class TableCenter extends TyleSource {

    /** STARTING_PLAYER=0, RED=1, GREEN=2, YELLOW=3, BLUE=4, BLACK=5 */
    HashMap<Integer, Tile> intToTile;
    HashMap<Tile, Integer> numOfTiles;
    public TableCenter() {
        this.intToTile = new HashMap<>();
        for (Tile t: Tile.values()) intToTile.put(t.ordinal(), t);

        this.numOfTiles = new HashMap<>();
        numOfTiles.put(Tile.STARTING_PLAYER, 1);
    }
    @Override
    Tile[] take(int idx) {
        ArrayList<Tile> result = new ArrayList<>();
        if (numOfTiles.containsKey(intToTile.get(idx))) {
            Tile tile = intToTile.get(idx);
            for (int i = 0; i < numOfTiles.get(tile); i++) {
                result.add(tile);
            }
            numOfTiles.remove(tile);
        }
        if (numOfTiles.get(Tile.STARTING_PLAYER) == 1) {
            result.add(Tile.STARTING_PLAYER);
            numOfTiles.remove(Tile.STARTING_PLAYER);
        }
        return result.toArray(new Tile[result.size()]);
    }

    @Override
    boolean isEmpty() {
        return numOfTiles.isEmpty();
    }

    @Override
    void startNewRound() {
        numOfTiles.clear();
        numOfTiles.put(Tile.STARTING_PLAYER, 1);
    }

    @Override
    String state() {
        return numOfTiles.toString();
    }

    public void add(Tile[] tiles) {
        for (Tile t: tiles) {
            if (numOfTiles.containsKey(t)) numOfTiles.put(t, numOfTiles.get(t) + 1);
            else numOfTiles.put(t, 1);
        }
    }

}
