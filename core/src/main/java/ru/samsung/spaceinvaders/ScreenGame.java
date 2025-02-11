package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class ScreenGame implements Screen {
    Main main;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    Texture imgJoystick;
    Texture imgBG;
    Texture imgShipsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];

    SpaceButton btnBack;

    Space[] space = new Space[2];
    Ship ship;

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font90yellow;

        imgJoystick = new Texture("joystick.png");
        imgBG = new Texture("bg0.jpg");
        imgShipsAtlas = new Texture("ships_atlas.png");
        for (int i = 0; i < 12; i++) {
            imgShip[i] = new TextureRegion(imgShipsAtlas, (i<7?i:12-i)*400, 0, 400, 400);
        }

        btnBack = new SpaceButton(font, "x", 850, 1600);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2, 200);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new SpaceInputProcessor());
    }

    @Override
    public void render(float delta) {
        //String z="ACC";
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }
        if(controls == ACCELEROMETER){
            //z="x:"+Gdx.input.getAccelerometerX()+"\ny:"+Gdx.input.getAccelerometerY()+"\nz:"+Gdx.input.getAccelerometerZ();
            ship.vx = -Gdx.input.getAccelerometerX()*10;
            ship.vy = -Gdx.input.getAccelerometerY()*10;
        }
        if(controls == GYROSCOPE){
            ship.vx = -Gdx.input.getGyroscopeX()*10;
            ship.vy = -Gdx.input.getGyroscopeY()*10;
        }

        // события
        for(Space s: space) s.move();
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBG, s.x, s.y, s.width, s.height);
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
        if(controls == JOYSTICK){
            batch.draw(imgJoystick, joystickX-JOYSTICK_WIDTH/2, joystickY-JOYSTICK_HEIGHT/2, JOYSTICK_WIDTH, JOYSTICK_HEIGHT);
        }
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
        imgShipsAtlas.dispose();
    }

    class SpaceInputProcessor implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touch(touch);
            }
            if(controls == JOYSTICK) {
                if(Math.pow(touch.x-joystickX, 2) + Math.pow(touch.y-joystickY, 2) <= Math.pow(JOYSTICK_WIDTH/2, 2)){
                    ship.vx = (touch.x-joystickX)/10;
                    ship.vy = (touch.y-joystickY)/10;
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            ship.stop();
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touch(touch);
            }
            if(controls == JOYSTICK) {
                if(Math.pow(touch.x-joystickX, 2) + Math.pow(touch.y-joystickY, 2) <= Math.pow(JOYSTICK_WIDTH/2, 2)){
                    ship.vx = (touch.x-joystickX)/10;
                    ship.vy = (touch.y-joystickY)/10;
                }
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
