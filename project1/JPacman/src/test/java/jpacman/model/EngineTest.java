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
        assertTrue(theEngine.inDiedState());
    }

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

}
