package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    public int number;
    public float rotation;
    private final float vRotation;

    public Fragment(float x, float y, int type, int number) {
        super(x, y);
        this.type = type;
        this.number = number;
        width = height = MathUtils.random(20f, 40f);
        float a = MathUtils.random(0f, 360f);
        float v = MathUtils.random(1f, 10f);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
        vRotation = MathUtils.random(-5f, 5f);
    }

    @Override
    public void move() {
        super.move();
        rotation += vRotation;
    }

    public boolean outOfScreen(){
        return x < -width/2 || x > SCR_WIDTH + width/2 || y < -height/2 || y > SCR_HEIGHT + height*2;
    }
}
