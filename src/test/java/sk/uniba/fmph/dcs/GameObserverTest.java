package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
class FakeObserver implements ObserverInterface {
    private String state;
    @Override
    public void notify(String state){
        this.state = state;
    }
    public String getState() {
        return state;
    }
}
public class GameObserverTest {

    @Before
    public void setUp(){
    }

    @Test
    public void notifyEverybody() {
    }

    @Test
    public void registerObserver() {
    }

    @Test
    public void cancelObserver() {
    }
}