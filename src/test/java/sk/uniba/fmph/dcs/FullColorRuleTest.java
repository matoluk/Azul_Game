package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullColorRuleTest extends ScoreRulesTest {

    @Override
    protected void initializeScoringRule() {
        scoringRule = new FullColorRule();
    }

    @Test
    public void testCalculatePointsWithCompleteDiagonal1() {
        for (int i = 0; i < wall.length; i++) {
            wall[i][i].put();
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(10, points);
    }

    @Test
    public void testCalculatePointsWithCompleteDiagonal2(){

        for (int i = 0; i < wall.length; i++) {
            wall[i][i].put();
            wall[i][(i+1) % wall.length].put();
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(20, points);
    }

    @Test
    public void testCalculatePointsWithFullEachDiagonal(){

        for (int i = 0; i < wall.length; i++) {
            wall[i][i].put();
            wall[i][(i+1) % wall.length].put();
            wall[i][(i+2) % wall.length].put();
            wall[i][(i+3) % wall.length].put();
            wall[i][(i+4) % wall.length].put();
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(50, points);
    }

    @Test
    public void testCalculatePointsWithNonCompleteDiagonal1() {

        wall[0][0].put();
        wall[1][1].put();

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

    @Test
    public void testCalculatePointsWithNonCompleteDiagonal2() {

        for(int i = 0; i < wall.length; i+=2){
            wall[i][i].put();
        }

        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }

    @Test
    public void testCalculateWithEmptyWall(){
        int points = scoringRule.calculatePoints(wall);

        assertEquals(0, points);
    }
}
