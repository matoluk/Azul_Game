package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompositePointsCalculationTest extends ScoreRulesTest {

    @Override
    protected void initializeScoringRule() {
        scoringRule = new CompositePointsCalculation();
    }

    @Test
    public void testCalculatePointsWithHorizontalLine() {

        wall[0][1].put();
        wall[0][4].put();
        for (int i = 0; i < wall.length; i++) {
            wall[1][i].put();
        }
        wall[2][0].put();
        wall[3][1].put();

        ScoringRule horizontalLineRule = new HorizontalLineRule();
        ScoringRule verticalLineRule = new VerticalLineRule();
        ScoringRule fullColorRule = new FullColorRule();
        ((CompositePointsCalculation) scoringRule).addRule(fullColorRule);
        ((CompositePointsCalculation) scoringRule).addRule(verticalLineRule);
        ((CompositePointsCalculation) scoringRule).addRule(horizontalLineRule);

        int points = scoringRule.calculatePoints(wall);

        assertEquals(2, points);
    }

    @Test
    public void testCalculatePointsWithRowAndColumn() {

        wall[0][0].put();
        wall[0][1].put();
        wall[0][2].put();
        wall[0][3].put();
        wall[0][4].put();


        wall[1][1].put();
        wall[2][1].put();
        wall[3][1].put();
        wall[4][1].put();

        wall[2][2].put();

        ScoringRule horizontalLineRule = new HorizontalLineRule();
        ScoringRule verticalLineRule = new VerticalLineRule();
        ScoringRule fullColorRule = new FullColorRule();
        ((CompositePointsCalculation) scoringRule).addRule(fullColorRule);
        ((CompositePointsCalculation) scoringRule).addRule(verticalLineRule);
        ((CompositePointsCalculation) scoringRule).addRule(horizontalLineRule);

        int points = scoringRule.calculatePoints(wall);

        assertEquals(9, points);
    }

    @Test
    public void testCalculatePointsWithEmptyRuleList() {
        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }
}