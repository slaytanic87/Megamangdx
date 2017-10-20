package org.megamangdx.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import org.megamangdx.game.application.MegamanGame;
import org.megamangdx.game.screens.PlayScreen;
import org.megamangdx.game.sprites.effects.Bullet;
import org.megamangdx.game.sprites.effects.DefeatLob;
import org.megamangdx.game.sprites.effects.Spawn;
import org.megamangdx.game.utils.GraphicUtils;

/**
 * @author Lam on 12.08.17.
 */
@Data
public class Megaman extends BaseCharacter {

    public static final String HEX_FIRST_COLOR = "0073f7ff"; // RRGGBBAA
    public static final String HEX_SECOND_COLOR = "00ffffff";

    public static final float MAX_VELOCITY = 1.2f;
    public static final int START_POSX = 40;
    public static final int START_POSY = 350;

    private Animation<TextureRegion> megamanRun;
    private Animation<TextureRegion> megamanStand;
    private Animation<TextureRegion> megamanClimb;
    private Animation<TextureRegion> megamanStandShoot;
    private Animation<TextureRegion> megamanRunShoot;
    private Animation<TextureRegion> megamanJumpShoot;
    private Animation<TextureRegion> megamanDamage;

    private TextureRegion megamanJump;

    private Array<Bullet> gunShots = new Array<Bullet>();

    private Array<DefeatLob> explosionLobs = new Array<DefeatLob>();

    private PlayScreen playScreen;

    private static float HIT_TIMER_MAX = 0.5f;
    private float hitTimer = 0;

    public Megaman(PlayScreen playScreen) {
        this.world = playScreen.getWorld();
        this.playScreen = playScreen;
        this.rightDirection = true;

        createModel();
        createSpawnAnimation();
        createStandAnimation();
        createRunAnimation();
        createJumpAnimation();
        createClimbAnimation();
        createStandShootTexture();
        createRunShootAnimation();
        createJumpShootAnimation();
        createHitAnimation();
        setBounds(0, 0, 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
    }

    private void createHitAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Hit")));
        frames.add(new TextureRegion(playScreen.getAtlas().findRegion("damage")));

        megamanDamage = new Animation<TextureRegion>(0.35f, frames);
    }

