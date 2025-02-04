package sk.uniba.fmph.dcs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternLine implements PatternLineInterface{
    private Tile[] patternLine;
    private WallLineInterface wallLine;
    private UsedTilesGiveInterface usedTiles;
    private FloorInterface floor;
    PatternLine(int capacity, WallLineInterface wallLine, UsedTilesGiveInterface usedTiles, FloorInterface floor){
        this.wallLine = wallLine;
        this.usedTiles = usedTiles;
        this.floor = floor;

        assert (capacity > 0);
        patternLine = new Tile[capacity];
        Arrays.fill(patternLine, null);
    }

    @Override
    public void put(Tile[] tiles) {
        assert (tiles != null);
        Tile color = null;
        int count = tiles.length;
        ArrayList<Tile> toFloor = new ArrayList<>(count);

        for (Tile tile: tiles) {
            assert (tile != null);
            if (tile == Tile.STARTING_PLAYER) {
                toFloor.add(tile);
                count--;
            }
            else {
                if (color == null)
                    color = tile;
                assert (tile == color);
            }
        }
        assert (color != null);

        if ((patternLine[0] == null && wallLine.canPutTile(color)) || patternLine[0] == color) {
            for (int i = 0; i < patternLine.length; i++) {
                if (patternLine[i] == null && count > 0) {
                    patternLine[i] = color;
                    count--;
                }
            }
        }

        while (count-- > 0)
            toFloor.add(color);
        floor.put(toFloor);
    }

    @Override
    public Points finishRound() {
        Tile color = patternLine[patternLine.length - 1];
        if (color == null)
            return new Points(0);

        ArrayList<Tile> toUsedTiles = new ArrayList<>(List.of(patternLine));
        toUsedTiles.remove(toUsedTiles.size() - 1);
        usedTiles.give(toUsedTiles);

        Arrays.fill(patternLine, null);
        return wallLine.putTile(color);
    }

    @Override
    public String state() {
        StringBuilder toReturn = new StringBuilder();
        for (Tile tile : patternLine)
            if (tile == null)
                toReturn.append("_");
            else
                toReturn.append(tile);
        return toReturn.toString();
    }
}