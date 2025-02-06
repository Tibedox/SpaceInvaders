package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.SCR_HEIGHT;
import static ru.samsung.spaceinvaders.Main.SCR_WIDTH;
import static ru.samsung.spaceinvaders.Main.isAccelerometerOn;
import static ru.samsung.spaceinvaders.Main.isGyroscopeOn;

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
    public BitmapFont font;

    Texture imgBG;

    SpaceButton btnBack;
    SpaceButton btnAccelerometer;
    SpaceButton btnGyroscope;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;

        imgBG = new Texture("bg2.jpg");

        btnAccelerometer = new SpaceButton(font, isAccelerometerOn?"Accelerometer ON":"Accelerometer OFF", 1100);
        btnGyroscope = new SpaceButton(font, isGyroscopeOn?"Gyroscope ON":"Gyroscope OFF", 1000);
        btnBack = new SpaceButton(font, "Back", 300);
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

            if(btnAccelerometer.hit(touch)){
                if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                    isAccelerometerOn = !isAccelerometerOn;
                    if(isAccelerometerOn) {
                        isGyroscopeOn = false;
                        btnGyroscope.setText("Gyroscope OFF");
                    }
                    btnAccelerometer.setText(isAccelerometerOn ? "Accelerometer ON" : "Accelerometer OFF");
                }
            }
            if(btnGyroscope.hit(touch)){
                if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)) {
                    isGyroscopeOn = !isGyroscopeOn;
                    if(isGyroscopeOn) {
                        isAccelerometerOn = false;
                        btnAccelerometer.setText("Accelerometer OFF");
                    }
                    btnGyroscope.setText(isGyroscopeOn ? "Gyroscope ON" : "Gyroscope OFF");
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
        font.draw(batch, "Settings", 0, 1400, SCR_WIDTH, Align.center, true);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnGyroscope.font.draw(batch, btnGyroscope.text, btnGyroscope.x, btnGyroscope.y);
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
}
