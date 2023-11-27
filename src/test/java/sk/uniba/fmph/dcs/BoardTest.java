package sk.uniba.fmph.dcs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

class FakePatternLine implements PatternLineInterface {
    public boolean putCalled = false;
    String state;
    FakePatternLine(String state){
        this.state = state;
    }
    public void put(Tile[] tiles) {
        putCalled = true;
    }
    public Points finishRound() {
        return new Points(5);
    }
    public String state() {
        return state;
    }
}
class FakeWallLine implements WallLineInterface{
    String state;
    FakeWallLine(String state){
        this.state = state;
    }
    public boolean canPutTile(Tile tile) {
        return false;
    }
    public TileField[] getTiles() {
        return new TileField[0];
    }
    public Points putTile(Tile tile) {
        return null;
    }
    public String state() {
        return state;
    }
}
class FakeFloor implements FloorInterface{
    public void put(Collection<Tile> tiles) {
    }
    public String state() {
        return "floor";
    }
    public Points finishRound() {
        return new Points(2);
    }
}
public class BoardTest {
    private FakePatternLine[] patternLines = new FakePatternLine[2];
    private FakeWallLine[] wallLines = new FakeWallLine[2];
    private FakeFloor floor;
    private Board board;
    @BeforeEach
    public void setUp() {
        patternLines[0] = new FakePatternLine("[]");
        patternLines[1] = new FakePatternLine("[][]");
        wallLines[0] = new FakeWallLine("[1][2]");
        wallLines[1] = new FakeWallLine("[3][4]");
        floor = new FakeFloor();
        board = new Board(patternLines, wallLines, floor);
    }
    @Test
    public void test_put() {
        assertEquals(false, patternLines[0].putCalled, "patternLine[0].put() should not be called.");
        board.put(0, new Tile[0]);
        assertEquals(false, patternLines[1].putCalled, "patternLine[1].put() should not be called.");
        assertEquals(true, patternLines[0].putCalled, "patternLine[0].put() should be called.");
    }
    @Test
    public void test_points() {
        assertEquals(0, board.getPoints().getValue(), "Points should be 0.");
        board.finishRound();
        assertEquals(8, board.getPoints().getValue(), "Points should be 8.");
        board.finishRound();
        assertEquals(16, board.getPoints().getValue(), "Points should be 16.");

    }
    @Test
    public void test_state() {
        assertEquals(board.getPoints() + "\n" +
                "[]   | [1][2]\n" +
                "[][] | [3][4]\n" +
                "floor", board.state(), "board.state() test.");
    }
}