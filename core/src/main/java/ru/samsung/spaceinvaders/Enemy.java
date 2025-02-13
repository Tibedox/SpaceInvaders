package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.SCR_HEIGHT;
import static ru.samsung.spaceinvaders.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Enemy extends SpaceObject{
    public int type;

    public Enemy() {
        width = height = 200;
        type = MathUtils.random(0, 3);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = MathUtils.random(-6f, -3f);
    }
}
