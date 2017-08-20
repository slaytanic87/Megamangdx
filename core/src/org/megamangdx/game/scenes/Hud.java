package org.megamangdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.megamangdx.game.MegamanGame;

/**
 * Created by Lam on 12.08.17.
 */
public class Hud {

    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreMegamanLabel;
    Label timeLabel;
    Label levelLabel;
    Label megamanLabel;
    Label opponentLabel;

    public Hud(SpriteBatch batch) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(MegamanGame.V_WIDTH,
                MegamanGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        // table is size of the stage
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreMegamanLabel = new Label(String.format("%06d", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label(String.format("TIME", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label(String.format("1-1", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        megamanLabel = new Label(String.format("MEGAMAN", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        opponentLabel = new Label(String.format("OPPONENT", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // first row
        table.add(megamanLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(opponentLabel).expandX().padTop(10);
        table.row();
        // second row
        table.add(scoreMegamanLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(levelLabel).expandX();
        stage.addActor(table);
    }
}
