package org.megamangdx.game.scenes;

import com.badlogic.gdx.audio.Sound;
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
import org.megamangdx.game.application.MegamanGame;
import org.megamangdx.game.utils.GraphicUtils;

/**
 * @author Lam on 12.08.17.
 */
public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private static final int MAXTIME = 300;
    private boolean timeUp = false; // true when the world timer reaches 0

    private Integer worldTimer;
    private float stateTimer;
    private float timeCount;

    // unit size 6x2
    private static final int MAX_ENERGY_UNIT = 27;
    private int currentEnergyLevel;
    private int currentOpponentEnergyLevel;

    private static final float UPDATE_ENERGYUNIT_INTERVALL = 0.12f;

    private Label countdownLabel;
    private Label timeLabel;
    private Label opponentLabel;
    private Label playerLabel;

    private SpriteBatch batch;

    @Getter
    private boolean energybarReady = false;

    private TextureRegion energymeterUnit;
    private TextureRegion energymeterUnitOpponent;

    private Image playerEnergyBar;
    private TextureRegion playerEnergyBarTexture;

    private Image opponentEnergyBar;
    private TextureRegion opponentEnergyBarTexture;

    private TextureAtlas textureAtlas;

    public Hud(SpriteBatch batch, TextureAtlas atlas) {
        worldTimer = MAXTIME;
        timeCount = 0;
        textureAtlas = atlas;
        currentEnergyLevel = MAX_ENERGY_UNIT;
        currentOpponentEnergyLevel = MAX_ENERGY_UNIT;
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
        energymeterUnitOpponent = new TextureRegion(atlas.findRegion("energymeter_unit"));
        setOpponentEnergymeterUnitColor(EnergyUnitSchema.BLUE_WHITE);

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
        texture.getTextureData().disposePixmap();
//        pixmap.dispose(); pixmaps already disposed!
        return newTextureRegion;
    }

    private TextureRegion setEnergyUnitColor(EnergyUnitSchema colorSchema, TextureRegion energymeterUnit) {
        TextureRegion textureRegion = new TextureRegion(energymeterUnit);
        EnergyUnitSchema defaultSchema = EnergyUnitSchema.NORMAL;

        switch (colorSchema) {
            case NORMAL:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case BLUE_WHITE:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case CYAN_WHITE:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case PINK_WHITE:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case RED_YELLOW:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case BLUE_ORANGE:
                return  GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case BROWN_WHITE:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case GREEN_WHITE:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            case MAGENTA_WHITE:
                return GraphicUtils.tintingSpriteColor(defaultSchema.getColors(),
                        colorSchema.getColors(), textureRegion);
            default:
                throw new RuntimeException("Unknown color schema!");
        }
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
            MegamanGame.assetManager.get(MegamanGame.MEGAMAN_ENERGYFILL, Sound.class).play();
            if (currentEnergyLevel > 0) {
                playerEnergyBarTexture = setEnergyUnitAt(playerEnergyBarTexture, energymeterUnit,
                        currentEnergyLevel);
                playerEnergyBar.setDrawable(new SpriteDrawable(new Sprite(playerEnergyBarTexture)));
                currentEnergyLevel--;
            }
            if (currentOpponentEnergyLevel > 0) {
                opponentEnergyBarTexture = setEnergyUnitAt(opponentEnergyBarTexture, energymeterUnitOpponent,
                        currentOpponentEnergyLevel);
                opponentEnergyBar.setDrawable(new SpriteDrawable(new Sprite(opponentEnergyBarTexture)));
                currentOpponentEnergyLevel--;
            }
            energybarReady = (currentEnergyLevel == 0 && currentOpponentEnergyLevel == 0);
        }
    }

    /**
     * Decrease player energy.
     * @param units number of units to be decrease
     */
    public void decreaseEnergy(int units) {
        TextureRegion emptyUnit = GraphicUtils.createEmptyTexture(energymeterUnit.getRegionWidth(),
                energymeterUnit.getRegionHeight());
        for (int i = 0; i < units; i++) {
            if ((currentEnergyLevel + 1) <= MAX_ENERGY_UNIT) {
                currentEnergyLevel += 1;
                playerEnergyBarTexture = setEnergyUnitAt(playerEnergyBarTexture, emptyUnit, currentEnergyLevel);
                playerEnergyBar.setDrawable(new SpriteDrawable(new Sprite(playerEnergyBarTexture)));
            }
        }

    }

    /**
     * Increase player energy.
     * @param units number of units to be increase
     */
    public void increaseEnergy(int units) {
        for (int i = 0; i < units; i++) {
            if ((currentEnergyLevel - 1) >= 0) {
                currentEnergyLevel -= 1;
                playerEnergyBarTexture = setEnergyUnitAt(playerEnergyBarTexture, energymeterUnit, currentEnergyLevel);
                playerEnergyBar.setDrawable(new SpriteDrawable(new Sprite(playerEnergyBarTexture)));
            }
        }
    }

    /**
     * Decrease opponent energy.
     * @param units number of units to be decrease
     */
    public void decreaseOpponentEnergy(int units) {
        TextureRegion emptyUnit = GraphicUtils.createEmptyTexture(energymeterUnit.getRegionWidth(),
                energymeterUnit.getRegionHeight());
        for (int i = 0; i < units; i++) {
            if ((currentOpponentEnergyLevel + 1) <= MAX_ENERGY_UNIT) {
                currentOpponentEnergyLevel++;
                opponentEnergyBarTexture = setEnergyUnitAt(opponentEnergyBarTexture, emptyUnit,
                        currentOpponentEnergyLevel);
                opponentEnergyBar.setDrawable(new SpriteDrawable(new Sprite(opponentEnergyBarTexture)));
            }
        }
    }

    /**
     * Increase opponent energy.
     * @param units number of units to be increase
     */
    public void increaseOpponentEnergy(int units) {
        for (int i = 0; i < units; i++) {
            if ((currentOpponentEnergyLevel - 1) >= 0) {
                currentOpponentEnergyLevel--;
                opponentEnergyBarTexture = setEnergyUnitAt(opponentEnergyBarTexture, energymeterUnit,
                        currentOpponentEnergyLevel);
                opponentEnergyBar.setDrawable(new SpriteDrawable(new Sprite(opponentEnergyBarTexture)));
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void setOpponentName(String name) {
        this.opponentLabel.setText(name);
    }

    public void setPlayerName(String name) {
        this.playerLabel.setText(name);
    }

    public void setPlayerEnergymeterUnitColor(EnergyUnitSchema schema) {
        TextureRegion textureRegion = new TextureRegion(textureAtlas.findRegion("energymeter_unit"));
        this.energymeterUnit = setEnergyUnitColor(schema, textureRegion);
    }

    public void setOpponentEnergymeterUnitColor(EnergyUnitSchema schema) {
        TextureRegion textureRegion = new TextureRegion(textureAtlas.findRegion("energymeter_unit"));
        this.energymeterUnitOpponent = setEnergyUnitColor(schema, textureRegion);
    }
}
