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
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    Main main;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    Texture imgJoystick;
    Texture imgBG;
    Texture imgShipsAtlas;
    Texture imgShotsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];
    TextureRegion[][] imgEnemy = new TextureRegion[4][12];
    TextureRegion imgShot;

    SpaceButton btnBack;

    Space[] space = new Space[2];
    Ship ship;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    private long timeLastSpawnEnemy, timeIntervalSpawnEnemy = 2000;
    private long timeLastShoot, timeShootInterval = 500;

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font90yellow;

        imgJoystick = new Texture("joystick.png");
        imgBG = new Texture("bg0.jpg");
        imgShipsAtlas = new Texture("ships_atlas.png");
        imgShotsAtlas = new Texture("shots.png");
        for (int i = 0; i < imgShip.length; i++) {
            imgShip[i] = new TextureRegion(imgShipsAtlas, (i<7?i:12-i)*400, 0, 400, 400);
        }
        for(int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgShipsAtlas, (i < 7 ? i : 12 - i) * 400, (j+1)*400, 400, 400);
            }
        }
        imgShot = new TextureRegion(imgShotsAtlas, 0, 0, 100, 350);

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
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }
        if(controls == ACCELEROMETER){
            ship.vx = -Gdx.input.getAccelerometerX()*10;
            ship.vy = -Gdx.input.getAccelerometerY()*10;
        }

        // события
        for (Space s: space) s.move();
        spawnEnemy();
        for (Enemy e: enemies) e.move();
        spawnShots();
        for (Shot s: shots) s.move();
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBG, s.x, s.y, s.width, s.height);
        for (Enemy e: enemies){
            batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);
        }
        for (Shot s: shots){
            batch.draw(imgShot, s.scrX(), s.scrY(), s.width, s.height);
        }
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

    private void spawnEnemy(){
        if(TimeUtils.millis()>timeLastSpawnEnemy+timeIntervalSpawnEnemy){
            enemies.add(new Enemy());
            timeLastSpawnEnemy = TimeUtils.millis();
        }
    }

    private void spawnShots(){
        if(TimeUtils.millis()>timeLastShoot+timeShootInterval){
            shots.add(new Shot(ship.x-60, ship.y));
            shots.add(new Shot(ship.x+60, ship.y));
            timeLastShoot = TimeUtils.millis();
        }
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
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(Math.pow(touch.x-joystickX, 2) + Math.pow(touch.y-joystickY, 2) <= Math.pow(JOYSTICK_WIDTH/2, 2)){
                    ship.touchJoystick(touch);
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
                ship.touchScreen(touch);
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
