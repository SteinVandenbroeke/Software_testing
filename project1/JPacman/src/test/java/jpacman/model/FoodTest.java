package jpacman.model;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * Series of test cases for the game itself.
 * It makes use of the GameTestCase fixture, which
 * contains a simple board.
 * @author Arie van Deursen, 2007
 * @version $Id: GameTest.java,v 1.7 2008/02/10 19:28:20 arie Exp $
 *
 */
public class FoodTest extends GameTestCase {

    //Survived Mutant 6.java
    /**
     * Food invariant for
     */
    @Test
    public void testFoodInvariant() throws NoSuchFieldException, IllegalAccessException {
        Food food = new Food();

        Field locationField = Guest.class.getDeclaredField("location");
        locationField.setAccessible(true);
        locationField.set(food, new Cell(0,0,new Board(5,5)));
        assertFalse(food.foodInvariant());


        Field pointsField = Food.class.getDeclaredField("points");
        pointsField.setAccessible(true);
        pointsField.set(food, -1);
        assertFalse(food.foodInvariant());


        locationField = Guest.class.getDeclaredField("location");
        locationField.setAccessible(true);
        locationField.set(food, null);
        assertFalse(food.foodInvariant());

        pointsField = Food.class.getDeclaredField("points");
        pointsField.setAccessible(true);
        pointsField.set(food, 1);
        assertTrue(food.foodInvariant());
    }
}
