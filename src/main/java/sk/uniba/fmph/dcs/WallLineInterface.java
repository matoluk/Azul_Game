package sk.uniba.fmph.dcs;

public interface WallLineInterface {
    boolean canPutTile(Tile tile);
    TileField[] getTiles();
    Points putTile(Tile tile);
    String state();
}
