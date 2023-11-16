package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Optional;

public class Board implements BoardInterface{
    public Points points;
    private int countColours;
    private PatternLine[] patternLines;
    private WallLine[] wallLines;
    private Floor floor;

    public Board(final ArrayList<Points> pointPattern){
        points = new Points(0);
        countColours = Tile.values().length - 1;
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
        patternLines[destinationIdx].put(tiles);
    }

    @Override
    public FinishRoundResult finishRound() {
        int newPoints = points.getValue();
        newPoints += patternLines.finishRound().getValue();
        newPoints -= floor.finishRound().getValue();
        points = new Points(newPoints);

        Optional<Tile>[][] wall = new Optional[countColours][countColours];
        for (int i = 0; i < countColours; i++){
            Optional<Tile>[] wallLine = wallLines[i].getTiles();
            for (int j = 0; j < countColours; j++)
                wall[i][j] = wallLine[j];
        }

        FinishRoundResult result = GameFinished.gameFinished(wall);
        if (result == FinishRoundResult.GAME_FINISHED)
            points = new Points(points.getValue() + FinalPointsCalculation.getPoints(wall).getValue());
        return result;
    }

    @Override
    public void endGame() {

    }

    @Override
    public String state() {
        return null;
    }
}
