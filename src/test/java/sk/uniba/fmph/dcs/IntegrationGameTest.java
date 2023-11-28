package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Observer implements ObserverInterface{
    private ArrayList<String> states = new ArrayList<>();

    @Override
    public void notify(String newState) {
        states.add(newState);
    }

    public ArrayList<String> getStates() {
        ArrayList<String> toReturn = new ArrayList<>(states);
        states.clear();
        return toReturn;
    }
}

public class IntegrationGameTest {
    private UsedTiles usedTiles;
    private Bag bag;
    private TableCenter tableCenter;
    private Factory[] factories;
    private TableArea tableArea;
    private Floor[] floors;
    private WallLine[][] wallLines;
    private PatternLine[][] patternLines;
    private Board[] boards;
    private GameObserver gameObserver;
    private Observer observer;
    private Game game;

    private final int factoryCount = 5;
    private final int players = 2;
    private final int colors = 5;

    @BeforeEach
    public void setUp(){
        usedTiles = new UsedTiles();
        ArrayList<Tile> bagTiles = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            bagTiles.addAll(Arrays.asList(Tile.RED, Tile.BLUE, Tile.GREEN, Tile.BLACK, Tile.YELLOW));
        bag = new Bag(bagTiles, usedTiles, 1);
        tableCenter = new TableCenter();
        factories = new Factory[factoryCount];
        for (int i = 0; i < factories.length; i++)
            factories[i] = new Factory(4, bag, tableCenter);
        ArrayList<TileSource> tileSources = new ArrayList<>(List.of(factories));
        tileSources.add(tableCenter);
        tableArea = new TableArea(tileSources);

        ArrayList<Points> floorPoints = new ArrayList<>(List.of(
                new Points(-1), new Points(-1),
                new Points(-2), new Points(-2), new Points(-2),
                new Points(-3), new Points(-3)));
        floors = new Floor[players];
        wallLines = new WallLine[players][];
        patternLines = new PatternLine[players][];
        boards = new Board[players];
        for(int player = 0; player < players; player++){
            floors[player] = new Floor(usedTiles, floorPoints);
            wallLines[player] = new WallLine[colors];
            for (int i = 0; i < colors; i++){
                Tile[] tiles = new Tile[colors];
                for (int j = 0; j < colors; j++)
                    tiles[j] = Tile.values()[1 + (j+colors-i) % colors];
                wallLines[player][i] = new WallLine(tiles, null, null);
            }
            for (int i = 1; i < colors; i++) {
                wallLines[player][i].setUp(wallLines[player][i - 1]);
                wallLines[player][i-1].setDown(wallLines[player][i]);
            }
            patternLines[player] = new PatternLine[colors];
            for (int i = 0; i < colors; i++)
                patternLines[player][i] = new PatternLine(i+1,wallLines[player][i], usedTiles, floors[player]);
            boards[player] = new Board(patternLines[player], wallLines[player], floors[player]);
        }

        observer = new Observer();
        gameObserver = new GameObserver();
        gameObserver.registerObserver(observer);

        game = new Game(tableArea, boards, gameObserver);
    }

