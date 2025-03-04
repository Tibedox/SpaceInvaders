package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.SCR_HEIGHT;
import static ru.samsung.spaceinvaders.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int health;
    public int price;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 30;
    public int nFragments;

    public Enemy() {
        type = MathUtils.random(0, 3);
        settings(type);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
    }

    @Override
    public void move() {
        super.move();
        changePhase();
    }

    private void changePhase(){
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public boolean outOfScreen(){
        return y < -height/2;
    }

    private void settings(int type){
        switch (type){
            case 0:
                health = 2;
                price = 2;
                width = height = 200;
                vy = MathUtils.random(-6f, -4f);
                nFragments = 200;
                break;
            case 1:
                health = 4;
                price = 3;
                width = height = 300;
                vy = MathUtils.random(-4f, -3f);
                nFragments = 500;
                break;
            case 2:
                health = 3;
                price = 2;
                width = height = 250;
                vy = MathUtils.random(-5f, -4f);
                nFragments = 300;
                break;
            case 3:
                health = 1;
                price = 1;
                width = height = 150;
                vy = MathUtils.random(-8f, -6f);
                nFragments = 150;
                break;
        }
    }
}
