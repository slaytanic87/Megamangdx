package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.screens.PlayScreen;

/**
 * Created by Lam on 14.08.17.
 */
public class GunShot extends Sprite {

    public enum WeaponType  { NORMAL }

    private PlayScreen screen;
    private World world;
    private Body b2body;

    private Array<TextureRegion> frames = new Array<TextureRegion>();
    private WeaponType weaponType = WeaponType.NORMAL;

    private Animation<TextureRegion> fireAnimation;

    private boolean rightDirection;
    private float stateTime = 0;

    @Getter
    private boolean destroyed;
    private boolean setToDestroy;

    public GunShot(PlayScreen playScreen, float x, float y, boolean rightDirection, WeaponType weaponType) {
        screen = playScreen;
        world = playScreen.getWorld();
        this.rightDirection = rightDirection;
        this.weaponType = weaponType;

        switch (weaponType) {
            case NORMAL: frames.add(new TextureRegion(screen.getAtlas().findRegion("normal1")));
            default:
        }

        fireAnimation = new Animation<TextureRegion>(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 6 / MegamanGame.PPM, 6 / MegamanGame.PPM);

        createWeapon();
    }

    public void createWeapon() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(rightDirection ? getX() + 12 / MegamanGame.PPM : getX() - 12 / MegamanGame.PPM, getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        if (!world.isLocked()) {
            b2body = world.createBody(bodyDef);
        }

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
        b2body.setLinearVelocity(new Vector2(rightDirection ? 2 : -2, 2.5f));
    }

    public void update(float delta) {
        stateTime += delta;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        if ((rightDirection && b2body.getLinearVelocity().x < 0) ||
                (!rightDirection && b2body.getLinearVelocity().x > 0)) {
            setToDestroy();
        }
    }

    public void setToDestroy() {
        this.setToDestroy = true;
    }

}
