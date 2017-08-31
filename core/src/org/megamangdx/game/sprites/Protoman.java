package org.megamangdx.game.sprites;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.megamangdx.game.ObjectState;

/**
 * @author Lam
 */
public class Protoman extends Sprite implements Telegraph {

    private ObjectState prevState;
    private ObjectState currentState;
    private World world;
    private Body b2body;

    private Animation<TextureRegion> protomanStand;
    private Animation<TextureRegion> protomanStandShoot;
    private Animation<TextureRegion> protomanRun;
    private Animation<TextureRegion> protomanRunShoot;

    private TextureRegion protomanJump;
    private TextureRegion protomanJumpShoot;

    private boolean rightDirection;

    private boolean isDead = false;

    private Array<Bullet> gunShots = new Array<Bullet>();

    private float stateTimer;

    /**
     * Handle incoming messages from Telegram.
     * @param msg message from sender
     * @return ist message was delivered?
     */
    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    private ObjectState getState() {
        if (isDead) {
            return ObjectState.DEAD;
        }
        return ObjectState.STANDING;
    }

    private TextureRegion getFrame(float delta) {
        ObjectState state = getState();

        switch (state) {
            case DEAD:
                // TODO sprite
                break;
            case RUNNING:
                break;
            case JUMPING_SHOOT:
                break;
            case STANDING:
                break;
            case FALLING:
            case JUMPING:
            default:
                break;
        }

        return null;
    }

    public void update(float delta) {

        // remove destroyed gunshots from memory
        for (Bullet bullet: gunShots) {
            if (bullet.isDestroyed()) {
                gunShots.removeValue(bullet, true);
            }
        }
    }

    public void draw(Batch batch) {
        super.draw(batch);

        for (Bullet shoot: gunShots) {
            shoot.draw(batch);
        }
    }

}
