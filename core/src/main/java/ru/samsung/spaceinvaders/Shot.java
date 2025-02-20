package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

public class Shot extends SpaceObject{
    public Shot(float x, float y){
        super(x, y);
        width = 50;
        height = 150;
        vy = 15;
    }

    public boolean outOfScreen(){
        return x < -width/2 || x > SCR_WIDTH + width/2 || y < -height/2 || y > SCR_HEIGHT + height/2;
    }
}
