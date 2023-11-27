package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FakeUsedTiles2 implements UsedTilesGiveInterface{
    @Override
    public void give(Collection<Tile> ts) {
    }
}
public class IntegrationBoardTest {
    PatternLine[] patternLines;
    WallLine[] wallLines;
    Floor floor;
    Board board;
    @BeforeEach
    public void setUp(){
        FakeUsedTiles2 usedTiles = new FakeUsedTiles2();
        patternLines = new PatternLine[3];
        wallLines = new WallLine[3];
        List<Points> toFloor = List.of(new Points(-1), new Points(-2), new Points(-3));
        floor = new Floor(usedTiles, new ArrayList<>(toFloor));
        wallLines[0] = new WallLine(new Tile[]{Tile.RED, Tile.BLUE}, null, wallLines[1]);
        wallLines[1] = new WallLine(new Tile[]{Tile.BLUE, Tile.RED}, wallLines[0], wallLines[2]);
        wallLines[2] = new WallLine(new Tile[]{Tile.BLUE, Tile.RED}, wallLines[1], null);
        for (int i = 0; i < 3; i++)
            patternLines[i] = new PatternLine(i+1, wallLines[i], usedTiles, floor);
        board = new Board(patternLines, wallLines,floor);
    }
    @Test
    public void test(){
        assertEquals("Points [value=0]\n" +
                "_   |      \n" +
                "__  |      \n" +
                "___ |      \n", board.state());
    }
}
