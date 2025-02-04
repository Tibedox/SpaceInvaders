package ru.samsung.spaceinvaders;

import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    public int phase, nPhases;
    private long timeLastPhase, timePhaseInterval;

    public Ship(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
        nPhases = 12;
        timePhaseInterval = 30;
    }

    @Override
    public void move() {
        super.move();
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }
}
