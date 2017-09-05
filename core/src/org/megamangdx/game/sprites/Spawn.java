package org.megamangdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;

/**
 * @author Lam 2017
 */
public class Spawn {

    @Getter
    private Animation<TextureRegion> landingAnimation;

    @Getter
    private TextureRegion beamTexture;

    @Getter
    private boolean landed;

    @Getter
    private boolean spawnFinished;

    Spawn() {
        this.landed = false;
        this.spawnFinished = false;
    }

    void loadLandingAnimation(Array<TextureRegion> frames, float duration) {
        this.landingAnimation = new Animation<TextureRegion>(duration, frames);
    }

    void loadBeamTexture(TextureRegion beamTexture) {
        this.beamTexture = beamTexture;
    }

    void finishSpawn() {
        this.spawnFinished = true;
    }

    void setLanded() {
        this.landed = true;
    }
}
