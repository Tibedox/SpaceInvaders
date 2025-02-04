package ru.samsung.spaceinvaders;

import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 30;

    public Ship(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
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

    public void touch(float tx, float ty) {
        vx = (tx - x)/50;
        vy = (ty - y)/50;
    }

    public void stop() {
        vx = 0;
        vy = 0;
    }
}
