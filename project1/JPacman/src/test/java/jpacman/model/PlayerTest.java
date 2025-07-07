package jpacman.model;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest extends GameTest {


    @Test
    public void playerInvariant() throws NoSuchFieldException, IllegalAccessException {
        assertTrue(thePlayer.getPointsEaten() >= 0);
        Field field = Player.class.getDeclaredField("pointsEaten");
        field.setAccessible(true);
        field.set(thePlayer, -2);
        assertEquals(-2, field.getInt(thePlayer));
        assertFalse(thePlayer.playerInvariant());
    }
}
