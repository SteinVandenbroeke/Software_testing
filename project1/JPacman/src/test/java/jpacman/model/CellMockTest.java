package jpacman.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CellMockTest {

    @Mock
    Board board1;

    @Mock
    Board board2;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(board1.withinBorders(anyInt(), anyInt())).thenReturn(true);
        when(board2.withinBorders(anyInt(), anyInt())).thenReturn(true);
    }

    @Test
    public void testAdjacentCellsCenter() {
        Cell aCell = new Cell(1, 1, board1);
        Cell cellUp = new Cell(aCell.getX(), aCell.getY()-1, board1);
        Cell cellDown = new Cell(aCell.getX(), aCell.getY()+1, board1);
        Cell cellRight = new Cell(aCell.getX()+1, aCell.getY(), board1);
        Cell cellLeft = new Cell(aCell.getX()-1, aCell.getY(), board1);

        assertTrue(aCell.adjacent(cellUp));
        assertTrue(aCell.adjacent(cellDown));
        assertTrue(aCell.adjacent(cellRight));
        assertTrue(aCell.adjacent(cellLeft));
    }

    @Test
    public void testAdjacentCellsCorner() {
        Cell aCell = new Cell(0, 0, board1);
        Cell cellDown = new Cell(aCell.getX(), aCell.getY()+1, board1);
        Cell cellRight = new Cell(aCell.getX()+1, aCell.getY(), board1);

        assertTrue(aCell.adjacent(cellDown));
        assertTrue(aCell.adjacent(cellRight));
    }

    @Test
    public void testNotAdjacentCells() {
        Cell aCell = new Cell(2, 2, board1);
        Cell bcell = new Cell(aCell.getX(), aCell.getY()-2, board1);
        assertFalse(aCell.adjacent(bcell));

        bcell = new Cell(aCell.getX() - 2, aCell.getY(), board1);
        assertFalse(aCell.adjacent(bcell));

        bcell = new Cell(aCell.getX() - 2, aCell.getY() - 2, board1);
        assertFalse(aCell.adjacent(bcell));
    }

    @Test
    public void testAdjacentCellsBoard() {
        Cell cell2 = Mockito.mock(Cell.class);
        when(cell2.getBoard()).thenReturn(board2);

        Cell cell1 = new Cell(1, 1, board1);

        assertFalse(cell1.adjacent(cell2));
    }

    @Test
    public void testAdjacentDiagonal() {
        Cell aCell = new Cell(0, 4, board1);

        Cell diagonalCell1 = new Cell(aCell.getX()+1, aCell.getY()+1, board1);
        Cell diagonalCell2 = new Cell(aCell.getX()-1, aCell.getY()+1, board1);
        Cell diagonalCell3 = new Cell(aCell.getX()+1, aCell.getY()-1, board1);
        Cell diagonalCell4 = new Cell(aCell.getX()-1, aCell.getY()-1, board1);

        assertFalse(diagonalCell1.adjacent(aCell));
        assertFalse(diagonalCell2.adjacent(aCell));
        assertFalse(diagonalCell3.adjacent(aCell));
        assertFalse(diagonalCell4.adjacent(aCell));
    }
}