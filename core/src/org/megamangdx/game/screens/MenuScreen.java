package org.megamangdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.megamangdx.game.application.MegamanGame;
import org.megamangdx.game.ScreenStateManager;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class MenuScreen extends AScreenState {

    // for on-screen controls
    protected Stage stage;
    private Skin skin;

    public MenuScreen(ScreenStateManager manager) {
        super(manager);
        viewport = new FitViewport(MegamanGame.V_WIDTH / MegamanGame.PPM,
                MegamanGame.V_HEIGHT / MegamanGame.PPM, gameCamera);
        centralizedGameCamera();
        stage = new Stage(viewport, game.batch);
        skin = new Skin();
    }

    private void centralizedGameCamera() {
        gameCamera.position.set(viewport.getWorldWidth() / 2,
                viewport.getWorldHeight() / 2, 0);
    }


    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.bottom();
        Label startgame = new Label("Start Game",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label options = new Label("Info",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//        TextButton textButton = new TextButton();
        table.add(startgame).expandX().padBottom(10);
        table.row();
        table.add(options).expandX().padBottom(10);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        this.update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        stage.draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        }
    }

    @Override
    protected void update(float delta) {
        handleInput();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        centralizedGameCamera();
        gameCamera.update();
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
