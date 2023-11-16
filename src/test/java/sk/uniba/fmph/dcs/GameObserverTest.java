package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
class FakeObserver implements ObserverInterface {
    private String state = null;
    @Override
    public void notify(String state){
        this.state = state;
    }
    public String getState() {
        return state;
    }
}
public class GameObserverTest {
    FakeObserver observer1, observer2, observer3;
    GameObserver gameObserver;

    @Before
    public void setUp(){
        observer1 = new FakeObserver();
        observer2 = new FakeObserver();
        observer3 = new FakeObserver();
        gameObserver = new GameObserver();
    }

    @Test
    public void testObserver() {
    }
}