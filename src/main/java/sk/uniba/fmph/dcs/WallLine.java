package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class WallLine {
    private ArrayList tiles;
    private int countColours;
    private WallLine lineUp;
    private WallLine lineDown;
    private Tile[] currentWallLine;

    public WallLine(Tile[] tiles, WallLine lineup, WallLine linedown) {
        this.lineDown = linedown;
        this.lineUp = lineup;
        this.tiles = new ArrayList<>(Arrays.asList(tiles));

        this.countColours = Tile.values().length - 1;
        this.currentWallLine = new Tile[countColours];
    }

    public boolean canPutTile(Tile tile) {
        int index = tiles.indexOf(tile);
        if (index != -1) {
            if (currentWallLine[index] != null) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Optional<Tile>[] getTiles() {
        Optional<Tile>[] line = new Optional[countColours];
        for (int i = 0; i < countColours; i++) {
            line[i] = Optional.ofNullable(currentWallLine[i]);
        }
        return line;
    }

//    public Points putTile(Tile tile){
//        int p = 0;
//        int idx = tiles.indexOf(tile);
//        if(canPutTile(tile)) {
//            currentWallLine[idx] = tile;
//            p++;
//            if(idx != 0){
//                int i = idx - 1;
//                while(i >= 0 & currentWallLine[i] != null) {
//                    p++;
//                    i--;
//                }
//            }
//            if(idx != countColours - 1){
//                int i = idx + 1;
//                while(i <= countColours - 1  & currentWallLine[i] != null){
//                    p++;
//                    i++;
//                }
//            }
//            if(lineUp != null){
//                Optional<Tile>[] up = lineUp.getTiles();
//                if(up[idx].isPresent()){
//                    p+=columnUp(lineUp, idx, p);
//                }
//            }
//        }
//
//    }
//    private int columnUp(WallLine wallLine, int idx, int p){
//        if(wallLine.lineUp != null){
//            Optional<Tile>[] up = wallLine.lineUp.getTiles();
//            if(up[idx].isPresent()){
//                p++;
//                p+=columnUp(wallLine.lineUp, idx, p);
//                return p;
//            }
//        }
//    }

    public String state(){
        StringBuilder toReturn = new StringBuilder();
        for (Tile tile : currentWallLine){
            if (tile == null)
                toReturn.append(" ");
            else
                toReturn.append(tile);
        }
        return toReturn.toString();
    }
}