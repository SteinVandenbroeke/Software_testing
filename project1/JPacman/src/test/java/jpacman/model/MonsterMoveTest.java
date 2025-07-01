package jpacman.model;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MonsterMoveTest extends MoveTest {

    Board b;
    Monster m, n, collega;
    Player p;
    Food f;
    Wall w;
    @Mock
    Cell outsideBoard;

    /**
     * Map:
     * NP
     *  F
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

        b.getCell(1,2).setGuest(p);
        b.getCell(1, 0).setGuest(m);
        b.getCell(0, 2).setGuest(n);
        b.getCell(2, 0).setGuest(collega);
        b.getCell(1, 1).setGuest(f);
        b.getCell(0,0).setGuest(w);
    }

    @Test
    public void testMoveToFruit() {
        Move m = createMove(b.getCell(1, 1));
        // Three checks:
        // The move is invalid
        assertFalse(m.tryMoveToGuest(f));
        // The food cell inhabitant is not null: it holds the food still
        assertNotNull(b.getCell(1,1).getInhabitant());
        // The location of the monster is the original position
        assertEquals(this.m.getLocation(), new Cell(1, 0, b));
    }

    @Test
    public void testMoveToWall() {
        Move m = createMove(b.getCell(0, 0));
        // Move is invalid
        assertFalse(m.tryMoveToGuest(w));
        // The location is the same as the original
        assertEquals(this.m.getLocation(), new Cell(0, 1, b));
    }

    @Test
    public void testMoveToPlayer() {
        Move m = new MonsterMove(n, b.getCell(1,2));
        assertTrue(m.tryMoveToGuest(p));
        assertFalse(p.living());
        assertEquals(this.n.getLocation(), new Cell(1, 2, b));
    }

    @Test
    public void testMoveAcrossBorder() {
        Move m = createMove(outsideBoard);
        assertFalse(m.tryMoveToGuest(p));
        assertEquals(this.m.getLocation(), new Cell(1, 0, b));
    }

    @Test
    public void testMoveToMonster() {
        Move m = createMove(b.getCell(2,0));
    }

    @Test
    public void testMoveInvalid() {
        Move m = createMove(b.getCell(0, 1));
        assertFalse(m.tryMoveToGuest(p)); // Precondition would fail, so it should? return false
        assertEquals(this.m.getLocation(), new Cell(1, 0, b)); // Assume the move is cancelled
    }

    @Override
    protected Move createMove(Cell target) {
        MonsterMove m = new MonsterMove(this.m, target);
        return m;
    }

}
