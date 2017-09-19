package org.megamangdx.game.sprites;

/**
 * @author Lam on 12.08.17.
 */
public enum ObjectState {

    BEAM,
    MATERIALIZED_BEAM,
    JUMPING,
    JUMPING_SHOOT,
    FALLING,
    RUNNING,
    RUNNING_SHOOT,
    CLIMBING,
    DEAD,
    HIT,
    STANDING,
    STANDING_SHOOT;


    private static boolean isShootState(ObjectState currentState) {
        boolean afterShoot = false;
        switch (currentState) {
            case JUMPING_SHOOT:
            case RUNNING_SHOOT:
            case STANDING_SHOOT:
                afterShoot = true;
                break;
            default:
        }
        return afterShoot;
    }
}
