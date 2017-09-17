package org.megamangdx.game.sprites.effects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.megamangdx.game.MegamanGame;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class Explosion extends Sprite {

    private World world;
    private Body b2body;
    private Animation<TextureRegion> explodeAnimation;
    private float stateTime = 0;

    public Explosion(World world, Body b2body, float x, float y, Array<TextureRegion> frames, Vector2 direction,
                     float duration) {
        this.world = world;
        this.b2body = b2body;
        this.explodeAnimation = new Animation<TextureRegion>(duration,  frames);
        setRegion(explodeAnimation.getKeyFrame(0));
        setBounds(x, y, frames.get(0).getRegionWidth() / MegamanGame.PPM,
                frames.get(0).getRegionHeight() / MegamanGame.PPM);
        createExplosionModel(direction);
    }

    private void createExplosionModel(Vector2 vector2) {
        BodyDef bodyDef = new BodyDef();
        // set init body position
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Is the world locked (in the middle of a time step)
        if (!world.isLocked()) {
            b2body = world.createBody(bodyDef);
        }
        // no gravity
        b2body.setGravityScale(0);
        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MegamanGame.PPM);

        // TODO collisions
        // fixtureDef.filter.categoryBits = .FIRE_BIT;
        // fixtureDef.filter.maskBits
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0;
        b2body.createFixture(fixtureDef).setUserData(this);
        // set direction impulse vector
        b2body.setLinearVelocity(vector2);
    }

    public void update(float delta) {
        stateTime += delta;

        TextureRegion textureRegion = explodeAnimation.getKeyFrame(stateTime, true);
        setRegion(textureRegion);

        setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2,
                textureRegion.getRegionWidth() / MegamanGame.PPM,
                textureRegion.getRegionHeight() / MegamanGame.PPM);
    }

}
