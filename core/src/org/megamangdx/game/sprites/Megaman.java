package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.ObjectState;
import org.megamangdx.game.screens.PlayScreen;

/**
 * @author Lam on 12.08.17.
 */
@Data
public class Megaman extends Sprite {

    public static final float MAX_VELOCITY = 1.2f;
    public static final int START_POSX = 48;
    public static final int START_POSY = 48;

    private ObjectState prevState = ObjectState.STANDING;
    private ObjectState currentState = ObjectState.STANDING;

    public World world;
    public Body b2body;

    private Animation<TextureRegion> megamanRun;
    private Animation<TextureRegion> megamanStand;
    private Animation<TextureRegion> megamanClimb;

    private TextureRegion megamanJump;

    private Array<GunShot> gunShots = new Array<GunShot>();

    private PlayScreen playScreen;

    private boolean rightDirection;

    public Megaman(PlayScreen playScreen) {
        world = playScreen.getWorld();
        this.playScreen = playScreen;
        this.rightDirection = true;
        createMegaman();

        createStandAnimation();
        createRunAnimation();

    }

    private void createRunAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i <= 3; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Run" + i)));
        }
        megamanRun = new Animation<TextureRegion>(0.1f, frames);
    }

    private void createStandAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Stand1")));
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Stand2")));
        megamanStand = new Animation<TextureRegion>(0.1f, frames);
    }

    public void createMegaman() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(START_POSX / MegamanGame.PPM, START_POSY / MegamanGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        // A fixture has a shape, density, friction and restitution attached to it
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MegamanGame.PPM);

        /* TODO Filter collision categories
        fixtureDef.filter.categoryBits =
        fixtureDef.filter.maskBits
        */

        fixtureDef.shape = shape;
        // set Spritedata
        b2body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float dt) {

        // delete Gun shoot
        for (GunShot shoot : gunShots) {
            shoot.update(dt);
            if (shoot.isDestroyed()) {
                gunShots.removeValue(shoot, true);
            }
        }
    }

    public void moveRight() {
        b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void moveLeft() {
        b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 2.8f), b2body.getWorldCenter(), true);
    }

    public void shoot() {
        gunShots.add(new GunShot(playScreen, b2body.getPosition().x, b2body.getPosition().y, rightDirection,
                GunShot.WeaponType.NORMAL));
    }

    public void hit() {
        // TODO is hit by enemy
    }

    public Vector2 getLinearVelocity() {
        return b2body.getLinearVelocity();
    }

    public TextureRegion getFrame(float delta) {
        // TODO implement textures
        switch (currentState) {
            case STANDING:
                break;
            case RUNNING:
                break;
            case CLIMBING:
                break;
            case FALLING:
                break;
            default:
        }

        return null;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        for (GunShot shoot: gunShots) {
            shoot.draw(batch);
        }
    }

    public ObjectState getState() {
        if (getLinearVelocity().x != 0) {
            return ObjectState.RUNNING;
        } else if (getLinearVelocity().y != 0) {
            return ObjectState.JUMPING;
        }
        return ObjectState.STANDING;
    }

}