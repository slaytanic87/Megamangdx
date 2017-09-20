package org.megamangdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import lombok.Getter;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.scenes.Hud;
import org.megamangdx.game.sprites.Megaman;
import org.megamangdx.game.sprites.Protoman;
import org.megamangdx.game.utils.B2WorldCreator;

/**
 * @author Lam on 12.08.17.
 */
public class PlayScreen implements Screen {

    private MegamanGame game;
    private Texture texture;
    private OrthographicCamera gameCamera;

    // for debugging purpose
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private Viewport viewport;
    private Hud hud;
    @Getter
    private TextureAtlas atlas;
    @Getter
    private World world;

    private Megaman player;
    private Protoman protoman;

    // Tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private B2WorldCreator b2WorldCreator;

    private Music music;

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
        loadNewMap("Stage.tmx");

        player = new Megaman(this);
        protoman = new Protoman(this);
        // create HUD Scores
        hud = new Hud(game.batch);

        music = MegamanGame.assetManager.get(MegamanGame.MEGAMAN_WILY_STAGE_1_2, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
    }

    public void playMusic() {
        music.play();
    }

    public void stopMusic() {
        music.stop();
    }

    public void loadNewMap(String fileName) {
        map = mapLoader.load(fileName);
        world = new World(new Vector2(0, -10), true);
        float unitScale = 1 / MegamanGame.PPM;

        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        gameCamera.position.set(viewport.getWorldWidth() / 2,
                viewport.getWorldHeight() / 2, 0);
        b2WorldCreator = new B2WorldCreator(world, map);
    }


    @Override
    public void show() {
        playMusic();
    }


    private void handleInput() {
        if (player.isReady()) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getLinearVelocity().x <= Megaman.MAX_VELOCITY) {
                player.moveRight();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getLinearVelocity().x >= -Megaman.MAX_VELOCITY) {
                player.moveLeft();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) && player.getLinearVelocity().y == 0) {
                player.jump();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                player.shoot();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player.die();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            player.hit();
        }
    }

    @Override
    public void render(float delta) {
        //separate update logic from render
        this.update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the map
        renderer.render();

        // render Bo2DDebugLines (game model)
        debugRenderer.render(world, gameCamera.combined);
        // tell game batch to recognize where the camera is in the game world
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        // drawplayer
        player.draw(game.batch);
        protoman.draw(game.batch);

        // TODO draw enemies etc.
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void update(float delta) {
        handleInput();
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(delta);
        protoman.update(delta);

        hud.update(delta);

//        if (player.getCurrentState() != ObjectState.DEAD) {
//            gameCamera.position.x = player.b2body.getPosition().x;
//        }

        gameCamera.update();
        renderer.setView(gameCamera);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        playMusic();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        music.stop();
        map.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
    }
}
