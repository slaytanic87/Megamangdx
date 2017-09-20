package org.megamangdx.game.sprites.effects;

import com.badlogic.gdx.audio.Sound;
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
 * @author Lam
 */
public class Bullet extends Sprite {

    public enum WeaponType  {BUSTER}

    private PlayScreen screen;
    private World world;
    private Body b2body;

    private Array<TextureRegion> frames = new Array<TextureRegion>();
    private WeaponType weaponType = WeaponType.BUSTER;

    private Animation<TextureRegion> fireAnimation;

    private boolean rightDirection;
    private float stateTime = 0;

    @Getter
    private boolean destroyed;
    private boolean setToDestroy;

    /**
     * Constructor with no gravity.
     * @param playScreen
     * @param x
     * @param y
     * @param rightDirection
     * @param weaponType
     */
    public Bullet(PlayScreen playScreen, float x, float y, boolean rightDirection, WeaponType weaponType) {
        this(playScreen, x, y, rightDirection, weaponType, 0);
    }

    /**
     * Constructor with specified gravity.
     * @param playScreen
     * @param x
     * @param y
     * @param rightDirection
     * @param weaponType
     * @param gravity gravity scale
     */
    public Bullet(PlayScreen playScreen, float x, float y, boolean rightDirection, WeaponType weaponType,
                  float gravity) {
        screen = playScreen;
        world = playScreen.getWorld();
        this.rightDirection = rightDirection;
        this.weaponType = weaponType;

        switch (weaponType) {
            case BUSTER:
                frames.add(new TextureRegion(screen.getAtlas().findRegion("normal1")));
                MegamanGame.assetManager.get(MegamanGame.MEGAMAN_BUSTER_SOUND, Sound.class).play();
                break;
            default:
        }

        fireAnimation = new Animation<TextureRegion>(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 12 / MegamanGame.PPM, 10 / MegamanGame.PPM);

        createBullet(gravity);
    }

    public void createBullet(float gravity) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(rightDirection ? getX() + 12 / MegamanGame.PPM : getX() - 12 / MegamanGame.PPM, getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        if (!world.isLocked()) {
            b2body = world.createBody(bodyDef);
        }
        // no gravity
        b2body.setGravityScale(gravity);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MegamanGame.PPM);

        // TODO collisions
        fixtureDef.filter.categoryBits = MegamanGame.BULLET_BIT;
        // define collision categorie, which should be detected
        fixtureDef.filter.maskBits = MegamanGame.GROUND_BIT | MegamanGame.PLATFORM_BIT
                | MegamanGame.ENEMY_BIT | MegamanGame.PLAYER_BIT;
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0;
        b2body.createFixture(fixtureDef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(rightDirection ? 2 : -2, 0));
    }

    public void update(float delta) {
        stateTime += delta;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        // destroy bullet on the next iteration
        if ((rightDirection && b2body.getLinearVelocity().x < 0) ||
                (!rightDirection && b2body.getLinearVelocity().x > 0)) {
            setToDestroy();
        }
    }

    public void setToDestroy() {
        this.setToDestroy = true;
    }

}
