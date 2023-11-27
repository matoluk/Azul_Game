package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        bag = new Bag(bagTiles, usedTiles);
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
                if (i == 0)
                    wallLines[player][i] = new WallLine(tiles, null, wallLines[player][i+1]);
                else if (i == colors - 1)
                    wallLines[player][i] = new WallLine(tiles,  wallLines[player][i-1],  wallLines[player][i+1]);
                else
                    wallLines[player][i] = new WallLine(tiles,  wallLines[player][i-1],  null);
            }
            patternLines[player] = new PatternLine[colors];
            for (int i = 0; i < colors; i++)
                patternLines[player][i] = new PatternLine(i+1,wallLines[player][i], usedTiles, floors[player]);
            boards[player] = new Board(patternLines[player], wallLines[player], floors[player]);
        }

        game = new Game(tableArea, boards, gameObserver);
    }

    @Test
    public void test(){

    }
}
