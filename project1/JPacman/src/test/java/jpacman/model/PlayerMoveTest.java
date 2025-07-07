package jpacman.model;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to PlayerMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in PlayerMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Arie van Deursen; August 21, 2003.
 * @version $Id: PlayerMoveTest.java,v 1.8 2008/02/10 19:51:11 arie Exp $
 */
public class PlayerMoveTest extends MoveTest {

    /**
     * The move the player would like to make.
     */
    private PlayerMove aPlayerMove;

    /**
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters() {
        PlayerMove playerMove = new PlayerMove(thePlayer, foodCell);
        assertEquals(thePlayer, playerMove.getPlayer());
        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(1, playerMove.getFoodEaten());
        assertTrue(playerMove.invariant());
    }


    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected PlayerMove createMove(Cell target) {
        aPlayerMove = new PlayerMove(thePlayer, target);
        return aPlayerMove;
    }

    //  playerCell, wallCell, monsterCell, foodCell, emptyCell;
    @Test
    public void testPlayerMoveEmptyCell(){
        PlayerMove playermove =  createMove(emptyCell);
        assertTrue(playermove.movePossible());
        playermove.apply();
        assertEquals(thePlayer.getLocation(), emptyCell);
        assertFalse(theGame.playerDied());
        assertFalse(theGame.playerWon());
    }

    @Test
    public void testPlayerMoveCellWithinBorders(){
        PlayerMove playermove =  createMove(thePlayer.getLocation().cellAtOffset(1,0));
        assertTrue(playermove.movePossible());

        playermove =  createMove(thePlayer.getLocation().cellAtOffset(2,1));
        assertFalse(playermove.movePossible());
        assertFalse(theGame.playerDied());
        assertFalse(theGame.playerWon());
    }

    @Test
    public void testPlayerMoveCellWithMonster(){
        PlayerMove playermove =  createMove(monsterCell);
        assertFalse(playermove.movePossible());
        assertTrue(playermove.playerDies());
        assertFalse(theGame.playerWon());
    }

    @Test
    public void testPlayerMoveFoodCell(){
        PlayerMove playermove =  createMove(foodCell);
        assertTrue(playermove.movePossible());
        int old_food = thePlayer.getPointsEaten();
        playermove.apply();
        int new_food = thePlayer.getPointsEaten();
        assertEquals(old_food+1, new_food);
        assertFalse(theGame.playerWon());
        assertFalse(theGame.playerDied());
    }

    @Test
    public void testPlayerMoveWall(){
        PlayerMove playermove =  createMove(wallCell);
        assertFalse(playermove.movePossible());
        assertFalse(theGame.playerDied());
        assertFalse(theGame.playerWon());
    }

    @Test
    public void testInvariantFood() throws IllegalAccessException, NoSuchFieldException {
        PlayerMove playerMove = new PlayerMove(thePlayer, foodCell);
        assertTrue(playerMove.getFoodEaten() >= 0);
        Field field = PlayerMove.class.getDeclaredField("foodEaten");
        field.setAccessible(true);
        field.set(playerMove, -2);
        assertEquals(-2, field.getInt(playerMove));
        assertFalse(playerMove.invariant());
    }

    @Test
    public void testInvariantPlayerNull() throws IllegalAccessException, NoSuchFieldException {
        PlayerMove playerMove = new PlayerMove(thePlayer, foodCell);
        assertTrue(playerMove.invariant());
        Field field = PlayerMove.class.getDeclaredField("thePlayer");
        field.setAccessible(true);
        field.set(playerMove, null);
        assertNull(field.get(playerMove));
        assertFalse(playerMove.invariant());
    }
}
