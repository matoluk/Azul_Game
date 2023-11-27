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
        wallLines[0] = new WallLine(new Tile[]{Tile.RED, Tile.BLUE, Tile.YELLOW}, null, wallLines[1]);
        wallLines[1] = new WallLine(new Tile[]{Tile.YELLOW, Tile.RED, Tile.BLUE}, wallLines[0], wallLines[2]);
        wallLines[2] = new WallLine(new Tile[]{Tile.BLUE, Tile.YELLOW, Tile.RED}, wallLines[1], null);
        for (int i = 0; i < 3; i++)
            patternLines[i] = new PatternLine(i+1, wallLines[i], usedTiles, floor);
        board = new Board(patternLines, wallLines,floor);
    }
    @Test
    public void test(){
        assertEquals("Points [value=0]\n" +
                "_   | rbi\n" +
                "__  | irb\n" +
                "___ | bir\n", board.state());
        board.put(0, new Tile[]{Tile.BLUE, Tile.BLUE});
        assertEquals("Points [value=0]\n" +
                "B   | rbi\n" +
                "__  | irb\n" +
                "___ | bir\nB", board.state());
        board.put(2, new Tile[]{Tile.RED});
        assertEquals("Points [value=0]\n" +
                "B   | rbi\n" +
                "__  | irb\n" +
                "R__ | bir\nB", board.state());
        board.put(2, new Tile[]{Tile.BLUE});
        assertEquals("Points [value=0]\n" +
                "B   | rbi\n" +
                "__  | irb\n" +
                "R__ | bir\nBB", board.state());
        board.put(1, new Tile[]{Tile.RED, Tile.STARTING_PLAYER, Tile.RED});
        assertEquals("Points [value=0]\n" +
                "B   | rbi\n" +
                "RR  | irb\n" +
                "R__ | bir\nBBS", board.state());
        assertEquals(FinishRoundResult.NORMAL, board.finishRound());
        assertEquals("Points [value=-3]\n" +
                "_   | rBi\n" +
                "__  | iRb\n" +
                "R__ | bir\n", board.state());
        board.put(2, new Tile[]{Tile.RED, Tile.RED, Tile.RED});
        assertEquals("Points [value=-3]\n" +
                "_   | rBi\n" +
                "__  | iRb\n" +
                "RRR | bir\nR", board.state());
        board.put(0, new Tile[]{Tile.RED});
        assertEquals("Points [value=-3]\n" +
                "R   | rBi\n" +
                "__  | iRb\n" +
                "RRR | bir\nR", board.state());
        assertEquals(FinishRoundResult.NORMAL, board.finishRound());
        assertEquals("Points [value=-1]\n" +
                "_   | RBi\n" +
                "__  | iRb\n" +
                "___ | biR\n", board.state());
        board.put(2, new Tile[]{Tile.YELLOW, Tile.YELLOW, Tile.YELLOW, Tile.YELLOW, Tile.YELLOW});
        board.put(0, new Tile[]{Tile.STARTING_PLAYER, Tile.YELLOW});
        assertEquals("Points [value=-1]\n" +
                "I   | RBi\n" +
                "__  | iRb\n" +
                "III | biR\nIIS", board.state());
        assertEquals(FinishRoundResult.GAME_FINISHED, board.finishRound());
        assertEquals("Points [value=1]\n" +
                "_   | RBI\n" +
                "__  | iRb\n" +
                "___ | bIR\n", board.state());
        board.endGame();
        assertEquals("Points [value=20]\n" +
                "_   | RBI\n" +
                "__  | iRb\n" +
                "___ | bIR\n", board.state());
        assertEquals(20, board.getPoints().getValue());
    }
    @Test
    public void test_putMoreColors(){
        assertThrows(AssertionError.class, () -> {
            board.put(1, new Tile[]{Tile.RED, Tile.YELLOW});
        });
    }
    @Test
    public void test_cantPutColor(){
        board.put(1, new Tile[]{Tile.YELLOW, Tile.YELLOW});
        assertEquals(FinishRoundResult.NORMAL, board.finishRound());
        assertEquals("Points [value=1]\n" +
                "_   | rbi\n" +
                "__  | Irb\n" +
                "___ | bir\n", board.state());
        board.put(1, new Tile[]{Tile.YELLOW});
        assertEquals("Points [value=1]\n" +
                "_   | rbi\n" +
                "__  | Irb\n" +
                "___ | bir\nI", board.state());
        board.put(0, new Tile[]{Tile.GREEN});
        assertEquals("Points [value=1]\n" +
                "_   | rbi\n" +
                "__  | Irb\n" +
                "___ | bir\nIG", board.state());
    }
}
