package org.megamangdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;

/**
 * @author Lam, Le (msg systems ag) 2017
 */
@Getter
public enum EnergyUnitSchema {

    // RRGGBBAA
    NORMAL("fce4a0ff", "ffffffff"),
    BLUE_ORANGE("0058f8ff", "ffa044ff"),
    BLUE_WHITE("0078f8ff", "f8f8f8ff"),
    BROWN_WHITE("881400ff", "ffffffff"),
    CYAN_WHITE("00e8d8ff", "f8f8f8ff"),
    GREEN_WHITE("00a800ff", "f8f8f8ff"),
    MAGENTA_WHITE("d800ccff", "ffffffff"),
    PINK_WHITE("f87858ff", "f8f8f8ff"),
    RED_YELLOW("ff3800ff", "ffbc00ff");

    private String innerColorCode;
    private String outerColorColor;

    private Color[] colors = new Color[2];

    EnergyUnitSchema(String innerColor, String outerColor) {
        this.innerColorCode = innerColor;
        this.outerColorColor = outerColor;
        colors[0] = new Color(Color.valueOf(innerColor));
        colors[1] = new Color(Color.valueOf(outerColor));
    }

    public Color[] getColorSchema() {
        return this.colors;
    }

}
