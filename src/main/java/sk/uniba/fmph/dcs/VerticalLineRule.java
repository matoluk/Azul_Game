package sk.uniba.fmph.dcs;

import java.util.Optional;

public class VerticalLineRule implements ScoringRule{
    @Override
    public int calculatePoints(TileField[][] wall){
        if (wall.length == 0)
            return 0;

        int points = 0;
        for(int column = 0; column < wall[0].length; column++){
            boolean fullColumn = true;
            for (TileField[] wallLine : wall)
                if (wallLine[column].isEmpty()) {
                    fullColumn = false;
                    break;
                }
            if(fullColumn){
                points += EndGameScoringConstants.VERTICAL_LINE_POINTS;
            }
        }
        return points;
    }
}
