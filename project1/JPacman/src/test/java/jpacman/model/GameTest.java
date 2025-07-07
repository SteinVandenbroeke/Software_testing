package jpacman.model;

import java.lang.reflect.Field;
import java.util.Vector;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Series of test cases for the game itself.
 * It makes use of the GameTestCase fixture, which
 * contains a simple board.
 * @author Arie van Deursen, 2007
 * @version $Id: GameTest.java,v 1.7 2008/02/10 19:28:20 arie Exp $
 *
 */
public class GameTest extends GameTestCase {

    /**
     * Is each list of monsters a fresh one?
     */
    @Test
    public void testGetMonsters() {
        assertEquals(2, theGame.getMonsters().size());
        // each call to getMonsters should deliver a fresh copy.
        Vector<Monster> ms1 = theGame.getMonsters();
        Vector<Monster> ms2 = theGame.getMonsters();
        assertNotSame(ms1, ms2);
    }

    /**
     * Are the dx/dy in the player correctly set after moving
     * the player around?
     */
    @Test
    public void testDxDyPossibleMove() {
        // start dx/dy should be zero.
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
        // move to left empty cell -- dx should have beeen adjusted.
        theGame.movePlayer(1, 0);
        assertEquals(1, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
        // move to up empty cell -- dy should have been adjusted.
        theGame.movePlayer(0, -1);
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(-1, theGame.getPlayerLastDy());
    }

    /**
     * Do the player dx/dy remain unaltered if a move fails?
     */
    @Test
    public void testDxDyImpossibleMove() {
        // start dx/dy should be zero.
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
        // move to a wallcell -- dxdy should have been adjusted.
        theGame.movePlayer(0, -1);
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(-1, theGame.getPlayerLastDy());
    }

    @Test
    public void testGameOver() throws NoSuchFieldException, IllegalAccessException {
        assertFalse(theGame.gameOver());
        Field gameField = Game.class.getDeclaredField("totalPoints");
        gameField.setAccessible(true);
        Field playerPoints = Player.class.getDeclaredField("pointsEaten");
        playerPoints.setAccessible(true);
        int old_points = (Integer) playerPoints.get(thePlayer);
        playerPoints.setInt(thePlayer, gameField.getInt(theGame));
        assertTrue(theGame.gameOver());
        playerPoints.setInt(thePlayer, old_points);
        assertFalse(theGame.gameOver());
        Field playerDead =  Player.class.getDeclaredField("alive");
        playerDead.setAccessible(true);
        playerDead.setBoolean(thePlayer, false);
        assertTrue(theGame.gameOver());
    }
    @Test
    public void testInit() throws NoSuchFieldException, IllegalAccessException {
        assertTrue(theGame.initialized());
        assertTrue(theGame.invariant());
        Field boardField = Game.class.getDeclaredField("theBoard");
        boardField.setAccessible(true);
        Board board = (Board) boardField.get(theGame);
        boardField.set(theGame, null);
        assertFalse(theGame.initialized());
        assertFalse(theGame.invariant());
        boardField.set(theGame, board);
        assertTrue(theGame.initialized());
        assertTrue(theGame.invariant());

        Field playerField = Game.class.getDeclaredField("thePlayer");
        playerField.setAccessible(true);
        Player player = (Player) playerField.get(theGame);
        playerField.set(theGame, null);
        assertFalse(theGame.initialized());
        assertFalse(theGame.invariant());
        playerField.set(theGame, player);
        assertTrue(theGame.initialized());
        assertTrue(theGame.invariant());

        Field foodField = Game.class.getDeclaredField("totalPoints");
        foodField.setAccessible(true);
        int old_points = (Integer) foodField.get(theGame);
        foodField.setInt(theGame, -1);
        assertFalse(theGame.initialized());
        assertFalse(theGame.invariant());
        foodField.set(theGame, old_points);
        assertTrue(theGame.initialized());
        assertTrue(theGame.invariant());


        Field monsterField = Game.class.getDeclaredField("monsters");
        monsterField.setAccessible(true);
        Vector<Monster> monsters = (Vector) monsterField.get(theGame);
        monsterField.set(theGame, null);
        assertFalse(theGame.initialized());
        assertFalse(theGame.invariant());
        monsterField.set(theGame, monsters);
        assertTrue(theGame.initialized());
        assertTrue(theGame.invariant());



    }
    @Test
    public void testConsistent() throws NoSuchFieldException, IllegalAccessException {
        assertTrue(theGame.invariant());
        assertTrue(theGame.consistent());
        Field pointField = Game.class.getDeclaredField("totalPoints");
        pointField.setAccessible(true);

        Field playerField = Player.class.getDeclaredField("pointsEaten");
        playerField.setAccessible(true);
        int old_points = (Integer) playerField.get(thePlayer);
        playerField.set(thePlayer, pointField.getInt(theGame)+1);
        assertFalse(theGame.consistent());
        assertFalse(theGame.invariant());
        playerField.set(thePlayer, pointField.getInt(theGame));
        Field playerDead =  Player.class.getDeclaredField("alive");
        playerDead.setAccessible(true);
        playerDead.setBoolean(thePlayer, false);
        assertFalse(theGame.consistent());
        assertFalse(theGame.invariant());

        playerField.set(thePlayer, old_points);
        assertTrue(theGame.consistent());
        assertTrue(theGame.invariant());
    }


}
