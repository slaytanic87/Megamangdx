package org.megamangdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Data;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.scenes.Hud;
import org.megamangdx.game.sprites.Megaman;
import org.megamangdx.game.utils.B2WorldCreator;

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

    // Tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private B2WorldCreator b2WorldCreator;

    public PlayScreen(MegamanGame game) {
        this.game = game;

        atlas = new TextureAtlas("megamanbase.atlas");

        //this.texture = new Texture("badlogic.jpg");
        gameCamera = new OrthographicCamera();

        // make the gamewindow is stretch able with StretchViewport or
        // make the gamewindow is scaleable with FitViewport
        viewport = new FitViewport(MegamanGame.V_WIDTH / MegamanGame.PPM,
                MegamanGame.V_HEIGHT / MegamanGame.PPM, gameCamera);

        // load map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Stage.tmx");

        //tells the renderer how many pixels map to a single world unit
        float unitScale = 1 / MegamanGame.PPM;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        gameCamera.position.set(viewport.getWorldWidth() / 2,
                viewport.getWorldHeight() / 2, 0);

        // create HUD Scores
        hud = new Hud(game.batch);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //debugRenderer.SHAPE_STATIC.set(1,0,0,1);

        player = new Megaman(this);
        b2WorldCreator = new B2WorldCreator(this);
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getLinearVelocity().x <= Megaman.MAX_VELOCITY) {
            player.moveRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getLinearVelocity().x >= -Megaman.MAX_VELOCITY) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) && player.getLinearVelocity().y == 0) {
            player.jump();
        }
    }

    @Override
    public void render(float delta) {
        //separate update logic from render
        update(delta);

        // clear screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the map
        renderer.render();

        // render Bo2DDebugLines
        debugRenderer.render(world, gameCamera.combined);
        // tell game batch to recognize where the camera is in the game world
        game.batch.setProjectionMatrix(gameCamera.combined);

        game.batch.begin();
        // drawplayer etc.
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void update(float delta) {
        handleInput(delta);
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(delta);
        hud.update(delta);
        gameCamera.update();
        renderer.setView(gameCamera);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
    }
}
