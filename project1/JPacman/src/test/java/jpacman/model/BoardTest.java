package jpacman.model;

import org.junit.Before;
import org.junit.Test;

import jpacman.TestUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * One of the simpler test classes, so a good point to start understanding
 * how testing JPacman has been setup.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: BoardTest.java,v 1.10 2008/02/03 19:43:38 arie Exp $
 */
public class BoardTest {

    /**
     * The width & height of the board to be used.
     */
    private final int width = 5, height = 10;

    /**
     * The board to be used in the tests.
     */
    private Board theBoard;

    /**
     * Create a simple (empty) board to be used for testing.
     * Note that JUnit invokes such a Before method before every test case
     * so that we start in a fresh situation.
     */
    @Before
    public void setUp() {
        theBoard = new Board(width, height);
    }

    /**
     * Simple test of the width/height getters,
     * to get in the mood for testing.
     */
    @Test
    public void testGettingWidthHeight() {
        assertEquals(width, theBoard.getWidth());
        assertEquals(height, theBoard.getHeight());
    }

    /**
     * Obtain a cell that is on the board at given (x,y) position,
     * and check if the (x,y) coordinates of the Cell obtained are ok.
     * To make the test a little more exciting use a point that is on the
     * border (an "invariant on point" in Binder's terminology).
     */
    @Test
    public void testGettingCellsFromBoard() {
        // the coordinates to be used.
        int x = 0;
        int y = 0;

        // actually get the cell.
        Cell aCell = theBoard.getCell(x, y);

        // compare the coordinates.
        assertEquals(x, aCell.getX());
        assertEquals(y, aCell.getY());
    }

    /**
     * Let a guest occupy one of the Cells on the board, and
     * check if it is actually there.
     * Again, pick a point on the border (but a different one)
     * to (slightly) increase the chances of failure.
     */
    @Test
    public void testOccupy() {
        // place to put the guest.
        int x = width - 1;
        int y = height - 1;
        Cell aCell = theBoard.getCell(x, y);

        // guest to be put on the board.
        Food food = new Food();

        // put the guest on the cell.
        food.occupy(aCell);

        // verify its presence.
        assertEquals(food, theBoard.getGuest(x, y));
        assertEquals(Guest.FOOD_TYPE, theBoard.guestCode(x, y));
    }

    /**
     * Try to create an illegal board (e.g., negative sizes),
     * and verify that this generates an assertion failure.
     * This test also serves to illustrate how to test whether a method
     * generates an assertion failure if a precondition is violated.
     */
    @Test
    public void testFailingBoardCreation() {
        if (TestUtils.assertionsEnabled()) {
            boolean failureGenerated;
            try {
                new Board(-1, -1);
                failureGenerated = false;
            } catch (AssertionError ae) {
                failureGenerated = true;
            }
            assertTrue(failureGenerated);
        }
        // else: nothing to test -- no guarantees what so ever!
    }

    @Test
    public void testOff() {
        Cell c1 = this.mockCell(width, 0, this.theBoard);
        Cell c2 = this.mockCell(0, height, this.theBoard);
        Cell c3 = this.mockCell(width, height, this.theBoard);
        Cell c4 = this.mockCell(-1, height, this.theBoard);
        Cell c5 = this.mockCell(width, -1, this.theBoard);
        assertFalse(this.theBoard.withinBorders(c1.getX(), c1.getY()));
        assertFalse(this.theBoard.withinBorders(c2.getX(), c2.getY()));
        assertFalse(this.theBoard.withinBorders(c3.getX(), c3.getY()));
        assertFalse(this.theBoard.withinBorders(c4.getX(), c4.getY()));
        assertFalse(this.theBoard.withinBorders(c5.getX(), c5.getY()));
    }
    @Test
    public void testOn() {
        Cell c1 = new Cell(width-1, height-1, this.theBoard);
        Cell c2 = new Cell(0, height-1, this.theBoard);
        Cell c3 = new Cell(width-1, 0, this.theBoard);
        Cell c4 = new Cell(0, 0, this.theBoard);
        assertTrue(this.theBoard.withinBorders(c1.getX(), c1.getY()));
        assertTrue(this.theBoard.withinBorders(c2.getX(), c2.getY()));
        assertTrue(this.theBoard.withinBorders(c3.getX(), c3.getY()));
        assertTrue(this.theBoard.withinBorders(c4.getX(), c4.getY()));
    }

    @Test
    public void testIn() {
        Cell c = new Cell(width/2, height/2, this.theBoard);
        assertTrue(this.theBoard.withinBorders(c.getX(), c.getY()));
    }

    @Test
    public void testOut() {
        Cell c1 = this.mockCell(width*2, height/2, this.theBoard);
        Cell c2 = this.mockCell(width/2, height*2, this.theBoard);
        Cell c3 = this.mockCell(width/2, -height*2, this.theBoard);
        Cell c4 = this.mockCell(-width*2, -height*2, this.theBoard);
        assertFalse(theBoard.withinBorders(c1.getX(), c1.getY()));
        assertFalse(theBoard.withinBorders(c2.getX(), c2.getY()));
        assertFalse(theBoard.withinBorders(c3.getX(), c3.getY()));
        assertFalse(theBoard.withinBorders(c4.getX(), c4.getY()));
    }

    //Survived Mutant 4.java
    @Test
    public void consistentBoardCellAssociationTest() throws NoSuchFieldException, IllegalAccessException {
        Board board = new Board(10,10);
        Board board1 = new Board(10,10);

        assertTrue(board.consistentBoardCellAssociation());

        Cell cell1 = board.getCell(1,1);

        Field boardField = Cell.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(cell1, board1);

        assertFalse(board.consistentBoardCellAssociation());
    }

    //Survived Mutant 24.java
    @Test
    public void negativeWidthOrHeight() throws NoSuchFieldException, IllegalAccessException {
        Board board = new Board(10,10);

        Field heightField = Board.class.getDeclaredField("height");
        heightField.setAccessible(true);
        heightField.set(board, -2);
        Field widthField = Board.class.getDeclaredField("width");
        widthField.setAccessible(true);
        widthField.set(board, 2);

        assertFalse(board.invariant());

        heightField.set(board, 2);
        widthField.set(board, -2);
        assertFalse(board.invariant());

        heightField.set(board, -2);
        widthField.set(board, -2);
        assertFalse(board.invariant());
    }

    private Cell mockCell(int x, int y, Board b) {
        Cell cell2 = Mockito.mock(Cell.class);
        when(cell2.getX()).thenReturn(x);
        when(cell2.getY()).thenReturn(y);
        when(cell2.getBoard()).thenReturn(b);
        when(cell2.invariant()).thenReturn(true);
        return cell2;
    }
}
