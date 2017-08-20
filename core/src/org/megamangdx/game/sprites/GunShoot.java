package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.megamangdx.game.screens.PlayScreen;

/**
 * Created by Lam on 14.08.17.
 */
public class GunShoot extends Sprite {

    private PlayScreen screen;
    private World world;
    private Array<TextureRegion> frames;

}
