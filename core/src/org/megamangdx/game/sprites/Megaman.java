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
    public static final int START_POSX = 32;
    public static final int START_POSY = 32;

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
    private boolean isDead = false;
    private float stateTimer = 0;

    public Megaman(PlayScreen playScreen) {
        world = playScreen.getWorld();
        this.playScreen = playScreen;
        this.rightDirection = true;
        createMegaman();

        createStandAnimation();
        createRunAnimation();
        createMegamanJump();
        createMegamanClimb();

        setBounds(0, 0, 32 / MegamanGame.PPM, 32 / MegamanGame.PPM);
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
        for (int i = 1; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Stand" + i)));
        }
        megamanStand = new Animation<TextureRegion>(0.9f, frames);
//        setRegion(frames.get(0));
    }

    private void createMegamanJump() {
        megamanJump = new TextureRegion(playScreen.getAtlas().findRegion("Jump"));
    }

    private void createMegamanClimb() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Climb" + i)));
        }
        megamanClimb = new Animation<TextureRegion>(0.1f, frames);
    }

    public void createMegaman() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(START_POSX / MegamanGame.PPM, START_POSY / MegamanGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        // A fixture has a shape, density, friction and restitution attached to it
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / MegamanGame.PPM);

        /* TODO Filter collision categories
        fixtureDef.filter.categoryBits =
        fixtureDef.filter.maskBits
        */

        fixtureDef.shape = shape;
        // set Spritedata
        b2body.createFixture(fixtureDef).setUserData(this);

//        EdgeShape head = new EdgeShape();
//        head.set(new Vector2(-2 / MegamanGame.PPM, 6 / MegamanGame.PPM),
//                new Vector2(2 / MegamanGame.PPM, 6 / MegamanGame.PPM));
//        // fixtureDef.filter.categoryBits
//        fixtureDef.shape = head;
//        fixtureDef.isSensor = true;
//        b2body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float delta) {
        setRegion(getFrame(delta));

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // delete Gun shoot
        for (GunShot shoot : gunShots) {
            shoot.update(delta);
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

    public void die() {
        this.isDead = true;
    }

    public void jump() {
        if (currentState != ObjectState.JUMPING) {
            b2body.applyLinearImpulse(new Vector2(0, 2.8f), b2body.getWorldCenter(), true);
            currentState = ObjectState.JUMPING;
        }
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
        currentState = getState();
        // TODO implement textures
        TextureRegion textureRegion = null;
        switch (currentState) {
            case DEAD:
                break;
            case RUNNING:
                textureRegion = megamanRun.getKeyFrame(delta, true);
                break;
            case CLIMBING:
                break;
            case STANDING:
                textureRegion = megamanStand.getKeyFrame(delta, true);
                break;
            case JUMPING:
            case FALLING:
            default:
                textureRegion = megamanJump;
                break;
        }

        if ((getLinearVelocity().x < 0 || !rightDirection) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            rightDirection = false;
        } else if ((getLinearVelocity().x > 0 || rightDirection)
                && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            rightDirection = true;
        }

        stateTimer = (currentState == prevState) ? stateTimer + delta : 0;
        prevState = currentState;

        return textureRegion;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        for (GunShot shoot: gunShots) {
            shoot.draw(batch);
        }
    }

    public ObjectState getState() {
        if (isDead) {
            return  ObjectState.DEAD;
        }
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
    }

}