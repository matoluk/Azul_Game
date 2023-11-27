package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.List;

public class CompositePointsCalculation implements ScoringRule {
    private List<ScoringRule> rules = new ArrayList<>();

    public void addRule(ScoringRule rule) {
        rules.add(rule);
    }

    @Override
    public int calculatePoints(TileField[][] wall) {
        int totalPoints = 0;
        for (ScoringRule rule : rules) {
            totalPoints += rule.calculatePoints(wall);
        }
        return totalPoints;
    }
}
