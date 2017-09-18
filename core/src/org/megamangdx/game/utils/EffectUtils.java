package org.megamangdx.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.megamangdx.game.sprites.effects.DefeatLob;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class EffectUtils {


    public static Array<DefeatLob> createExplosionLobEffects(World world, Body b2body, Array<TextureRegion> frames) {
        Array<DefeatLob> effects = new Array<DefeatLob>();
        final float impulse = 1.2f;
        final float inpulse2 = 0.8f;
        final float duration = 0.12f;
        effects.addAll(
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-impulse, 0), duration), // left
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(impulse, 0), duration), // right
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(0, impulse), duration), // up
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(0, -impulse), duration), // down
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-inpulse2, -inpulse2), duration), // left down
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(inpulse2, -inpulse2), duration), // right down
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(inpulse2, inpulse2), duration), // right up
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-inpulse2, inpulse2), duration) // left up
        );
        return effects;
    }

}
