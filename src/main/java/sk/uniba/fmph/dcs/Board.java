package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class Board implements BoardInterface{
    private Points points;
    private PatternLine[] patternLines;
    private WallLine[] wallLines;
    private Floor floor;

    public Board(final ArrayList<Points> pointPattern){
        int countColours = Tile.values().length - 1;

        points = new Points(0);
        patternLines = new PatternLine[countColours];
        wallLines = new WallLine[countColours];
        floor = new Floor(new UsedTiles(), pointPattern);

        for (int i = 0; i < countColours; i++){
            patternLines[i] = new PatternLine(i+1);

            Tile[] tiles = new Tile[countColours];
            int j = i;
            for (Tile tile : Tile.values()){
                if (tile != Tile.STARTING_PLAYER){
                    tiles[j] = tile;
                    j++;
                    if (j >= countColours)
                        j = 0;
                }
            }
            wallLines[i] = new WallLine(tiles);
        }
    }

    @Override
    public void put(int destinationIdx, Tile[] tiles) {

    }

    @Override
    public FinishRoundResult finishRound() {
        return null;
    }

    @Override
    public void endGame() {

    }

    @Override
    public String state() {
        return null;
    }
}
