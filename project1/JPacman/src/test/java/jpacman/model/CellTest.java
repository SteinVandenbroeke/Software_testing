package jpacman.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for methods working directly on Cells.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: CellTest.java,v 1.16 2008/02/10 12:51:55 arie Exp $
 */
public class CellTest {

    /**
     * Width & heigth of board to be used.
     */
    private final int width = 4, height = 5;

    /**
     * The board the cells occur on.
     */
    private Board aBoard, bBoard;

    /**
     * The "Cell Under Test".
     */
    private Cell aCell, diagonalCell1, diagonalCell2, diagonalCell3, diagonalCell4;
    private Cell cellUp, cellDown, cellLeft, cellRight, cellNewBoard;

    /**
     * Actually create the board and the cell. *
     */
    @Before
    public void setUpBoard() {
        aBoard = new Board(width, height);
        bBoard = new Board(width, height);
        // put the cell on an invariant boundary value.
        aCell = new Cell(0, height - 1, aBoard);

        cellNewBoard = new Cell(0, height - 1, bBoard);

        //
        diagonalCell1 = new Cell(aCell.getX()+1, aCell.getY()+1, aBoard);
        diagonalCell2 = new Cell(aCell.getX()-1, aCell.getY()+1, aBoard);
        diagonalCell3 = new Cell(aCell.getX()+1, aCell.getY()-1, aBoard);
        diagonalCell4 = new Cell(aCell.getX()-1, aCell.getY()-1, aBoard);


        cellUp = new Cell(aCell.getX(), aCell.getY()-1, aBoard);
        cellDown = new Cell(aCell.getX(), aCell.getY()+1, aBoard);
        cellRight = new Cell(aCell.getX()+1, aCell.getY(), aBoard);
        cellLeft = new Cell(aCell.getX()-1, aCell.getY(), aBoard);
    }


    @Test
    public void testAdjacentCellsCenter() {
        aCell = new Cell(2, 2, aBoard);
        cellUp = new Cell(aCell.getX(), aCell.getY()-1, aBoard);
        cellDown = new Cell(aCell.getX(), aCell.getY()+1, aBoard);
        cellRight = new Cell(aCell.getX()+1, aCell.getY(), aBoard);
        cellLeft = new Cell(aCell.getX()-1, aCell.getY(), aBoard);

        assertTrue(aCell.adjacent(cellUp));
        assertTrue(aCell.adjacent(cellDown));
        assertTrue(aCell.adjacent(cellRight));
        assertTrue(aCell.adjacent(cellLeft));
    }

    @Test
    public void testAdjacentCellsCorner() {
        aCell = new Cell(0, 0, aBoard);
        cellDown = new Cell(aCell.getX(), aCell.getY()+1, aBoard);
        cellRight = new Cell(aCell.getX()+1, aCell.getY(), aBoard);

        assertTrue(aCell.adjacent(cellDown));
        assertTrue(aCell.adjacent(cellRight));
    }

    @Test
    public void testAdjacentCellsBorder() {
        aCell = new Cell(0, 2, aBoard);
        cellUp = new Cell(aCell.getX(), aCell.getY()-1, aBoard);
        cellDown = new Cell(aCell.getX(), aCell.getY()+1, aBoard);
        cellRight = new Cell(aCell.getX()+1, aCell.getY(), aBoard);

        assertTrue(aCell.adjacent(cellDown));
        assertTrue(aCell.adjacent(cellUp));
        assertTrue(aCell.adjacent(cellRight));
    }

    @Test
    public void testNotAdjacentCells() {
        aCell = new Cell(2, 2, aBoard);
        Cell bcell = new Cell(aCell.getX(), aCell.getY()-2, aBoard);
        assertFalse(aCell.adjacent(bcell));

        bcell = new Cell(aCell.getX() - 2, aCell.getY(), aBoard);
        assertFalse(aCell.adjacent(bcell));

        bcell = new Cell(aCell.getX() - 2, aCell.getY() - 2, aBoard);
        assertFalse(aCell.adjacent(bcell));
    }

    @Test
    public void testAdjacentCellsBoard() {
        assertFalse(cellNewBoard.adjacent(aCell));
    }

    @Test
    public void testAdjacentDiagonal() {
        assertFalse(diagonalCell1.adjacent(aCell));
        assertFalse(diagonalCell2.adjacent(aCell));
        assertFalse(diagonalCell3.adjacent(aCell));
        assertFalse(diagonalCell4.adjacent(aCell));
    }

}
