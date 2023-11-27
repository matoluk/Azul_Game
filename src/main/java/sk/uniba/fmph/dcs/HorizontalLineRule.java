package sk.uniba.fmph.dcs;

import java.util.Optional;

public class HorizontalLineRule implements ScoringRule{
    @Override
    public int calculatePoints(TileField[][] wall){
        int points = 0;
        for (TileField[] wallLine : wall) {
            boolean fullLine = true;
            for (TileField field : wallLine)
                if (field.isEmpty()) {
                    fullLine = false;
                    break;
                }
            if (fullLine)
                points += EndGameScoringConstants.HORIZONTAL_LINE_POINTS;
        }
        return points;
    }
}
