package org.megamangdx.game.sprites;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.screens.PlayScreen;
import org.megamangdx.game.sprites.effects.Bullet;
import org.megamangdx.game.sprites.effects.DefeatLob;
import org.megamangdx.game.sprites.effects.Spawn;
import org.megamangdx.game.utils.EffectUtils;

/**
 * @author Lam
 */
@Data
public class Protoman extends BaseCharacter implements Telegraph {

    // red
    public static final String HEX_FIRST_COLOR = "dc2800ff"; // RRGGBBAA
    // gray
    public static final String HEX_SECOND_COLOR = "bcbcbcff";

    private static final float START_POSX = 350;
    private static final float START_POSY = 350;
    private static float HIT_TIMER_MAX = 0.5f;

    private Animation<TextureRegion> protomanStand;
    private Animation<TextureRegion> protomanStandShoot;
    private Animation<TextureRegion> protomanRun;
    private Animation<TextureRegion> protomanRunShoot;
    private Animation<TextureRegion> protomanJumpShoot;
    private Animation<TextureRegion> protomanHit;
    private TextureRegion protomanJump;

    private Array<Bullet> gunShots = new Array<Bullet>();
    private Array<DefeatLob> explosionLobs = new Array<DefeatLob>();

    private PlayScreen playScreen;
    private float hitTimer = 0;


    public Protoman(PlayScreen playScreen) {
        this.playScreen = playScreen;
        world = playScreen.getWorld();
        rightDirection = false;

        createModel();
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

    private void createExplosionLobs() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        Color[] targetColor = {
                new Color(Color.valueOf(HEX_FIRST_COLOR)),
                new Color(Color.valueOf(HEX_SECOND_COLOR))
        };
        Color[] oldColor = {
                new Color(Color.valueOf("0088fcff")),
                new Color(Color.valueOf("00d0eaff"))
        };
        for (int i = 1; i <= 4; i++) {
            TextureRegion textureRegion = new TextureRegion(playScreen.getAtlas().findRegion("Explosion" + i));
            frames.add(EffectUtils.tintingSpriteColor(oldColor, targetColor, textureRegion));
        }
        explosionLobs = EffectUtils.createExplosionLobEffects(world, b2body, frames);
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

    @Override
    protected void createModel() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(START_POSX / MegamanGame.PPM, START_POSY / MegamanGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        // A fixture has a shape, density, friction and restitution attached to it
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / MegamanGame.PPM);

        fixtureDef.filter.categoryBits = MegamanGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = MegamanGame.GROUND_BIT | MegamanGame.PLATFORM_BIT
                | MegamanGame.PLAYER_BIT | MegamanGame.WALL_BIT | MegamanGame.BULLET_BIT;

        fixtureDef.shape = shape;
        // set Spritedata
        b2body.createFixture(fixtureDef).setUserData(this);
    }

    public void hit() {
        if (!isHit) {
            super.hit();
            hitTimer = 0;
            MegamanGame.assetManager.get(MegamanGame.MEGAMAN_DAMAGE, Sound.class).play();
        }
    }

    public void shoot() {
        super.shoot();
        gunShots.add(new Bullet(playScreen, b2body.getPosition().x, b2body.getPosition().y, rightDirection,
                Bullet.WeaponType.BUSTER));
    }

    public void die() {
        if (!isDead) {
            super.die();
            createExplosionLobs();
            playScreen.stopMusic();
            MegamanGame.assetManager.get(MegamanGame.MEGAMAN_DEFEAT_SOUND, Sound.class).play();
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

    @Override
    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2,
                b2body.getPosition().y - getHeight() / 2);

        hitTimer = !isHit ? 0: hitTimer + delta;
        // reset hit animation after character was hit
        if (isHit && hitTimer >= HIT_TIMER_MAX) {
            isHit = false;
        }

        setRegion(getFrame(delta));
        // remove destroyed gunshots from memory
        for (Bullet bullet: gunShots) {
            bullet.update(delta);
            if (bullet.isDestroyed()) {
                gunShots.removeValue(bullet, true);
            }
        }
        for (DefeatLob lob: explosionLobs) {
            lob.update(delta);
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
        for (DefeatLob lob: explosionLobs) {
            lob.draw(batch);
        }
    }

    @Override
    public boolean isReady() {
        return spawn.isSpawnFinished() && !isDead;
    }
}
