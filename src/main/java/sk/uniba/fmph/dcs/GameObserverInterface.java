package sk.uniba.fmph.dcs;

public interface GameObserverInterface {
    void registerObserver(ObserverInterface observer);
    void cancelObserver(ObserverInterface observer);
}
