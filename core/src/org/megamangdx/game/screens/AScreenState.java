package org.megamangdx.game.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.ScreenStateManager;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public abstract class AScreenState {

    protected ScreenStateManager stateManager;
    protected MegamanGame game;
    protected OrthographicCamera gameCamera;

    // for debugging purpose
    protected Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    protected Viewport viewport;

    protected Music music;

    public AScreenState(ScreenStateManager stateManager) {
        this.game = stateManager.getMegamanGame();
        this.stateManager = stateManager;

        gameCamera = new OrthographicCamera();

        // make the gamewindow is stretchable with StretchViewport or
        // make the gamewindow is scaleable with FitViewport
        viewport = new FitViewport(MegamanGame.V_WIDTH / MegamanGame.PPM,
                MegamanGame.V_HEIGHT / MegamanGame.PPM, gameCamera);
    }

    public abstract void update(float delta);

    public abstract void render(float delta);

    public abstract void dispose();

    public abstract void resize(int width, int height);

}
