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
        return null;
    }

    @Override
    public String state() {
        return null;
    }
}