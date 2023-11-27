package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HorizontalLineRuleTest extends ScoreRulesTest {
    @Override
    protected void initializeScoringRule() {
        scoringRule = new HorizontalLineRule();
    }

    @Test
    public void testCalculatePointsWithCompleteHorizontalLine1() {
        wall[0][1].put();
        wall[0][4].put();
        for (int i = 0; i < wall.length; i++) {
            wall[1][i].put();
        }

        wall[2][0].put();
        wall[3][1].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(2, points);
    }

    @Test
    public void testCalculatePointsWithCompleteHorizontalLine2() {

        for (int i = 0; i < wall.length; i++) {
            wall[2][i].put();
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(2, points);
    }

    @Test
    public void testCalculatePointsWithAllCompleteHorizontalLine(){
        for(int i = 0; i < wall.length; i++){
            for(int j = 0; j < wall.length; j++){
                wall[i][j].put();
            }
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(10, points);
    }

    @Test
    public void testCalculatePointsWithNonCompleteHorizontalLine1() {

        wall[3][0].put();
        wall[3][2].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

    @Test
    public void testCalculatePointsWithNonCompleteHorizontalLine2() {

        wall[0][1].put();
        wall[0][3].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

    @Test
    public void testCalculatePointsWithEmptyWall() {

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

}
