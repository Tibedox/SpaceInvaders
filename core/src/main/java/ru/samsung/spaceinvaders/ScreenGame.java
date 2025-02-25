package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
    TextureRegion[][] imgFragment = new TextureRegion[5][25];
    TextureRegion imgShot;

    Sound sndExplosion;
    Sound sndBlaster;

    SpaceButton btnBack;

    Space[] space = new Space[2];
    Ship ship;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    private int numFragments = 150;
    private long timeLastSpawnEnemy, timeIntervalSpawnEnemy = 2000;
    private long timeLastShoot, timeShootInterval = 1000;

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
        for(int i = 0; i < imgEnemy.length; i++) {
            for (int j = 0; j < imgEnemy[i].length; j++) {
                imgEnemy[i][j] = new TextureRegion(imgShipsAtlas, (j < 7 ? j : 12 - j) * 400, (i+1)*400, 400, 400);
            }
        }
        int k = (int) Math.sqrt(imgFragment[0].length);
        int size = 400/k;
        for (int i = 0; i < imgFragment.length; i++) {
            for (int j = 0; j < imgFragment[i].length; j++) {
                if(i<imgEnemy.length) {
                    imgFragment[i][j] = new TextureRegion(imgEnemy[i][0], j % k * size, j / k * size, size, size);
                }
                else {
                    imgFragment[i][j] = new TextureRegion(imgShip[0], j % k * size, j / k * size, size, size);
                }
            }
        }
        imgShot = new TextureRegion(imgShotsAtlas, 0, 0, 100, 350);

        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));

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

        for (int i = fragments.size()-1; i>=0; i--) {
            fragments.get(i).move();
            if(fragments.get(i).outOfScreen()) fragments.remove(i);
        }

        for (int i = shots.size()-1; i>=0; i--) {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) shots.remove(i);
        }

        for (int i = shots.size()-1; i>=0; i--) {
            for (int j = enemies.size()-1; j>=0; j--) {
                if(shots.get(i).overlap(enemies.get(j))){
                    shots.remove(i);
                    spawnFragments(enemies.get(j).x, enemies.get(j).y, enemies.get(j).type);
                    enemies.remove(j);
                    if(isSound) sndExplosion.play();
                    break;
                }
            }
        }

        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBG, s.x, s.y, s.width, s.height);
        if(controls == JOYSTICK){
            batch.draw(imgJoystick, main.joystick.scrX(), main.joystick.scrY(), main.joystick.width, main.joystick.height);
        }
        for (Fragment f: fragments){
            batch.draw(imgFragment[f.type][f.number], f.scrX(), f.scrY(), f.width/2, f.height/2, f.width, f.height, 1, 1, f.rotation);
        }
        for (Enemy e: enemies){
            batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);
        }
        for (Shot s: shots){
            batch.draw(imgShot, s.scrX(), s.scrY(), s.width, s.height);
        }
        /*for (int i = 0; i < imgFragment[4].length; i++) {
            batch.draw(imgFragment[4][i], 100, i*SCR_HEIGHT/25, SCR_HEIGHT/25, SCR_HEIGHT/25);
        }*/
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
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
        imgShotsAtlas.dispose();
        sndBlaster.dispose();
        sndExplosion.dispose();
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
            if(isSound) sndBlaster.play();
            timeLastShoot = TimeUtils.millis();
        }
    }

    private void spawnFragments(float x, float y, int type){
        for (int i = 0; i < numFragments; i++) {
            fragments.add(new Fragment(x, y, type, MathUtils.random(0, imgFragment[0].length-1)));
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
                if(main.joystick.isTouchInside(touch)){
                    ship.touchJoystick(touch, main.joystick);
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
                if(main.joystick.isTouchInside(touch)){
                    ship.touchJoystick(touch, main.joystick);
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
