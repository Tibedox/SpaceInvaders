package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    public int type;
    public Fragment(float x, float y) {
        super(x, y);
        width = height = MathUtils.random(30, 50);
        vx = MathUtils.random(-10f, 10f);
        vy = MathUtils.random(-10f, 10f);
        type = MathUtils.random(0, 24);
    }

    public boolean outOfScreen(){
        return x < -width/2 || x > SCR_WIDTH + width/2 || y < -height/2 || y > SCR_HEIGHT + height/2;
    }
}
