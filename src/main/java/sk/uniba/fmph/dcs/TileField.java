package sk.uniba.fmph.dcs;

public class TileField {
    private boolean empty = true;
    private Tile color;
    TileField(Tile color){
        this.color = color;
    }
    public Tile getColor(){
        return color;
    }
    public void put(){
        empty = false;
    }
    public void remove(){
        empty = true;
    }
    public boolean isEmpty(){
        return empty;
    }
}
