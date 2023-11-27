package sk.uniba.fmph.dcs;

public interface ScoringRule {
    int calculatePoints(TileField[][] wall);
}
