package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.megamangdx.game.sprites.effects.Spawn;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public abstract class BaseCharacter extends Sprite {

    protected boolean isDead = false;
    protected boolean isShooting = false;
    protected boolean isHit = false;
    protected boolean rightDirection;

    protected ObjectState prevState = ObjectState.STANDING;
    protected ObjectState currentState = ObjectState.STANDING;

    public World world;
    public Body b2body;

    protected Spawn spawn;

    protected float stateTimer = 0;

    public void hit() {
        isHit = true;
        currentState = ObjectState.HIT;
    }

    public void die() {
        this.isDead = true;
        world.destroyBody(b2body);
    }

    public void jump() {
        if (getLinearVelocity().y == 0) {
            if (!isShooting) { // if shoot button was'n hit before jumping
                if (currentState != ObjectState.JUMPING) {
                    b2body.applyLinearImpulse(new Vector2(0, 2.8f), b2body.getWorldCenter(), true);
                    currentState = ObjectState.JUMPING;
                }
            } else {
                if (currentState != ObjectState.JUMPING_SHOOT) {
                    b2body.applyLinearImpulse(new Vector2(0, 2.8f), b2body.getWorldCenter(), true);
                    currentState = ObjectState.JUMPING_SHOOT;
                }
            }
        }
    }

    public void shoot() {
        isShooting = true;
        // if the player is in the air and shoot button was hit
        if (currentState == ObjectState.JUMPING || currentState == ObjectState.FALLING) {
            currentState = ObjectState.JUMPING_SHOOT;
        }
    }

    public void moveRight() {
        if(getLinearVelocity().x <= Megaman.MAX_VELOCITY) {
            b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
        }
    }

    public void moveLeft() {
        if (getLinearVelocity().x >= -Megaman.MAX_VELOCITY) {
            b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
        }
    }

    public Vector2 getLinearVelocity() {
        return b2body.getLinearVelocity();
    }

    protected ObjectState getState() {
        if (!spawn.isSpawnFinished()) {
            // if megaman is landing
            if (getLinearVelocity().y == 0) {
                spawn.setLanded();
            }
            if (!spawn.isLanded()) {
                return ObjectState.BEAM;
            }
            return ObjectState.MATERIALIZED_BEAM;
        }

        if (isDead) {
            return  ObjectState.DEAD;
        }
        if (isHit) {
            return ObjectState.HIT;
        }
        if (!isShooting) {
            if ((getLinearVelocity().y > 0 && currentState == ObjectState.JUMPING) ||
                    (getLinearVelocity().y < 0 && prevState == ObjectState.JUMPING)) {
                return ObjectState.JUMPING;
            }
            if (getLinearVelocity().y < 0) {
                return ObjectState.FALLING;
            }
            if (getLinearVelocity().x != 0) {
                return ObjectState.RUNNING;
            }
            return ObjectState.STANDING;
        } else {
            if ((getLinearVelocity().y > 0 && currentState == ObjectState.JUMPING_SHOOT) ||
                    (getLinearVelocity().y < 0 && prevState == ObjectState.JUMPING_SHOOT)) {
                return ObjectState.JUMPING_SHOOT;
            }
            if ((getLinearVelocity().y > 0 && currentState == ObjectState.RUNNING_SHOOT) ||
                    (getLinearVelocity().y < 0 && prevState == ObjectState.RUNNING_SHOOT)) {
                return ObjectState.JUMPING_SHOOT; // Falling shoot
            }
            if (getLinearVelocity().x != 0) {
                return ObjectState.RUNNING_SHOOT;
            }
            return ObjectState.STANDING_SHOOT;
        }
    }

    public abstract void update(float delta);

    public abstract boolean isReady();

    protected abstract void createModel();

}
