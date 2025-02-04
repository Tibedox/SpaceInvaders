package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

public class Space extends SpaceObject{
    public Space(float x, float y) {
        super(x, y);
        width = SCR_WIDTH;
        height = SCR_HEIGHT;
        vy = -2;
    }

    @Override
    public void move() {
        super.move();
        if(y<-SCR_HEIGHT) y = SCR_HEIGHT;
    }
}
