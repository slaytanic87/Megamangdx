package org.megamangdx.game.sprites;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.screens.PlayScreen;
import org.megamangdx.game.sprites.effects.Bullet;
import org.megamangdx.game.sprites.effects.Spawn;

import static org.megamangdx.game.sprites.ObjectState.*;

/**
 * @author Lam
 */
@Data
public class Protoman extends Sprite implements Telegraph {

    private static final float START_POSX = 350;
    private static final float START_POSY = 350;

    private ObjectState prevState;
    private ObjectState currentState;
    private World world;
    private Body b2body;

    private Animation<TextureRegion> protomanStand;
    private Animation<TextureRegion> protomanStandShoot;
    private Animation<TextureRegion> protomanRun;
    private Animation<TextureRegion> protomanRunShoot;
    private Animation<TextureRegion> protomanJumpShoot;
    private Animation<TextureRegion> protomanHit;

    private TextureRegion protomanJump;

    private boolean rightDirection;

    private boolean isDead = false;
    private boolean isShooting = false;

    private Array<Bullet> gunShots = new Array<Bullet>();

    private float stateTimer;

    private PlayScreen playScreen;
    private Spawn spawn;

    public Protoman(PlayScreen playScreen) {
        this.playScreen = playScreen;
        world = playScreen.getWorld();
        rightDirection = false;

        createProtomanModel();
        createSpawnAnimation();
        createStandAnimation();
        createStandShoot();
        createRunAnimation();
        createJumpAnimation();
        createJumpShootAnimation();
        createHitAnimation();
        createRunShootAnimation();

        setBounds(0, 0, 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
    }

    private void createHitAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("protoman_hit")));
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("damage")));
        protomanHit = new Animation<TextureRegion>(0.8f, frames);
    }

    private void createSpawnAnimation() {
        this.spawn = new Spawn();
        spawn.loadBeamTexture(new TextureRegion(playScreen.getAtlas().findRegion("protoman_spawn0")));
        Array<TextureRegion> materializedFrames = new Array<TextureRegion>();
        for (int i = 1; i <= 2; i++) {
            materializedFrames.add(new TextureRegion(playScreen.getAtlas().findRegion("protoman_spawn" + i)));
        }
        spawn.loadLandingAnimation(materializedFrames, 0.25f);
    }

    private void createStandShoot() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("protoman_stand_shoot")));
        protomanStandShoot = new Animation<TextureRegion>(0.2f, frames);
    }

    private void createRunShootAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("protoman_run_shoot" + i)));
        }
        protomanRunShoot = new Animation<TextureRegion>(0.077f, frames);
    }

    private void createJumpShootAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion jumpShoot = new TextureRegion(playScreen.getAtlas().findRegion("protoman_jump_shoot"));
        frames.add(jumpShoot);
        frames.add(jumpShoot);
        protomanJumpShoot = new Animation<TextureRegion>(0.4f, frames);
    }

    private void createJumpAnimation() {
        protomanJump = new TextureRegion(playScreen.getAtlas().findRegion("protoman_jump"));
    }

    private void createRunAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("protoman_run" + i)));
        }
        protomanRun = new Animation<TextureRegion>(0.087f, frames);
    }

    private void createStandAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("protoman_stand" + i)));
        }
        protomanStand = new Animation<TextureRegion>(0.2f, frames);
    }

    private void createProtomanModel() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(START_POSX / MegamanGame.PPM, START_POSY / MegamanGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        // A fixture has a shape, density, friction and restitution attached to it
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / MegamanGame.PPM);

        //TODO Filter collision categories
        fixtureDef.filter.categoryBits = MegamanGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = MegamanGame.GROUND_BIT | MegamanGame.PLATFORM_BIT
                | MegamanGame.PLAYER_BIT | MegamanGame.WALL_BIT | MegamanGame.BULLET_BIT;

        fixtureDef.shape = shape;
        // set Spritedata
        b2body.createFixture(fixtureDef).setUserData(this);
    }

    public void shoot() {
        isShooting = true;
        gunShots.add(new Bullet(playScreen, b2body.getPosition().x, b2body.getPosition().y, rightDirection,
                Bullet.WeaponType.BUSTER));
        // if the player is in the air and shoot button was hit
        if (currentState == ObjectState.JUMPING || currentState == ObjectState.FALLING) {
            currentState = JUMPING_SHOOT;
        }
    }

    public void die() {
        if (!isDead()) {
            this.isDead = true;
            world.destroyBody(b2body);
            MegamanGame.assetManager.get(MegamanGame.MEGAMAN_DEFEAT_SOUND, Sound.class).play();
            playScreen.stopMusic();
        }
    }

    /**
     * Handle incoming messages from Telegram.
     * @param msg message from sender
     * @return ist message was delivered?
     */
    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }


    public void moveLeft() {
        b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void moveRight() {
        b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void jump() {
        if (!isShooting) {
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

    public Vector2 getLinearVelocity() {
        return b2body.getLinearVelocity();
    }

    private ObjectState getState() {
        if (!spawn.isSpawnFinished()) {
            if (getLinearVelocity().y == 0) {
                spawn.setLanded();
            }
            if (!spawn.isLanded()) {
                return BEAM;
            }
            return MATERIALIZED_BEAM;
        }
        if (isDead) {
            return DEAD;
        }
        if (!isShooting) {
            if ((getLinearVelocity().y > 0 && currentState == ObjectState.JUMPING)
                    || (getLinearVelocity().y < 0 && prevState == ObjectState.JUMPING)) {
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
            if ((getLinearVelocity().y > 0 && currentState == ObjectState.JUMPING_SHOOT)
                    || (getLinearVelocity().y < 0 && prevState == ObjectState.JUMPING_SHOOT)) {
                return JUMPING_SHOOT;
            }
            if (getLinearVelocity().x != 0) {
                return ObjectState.RUNNING_SHOOT;
            }
            return ObjectState.STANDING_SHOOT;
        }
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion textureRegion = null;
        switch (currentState) {
            case BEAM:
                setBounds(getX(), getY(), 7 / MegamanGame.PPM, 32 / MegamanGame.PPM);
                textureRegion = spawn.getBeamTexture();
                break;
            case MATERIALIZED_BEAM:
                setBounds(getX(), getY(), 22 / MegamanGame.PPM, 19 / MegamanGame.PPM);
                textureRegion = spawn.getLandingAnimation().getKeyFrame(stateTimer);
                break;
            case HIT:
            case DEAD:
                setBounds(getX(), getY(), 30 / MegamanGame.PPM, 30 / MegamanGame.PPM);
                textureRegion = protomanHit.getKeyFrame(stateTimer, true);
                break;
            case RUNNING:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = protomanRun.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_SHOOT:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = protomanRunShoot.getKeyFrame(stateTimer, true);
                break;
            case JUMPING_SHOOT:
                setBounds(getX(), getY(), 30 / MegamanGame.PPM, 30 / MegamanGame.PPM);
                textureRegion = protomanJumpShoot.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = protomanStand.getKeyFrame(stateTimer, true);
                break;
            case STANDING_SHOOT:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = protomanStandShoot.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case JUMPING:
            default:
                setBounds(getX(), getY(), 27 / MegamanGame.PPM, 30 / MegamanGame.PPM);
                textureRegion = protomanJump;
                break;
        }

        // check if protoman is landing when beam
        spawn.processFinishSpawn(stateTimer);

        // flip sprite, depends on direction
        if ((getLinearVelocity().x < 0 || !rightDirection) && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            rightDirection = false;
        } else if ((getLinearVelocity().x > 0 || rightDirection) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            rightDirection = true;
        }

        // reset shoot state if animation is over
        if (protomanStandShoot.isAnimationFinished(stateTimer) && protomanRunShoot.isAnimationFinished(stateTimer)
                && protomanJumpShoot.isAnimationFinished(stateTimer)) {
            isShooting = false;
        }

        // reset shoot state if the state is changing from shooting mode to not shooting mode
        isShooting = ObjectState.resetShootState(prevState, currentState, isShooting);

        stateTimer = (prevState == currentState) ? stateTimer + delta : 0;
        prevState = currentState;

        return textureRegion;
    }

    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2,
                b2body.getPosition().y - getHeight() / 2);

        setRegion(getFrame(delta));
        // remove destroyed gunshots from memory
        for (Bullet bullet: gunShots) {
            bullet.update(delta);
            if (bullet.isDestroyed()) {
                gunShots.removeValue(bullet, true);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!isDead) {
            super.draw(batch);
        }
        for (Bullet shoot: gunShots) {
            shoot.draw(batch);
        }
    }

    public boolean isReady() {
        return spawn.isSpawnFinished() && !isDead();
    }
}
