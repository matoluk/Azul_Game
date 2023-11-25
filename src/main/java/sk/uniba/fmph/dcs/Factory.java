package sk.uniba.fmph.dcs;

public class Factory extends TileSource{
    private int capacity;
    private TableCenterAddInterface tableCenter;
    private BagInterface bag;
    Factory(int capacity, BagInterface bag, TableCenterAddInterface tableCenter){
        this.capacity = capacity;
        this.bag = bag;
        this.tableCenter = tableCenter;
    }
    @Override
    public Tile[] take(int idx){
        Tile[] toReturn = super.take(idx);
        Tile[] toTableCenter = tiles.toArray(new Tile[tiles.size()]);
        tiles.clear();
        tableCenter.add(toTableCenter);
        return toReturn;
    }
    @Override
    public void startNewRound(){
        super.startNewRound();
        tiles.addAll(bag.take(capacity));
    }
}