    private void createExplosionLobs() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i <= 4; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Explosion" + i)));
        }
        explosionLobs = GraphicUtils.createExplosionLobEffects(world, b2body, frames);
    }

    private void createSpawnAnimation() {
        this.spawn = new Spawn();
        spawn.loadBeamTexture(new TextureRegion(playScreen.getAtlas().findRegion("Spawn0")));
        Array<TextureRegion> materializedFrames = new Array<TextureRegion>();
        for (int i = 1; i <= 2; i++) {
            materializedFrames.add(new TextureRegion(playScreen.getAtlas().findRegion("Spawn" + i)));
        }
        spawn.loadLandingAnimation(materializedFrames, 0.25f);
    }

    private void createRunAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i <= 3; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Run" + i)));
        }
        megamanRun = new Animation<TextureRegion>(0.087f, frames);
    }

    private void createRunShootAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i <= 3; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Run_shoot" + i)));
        }
        megamanRunShoot = new Animation<TextureRegion>(0.077f, frames);
    }

    private void createJumpShootAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion jumpShoot = new TextureRegion(playScreen.getAtlas().findRegion("Jump_shoot"));
        frames.add(jumpShoot);
        frames.add(jumpShoot);
        megamanJumpShoot = new Animation<TextureRegion>(0.4f, frames);
    }

    private void createStandAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Stand" + i)));
        }
        megamanStand = new Animation<TextureRegion>(0.9f, frames);

    }

    private void createStandShootTexture() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        TextureRegion stand = new TextureRegion(playScreen.getAtlas().findRegion("Stand_shoot"));
        frames.add(stand);
        megamanStandShoot = new Animation<TextureRegion>(0.2f, frames);
    }

    private void createJumpAnimation() {
        megamanJump = new TextureRegion(playScreen.getAtlas().findRegion("Jump"));
    }

    private void createClimbAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i <= 2; i++) {
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Climb" + i)));
        }
        megamanClimb = new Animation<TextureRegion>(0.1f, frames);
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

        fixtureDef.filter.categoryBits = MegamanGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = MegamanGame.GROUND_BIT | MegamanGame.PLATFORM_BIT
                | MegamanGame.ENEMY_BIT | MegamanGame.WALL_BIT | MegamanGame.BULLET_BIT;

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

    @Override
    public void update(float delta) {

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        hitTimer = !isHit ? 0: hitTimer + delta;
        // reset hit animation after character was hit
        if (isHit && hitTimer >= HIT_TIMER_MAX) {
            isHit = false;
        }

        setRegion(getFrame(delta));
        // delete Gun shoot
        for (Bullet shoot : gunShots) {
            shoot.update(delta);
            if (shoot.isDestroyed()) {
                gunShots.removeValue(shoot, true);
            }
        }

        for (DefeatLob explosion : explosionLobs) {
            explosion.update(delta);
        }
    }

    @Override
    public void die() {
        if (!isDead) {
            super.die();
            createExplosionLobs();
            MegamanGame.assetManager.get(MegamanGame.MEGAMAN_DEFEAT_SOUND, Sound.class).play();
            playScreen.stopMusic();
        }
    }

    @Override
    public void shoot() {
        super.shoot();
        gunShots.add(new Bullet(playScreen, b2body.getPosition().x, b2body.getPosition().y, rightDirection,
                Bullet.WeaponType.BUSTER));
    }

    @Override
    public void hit() {
        if (!isHit) {
            super.hit();
            hitTimer = 0;
            MegamanGame.assetManager.get(MegamanGame.MEGAMAN_DAMAGE, Sound.class).play();
        }
    }


    private TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion textureRegion = null;
        // reset sprite size and position
        switch (currentState) {
            case BEAM:
                setBounds(getX(), getY(), 12 / MegamanGame.PPM, 32 / MegamanGame.PPM);
                textureRegion = spawn.getBeamTexture();
                break;
            case MATERIALIZED_BEAM:
                setBounds(getX(), getY(), 22 / MegamanGame.PPM, 20 / MegamanGame.PPM);
                textureRegion = spawn.getLandingAnimation().getKeyFrame(stateTimer, false);
                break;
            case HIT:
            case DEAD:
                setBounds(getX(), getY(), 30 / MegamanGame.PPM, 30 / MegamanGame.PPM);
                textureRegion = megamanDamage.getKeyFrame(stateTimer, true);
                break;
            case RUNNING:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = megamanRun.getKeyFrame(stateTimer, true);
                break;
            case CLIMBING:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = megamanClimb.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
                setBounds(getX(), getY(), 24 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = megamanStand.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_SHOOT:
                setBounds(getX(), getY(), 32 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = megamanRunShoot.getKeyFrame(stateTimer, true);
                break;
            case STANDING_SHOOT:
                setBounds(getX(), getY(), 32 / MegamanGame.PPM, 24 / MegamanGame.PPM);
                textureRegion = megamanStandShoot.getKeyFrame(stateTimer, true);
                break;
            case JUMPING_SHOOT:
                setBounds(getX(), getY(), 32 / MegamanGame.PPM, 32 / MegamanGame.PPM);
                textureRegion = megamanJumpShoot.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
            case FALLING:
            default:
                // set new sprite size and position because of size and position is changing
                setBounds(getX(), getY(), 27 / MegamanGame.PPM, 32 / MegamanGame.PPM);
                textureRegion = megamanJump;
                break;
        }
        // check if megaman is landing when beam
        spawn.processFinishSpawn(stateTimer);

        // flip sprite, depends on direction
        if ((getLinearVelocity().x < 0 || !rightDirection) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            rightDirection = false;
        } else if ((getLinearVelocity().x > 0 || rightDirection) && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            rightDirection = true;
        }

        // reset shoot state if animation is over
        if (megamanStandShoot.isAnimationFinished(stateTimer) && megamanRunShoot.isAnimationFinished(stateTimer)
                && megamanJumpShoot.isAnimationFinished(stateTimer)) {
            isShooting = false;
        }

        // reset shoot state if the state is changing from shooting mode to not shooting mode
        isShooting = ObjectState.resetShootState(prevState, currentState, isShooting);

        // if any state or tileset was change, then reset the state timer for the new tileset
        stateTimer = (currentState == prevState) ? stateTimer + delta : 0;
        prevState = currentState;

        return textureRegion;
    }


    @Override
    public void draw(Batch batch) {
        if (!isDead) {
            super.draw(batch);
        }

        for (Bullet shoot: gunShots) {
            shoot.draw(batch);
        }
        for (DefeatLob effect: explosionLobs) {
            effect.draw(batch);
        }
    }

    @Override
    public boolean isReady() {
        return spawn.isSpawnFinished() && !isDead && !isHit;
    }
}