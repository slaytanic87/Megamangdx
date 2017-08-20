package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private ObjectState prevState;
    private ObjectState currentState = ObjectState.STANDING;

    public World world;
    public Body b2body;

    private Animation megamanRun;
    private Animation megamanStand;
    private Animation megamanClimb;

    private TextureRegion megamanJump;

    private PlayScreen screen;

    private Array<GunShoot> gunShoots = new Array<GunShoot>();

    public Megaman(PlayScreen playScreen) {
        screen = playScreen;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        /*

        //get run animation frames and add them to marioRun Animation
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        marioRun = new Animation(0.1f, frames);

        frames.clear();

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigMarioRun = new Animation(0.1f, frames);
        * */

    }

    public void createMegaman() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MegamanGame.PPM, 32 / MegamanGame.PPM);
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

    }

    public TextureRegion getFrame(float dt) {

        switch (currentState) {
            case STANDING: break;
            case RUNNING: break;
            case CLIMBING: break;
            case FALLING: break;
            default:;
        }

        return null;
    }


}