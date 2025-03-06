package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    private final Main main;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font90yellow, font90gray;
    public BitmapFont font50yellow;
    private InputKeyboard keyboard;

    private final Texture imgBG;

    SpaceButton btnPlayerName;
    SpaceButton btnControls;
    SpaceButton btnScreen;
    SpaceButton btnJoystick;
    SpaceButton btnAccelerometer;
    SpaceButton btnSound;
    SpaceButton btnBack;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font90yellow = main.font90yellow;
        font90gray = main.font90gray;
        font50yellow = main.font50yellow;
        keyboard = new InputKeyboard(font50yellow, SCR_WIDTH, SCR_HEIGHT/2, 12);

        imgBG = new Texture("bg2.jpg");

        btnPlayerName = new SpaceButton(font90yellow, "Name: "+main.player.name, 100, 1250);
        btnControls = new SpaceButton(font90yellow, "Controls", 100, 1100);
        btnScreen = new SpaceButton(getFont(SCREEN), "Screen", 200, 1000);
        btnJoystick = new SpaceButton(getFont(JOYSTICK), joystickBtnText(), 200, 900);
        btnAccelerometer = new SpaceButton(getFont(ACCELEROMETER), "Accelerometer", 200, 800);
        btnSound = new SpaceButton(font90yellow, soundBtnText(), 100, 650);
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

            if(keyboard.isKeyboardShow) {
                if (keyboard.touch(touch)) {
                    main.player.name = keyboard.getText();
                    btnPlayerName.setText("Name: "+main.player.name);
                }
            } else {
                if (btnPlayerName.hit(touch)) {
                    keyboard.start();
                }
                if (btnScreen.hit(touch)) {
                    controls = SCREEN;
                    selectControls();
                }
                if (btnJoystick.hit(touch)) {
                    if (controls == JOYSTICK) {
                        main.joystick.setSide(!main.joystick.side);
                        btnJoystick.setText(joystickBtnText());
                    } else {
                        controls = JOYSTICK;
                    }
                    selectControls();
                }
                if (btnAccelerometer.hit(touch)) {
                    if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                        controls = ACCELEROMETER;
                        selectControls();
                    }
                }
                if (btnSound.hit(touch)) {
                    isSound = !isSound;
                    btnSound.setText(soundBtnText());
                }
                if (btnBack.hit(touch)) {
                    main.setScreen(main.screenMenu);
                }
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font90yellow.draw(batch, "Settings", 0, 1500, SCR_WIDTH, Align.center, true);
        btnPlayerName.font.draw(batch, btnPlayerName.text, btnPlayerName.x, btnPlayerName.y);
        btnControls.font.draw(batch, btnControls.text, btnControls.x, btnControls.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnSound.font.draw(batch, btnSound.text, btnSound.x, btnSound.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        keyboard.draw(batch);
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
        saveSettings();
    }

    @Override
    public void dispose() {
        imgBG.dispose();
        keyboard.dispose();
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

    private String joystickBtnText(){
        return main.joystick.side ? "Joystick Right" : "Joystick Left";
    }

    private String soundBtnText() {
        return isSound ? "Sound On" : "Sound Off";
    }

    public BitmapFont getFont(int type) {
        return (controls == type)? font90yellow : font90gray;
    }

    private void saveSettings() {
        Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSettings");
        prefs.putInteger("controls", controls);
        prefs.putBoolean("joystick", main.joystick.side);
        prefs.putBoolean("sound", isSound);
        prefs.flush();
    }
}
