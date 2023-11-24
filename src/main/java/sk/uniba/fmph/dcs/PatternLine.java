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