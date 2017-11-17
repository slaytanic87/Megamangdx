package org.megamangdx.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import lombok.Getter;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class SoundUtils {

    public enum SoundType {
        MEGAMAN_WILY_STAGE_1_2("audio/music/wily-stage-1-2.mp3"),
        MEGAMAN_DEFEAT_SOUND("audio/sounds/megaman-defeat.mp3"),
        MEGAMAN_BUSTER_SOUND("audio/sounds/megaman-buster.mp3"),
        MEGAMAN_DAMAGE("audio/sounds/megaman-damage.mp3"),
        MEGAMAN_ENERGYFILL("audio/sounds/megaman-energyfill.mp3");

        @Getter
        String path;

        SoundType(String path) {
            this.path = path;
        }

        public SoundType getSound() {
            return this;
        }

        public String toString() {
            return path;
        }
    }

    public static SoundUtils INSTANCE = new SoundUtils();
    private static AssetManager assetManager;

    private SoundUtils() {
        assetManager = new AssetManager();
        assetManager.load(SoundType.MEGAMAN_WILY_STAGE_1_2.getPath(), Music.class);
        assetManager.load(SoundType.MEGAMAN_DEFEAT_SOUND.getPath(), SoundType.class);
        assetManager.load(SoundType.MEGAMAN_BUSTER_SOUND.getPath(), SoundType.class);
        assetManager.load(SoundType.MEGAMAN_DAMAGE.getPath(), SoundType.class);
        assetManager.load(SoundType.MEGAMAN_ENERGYFILL.getPath(), SoundType.class);
        assetManager.finishLoading();
    }

    public void playEffect(SoundType type) {
        assetManager.get(type.getPath(), Sound.class).play();
    }

}
