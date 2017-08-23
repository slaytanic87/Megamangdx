package org.megamangdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.megamangdx.game.MegamanGame;

/**
 * @author Lam on 12.08.17.
 */
public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private boolean timeUp = false; // true when the world timer reaches 0

    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private Integer opponentScore;

    Label countdownLabel;
    Label scoreMegamanLabel;
    Label timeLabel;
    Label opponentLabel;
    Label megamanLabel;
    Label opponentScoreLabel;

    public Hud(SpriteBatch batch) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        opponentScore = 0;

        viewport = new FitViewport(MegamanGame.V_WIDTH,
                MegamanGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        // table fill the size of the stage
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreMegamanLabel = new Label(String.format("%06d", score),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        opponentScoreLabel = new Label(String.format("%06d", opponentScore),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        opponentLabel = new Label("PROTOMAN",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        megamanLabel = new Label("MEGAMAN",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        // first row
        table.add(megamanLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(opponentLabel).expandX().padTop(10);
        table.row();
        // second row
        table.add(scoreMegamanLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(opponentScoreLabel).expandX();
        stage.addActor(table);
    }

    public void update(float delta) {
        timeCount += delta;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void addPlayerScore(int value) {
        score += value;
        scoreMegamanLabel.setText(String.format("%06d", score));
    }

    public void addOpponentScore(int value) {
        opponentScore += value;
        opponentScoreLabel.setText(String.format("%06d", opponentScore));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
