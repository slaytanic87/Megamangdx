package org.megamangdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Data;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.scenes.Hud;
import org.megamangdx.game.sprites.Megaman;

/**
 * @author Lam on 12.08.17.
 */
@Data
public class PlayScreen implements Screen {

    private MegamanGame game;
    private Texture texture;
    private OrthographicCamera gameCamera;
    // for debugging purpose
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private Viewport viewport;
    private Hud hud;

    private TextureAtlas atlas;
    private World world;
    private Megaman player;

    public PlayScreen(MegamanGame game) {
        this.game = game;
        this.texture = new Texture("badlogic.jpg");
        gameCamera = new OrthographicCamera();

        // make the gamewindow is stretch able with StretchViewport or
        // make the gamewindow is scaleable with FitViewport
        viewport = new FitViewport(MegamanGame.V_WIDTH,
                MegamanGame.V_HEIGHT, gameCamera);
        hud = new Hud(game.batch);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell game batch to recognize where the camera is in the game world
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    public void update(float delta) {
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(delta);

        gameCamera.update();

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
