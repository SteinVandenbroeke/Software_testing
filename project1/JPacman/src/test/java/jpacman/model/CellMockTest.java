package jpacman.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

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
    public void testAdjacentCellsBorder() {
        Cell aCell = new Cell(0, 2, board1);
        Cell cellUp = new Cell(aCell.getX(), aCell.getY()-1, board1);
        Cell cellDown = new Cell(aCell.getX(), aCell.getY()+1, board1);
        Cell cellRight = new Cell(aCell.getX()+1, aCell.getY(), board1);

        assertTrue(aCell.adjacent(cellDown));
        assertTrue(aCell.adjacent(cellUp));
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

    //Survived Mutant 3.java
    @Test
    public void testBoardInvariant() throws NoSuchFieldException, IllegalAccessException {
        when(board1.withinBorders(anyInt(), anyInt())).thenReturn(true);
        Cell cell = new Cell(0, 0, board1);

        Field boardField = Cell.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(cell, null);
        assertFalse(cell.boardInvariant());

        when(board1.withinBorders(anyInt(), anyInt())).thenReturn(true);
        cell = new Cell(0, 0, board1);
        when(board1.withinBorders(anyInt(), anyInt())).thenReturn(false);
        boardField = Cell.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(cell, board1);
        assertFalse(cell.boardInvariant());
    }

    //Survived Mutant 50.java
    @Test
    public void testCellInvariant() throws NoSuchFieldException, IllegalAccessException {
        when(board1.withinBorders(anyInt(), anyInt())).thenReturn(true);
        Cell cell = new Cell(0, 0, board1);

        assertTrue(cell.invariant());

        Field boardField = Cell.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(cell, null);

        assertFalse(cell.invariant());
    }

    //Survived Mutant 55.java - 56.java
    @Test
    public void testCellOffsets() throws NoSuchFieldException, IllegalAccessException {
        Board board = new Board(10,10);

        assertEquals(board.getCell(2,2).cellAtOffset(-1, 0), board.getCell(1,2));
        assertEquals(board.getCell(2,2).cellAtOffset(1, 0), board.getCell(3,2));
        assertEquals(board.getCell(2,2).cellAtOffset(0, -1), board.getCell(2,1));
        assertEquals(board.getCell(2,2).cellAtOffset(0, 1), board.getCell(2,3));
        assertEquals(board.getCell(2,2).cellAtOffset(-1, -1), board.getCell(1,1));
        assertEquals(board.getCell(2,2).cellAtOffset(1, 1), board.getCell(3,3));
    }
}