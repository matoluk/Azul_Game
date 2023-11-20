package sk.uniba.fmph.dcs;

abstract class TyleSource {
    abstract Tile[] take(int idx);

    abstract boolean isEmpty();

    abstract void startNewRound();

    abstract String state();
}