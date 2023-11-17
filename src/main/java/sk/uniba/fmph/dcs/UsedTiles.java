package sk.uniba.fmph.dcs;

import java.util.*;

public class UsedTiles {
    private List<Tile> usedTiles; //List to hold used tiles

    public UsedTiles() {
        this.usedTiles = new ArrayList<>();
    }

    // Method to add an array of Tile objects to the usedTiles list.
    public void give(Tile[] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Cannot add null array of tiles.");
        }
        this.usedTiles.addAll(Arrays.asList(tiles));
    }

    // Method to retrieve and remove all Tile objects from the usedTiles list.
    public Tile[] takeAll() {
        if (this.usedTiles.isEmpty()) {
            throw new NoSuchElementException("No tiles to take. The usedTiles list is empty.");
        }
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
