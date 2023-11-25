package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    public void setUp(){
        observer1 = new FakeObserver();
        observer2 = new FakeObserver();
        observer3 = new FakeObserver();
        gameObserver = new GameObserver();
    }

    @Test
    public void testObserver() {

        gameObserver.notifyEverybody("");
        assertNull(observer1.getState(), "Concrete observer's state at start should be empty");
        gameObserver.registerObserver(observer1);
        gameObserver.registerObserver(observer2);
        assertNull(observer1.getState(), "Concrete observer's state at start should be empty");
        gameObserver.notifyEverybody("Start game");
        assertEquals("Start game", observer1.getState(), "Observer's state after notify should something contains");
        assertEquals("Start game", observer2.getState(), "Observer's state after notify should something contains");
        assertNull(observer3.getState(), "Concrete observer's state at start should be empty");
        gameObserver.cancelObserver(observer3);
        gameObserver.notifyEverybody("Start new game");
        assertEquals("Start new game", observer1.getState(), "Observer's state after notify should contains notified state");
        assertEquals("Start new game", observer2.getState(), "Observer's state after notify should contains notified state");
        gameObserver.cancelObserver(observer1);
        gameObserver.registerObserver(observer3);
        gameObserver.notifyEverybody("End of game");
        assertEquals("Start new game", observer1.getState(), "Observer's state after cancelling shouldn't changed");
        assertEquals("End of game", observer2.getState(), "Observer's state after notify should contains notified state");
        assertEquals("End of game", observer3.getState(), "Observer's state after notify should contains notified state");
        gameObserver.cancelObserver(observer2);
        gameObserver.cancelObserver(observer3);
        gameObserver.registerObserver(observer1);
        gameObserver.notifyEverybody("Player 2 won game");
        assertEquals("Player 2 won game", observer1.getState(), "Observer's state after notify should contains notified state");
        assertEquals("End of game", observer2.getState(), "Observer's state after cancelling shouldn't changed");
        assertEquals("End of game", observer3.getState(), "Observer's state after cancelling shouldn't changed");
    }
}