    @Test
    public void test(){
        ArrayList<String> state = observer.getStates();
        assertEquals(3 + players, state.size());
        assertEquals("Game started", state.get(0));
        assertEquals("TableArea:\n" +
                "RLRB\n" +
                "RIRI\n" +
                "LILR\n" +
                "RIIB\n" +
                "LGLR\n" +
                "S\n", state.get(1));
        for (int i = 0; i < players; i++)
            assertEquals("Player"+i+"'s Board:\n" +
                    "Points [value=0]\n" +
                    "_     | rgibl\n" +
                    "__    | lrgib\n" +
                    "___   | blrgi\n" +
                    "____  | iblrg\n" +
                    "_____ | giblr\n", state.get(2 + i));
        assertEquals("Starts player 0", state.get(2+players));

        game.take(1, 0, 1, 0);
        assertEquals(0, observer.getStates().size());

        game.take(0, 10, 1, 0);
        assertEquals(0, observer.getStates().size());

        game.take(0, 0, Tile.GREEN.ordinal(), 0);
        assertEquals(0, observer.getStates().size());

        game.take(0, 0, Tile.RED.ordinal(), 10);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "RIRI\n" +
                "LILR\n" +
                "RIIB\n" +
                "LGLR\n" +
                "SLB\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgibl\n" +
                "__    | lrgib\n" +
                "___   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "RR", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(0, 1, 1, 0);
        assertEquals(0, observer.getStates().size());

        game.take(1, 3, Tile.YELLOW.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "RIRI\n" +
                "LILR\n" +
                "\n" +
                "LGLR\n" +
                "SLBRB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "I     | rgibl\n" +
                "__    | lrgib\n" +
                "___   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "I", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 1, Tile.YELLOW.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "LILR\n" +
                "\n" +
                "LGLR\n" +
                "SLBRBRR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgibl\n" +
                "II    | lrgib\n" +
                "___   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "RR", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.RED.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "LILR\n" +
                "\n" +
                "LGLR\n" +
                "LBB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "I     | rgibl\n" +
                "RR    | lrgib\n" +
                "___   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "ISR", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 2, Tile.YELLOW.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "LGLR\n" +
                "LBBLLR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgibl\n" +
                "II    | lrgib\n" +
                "___   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "RRI", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLACK.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "LGLR\n" +
                "BBR\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "I     | rgibl\n" +
                "RR    | lrgib\n" +
                "LLL   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "ISR", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 4, Tile.GREEN.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "BBRLLR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=0]\n" +
                "G     | rgibl\n" +
                "II    | lrgib\n" +
                "___   | blrgi\n" +
                "____  | iblrg\n" +
                "_____ | giblr\n" +
                "RRI", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLACK.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "BBRR\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "I     | rgibl\n" +
                "RR    | lrgib\n" +
                "LLL   | blrgi\n" +
                "LL__  | iblrg\n" +
                "_____ | giblr\n" +
                "ISR", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.BLUE.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "RR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=0]\n" +
                "G     | rgibl\n" +
                "II    | lrgib\n" +
                "___   | blrgi\n" +
                "BB__  | iblrg\n" +
                "_____ | giblr\n" +
                "RRI", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.RED.ordinal(), 4);
        state = observer.getStates();
        assertEquals(6 + players, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "I     | rgibl\n" +
                "RR    | lrgib\n" +
                "LLL   | blrgi\n" +
                "LL__  | iblrg\n" +
                "RR___ | giblr\n" +
                "ISR", state.get(1));
        assertEquals("Round ended", state.get(2));
        assertEquals("Player0's Board:\n" +
                "Points [value=-2]\n" +
                "_     | rGibl\n" +
                "__    | lrgIb\n" +
                "___   | blrgi\n" +
                "BB__  | iblrg\n" +
                "_____ | giblr\n" +
                "", state.get(3));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgIbl\n" +
                "__    | lRgib\n" +
                "___   | bLrgi\n" +
                "LL__  | iblrg\n" +
                "RR___ | giblr\n" +
                "", state.get(4));
        assertEquals("New round", state.get(5));
        assertEquals("TableArea:\n" +
                "RRBL\n" +
                "ILBL\n" +
                "IBIL\n" +
                "LRBB\n" +
                "BRIG\n" +
                "S\n", state.get(6));
        assertEquals("Starts player 1", state.get(7));

        assertEquals(100, bag.state().length() + 4 + 7 + 20);
        assertEquals("UsedTiles{count=0, usedTiles=[]}", usedTiles.state());

        game.take(1, 1, Tile.BLACK.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "RRBL\n" +
                "\n" +
                "IBIL\n" +
                "LRBB\n" +
                "BRIG\n" +
                "SIB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgIbl\n" +
                "__    | lRgib\n" +
                "___   | bLrgi\n" +
                "LLLL  | iblrg\n" +
                "RR___ | giblr\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 3, Tile.BLUE.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "RRBL\n" +
                "\n" +
                "IBIL\n" +
                "\n" +
                "BRIG\n" +
                "SIBLR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=-2]\n" +
                "_     | rGibl\n" +
                "__    | lrgIb\n" +
                "___   | blrgi\n" +
                "BBBB  | iblrg\n" +
                "_____ | giblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 0, Tile.RED.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "IBIL\n" +
                "\n" +
                "BRIG\n" +
                "SIBLRBL\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgIbl\n" +
                "__    | lRgib\n" +
                "___   | bLrgi\n" +
                "LLLL  | iblrg\n" +
                "RRRR_ | giblr\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 2, Tile.YELLOW.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "BRIG\n" +
                "SIBLRBLBL\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=-2]\n" +
                "I     | rGibl\n" +
                "__    | lrgIb\n" +
                "___   | blrgi\n" +
                "BBBB  | iblrg\n" +
                "_____ | giblr\n" +
                "I", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 4, Tile.RED.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "SIBLRBLBLBIG\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgIbl\n" +
                "__    | lRgib\n" +
                "___   | bLrgi\n" +
                "LLLL  | iblrg\n" +
                "RRRRR | giblr\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.BLACK.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IBRBBBIG\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=-2]\n" +
                "I     | rGibl\n" +
                "__    | lrgIb\n" +
                "LLL   | blrgi\n" +
                "BBBB  | iblrg\n" +
                "_____ | giblr\n" +
                "IS", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLUE.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IRIG\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "_     | rgIbl\n" +
                "__    | lRgib\n" +
                "BBB   | bLrgi\n" +
                "LLLL  | iblrg\n" +
                "RRRRR | giblr\n" +
                "B", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.YELLOW.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "RG\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=-2]\n" +
                "I     | rGibl\n" +
                "__    | lrgIb\n" +
                "LLL   | blrgi\n" +
                "BBBB  | iblrg\n" +
                "II___ | giblr\n" +
                "IS", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.GREEN.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "R\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=0]\n" +
                "G     | rgIbl\n" +
                "__    | lRgib\n" +
                "BBB   | bLrgi\n" +
                "LLLL  | iblrg\n" +
                "RRRRR | giblr\n" +
                "B", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.RED.ordinal(), 1);
        state = observer.getStates();
        assertEquals(6 + players, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=-2]\n" +
                "I     | rGibl\n" +
                "R_    | lrgIb\n" +
                "LLL   | blrgi\n" +
                "BBBB  | iblrg\n" +
                "II___ | giblr\n" +
                "IS", state.get(1));
        assertEquals("Round ended", state.get(2));
        assertEquals("Player0's Board:\n" +
                "Points [value=1]\n" +
                "_     | rGIbl\n" +
                "R_    | lrgIb\n" +
                "___   | bLrgi\n" +
                "____  | iBlrg\n" +
                "II___ | giblr\n" +
                "", state.get(3));
        assertEquals("Player1's Board:\n" +
                "Points [value=8]\n" +
                "_     | rGIbl\n" +
                "__    | lRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "_____ | giblR\n" +
                "", state.get(4));
        assertEquals("New round", state.get(5));
        assertEquals("TableArea:\n" +
                "BLII\n" +
                "LLLL\n" +
                "IIIB\n" +
                "RIRL\n" +
                "GLGB\n" +
                "S\n", state.get(6));
        assertEquals("Starts player 0", state.get(7));
    }
}
