package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;

public abstract class ScoreRulesTest {
    protected ScoringRule scoringRule;
    protected TileField[][] wall;
    private int colours = Tile.values().length - 1;

    @BeforeEach
    public void setUp() {
        initializeScoringRule();
        initializeWall();
    }

    protected abstract void initializeScoringRule();

    protected void initializeWall() {
        wall = new TileField[colours][colours];
        for(int i = 0; i < wall.length; i++){
            for(int j = 0; j < wall.length; j++){
                wall[i][j] = new TileField(Tile.values()[1 + ((i+colours-j) % colours)]);
            }
        }
    }
}
