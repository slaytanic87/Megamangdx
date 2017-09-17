package org.megamangdx.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.megamangdx.game.sprites.effects.Explosion;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class Utils {


    public static Array<Explosion> createExplosionEffect(World world, Body b2body, Array<TextureRegion> frames) {
        Array<Explosion> effects = new Array<Explosion>();
        final int impulse = 2;
        final float duration = 0.12f;
        effects.addAll(
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-impulse, 0), duration), // left
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(impulse, 0), duration), // right
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(0, impulse), duration), // up
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(0, -impulse), duration), // down
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-impulse, -impulse), duration), // left down
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(impulse, -impulse), duration), // right down
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(impulse, impulse), duration), // right up
                new Explosion(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-impulse, impulse), duration) // left up
        );
        return effects;
    }

}
