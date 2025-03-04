package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 30;

    public Ship(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
        type = 4;
    }

    @Override
    public void move() {
        super.move();
        changePhase();
        outOfScreen();
    }

    private void outOfScreen(){
        if(x < width/2){
            vx = 0;
            x = width/2;
        }
        if(x > SCR_WIDTH - width/2){
            vx = 0;
            x = SCR_WIDTH - width/2;
        }
        if(y < height/2){
            vy = 0;
            y = height/2;
        }
        if(y > SCR_HEIGHT - height/2){
            vy = 0;
            y = SCR_HEIGHT - height/2;
        }
    }

    private void changePhase(){
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public void touchScreen(float tx, float ty) {
        vx = (tx - x)/30;
        vy = (ty - y)/30;
    }

    public void touchScreen(Vector3 t) {
        vx = (t.x - x)/30;
        vy = (t.y - y)/30;
    }

    public void touchJoystick(Vector3 t, Joystick j) {
        vx = (t.x - j.x)/10;
        vy = (t.y - j.y)/10;
    }

    public void stop() {
        vx = 0;
        vy = 0;
    }

    public void dead(){
        x = -10000;
    }
}
