package sk.uniba.fmph.dcs;

import java.util.List;
import java.util.Optional;

public class Board implements BoardInterface{
    private Points points;
    private PatternLineInterface[] patternLines;
    private WallLineInterface[] wallLines;
    private FloorInterface floor;

    public Board(PatternLineInterface[] patternLines, WallLineInterface[] wallLines, FloorInterface floor){
        points = new Points(0);
        this.patternLines = patternLines;
        this.wallLines = wallLines;
        this.floor = floor;
    }

    private TileField[][] getWall(){
        TileField[][] wall = new TileField[wallLines.length][];
        for (int i = 0; i < wall.length; i++)
            wall[i] = wallLines[i].getTiles();
        return wall;
    }

    @Override
    public void put(int destinationIdx, Tile[] tiles) {
        if (destinationIdx < 0 || destinationIdx >= patternLines.length)
            floor.put(List.of(tiles));
        else
            patternLines[destinationIdx].put(tiles);
    }

    @Override
    public FinishRoundResult finishRound() {
        int newPoints = points.getValue();
        for (int i = 0; i < patternLines.length; i++)
            newPoints += patternLines[i].finishRound().getValue();
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(points).append("\n");

        int length = patternLines[patternLines.length - 1].state().length();
        for (int i = 0; i < patternLines.length; i++){
            String alignedPatternLine = patternLines[i].state();
            int spaces = length - alignedPatternLine.length();
            alignedPatternLine = alignedPatternLine + " ".repeat(spaces);
            stringBuilder.append(alignedPatternLine).append(" | ").append(wallLines[i].state()).append("\n");
        }

        stringBuilder.append(floor.state());
        return stringBuilder.toString();
    }
    @Override
    public Points getPoints(){
        return points;
    }
}
