package org.megamangdx.game;

import lombok.Getter;
import org.megamangdx.game.application.MegamanGame;
import org.megamangdx.game.screens.AScreenState;
import org.megamangdx.game.screens.MenuScreen;
import org.megamangdx.game.screens.PlayScreen;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Stack;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
public class ScreenStateManager {

    @Getter
    private MegamanGame megamanGame;

    public enum GameState {
        SPLASHSCREEN,
        MENUSCREEN,
        GAMESCREEN
    }

    private Stack<AScreenState> screenStack;

    public ScreenStateManager(MegamanGame game) {
        this.megamanGame = game;
        this.screenStack = new Stack<AScreenState>();
    }

    private AScreenState getScreenByState(GameState state) {
        AScreenState screenState;
        switch (state) {
            case MENUSCREEN:
                screenState = new MenuScreen(this);
                megamanGame.setScreen(screenState);
                return screenState;
            case GAMESCREEN:
                screenState = new PlayScreen(this);
                megamanGame.setScreen(screenState);
                return  screenState;
            case SPLASHSCREEN:
                throw new NotImplementedException();
            default:
                throw new RuntimeException("Unknown state");
        }
    }

    public void render(float delta) {
        if (screenStack.size() != 0) {
            screenStack.peek().render(delta);
        }
    }

    public void dispose() {
        for (AScreenState aScreen: screenStack) {
            aScreen.dispose();
        }
        screenStack.clear();
    }

    public void setState(GameState state) {
        if (!screenStack.isEmpty()) {
            screenStack.pop().dispose();
        }
        screenStack.push(getScreenByState(state));
    }

}
