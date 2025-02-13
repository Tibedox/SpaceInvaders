package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    Main main;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font90yellow, font90gray;

    Texture imgBG;

    SpaceButton btnControls;
    SpaceButton btnScreen;
    SpaceButton btnJoystick;
    SpaceButton btnAccelerometer;
    SpaceButton btnBack;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font90yellow = main.font90yellow;
        font90gray = main.font90gray;

        imgBG = new Texture("bg2.jpg");

        btnControls = new SpaceButton(font90yellow, "Controls", 100, 1200);
        btnScreen = new SpaceButton(font90yellow, "Screen", 200, 1100);
        btnJoystick = new SpaceButton(font90gray, "Joystick Right", 200, 1000);
        btnAccelerometer = new SpaceButton(font90gray, "Accelerometer", 200, 900);
        btnBack = new SpaceButton(font90yellow, "Back", 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnScreen.hit(touch)){
                controls = SCREEN;
                selectControls();
            }
            if(btnJoystick.hit(touch)){
                if(controls == JOYSTICK){
                    if(isJoystickRight) {
                        isJoystickRight = false;
                        btnJoystick.setText("Joystick Left");
                        joystickX = JOYSTICK_WIDTH/2;
                    } else {
                        isJoystickRight = true;
                        btnJoystick.setText("Joystick Right");
                        joystickX = SCR_WIDTH-JOYSTICK_WIDTH/2;
                    }
                }
                controls = JOYSTICK;
                selectControls();
            }
            if(btnAccelerometer.hit(touch)){
                if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                    controls = ACCELEROMETER;
                    selectControls();
                }
            }

            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font90yellow.draw(batch, "Settings", 0, 1500, SCR_WIDTH, Align.center, true);
        btnControls.font.draw(batch, btnControls.text, btnControls.x, btnControls.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgBG.dispose();
    }

    void selectControls(){
        btnScreen.setFont(font90gray);
        btnJoystick.setFont(font90gray);
        btnAccelerometer.setFont(font90gray);
        switch (controls){
            case SCREEN: btnScreen.setFont(font90yellow); break;
            case JOYSTICK: btnJoystick.setFont(font90yellow); break;
            case ACCELEROMETER: btnAccelerometer.setFont(font90yellow);
        }
    }
}
