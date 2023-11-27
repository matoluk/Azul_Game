package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerticalLineRuleTest extends ScoreRulesTest {

    @Override
    protected void initializeScoringRule() {
        scoringRule = new VerticalLineRule();
    }

    @Test
    public void testCalculatePointsWithCompleteVerticalLine1() {

        for(int i = 0; i < 5; i++)
            wall[i][1].put();

        wall[0][2].put();
        wall[2][2].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(7, points);
    }

    @Test
    public void testCalculatePointsWithCompleteVerticalLine2() {

        for (int i = 0; i < wall.length; i++) {
            wall[i][3].put();
        }

        wall[0][2].put();
        wall[4][4].put();

        int points = scoringRule.calculatePoints(wall);

        // Assert that the points are as expected
        assertEquals(7, points);
    }

    @Test
    public void testCalculatePointsWithAllCompleteVerticalLine() {

        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall.length; j++) {
                wall[j][i].put();
            }
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(35, points);
    }

    @Test
    public void testCalculatePointsWithNonCompleteVerticalLine1() {

        wall[0][2].put();
        wall[2][2].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

    @Test
    public void testCalculatePointsWithNonCompleteVerticalLine2() {

        wall[1][0].put();
        wall[3][0].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

    @Test
    public void testCalculatePointsWithEmptyWall() {
        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }
}
