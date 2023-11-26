package sk.uniba.fmph.dcs;

import java.util.*;

public class UsedTiles implements UsedTilesGiveInterface, UsedTilesTakeAllInterface{
    private final List<Tile> usedTiles; //List to hold used tiles

    public UsedTiles() {
        this.usedTiles = new ArrayList<>();
    }

    // Method to add an array of Tile objects to the usedTiles list without STARTING_PLAYER tile.
    @Override
    public void give(Collection<Tile> tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Cannot add null array of tiles.");
        }
        for (Tile tile : tiles) {
            if (tile != Tile.STARTING_PLAYER) {
                this.usedTiles.add(tile);
            }
        }
    }

    // Method to retrieve and remove all Tile objects from the usedTiles list.
    @Override
    public List<Tile> takeAll() {
        List<Tile> toReturn = List.copyOf(usedTiles);
        usedTiles.clear();
        return toReturn;
    }

    // Method to return a string representation of the current state of usedTiles.
    public String state() {
        return "UsedTiles{" +
                "count=" + usedTiles.size() +
                ", usedTiles=" + usedTiles +
                '}';
    }
}
