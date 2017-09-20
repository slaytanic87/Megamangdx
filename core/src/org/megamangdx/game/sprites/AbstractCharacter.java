package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public abstract class AbstractCharacter extends Sprite {

    protected boolean isDead = false;
    protected boolean isShooting = false;
    protected boolean isHit = false;

    public World world;
    public Body b2body;

    protected float stateTimer = 0;

    public void hit() {

    }

    public void die() {

    }

    public void jump() {

    }

    public void shoot() {

    }

    public void moveRight() {

    }

    public abstract void update(float delta);

    public abstract void isReady();

}
