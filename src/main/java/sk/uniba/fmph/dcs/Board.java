package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Optional;

public class Board implements BoardInterface{
    public Points points;
    private int countColours;
    private PatternLine[] patternLines;
    private WallLine[] wallLines;
    private Floor floor;

    public Board(UsedTilesGiveInterface usedTiles,final ArrayList<Points> pointPattern){
        points = new Points(0);
        countColours = Tile.values().length - 1;
        patternLines = new PatternLine[countColours];
        wallLines = new WallLine[countColours];
        floor = new Floor(usedTiles, pointPattern);

        for (int i = 0; i < countColours; i++){
            patternLines[i] = new PatternLine(i+1, usedTiles, floor, wallLines[i]);

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
            WallLine lineUp = (i > 0 ? wallLines[i-1] : null);
            WallLine lineDown = (i < countColours - 1 ? wallLines[i+1] : null);
            wallLines[i] = new WallLine(tiles, lineUp, lineDown);
        }
    }

    private Optional<Tile>[][] getWall(){
        Optional<Tile>[][] wall = new Optional[countColours][];
        for (int i = 0; i < countColours; i++)
            wall[i] = wallLines[i].getTiles();
        return wall;
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

        return GameFinished.gameFinished(getWall());
    }

    @Override
    public void endGame() {
        Points finalPoints = FinalPointsCalculation.getPoints(getWall());
        points = new Points(points.getValue() + finalPoints.getValue());
    }

    @Override
    public String state() {
        String toReturn = points.toString() + "\n";

        int length = patternLines[countColours - 1].state().length();
        for (int i = 0; i < countColours; i++){
            String alignedPatternLine = patternLines[i].state();
            int spaces = length - alignedPatternLine.length();
            for (int j = 0; j < spaces; j++)
                alignedPatternLine += " ";

            toReturn += alignedPatternLine + " | " + wallLines[i].state() + "\n";
        }

        toReturn += floor.state();
        return toReturn;
    }
}
