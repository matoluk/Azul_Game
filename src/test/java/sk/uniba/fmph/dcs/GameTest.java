package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class FakeTableArea implements TableAreaInterface{
    public Tile tile = Tile.RED;
    public boolean start = false;
    public boolean end = false;
    @Override
    public Tile[] take(int sourceId, int idx) {
        Tile[] toReturn = {tile, start ? Tile.STARTING_PLAYER : null};
        tile = null;
        return toReturn;
    }
    @Override
    public boolean isRoundEnd() {
        return end;
    }
    @Override
    public void startNewRound() {
        tile = Tile.BLUE;
    }
    @Override
    public String state() {
        if (tile == null)
            return "";
        return tile.toString();
    }
}
class FakeBoard implements BoardInterface{
    public Points points = new Points(0);
    public Tile tile = null;
    public FinishRoundResult fin = FinishRoundResult.NORMAL;
    @Override
    public void put(int destinationIdx, Tile[] tiles) {
        tile = tiles[0];
    }
    @Override
    public FinishRoundResult finishRound() {
        if (tile != null)
            points = new Points(points.getValue() + 1);
        return fin;
    }
    @Override
    public void endGame() {
        points = new Points(points.getValue() + 100);
    }
    @Override
    public String state() {
        if (tile == null)
            return points.toString()+" [ ]";
        return points.toString()+" ["+tile.toString()+"]";
    }

    @Override
    public Points getPoints() {
        return points;
    }
}
class FakeObserverNotify implements ObserverNotifyInterface{
    public ArrayList<String> messages = new ArrayList<>();
    @Override
    public void notifyEverybody(String state) {
        messages.add(state);
    }
}
public class GameTest {
    FakeTableArea tableArea;
    FakeBoard[] boards;
    FakeObserverNotify observer;
    Game game;
    @BeforeEach
    public void setUp() {
        tableArea = new FakeTableArea();
        boards = new FakeBoard[3];
        for (int i = 0; i < 3; i++)
            boards[i] = new FakeBoard();
        observer = new FakeObserverNotify();
        game = new Game(tableArea, boards, observer);
    }
    @Test
    public void test_startGame(){
        assertEquals(6, observer.messages.size());
        assertEquals("Game started", observer.messages.get(0));
        assertEquals("TableArea:\nR", observer.messages.get(1));
        for (int i = 0; i < 3; i++)
            assertEquals("Player"+i+"'s Board:\nPoints [value=0] [ ]", observer.messages.get(2 + i));
        assertEquals("Starts player 0", observer.messages.get(5));
    }
    @Test
    public void test_take(){
        observer.messages.clear();
        assertFalse(game.take(1, 1,1,0));
        assertTrue(observer.messages.isEmpty());
        assertTrue(game.take(0, 1,1,0));
        assertEquals(3, observer.messages.size());
        assertEquals("TableArea:\n", observer.messages.get(0));
        assertEquals("Player0's Board:\nPoints [value=0] [R]", observer.messages.get(1));
        assertEquals("Current player 1", observer.messages.get(2));
    }
    @Test
    public void test_endRound(){
        observer.messages.clear();
        tableArea.end = true;
        assertTrue(game.take(0, 1,1,0));
        assertEquals(9, observer.messages.size());
        assertEquals("TableArea:\n", observer.messages.get(0));
        assertEquals("Player0's Board:\nPoints [value=0] [R]", observer.messages.get(1));

        assertEquals("Round ended", observer.messages.get(2));
        assertEquals("Player0's Board:\nPoints [value=1] [R]", observer.messages.get(3));
        assertEquals("Player1's Board:\nPoints [value=0] [ ]", observer.messages.get(4));
        assertEquals("Player2's Board:\nPoints [value=0] [ ]", observer.messages.get(5));

        assertEquals("New round", observer.messages.get(6));
        assertEquals("TableArea:\nB", observer.messages.get(7));
        assertEquals("Starts player 0", observer.messages.get(8));
    }
    @Test
    public void test_startPlayer(){
        assertTrue(game.take(0, 1,1,0));
        boards[0].tile = null;
        tableArea.tile = Tile.GREEN;
        assertTrue(game.take(1, 1,1,0));
        assertEquals(12, observer.messages.size());
        observer.messages.clear();

        tableArea.tile = Tile.YELLOW;
        tableArea.start = true;
        tableArea.end = true;
        assertTrue(game.take(2, 1,1,0));
        assertEquals(9, observer.messages.size());
        assertEquals("TableArea:\n", observer.messages.get(0));
        assertEquals("Player2's Board:\nPoints [value=0] [I]", observer.messages.get(1));

        assertEquals("Round ended", observer.messages.get(2));
        assertEquals("Player0's Board:\nPoints [value=0] [ ]", observer.messages.get(3));
        assertEquals("Player1's Board:\nPoints [value=1] [G]", observer.messages.get(4));
        assertEquals("Player2's Board:\nPoints [value=1] [I]", observer.messages.get(5));

        assertEquals("New round", observer.messages.get(6));
        assertEquals("TableArea:\nB", observer.messages.get(7));
        assertEquals("Starts player 2", observer.messages.get(8));
    }
    @Test
    public void test_endGame(){
        observer.messages.clear();
        tableArea.end = true;
        boards[1].fin = FinishRoundResult.GAME_FINISHED;

        assertTrue(game.take(0, 1,1,0));
        assertEquals(11, observer.messages.size());
        assertEquals("TableArea:\n", observer.messages.get(0));
        assertEquals("Player0's Board:\nPoints [value=0] [R]", observer.messages.get(1));

        assertEquals("Round ended", observer.messages.get(2));
        assertEquals("Player0's Board:\nPoints [value=1] [R]", observer.messages.get(3));
        assertEquals("Player1's Board:\nPoints [value=0] [ ]", observer.messages.get(4));
        assertEquals("Player2's Board:\nPoints [value=0] [ ]", observer.messages.get(5));

        assertEquals("Game ended", observer.messages.get(6));
        assertEquals("Player0: 101 points", observer.messages.get(7));
        assertEquals("Player1: 100 points", observer.messages.get(8));
        assertEquals("Player2: 100 points", observer.messages.get(9));
        assertEquals("Player0 wins", observer.messages.get(10));
    }
    @Test
    public void test_twoWinners() {
        observer.messages.clear();
        tableArea.end = true;
        boards[1].fin = FinishRoundResult.GAME_FINISHED;

        boards[2].tile = Tile.BLUE;
        assertTrue(game.take(0, 1,1,0));
        assertEquals(12, observer.messages.size());
        assertEquals("Game ended", observer.messages.get(6));
        assertEquals("Player0: 101 points", observer.messages.get(7));
        assertEquals("Player1: 100 points", observer.messages.get(8));
        assertEquals("Player2: 101 points", observer.messages.get(9));
        assertEquals("Player0 wins", observer.messages.get(10));
        assertEquals("Player2 wins", observer.messages.get(11));
    }
    @Test
    public void test_takeAfterGameEnded() {
        tableArea.end = true;
        boards[1].fin = FinishRoundResult.GAME_FINISHED;
        assertTrue(game.take(0, 1,1,0));
        assertEquals(17, observer.messages.size());
        assertEquals("Game ended", observer.messages.get(12));

        for (int player = 0; player < 3; player++)
            assertFalse(game.take(player, 1,1,0));
    }
}
