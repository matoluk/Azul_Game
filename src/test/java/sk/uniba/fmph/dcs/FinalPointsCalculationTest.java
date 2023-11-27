package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinalPointsCalculationTest {
    private CompositePointsCalculation compositePointsCalculation;
    private int colours = Tile.values().length - 1;
    private TileField[][] wall;

    @BeforeEach
    public void setUp() {
        HorizontalLineRule horizontalLineRule = new HorizontalLineRule();
        VerticalLineRule verticalLineRule = new VerticalLineRule();
        FullColorRule fullColorRule = new FullColorRule();

        compositePointsCalculation = new CompositePointsCalculation();
        compositePointsCalculation.addRule(horizontalLineRule);
        compositePointsCalculation.addRule(verticalLineRule);
        compositePointsCalculation.addRule(fullColorRule);

        initializeWall();
    }

    public void initializeWall() {
        wall = new TileField[colours][colours];
        for(int i = 0; i < wall.length; i++){
            for(int j = 0; j < wall.length; j++){
                wall[i][j] = new TileField(Tile.values()[1 + ((i+colours-j) % colours)]);
            }
        }
    }

    @Test
    public void testGetPointsWithCompleteHorizontalLine() {
        // Set up a wall with a complete horizontal line
        wall[0][1].put();
        wall[0][4].put();
        for (int i = 0; i < wall.length; i++) {
            wall[1][i].put();
        }
        wall[2][0].put();
        wall[3][1].put();

        Points points = FinalPointsCalculation.getPoints(wall);

        // Assert that the points are as expected (2 points from HorizontalLineRule)
        assertEquals(2, points.getValue());
    }

    @Test
    public void testGetPointsWithCompleteVerticalLine() {
        // Set up a wall with a complete vertical line
        wall[0][0].put();
        wall[1][0].put();
        wall[2][0].put();
        wall[3][0].put();
        wall[4][0].put();

        Points points = FinalPointsCalculation.getPoints(wall);

        // Assert that the points are as expected (7 points from VerticalLineRule)
        assertEquals(7, points.getValue());
    }

    @Test
    public void testGetPointsWithCompleteFullColorRule() {
        // Set up a wall with all tiles filled
        wall[0][0].put();
        wall[1][1].put();
        wall[2][2].put();
        wall[3][3].put();
        wall[4][4].put();


        Points points = FinalPointsCalculation.getPoints(wall);

        // Assert that the points are as expected (10 points from FullColorRule)
        assertEquals(10, points.getValue());
    }

    @Test
    public void testGetPointsWithEmptyWall() {
        // Set up an empty wall
        Points points = FinalPointsCalculation.getPoints(wall);

        // Assert that the points are 0 for an empty wall
        assertEquals(0, points.getValue());
    }
}
