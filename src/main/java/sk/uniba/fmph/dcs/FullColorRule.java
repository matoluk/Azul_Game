package sk.uniba.fmph.dcs;

import java.util.HashMap;
import java.util.Map;

public class FullColorRule implements ScoringRule{
    @Override
    public int calculatePoints(TileField[][] wall){
        Map<Tile,Boolean> colorIsFull = new HashMap<>();
        for (TileField[] wallLine : wall)
            for (TileField field : wallLine){
                Tile color = field.getColor();
                if (field.isEmpty())
                    colorIsFull.put(color, false);
                else if (! colorIsFull.containsKey(color))
                    colorIsFull.put(color, true);
            }

        int points = 0;
        for (Tile color : colorIsFull.keySet())
            if (colorIsFull.get(color))
                points += EndGameScoringConstants.FULL_COLOR_SET_POINTS;
        return points;
    }
}
