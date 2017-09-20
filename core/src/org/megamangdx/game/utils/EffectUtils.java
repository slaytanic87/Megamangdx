package org.megamangdx.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
        float impulse = 1.2f;
        float inpulse2 = 0.8f;
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

        impulse -= 0.7f;

        effects.addAll(
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(-impulse, 0), duration), // left
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(impulse, 0), duration), // right
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(0, impulse), duration), // up
                new DefeatLob(world, b2body, b2body.getPosition().x, b2body.getPosition().y,
                        frames, new Vector2(0, -impulse), duration) // down
        );
        return effects;
    }

    public static TextureRegion tintingSpriteColor(Color[] oldColors, Color[] targetColors,
                                                   TextureRegion textureRegion) {
        if (oldColors.length != targetColors.length) {
            throw new RuntimeException("the number of reference colors size have to be the same "
                   + "length as target colors!");
        }

        Texture texture = textureRegion.getTexture();
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        for (int x = 0; x < textureRegion.getRegionWidth(); x++) {
            for (int y = 0; y < textureRegion.getRegionHeight(); y++) {
                int colorInt8888 = pixmap.getPixel(textureRegion.getRegionX() + x,
                        textureRegion.getRegionY() + y);
                for (int i = 0; i < oldColors.length; i++) {
                    if (Color.rgba8888(oldColors[i]) == colorInt8888) {
                        pixmap.drawPixel(textureRegion.getRegionX() + x, textureRegion.getRegionY() + y,
                                Color.rgba8888(targetColors[i]));
                    }
                }
            }
        }
        textureRegion.setTexture(new Texture(pixmap));
        texture.getTextureData().disposePixmap();
        pixmap.dispose();
        return textureRegion;
    }


}
