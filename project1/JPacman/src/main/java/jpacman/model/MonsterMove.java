package jpacman.model;

/**
 * Class to represent the effects of moving the player.
 *
 * @author Arie van Deursen; Aug 18, 2003
 * @version $Id: PlayerMove.java,v 1.6 2008/02/11 13:05:20 arie Exp $
 */
public class MonsterMove extends Move {

    /**
     * The player wishing to move.
     */
    private Monster theMonster;

    /**
     * Create a move for the given monster to a given target cell.
     *
     * @param monster
     *            the Monster to be moved
     * @param newCell
     *            the target location.
     * @see Move
     */
    public MonsterMove(Monster monster, Cell newCell) {
        // preconditions checked in super method,
        // and cannot be repeated here ("super(...)" must be 1st stat.).
        super(monster, newCell);
        theMonster = monster;
        precomputeEffects();
        assert invariant();
    }

    /**
     * Verify that the monster/mover equal
     * and non-null.
     *
     * @return true iff the invariant holds.
     */
    public boolean invariant() {
        return moveInvariant()
        && getMovingGuest().equals(theMonster);
    }

    /**
     * Attempt to move the monster towards a target guest.
     * @param targetGuest The guest that the monster will meet.
     * @return true if the move is possible, false otherwise.
     * @see Move#tryMoveToGuest(Guest)
     */
    @Override
    protected boolean tryMoveToGuest(Guest targetGuest) {
        assert tryMoveToGuestPrecondition(targetGuest)
            : "percolated precondition";
        return targetGuest.meetMonster(this);
    }

    /**
     * Return the monster initiating this move.
     *
     * @return The moving player.
     */
    public Monster getMonster() {
        assert invariant();
        return theMonster;
    }

    /**
     * Actually apply the move, assuming it is possible.
     */
    @Override
    public void apply() {
        assert invariant();
        assert movePossible();
        super.apply();
        assert invariant();
    }

}
