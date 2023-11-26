package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bag implements BagInterface{
    private UsedTilesTakeAllInterface usedTiles;
    private ArrayList<Tile> tiles;
    private Random random;
    Bag(ArrayList<Tile> tiles, UsedTilesTakeAllInterface usedTiles){
        this.tiles = tiles;
        this.usedTiles = usedTiles;
        random = new Random();
    }
    Bag(ArrayList<Tile> tiles, UsedTilesTakeAllInterface usedTiles, int seed){
        this(tiles, usedTiles);
        random = new Random(seed);
    }
    private void startNewRound(){
        tiles.addAll(usedTiles.takeAll());
    }
    @Override
    public List<Tile> take(int count) {
        assert (count >= 0);
        startNewRound();
        count = Integer.min(count, tiles.size());

        List<Tile> toReturn = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(tiles.size());
            toReturn.add(tiles.remove(index));
        }
        return toReturn;
    }
    @Override
    public String state() {
        StringBuilder toReturn = new StringBuilder();
        for (final Tile tile : tiles) {
            toReturn.append(tile.toString());
        }
        return toReturn.toString();
    }
}
