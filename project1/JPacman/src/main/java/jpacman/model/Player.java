package jpacman.model;

/**
 * The player which can walk around on the board, eat food, and die when meeting
 * a monster.
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: Player.java,v 1.8 2008/02/07 17:59:20 arie Exp $
 */
public class Player extends MovingGuest {

    /**
     * Amount of food eaten so far.
     */
    private int pointsEaten;

    /**
     * Are we dead or alive?
     */
    private boolean alive;

    /**
     * Most recent movements in x and y direction.
     */
    private int lastDx, lastDy;


    /**
     * Create a new player.
     */
    public Player() {
        pointsEaten = 0;
        alive = true;
    }

    /**
     * A player cannot eat negative food.
     *
     * @return true iff the guest invariant holds and the food is non-negative.
     */
    public boolean playerInvariant() {
        return guestInvariant() && pointsEaten >= 0;
    }

    /**
     * Is the player still alive?
     *
     * @return true iff the player is still alive.
     */
    public boolean living() {
        return alive;
    }

    /**
     * Return the amount of food eaten.
     *
     * @return Points collected so far.
     */
    public int getPointsEaten() {
        return pointsEaten;
    }

    /**
     * Increase the amount of food eaten by an extra meal worth a certain amount
     * of points.
     *
     * @param foodPoints
     *            calories that should be added.
     */
    protected void eat(int foodPoints) {
        assert playerInvariant();
        assert living();
        pointsEaten += foodPoints;
        assert living();
        assert playerInvariant();
    }

    /**
     * The player has been killed by a monster -- set the state accordingly.
     * Precondition: not killed before.
     */
    protected void die() {
        assert playerInvariant();
        assert living();
        alive = false;
        assert playerInvariant();
    }


    /**
     * Another player wants to occupy this player's state. With one player
     * active this will be impossible, but with multiple players this may
     * happen. In any case, the move itself will not be possible.
     *
     * @param theMove
     *            move object representing intended move and its effects.
     * @return false, the player cannot occupy another player's cell.
     *
     * @see jpacman.model.Guest#meetPlayer(jpacman.model.PlayerMove)
     */
    @Override
    protected boolean meetPlayer(PlayerMove theMove) {
        assert playerInvariant();
        assert theMove != null;
        assert !theMove.initialized();
        assert this.equals(theMove.getPlayer())
            : "Move: only one player supported";
        return false;
    }

    @Override
    protected boolean meetMonster(MonsterMove theMove){
        assert guestInvariant();
        assert theMove != null;
        assert !theMove.initialized();
        this.die();
        return true;
    }

    /**
     * @see jpacman.model.Guest#guestType()
     * @return character encoding for a player.
     */
    @Override
    public char guestType() {
        return Guest.PLAYER_TYPE;
    }

    /**
     * @return The player's most recent advancement in the x-direction.
     */
    public int getLastDx() {
        return lastDx;
    }

    /**
     * @param dx Most recent advancement in x-direction.
     * @param dy Most recent advancement in y-direction.
     */
    public void setLastDirection(int dx, int dy) {
        lastDx = dx;
        lastDy = dy;
    }

    /**
     * @return The player's most recent advancement in the y-direction.
     */
    public int getLastDy() {
        return lastDy;
    }
}
