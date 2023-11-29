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

        game.take(0, 2, Tile.YELLOW.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "BLII\n" +
                "LLLL\n" +
                "\n" +
                "RIRL\n" +
                "GLGB\n" +
                "SB\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=1]\n" +
                "_     | rGIbl\n" +
                "R_    | lrgIb\n" +
                "___   | bLrgi\n" +
                "____  | iBlrg\n" +
                "IIIII | giblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 1, Tile.BLACK.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "BLII\n" +
                "\n" +
                "\n" +
                "RIRL\n" +
                "GLGB\n" +
                "SB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=8]\n" +
                "_     | rGIbl\n" +
                "__    | lRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "LLLL_ | giblR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 3, Tile.RED.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "BLII\n" +
                "\n" +
                "\n" +
                "\n" +
                "GLGB\n" +
                "SBIL\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=1]\n" +
                "_     | rGIbl\n" +
                "RR    | lrgIb\n" +
                "___   | bLrgi\n" +
                "____  | iBlrg\n" +
                "IIIII | giblr\n" +
                "R", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 4, Tile.BLACK.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "BLII\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "SBILGGB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=8]\n" +
                "_     | rGIbl\n" +
                "__    | lRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "LLLLL | giblR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 0, Tile.BLUE.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "SBILGGBLII\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=1]\n" +
                "B     | rGIbl\n" +
                "RR    | lrgIb\n" +
                "___   | bLrgi\n" +
                "____  | iBlrg\n" +
                "IIIII | giblr\n" +
                "R", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLACK.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "BIGGBII\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=8]\n" +
                "_     | rGIbl\n" +
                "LL    | lRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "LLLLL | giblR\n" +
                "S", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.YELLOW.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "BGGB\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=1]\n" +
                "B     | rGIbl\n" +
                "RR    | lrgIb\n" +
                "III   | bLrgi\n" +
                "____  | iBlrg\n" +
                "IIIII | giblr\n" +
                "R", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLUE.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "GG\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=8]\n" +
                "B     | rGIbl\n" +
                "LL    | lRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "LLLLL | giblR\n" +
                "SB", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.GREEN.ordinal(), 3);
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
                "Points [value=1]\n" +
                "B     | rGIbl\n" +
                "RR    | lrgIb\n" +
                "III   | bLrgi\n" +
                "GG__  | iBlrg\n" +
                "IIIII | giblr\n" +
                "R", state.get(1));
        assertEquals("Round ended", state.get(2));
        assertEquals("Player0's Board:\n" +
                "Points [value=15]\n" +
                "_     | rGIBl\n" +
                "__    | lRgIb\n" +
                "___   | bLrgI\n" +
                "GG__  | iBlrg\n" +
                "_____ | gIblr\n" +
                "", state.get(3));
        assertEquals("Player1's Board:\n" +
                "Points [value=15]\n" +
                "_     | rGIBl\n" +
                "__    | LRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "_____ | gibLR\n" +
                "", state.get(4));
        assertEquals("New round", state.get(5));
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "LIBR\n" +
                "GGGB\n" +
                "RLGR\n" +
                "RRGB\n" +
                "S\n", state.get(6));
        assertEquals("Starts player 1", state.get(7));

        game.take(1, 2, Tile.GREEN.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "LIBR\n" +
                "\n" +
                "RLGR\n" +
                "RRGB\n" +
                "SB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=15]\n" +
                "_     | rGIBl\n" +
                "GG    | LRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "_____ | gibLR\n" +
                "G", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 3, Tile.BLACK.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "LIBR\n" +
                "\n" +
                "\n" +
                "RRGB\n" +
                "SBRGR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "__    | lRgIb\n" +
                "___   | bLrgI\n" +
                "GG__  | iBlrg\n" +
                "_____ | gIblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 1, Tile.BLACK.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "\n" +
                "\n" +
                "\n" +
                "RRGB\n" +
                "SBRGRIBR\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "GG    | LRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "_____ | gibLR\n" +
                "G", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 4, Tile.RED.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "SBRGRIBRGB\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "__    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "GG__  | iBlrg\n" +
                "_____ | gIblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLUE.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "RGRIRG\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "GG    | LRgib\n" +
                "___   | BLrgi\n" +
                "____  | ibLrg\n" +
                "BBB__ | gibLR\n" +
                "GS", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.GREEN.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "RRIR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "__    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "GGGG  | iBlrg\n" +
                "_____ | gIblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.RED.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "IILB\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "I\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "GG    | LRgib\n" +
                "RRR   | BLrgi\n" +
                "____  | ibLrg\n" +
                "BBB__ | gibLR\n" +
                "GS", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 0, Tile.BLUE.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IIIL\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "B_    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "GGGG  | iBlrg\n" +
                "_____ | gIblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLACK.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "III\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "GG    | LRgib\n" +
                "RRR   | BLrgi\n" +
                "____  | ibLrg\n" +
                "BBB__ | gibLR\n" +
                "GSL", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.YELLOW.ordinal(), 1);
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
                "Points [value=15]\n" +
                "L     | rGIBl\n" +
                "B_    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "GGGG  | iBlrg\n" +
                "_____ | gIblr\n" +
                "III", state.get(1));
        assertEquals("Round ended", state.get(2));
        assertEquals("Player0's Board:\n" +
                "Points [value=17]\n" +
                "_     | rGIBL\n" +
                "B_    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "____  | iBlrG\n" +
                "_____ | gIblr\n" +
                "", state.get(3));
        assertEquals("Player1's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "__    | LRGib\n" +
                "___   | BLRgi\n" +
                "____  | ibLrg\n" +
                "BBB__ | gibLR\n" +
                "", state.get(4));
        assertEquals("New round", state.get(5));
        assertEquals("TableArea:\n" +
                "BIIL\n" +
                "IBLR\n" +
                "GBRG\n" +
                "IIRG\n" +
                "RRGG\n" +
                "S\n", state.get(6));
        assertEquals("Starts player 1", state.get(7));

        game.take(1, 0, Tile.BLUE.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "IBLR\n" +
                "GBRG\n" +
                "IIRG\n" +
                "RRGG\n" +
                "SIIL\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "__    | LRGib\n" +
                "___   | BLRgi\n" +
                "____  | ibLrg\n" +
                "BBBB_ | gibLR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 4, Tile.RED.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "IBLR\n" +
                "GBRG\n" +
                "IIRG\n" +
                "\n" +
                "SIILGG\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=17]\n" +
                "_     | rGIBL\n" +
                "B_    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "____  | iBlrG\n" +
                "RR___ | gIblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 2, Tile.BLUE.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "IBLR\n" +
                "\n" +
                "IIRG\n" +
                "\n" +
                "SIILGGGRG\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "__    | LRGib\n" +
                "___   | BLRgi\n" +
                "____  | ibLrg\n" +
                "BBBBB | gibLR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 1, Tile.RED.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "IIRG\n" +
                "\n" +
                "SIILGGGRGIBL\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=17]\n" +
                "_     | rGIBL\n" +
                "B_    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "____  | iBlrG\n" +
                "RRR__ | gIblr\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 3, Tile.YELLOW.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "SIILGGGRGIBLRG\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "II    | LRGib\n" +
                "___   | BLRgi\n" +
                "____  | ibLrg\n" +
                "BBBBB | gibLR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.RED.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IILGGGGIBLG\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=17]\n" +
                "_     | rGIBL\n" +
                "B_    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "____  | iBlrG\n" +
                "RRRRR | gIblr\n" +
                "S", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.GREEN.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IILIBL\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "II    | LRGib\n" +
                "___   | BLRgi\n" +
                "GGGG  | ibLrg\n" +
                "BBBBB | gibLR\n" +
                "G", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.BLUE.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IILIL\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=17]\n" +
                "_     | rGIBL\n" +
                "BB    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "____  | iBlrG\n" +
                "RRRRR | gIblr\n" +
                "S", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.YELLOW.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "LL\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "II    | LRGib\n" +
                "III   | BLRgi\n" +
                "GGGG  | ibLrg\n" +
                "BBBBB | gibLR\n" +
                "G", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.BLACK.ordinal(), 3);
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
                "Points [value=17]\n" +
                "_     | rGIBL\n" +
                "BB    | lRgIb\n" +
                "RR_   | bLrgI\n" +
                "LL__  | iBlrG\n" +
                "RRRRR | gIblr\n" +
                "S", state.get(1));
        assertEquals("Round ended", state.get(2));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "_     | rGIBL\n" +
                "__    | lRgIB\n" +
                "RR_   | bLrgI\n" +
                "LL__  | iBlrG\n" +
                "_____ | gIblR\n" +
                "", state.get(3));
        assertEquals("Player1's Board:\n" +
                "Points [value=44]\n" +
                "_     | rGIBL\n" +
                "__    | LRGIb\n" +
                "___   | BLRgI\n" +
                "____  | ibLrG\n" +
                "_____ | giBLR\n" +
                "", state.get(4));
        assertEquals("New round", state.get(5));
        assertEquals("TableArea:\n" +
                "GRRG\n" +
                "IGRB\n" +
                "GGIL\n" +
                "IRGL\n" +
                "IRLB\n" +
                "S\n", state.get(6));
        assertEquals("Starts player 0", state.get(7));

        game.take(0, 1, Tile.RED.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "GRRG\n" +
                "\n" +
                "GGIL\n" +
                "IRGL\n" +
                "IRLB\n" +
                "SIGB\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "R     | rGIBL\n" +
                "__    | lRgIB\n" +
                "RR_   | bLrgI\n" +
                "LL__  | iBlrG\n" +
                "_____ | gIblR\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 2, Tile.GREEN.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "GRRG\n" +
                "\n" +
                "\n" +
                "IRGL\n" +
                "IRLB\n" +
                "SIGBIL\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=44]\n" +
                "_     | rGIBL\n" +
                "__    | LRGIb\n" +
                "GG_   | BLRgI\n" +
                "____  | ibLrG\n" +
                "_____ | giBLR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 0, Tile.GREEN.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "IRGL\n" +
                "IRLB\n" +
                "SIGBILRR\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "R     | rGIBL\n" +
                "GG    | lRgIB\n" +
                "RR_   | bLrgI\n" +
                "LL__  | iBlrG\n" +
                "_____ | gIblR\n" +
                "", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 4, Tile.RED.ordinal(), 0);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "IRGL\n" +
                "\n" +
                "SIGBILRRILB\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=44]\n" +
                "R     | rGIBL\n" +
                "__    | LRGIb\n" +
                "GG_   | BLRgI\n" +
                "____  | ibLrG\n" +
                "_____ | giBLR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.BLACK.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "IRGL\n" +
                "\n" +
                "IGBIRRIB\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "R     | rGIBL\n" +
                "GG    | lRgIB\n" +
                "RR_   | bLrgI\n" +
                "LLLL  | iBlrG\n" +
                "_____ | gIblR\n" +
                "S", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.BLUE.ordinal(), 1);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "IRGL\n" +
                "\n" +
                "IGIRRI\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=44]\n" +
                "R     | rGIBL\n" +
                "BB    | LRGIb\n" +
                "GG_   | BLRgI\n" +
                "____  | ibLrG\n" +
                "_____ | giBLR\n" +
                "", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 3, Tile.RED.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IGIRRIIGL\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "R     | rGIBL\n" +
                "GG    | lRgIB\n" +
                "RRR   | bLrgI\n" +
                "LLLL  | iBlrG\n" +
                "_____ | gIblR\n" +
                "S", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.GREEN.ordinal(), 2);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IIRRIIL\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=44]\n" +
                "R     | rGIBL\n" +
                "BB    | LRGIb\n" +
                "GGG   | BLRgI\n" +
                "____  | ibLrG\n" +
                "_____ | giBLR\n" +
                "G", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.BLACK.ordinal(), 4);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "IIRRII\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "R     | rGIBL\n" +
                "GG    | lRgIB\n" +
                "RRR   | bLrgI\n" +
                "LLLL  | iBlrG\n" +
                "L____ | gIblR\n" +
                "S", state.get(1));
        assertEquals("Current player 1", state.get(2));

        game.take(1, 5, Tile.YELLOW.ordinal(), 3);
        state = observer.getStates();
        assertEquals(3, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "RR\n", state.get(0));
        assertEquals("Player1's Board:\n" +
                "Points [value=44]\n" +
                "R     | rGIBL\n" +
                "BB    | LRGIb\n" +
                "GGG   | BLRgI\n" +
                "IIII  | ibLrG\n" +
                "_____ | giBLR\n" +
                "G", state.get(1));
        assertEquals("Current player 0", state.get(2));

        game.take(0, 5, Tile.RED.ordinal(), 5);
        state = observer.getStates();
        assertEquals(9, state.size());
        assertEquals("TableArea:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n", state.get(0));
        assertEquals("Player0's Board:\n" +
                "Points [value=27]\n" +
                "R     | rGIBL\n" +
                "GG    | lRgIB\n" +
                "RRR   | bLrgI\n" +
                "LLLL  | iBlrG\n" +
                "L____ | gIblR\n" +
                "SRR", state.get(1));
        assertEquals("Round ended", state.get(2));
        assertEquals("Player0's Board:\n" +
                "Points [value=45]\n" +
                "_     | RGIBL\n" +
                "__    | lRGIB\n" +
                "___   | bLRgI\n" +
                "____  | iBLrG\n" +
                "L____ | gIblR\n" +
                "", state.get(3));
        assertEquals("Player1's Board:\n" +
                "Points [value=73]\n" +
                "_     | RGIBL\n" +
                "__    | LRGIB\n" +
                "___   | BLRGI\n" +
                "____  | IbLrG\n" +
                "_____ | giBLR\n" +
                "", state.get(4));
        assertEquals("Game ended", state.get(5));
        assertEquals("Player0: 61 points", state.get(6));
        assertEquals("Player1: 103 points", state.get(7));
        assertEquals("Player1 wins", state.get(8));
    }
}
