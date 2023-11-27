package sk.uniba.fmph.dcs;

public class WallLine implements WallLineInterface{
    private WallLine lineUp;
    private WallLine lineDown;
    private TileField[] wallLine;

    public WallLine(Tile[] tiles, WallLine lineUp, WallLine lineDown) {
        this.lineDown = lineDown;
        this.lineUp = lineUp;
        wallLine = new TileField[tiles.length];
        for(int i = 0; i < tiles.length; i++)
            wallLine[i] = new TileField(tiles[i]);
    }
    public void setUp(WallLine line){
        this.lineUp = line;
    }
    public void setDown(WallLine line){
        this.lineDown = line;
    }
    public WallLine getUp(){
        return lineUp;
    }
    public WallLine getDown(){
        return lineDown;
    }

    public boolean canPutTile(Tile color) {
        for (TileField tileField : wallLine)
            if (tileField.isEmpty() && (tileField.getColor() == color))
                return true;
        return false;
    }

    public TileField[] getTiles() {
        return wallLine;
    }

    public Points putTile(Tile color){
        if (!canPutTile(color))
            return null;
        int idx;
        for (idx = 0; idx < wallLine.length; idx++)
            if (wallLine[idx].isEmpty() && (wallLine[idx].getColor() == color))
                break;
        wallLine[idx].put();

        int inRow = inRow(idx);
        int inCol = inCol(idx);
        if (inRow == 1 || inCol == 1)
            return new Points(inRow + inCol - 1);
        return new Points(inRow + inCol);
    }
    private int inRow(int idx) {
        int inRow = 1;
        for (int left = idx - 1; left >= 0; left--){
            if(wallLine[left].isEmpty())
                break;
            else
                inRow++;
        }
        for (int right = idx + 1; right < wallLine.length; right++){
            if(wallLine[right].isEmpty())
                break;
            else
                inRow++;
        }
        return inRow;
    }
    private int inCol(int idx){
        int inCol = 1;
        for (WallLine line = lineUp; line != null; line = line.getUp()){
            if(line.getTiles()[idx].isEmpty())
                break;
            else
                inCol++;
        }
        for (WallLine line = lineDown; line != null; line = line.getDown()){
            if(line.getTiles()[idx].isEmpty())
                break;
            else
                inCol++;
        }
        return inCol;
    }

    public String state(){
        StringBuilder toReturn = new StringBuilder();
        for (TileField field : wallLine){
            if (field.isEmpty())
                toReturn.append(field.getColor().toString().toLowerCase());
            else
                toReturn.append(field.getColor().toString().toUpperCase());
        }
        return toReturn.toString();
    }
}