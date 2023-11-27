package sk.uniba.fmph.dcs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WallLineTest {
    WallLine[] wallLines;
    @BeforeEach
    public void setUp() {
        wallLines = new WallLine[5];
        for (int i = 0; i < 5; i++){

            Tile[] tiles = new Tile[5];
            int j = i;
            for (Tile tile : Tile.values()){
                if (tile != Tile.STARTING_PLAYER){
                    tiles[j] = tile;
                    j++;
                    if (j >= 5)
                        j = 0;
                }
            }
            WallLine lineUp = null;
            WallLine lineDown = null;
            wallLines[i] = new WallLine(tiles, lineUp, lineDown);
        }
        for (int i = 0; i < 5; i++){
            if(i == 0){
                wallLines[i].setUp(null);
                wallLines[i].setDown(wallLines[i+1]);
            }
            else if(i == 4) {
                wallLines[i].setDown(null);
                wallLines[i].setUp(wallLines[i-1]);
            }
            else{
                wallLines[i].setUp(wallLines[i-1]);
                wallLines[i].setDown(wallLines[i+1]);
            }
        }
    }

    @Test
    public void test_wallLines(){
        for (int i = 0; i < 5; i++) {
            assertEquals(wallLines[i].state().toLowerCase(), wallLines[i].state(), "WallLine should be empty when created.");
            assertEquals(true, wallLines[i].canPutTile(Tile.BLACK), "The output of canPut should be true");
        }
        assertEquals(1, wallLines[2].putTile(Tile.BLACK).getValue(), "Method should add 1 point");
        assertEquals(false, wallLines[2].canPutTile(Tile.BLACK), "The output of canPut should be false");
        assertEquals("bLrgi", wallLines[2].state(), "WallLine state() test");
        assertEquals(2, wallLines[1].putTile(Tile.RED).getValue(), "Method should add 2 points");
        wallLines[0].putTile(Tile.BLACK);
        assertEquals(1, wallLines[0].putTile(Tile.RED).getValue(), "Method should add 1 point");
        assertEquals(5, wallLines[0].putTile(Tile.GREEN).getValue(), "Method should add 5 points");
        assertEquals(2, wallLines[0].putTile(Tile.BLUE).getValue(), "Method should add 2 points");
        wallLines[1].putTile(Tile.GREEN);
        wallLines[3].putTile(Tile.BLACK);
        assertEquals(7, wallLines[0].putTile(Tile.YELLOW).getValue(), "Method should add 7 points");
        assertEquals(2, wallLines[4].putTile(Tile.BLUE).getValue(), "Method should add 2 points");
        assertEquals(7, wallLines[2].putTile(Tile.RED).getValue(), "Method should add 7 points");

        assertEquals("bLRgi", wallLines[2].state(), "WallLine state() test");
        assertEquals("RGIBL", wallLines[0].state(), "WallLine state() test");
    }

}
