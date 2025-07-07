package jpacman.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;


/**
 * Systematic testing of the game state transitions.
 *
 * The test makes use of the simple map and its containing monsters
 * and players, as defined in the GameTestCase.
 * <p>
 *
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: EngineTest.java,v 1.6 2008/02/04 23:00:12 arie Exp $
 */
public class EngineTest extends GameTestCase {

    /**
     * The engine that we'll push along every possible transition.
     */
    private Engine theEngine;


    /**
     * Set up an Engine, making use of the Game object
     * (with a small map containing all sorts of guests)
     * created in the superclass.
     */
    @Before public void setUp() {
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());
    }

     // Create state model test cases.
     @Test public void addTestCasesHere() {}

     //mutation 11.java
     @Test public void inWonStateCheck() throws NoSuchFieldException, IllegalAccessException {
         theEngine = new Engine(theGame);
         assertFalse(theEngine.inWonState());

         Field startingField = Engine.class.getDeclaredField("starting");
         startingField.setAccessible(true);
         startingField.set(theEngine, false);

         assertFalse(theEngine.inWonState());

         theEngine.movePlayer(-1,0);
         theEngine.movePlayer(0,1);
         assertTrue(theEngine.inWonState());

         startingField.setAccessible(true);
         startingField.set(theEngine, true);
         assertFalse(theEngine.inWonState());
     }

    //mutation 11.java
    @Test public void inDiedStateCheck() throws NoSuchFieldException, IllegalAccessException {
        theEngine = new Engine(theGame);
        assertFalse(theEngine.inDiedState());

        Field startingField = Engine.class.getDeclaredField("starting");
        startingField.setAccessible(true);
        startingField.set(theEngine, false);

        assertFalse(theEngine.inDiedState());

        theEngine.movePlayer(1,0);
        theEngine.movePlayer(0,1);
        theEngine.movePlayer(0,1);
        assertTrue(theEngine.inDiedState());

        startingField.setAccessible(true);
        startingField.set(theEngine, true);
        assertFalse(theEngine.inDiedState());
    }

    //Mutation
    @Test public void testInStartingState() throws NoSuchFieldException, IllegalAccessException {
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());

        theEngine.start();
        theEngine.movePlayer(-1,0);
        theEngine.movePlayer(0,1);

        assertFalse(theEngine.inStartingState());

        theGame.initialize();
        //Run in the monster
        theEngine = new Engine(theGame);
        theEngine.start();
        theEngine.movePlayer(0,1);
        assertFalse(theEngine.inStartingState());
    }

    //Mutation
    @Test public void testIsGameOverState() throws NoSuchFieldException, IllegalAccessException {
        Game game = Mockito.mock(Game.class);
        when(game.initialized()).thenReturn(true);
        when(game.playerDied()).thenReturn(false);
        when(game.playerWon()).thenReturn(false);
        theEngine = new Engine(game);
        theEngine.start();

        when(game.playerDied()).thenReturn(true);
        when(game.playerWon()).thenReturn(true);
        assertTrue(theEngine.inGameOverState());

        when(game.playerDied()).thenReturn(false);
        when(game.playerWon()).thenReturn(true);
        assertTrue(theEngine.inGameOverState());

        when(game.playerDied()).thenReturn(false);
        when(game.playerWon()).thenReturn(false);
        assertFalse(theEngine.inGameOverState());
    }

    //Mutation
    @Test public void invariantTest() throws NoSuchFieldException, IllegalAccessException {
        theEngine = new Engine(theGame);
        theEngine.start();
        assertTrue(theEngine.invariant());

        theEngine.movePlayer(0,1);
        Field haltedField = Engine.class.getDeclaredField("halted");
        haltedField.setAccessible(true);
        haltedField.set(theEngine, true);
        assertFalse(theEngine.invariant());

        Game game = Mockito.mock(Game.class);
        when(game.playerDied()).thenReturn(true);
        when(game.playerWon()).thenReturn(true);
        assertFalse(theEngine.invariant());
    }

    @Test
    public void startingState_StartingTrue() {
        assertTrue(theEngine.isStarting());
        theGame.movePlayer(0, 1); // Move to monster, player will die
        assertTrue(theGame.playerDied());
    }

    @Test
    public void wonDieNotTrueAtSameTime_SFalse() throws NoSuchFieldException, IllegalAccessException {
        theEngine.start();
        // Player Won: false ; Player Dead: true
        Field field = thePlayer.getClass().getDeclaredField("pointsEaten");
        field.setAccessible(true);
        field.set(thePlayer, 0);

        Field field2 = thePlayer.getClass().getDeclaredField("alive");
        field2.setAccessible(true);
        field2.set(thePlayer, false);

        setStarting(false);

        // For starting = true
        assertTrue(theGame.playerDied());
        assertFalse(theGame.playerWon());
        assertTrue(theEngine.inDiedState());
        assertTrue(theEngine.inGameOverState());
        assertFalse(theEngine.inStartingState());
    }

    @Test
    public void wonDieNotTrueAtSameTime_STrue() throws NoSuchFieldException, IllegalAccessException {
        theEngine.start();
        // Player won: True ; Player dead: False
        Field field = thePlayer.getClass().getDeclaredField("pointsEaten");
        field.setAccessible(true);
        field.set(thePlayer, 2300);

        Field field2 = thePlayer.getClass().getDeclaredField("alive");
        field2.setAccessible(true);
        field2.set(thePlayer, true);

        setStarting(false);

        // For starting = true
        assertTrue(theGame.playerWon());
        assertFalse(theGame.playerDied());
        assertFalse(theEngine.inStartingState());
        assertTrue(theEngine.inWonState());
        assertTrue(theEngine.inGameOverState());
    }

    // The player moves to a point, engine not started yet
    // Player does not die, game is not ended, player is not won
    @Test
    public void startingAndPlayerDiedWon_3() {
        assertTrue(theEngine.isStarting());
        theGame.movePlayer(-1, 0);
        assertTrue(thePlayer.living());
        assertFalse(theGame.playerDied());
        assertFalse(theGame.playerWon());
        assertFalse(theEngine.inDiedState());
        assertFalse(theEngine.inWonState());
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertFalse(theEngine.inStartingState());
        assertTrue(theGame.consistent());
    }

    // The player moves to the wall, engine is started
    @Test
    public void startingAndPlayerDiedWon_0Start() {
        assertTrue(theEngine.isStarting());
        theEngine.start();
        assertFalse(theEngine.isStarting());
        theGame.movePlayer(0, 1);
        assertFalse(thePlayer.living());
        assertTrue(theGame.playerDied());           // Player is dead
        assertFalse(theGame.playerWon());           // Player did not win
        assertTrue(theEngine.inDiedState());        // In died state
        assertFalse(theEngine.inWonState());        // Not in won state
        assertFalse(theEngine.inStartingState());   // Not in start state
        assertTrue(theGame.consistent());
    }

    @Test
    public void startingAndPlayerDiedWon_0() {
        assertTrue(theEngine.isStarting());
        theGame.movePlayer(0, 1);
        assertFalse(thePlayer.living());
        assertTrue(theGame.playerDied());
        assertFalse(theGame.playerWon());
        assertFalse(theEngine.inDiedState());
        assertFalse(theEngine.inWonState());
        assertFalse(theEngine.inStartingState());   // We are in starting state
        assertTrue(theGame.consistent());
    }

    @Test
    public void startingAndPlayerDiedWon_1() {
        theEngine.start();
        assertFalse(theEngine.isStarting());
        theGame.movePlayer(0, 1);       // Move to monster
        assertFalse(thePlayer.living());        // not alive
        assertTrue(theGame.playerDied());       // Player died
        assertFalse(theGame.playerWon());       // Player does not win
        assertTrue(theEngine.inDiedState());    // We're in die state
        assertFalse(theEngine.inWonState());    // We're not in won state
        assertFalse(theEngine.inStartingState());
        assertTrue(theGame.consistent());
    }

    @Test
    public void startingAndPlayerDiedWon_2() {
        theEngine.start();
        assertFalse(theEngine.isStarting());
        theGame.movePlayer(-1, 0);
        theGame.movePlayer(0, 1);
        assertTrue(thePlayer.living());
        assertFalse(theGame.playerDied());
        assertTrue(theGame.playerWon());
        assertFalse(theEngine.inDiedState());
        assertTrue(theEngine.inWonState());
        assertTrue(theGame.consistent());
    }


    @Test
    public void notStartingWhenInGameOver() throws NoSuchFieldException, IllegalAccessException {
        assertFalse(theEngine.inDiedState());
        assertFalse(theEngine.inWonState());

        setStarting(false);

        theGame.movePlayer(-1, 0);
        theGame.movePlayer(0, 1);

        assertFalse(theGame.playerDied());
        assertTrue(!theEngine.inDiedState() && theEngine.inWonState());

    }

    @Test
    public void notStartingWhenInGameOver2() throws NoSuchFieldException, IllegalAccessException {
        assertFalse(theEngine.inDiedState());
        assertFalse(theEngine.inWonState());

        setStarting(false);

        theGame.movePlayer(0, 1);

        assertTrue(theGame.playerDied());
        assertTrue(theEngine.inDiedState() && !theEngine.inWonState());

    }

    private void setStarting(boolean type) throws NoSuchFieldException, IllegalAccessException {
        Field field = Engine.class.getDeclaredField("starting");
        field.setAccessible(true);
        field.set(theEngine, type);
    }

}
