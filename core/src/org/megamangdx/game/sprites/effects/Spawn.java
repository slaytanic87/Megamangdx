package org.megamangdx.game.sprites.effects;

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

    public Spawn() {
        this.landed = false;
        this.spawnFinished = false;
    }

    public void loadLandingAnimation(Array<TextureRegion> frames, float duration) {
        this.landingAnimation = new Animation<TextureRegion>(duration, frames);
    }

    public void loadBeamTexture(TextureRegion beamTexture) {
        this.beamTexture = beamTexture;
    }

    public void processFinishSpawn(float stateTimer) {
        if(getLandingAnimation().isAnimationFinished(stateTimer) && !isSpawnFinished()) {
            this.spawnFinished = true;
        }
    }

    public void setLanded() {
        this.landed = true;
    }
}
