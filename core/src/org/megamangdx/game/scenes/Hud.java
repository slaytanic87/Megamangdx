package org.megamangdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.utils.GraphicUtils;

/**
 * @author Lam on 12.08.17.
 */
public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private boolean timeUp = false; // true when the world timer reaches 0

    private Integer worldTimer;
    private float stateTimer;
    private float timeCount;

    // unit size 6x2
    private static final int MAX_ENERGY_UNIT = 27;
    private int currentEnergyLevel = 27;

    private int currentOpponentEnergyLevel = 27;

    private static final float UPDATE_ENERGYUNIT_INTERVALL = 0.15f;

    Label countdownLabel;
    Label timeLabel;
    Label opponentLabel;
    Label playerLabel;

    private SpriteBatch batch;

    @Getter
    private boolean energybarReady = false;

    private TextureRegion energymeterUnit;

    private Image playerEnergyBar;
    private TextureRegion playerEnergyBarTexture;

    private Image opponentEnergyBar;
    private TextureRegion opponentEnergyBarTexture;

    public Hud(SpriteBatch batch, TextureAtlas atlas) {
        worldTimer = 300;
        timeCount = 0;
        this.batch = batch;
        viewport = new FitViewport(MegamanGame.V_WIDTH,
                MegamanGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        // table fill the size of the stage
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        opponentLabel = new Label("PROTOMAN",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label("MEGAMAN",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        // first row
        table.add(playerLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(opponentLabel).expandX().padTop(10);
        table.row();

        // second row
        playerEnergyBarTexture = new TextureRegion(atlas.findRegion("energymeter_empty"));
        playerEnergyBar = new Image(playerEnergyBarTexture);

        opponentEnergyBarTexture = new TextureRegion(atlas.findRegion("energymeter_empty"));
        opponentEnergyBar = new Image(opponentEnergyBarTexture);

        energymeterUnit = new TextureRegion(atlas.findRegion("energymeter_unit"));

        table.add(playerEnergyBar).expandX().padTop(10);
        table.add(countdownLabel).expandX();
        table.add(opponentEnergyBar).expandX().padTop(10);
        stage.addActor(table);
    }


    private TextureRegion setEnergyUnitAt(final TextureRegion textureRegion,
                                          final TextureRegion unit, int position) {

        TextureRegion newTextureRegion = new TextureRegion(textureRegion);
        Texture texture = new Texture(newTextureRegion.getTexture().getTextureData());
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap targetPixmap = texture.getTextureData().consumePixmap();
        Pixmap unitPixmap = GraphicUtils.copyPixmap(unit);

        targetPixmap.drawPixmap(unitPixmap, newTextureRegion.getRegionX() + 1,
                newTextureRegion.getRegionY() + position * 2);

        newTextureRegion.setTexture(new Texture(targetPixmap));
//        texture.getTextureData().disposePixmap();
//        pixmap.dispose();
        return newTextureRegion;
    }

    public void update(float delta) {
        timeCount += delta;
        stateTimer += delta;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
        initEnergyBars();
    }

    private void initEnergyBars() {
        if (!energybarReady && stateTimer >= UPDATE_ENERGYUNIT_INTERVALL) {
            stateTimer = 0;
            if (currentEnergyLevel > 0) {
                TextureRegion textureRegion = setEnergyUnitAt(playerEnergyBarTexture, energymeterUnit,
                        currentEnergyLevel);
                playerEnergyBarTexture = textureRegion;
                playerEnergyBar.setDrawable(new SpriteDrawable(new Sprite(textureRegion)));
                currentEnergyLevel--;
            }
            if (currentOpponentEnergyLevel > 0) {
                TextureRegion textureRegion = setEnergyUnitAt(opponentEnergyBarTexture, energymeterUnit,
                        currentOpponentEnergyLevel);
                opponentEnergyBarTexture = textureRegion;
                opponentEnergyBar.setDrawable(new SpriteDrawable(new Sprite(textureRegion)));
                currentOpponentEnergyLevel--;
            }
            energybarReady = (currentEnergyLevel == 0 && currentOpponentEnergyLevel == 0);
        }
    }

    public void decreaseEnergy(int percent) {
    }

    public void increaseEnergy(int percent) {
        int unit = Math.round(percent * MAX_ENERGY_UNIT / 100);
        currentEnergyLevel -= unit;
    }

    public void decreaseEnergyOpponent(int percent) {
    }

    public void increaseEnergyOpponent(int percent) {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
