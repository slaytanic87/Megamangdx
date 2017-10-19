package org.megamangdx.game;

import lombok.Getter;
import org.megamangdx.game.screens.AScreenState;
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

    public AScreenState getScreenByState(GameState state) {
        switch (state) {
            case MENUSCREEN:
                throw new NotImplementedException();
            case GAMESCREEN:
                throw new NotImplementedException();
            case SPLASHSCREEN:
                throw new NotImplementedException();
            default:
                throw new RuntimeException("Unknown state");
        }
    }


    public void update(float delta) {
        if (screenStack.size() != 0) {
            screenStack.peek().update(delta);
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
        screenStack.pop().dispose();
        screenStack.push(getScreenByState(state));
    }

}
