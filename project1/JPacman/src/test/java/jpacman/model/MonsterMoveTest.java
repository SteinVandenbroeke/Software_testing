package jpacman.model;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class MonsterMoveTest {

    Board b;
    Monster m, n, collega;
    Player p;
    Food f;
    Wall w;
    @Mock
    Cell outsideBoard;
    MonsterMove move;

    /**
     * Map:
     * NP
     * SF
     * WMC
     */
    @Before
    public void init() {
        this.m = new Monster();
        this.n = new Monster();
        this.collega = new Monster();
        this.p = new Player();
        this.f = new Food();
        this.w = new Wall();
        this.b = new Board(10, 10);
        this.outsideBoard = Mockito.mock(Cell.class);
        when(outsideBoard.getX()).thenReturn(1);
        when(outsideBoard.getY()).thenReturn(-1);
        when(outsideBoard.getBoard()).thenReturn(b);

        p.occupy(b.getCell(1,2));
        m.occupy(b.getCell(1,0));
        n.occupy(b.getCell(0,2));
        collega.occupy(b.getCell(2,0));
        f.occupy(b.getCell(1,1));
        w.occupy(b.getCell(0,0));

        /*
        b.getCell(1,2).setGuest(p);
        b.getCell(1, 0).setGuest(m);
        b.getCell(0, 2).setGuest(n);
        b.getCell(2, 0).setGuest(collega);
        b.getCell(1, 1).setGuest(f);
        b.getCell(0,0).setGuest(w);*/
    }

    @Test
    public void testMonsterMove_Monster() {
        Move m = createMove(b.getCell(1,1));
        assertEquals(((MonsterMove)m).getMonster(), this.m);
    }

    @Test
    public void testMoveToFruit() {
        Move m = createMove(b.getCell(1, 1));
        assertFalse(m.moveDone());
        // Three checks:
        // The move is invalid
        assertFalse(m.movePossible());
        // The food cell inhabitant is not null: it holds the food still
        assertEquals(b.getCell(1, 1).getInhabitant(), f);
        // The location of the monster is the original position
        assertEquals(this.m.getLocation(), b.getCell(1,0));
    }

    @Test
    public void testMoveToWall() {
        Move m = createMove(b.getCell(0, 0));
        assertFalse(m.movePossible());
        assertFalse(m.moveDone());
        // The location is the same as the original
        assertEquals(this.m.getLocation(), b.getCell(1,0));
    }

    @Test
    public void testMoveToPlayer() {
        Move m = new MonsterMove(n, b.getCell(1,2));
        assertTrue(m.movePossible());
        m.apply();
        assertTrue(m.moveDone());
        assertFalse(p.living());
        assertEquals(this.n.getLocation(), b.getCell(1,2));
    }

    @Test
    public void testMoveAcrossBorder() {
        Move m = createMove(outsideBoard);
        assertFalse(m.movePossible());
        assertFalse(m.moveDone());
        assertEquals(this.m.getLocation(), b.getCell(1,0));
    }

    @Test
    public void testMoveToMonster() {
        Cell originalLoc = m.getLocation();
        Move m = new MonsterMove(this.m, collega.getLocation());
        assertFalse(m.movePossible());
        assertEquals(this.m.getLocation(), originalLoc);
    }

    @Test
    public void testMoveInvalid() {
        Move m = createMove(b.getCell(0, 1));
        assertFalse(m.movePossible()); // Precondition would fail, so it should? return false
        assertEquals(this.m.getLocation(), b.getCell(1, 0)); // Assume the move is cancelled
    }

    @Test
    public void testMoveEmpty() {
        Move m = new MonsterMove(this.n, b.getCell(0, 3));
        assertTrue(m.movePossible());
        m.apply();
        assertEquals(this.n.getLocation(), b.getCell(0, 3));
    }

    protected Move createMove(Cell target) {
        move = new MonsterMove(m, target);
        return move;
    }

}
