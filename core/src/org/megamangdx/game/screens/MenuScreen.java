package org.megamangdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.megamangdx.game.MegamanGame;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class MenuScreen implements Screen {

    private Viewport viewport;
    private OrthographicCamera gameCamera;

    public MenuScreen() {
        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(MegamanGame.V_WIDTH / MegamanGame.PPM,
                MegamanGame.V_HEIGHT / MegamanGame.PPM, gameCamera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        }
    }

    private void update(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
