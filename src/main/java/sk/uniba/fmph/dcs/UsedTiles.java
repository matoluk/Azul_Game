package sk.uniba.fmph.dcs;

import java.util.*;

public class UsedTiles {
    private final List<Tile> usedTiles; //List to hold used tiles

    public UsedTiles() {
        this.usedTiles = new ArrayList<>();
    }

    // Method to add an array of Tile objects to the usedTiles list without STARTING_PLAYER tile.
    public void give(Tile[] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Cannot add null array of tiles.");
        }
        for (Tile tile : tiles) {
            // Check if the current tile is the STARTING_PLAYER tile
            if (tile == Tile.STARTING_PLAYER) {
                continue;
            }
            this.usedTiles.add(tile);
        }
    }

    // Method to retrieve and remove all Tile objects from the usedTiles list.
    public Tile[] takeAll() {
        Tile[] tilesArray = new Tile[this.usedTiles.size()];
        tilesArray = this.usedTiles.toArray(tilesArray);
        this.usedTiles.clear();
        return tilesArray;
    }

    // Method to return a string representation of the current state of usedTiles.
    public String state() {
        return "UsedTiles{" +
                "count=" + usedTiles.size() +
                ", usedTiles=" + usedTiles +
                '}';
    }
}
