package jpacman.model;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class MonsterMoveTest {

    Board b;
    Monster m, collega;
    Player p;
    Food f;
    Wall w;

    /**
     * Map:
     *  P
     *  F
     * WMC
     */
    @Before
    public void init() {
        this.m = new Monster();
        this.collega = new Monster();
        this.p = new Player();
        this.f = new Food();
        this.w = new Wall();
        this.b = new Board(10, 10);

        p.occupy(b.getCell(1,2));
        m.occupy(b.getCell(1,0));
        collega.occupy(b.getCell(2,0));
        f.occupy(b.getCell(1,1));
        w.occupy(b.getCell(0,0));

        b.getCell(1,2).setGuest(p);
        b.getCell(1, 0).setGuest(m);
        b.getCell(2, 0).setGuest(collega);
        b.getCell(1, 1).setGuest(f);
        b.getCell(0,0).setGuest(w);
    }

    @Test
    public void testMoveToFruit() {
        
    }

    @Test
    public void testMoveToWall() {

    }

    @Test
    public void testMoveToPlayer() {

    }

    @Test
    public void testMoveAcrossBorder() {

    }

    @Test
    public void testMoveToMonster() {

    }

}
