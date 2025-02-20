package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    public int type;
    public int number;
    public Fragment(float x, float y, int type, int number) {
        super(x, y);
        this.type = type;
        this.number = number;
        width = height = MathUtils.random(30, 50);
        vx = MathUtils.random(-10f, 10f);
        vy = MathUtils.random(-10f, 10f);

    }

    public boolean outOfScreen(){
        return x < -width/2 || x > SCR_WIDTH + width/2 || y < -height/2 || y > SCR_HEIGHT + height/2;
    }
}
