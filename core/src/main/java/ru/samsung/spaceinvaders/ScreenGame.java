package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    Main main;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font90;
    public BitmapFont font50;

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
    private boolean gameOver;
    public Player[] players = new Player[10];

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font90 = main.font90yellow;
        font50 = main.font50yellow;

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

        btnBack = new SpaceButton(font50, "X", 850, 1590);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }
        loadTableOfRecords();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new SpaceInputProcessor());
        gameStart();
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

        if(!gameOver) {
            ship.move();
            spawnShots();
        }

        spawnEnemy();
        for (int i = enemies.size()-1; i >= 0; i--) {
            enemies.get(i).move();
            if(!gameOver && enemies.get(i).outOfScreen()){
                gameOver();
                break;
            }
            if(enemies.get(i).overlap(ship)){
                spawnFragments(enemies.get(i));
                enemies.remove(i);
                gameOver();
            }
        }
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
                    if(isSound) sndExplosion.play();
                    if(--enemies.get(j).health == 0) {
                        spawnFragments(enemies.get(j));
                        main.player.kills++;
                        main.player.score += enemies.get(j).price;
                        enemies.remove(j);
                    }
                    break;
                }
            }
        }

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
        font50.draw(batch, "score:"+main.player.score, 10, 1590);
        if(gameOver) {
            font90.draw(batch, "GAME OVER", 0, 1200, SCR_WIDTH, Align.center, true);
            font50.draw(batch, "score", 400, 1080, 200, Align.right, false);
            font50.draw(batch, "kills", 550, 1080, 200, Align.right, false);
            for (int i = 0; i < players.length; i++) {
                font50.draw(batch, players[i].name, 150, 1000 - 70*i);
                font50.draw(batch, ""+players[i].score, 400, 1000 - 70*i, 200, Align.right, false);
                font50.draw(batch, ""+players[i].kills, 550, 1000 - 70*i, 200, Align.right, false);
            }
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

    private void spawnFragments(SpaceObject o){
        for (int i = 0; i < numFragments; i++) {
            fragments.add(new Fragment(o.x, o.y, o.type, MathUtils.random(0, imgFragment[0].length-1)));
        }
    }

    private void gameStart(){
        ship = new Ship(SCR_WIDTH/2, 200);
        gameOver = false;
        main.player.score = 0;
        main.player.kills = 0;
        enemies.clear();
        fragments.clear();
        shots.clear();
    }

    private void gameOver(){
        if(isSound) sndExplosion.play();
        spawnFragments(ship);
        ship.dead();
        gameOver = true;
        if(main.player.score >= players[players.length-1].score) {
            players[players.length - 1].clone(main.player);
            sortTableOfRecords();
            saveTableOfRecords();
        }
    }

    private void sortTableOfRecords(){
        for (int i = 0; i < players.length - 1; i++) {
            for (int j = 0; j < players.length - i - 1; j++) {
                if (players[j].score < players[j + 1].score) {
                    Player temp = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = temp;
                }
            }
        }
    }

    public void saveTableOfRecords(){
        Preferences prefs = Gdx.app.getPreferences("SpaceInvadersLeaderBoard");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("score"+i, players[i].score);
            prefs.putInteger("kills"+i, players[i].kills);
        }
        prefs.flush();
    }

    public void loadTableOfRecords(){
        Preferences prefs = Gdx.app.getPreferences("SpaceInvadersLeaderBoard");
        for (int i = 0; i < players.length; i++) {
            players[i].name = prefs.getString("name"+i, "Noname");
            players[i].score = prefs.getInteger("score"+i, 0);
            players[i].kills = prefs.getInteger("kills"+i, 0);
        }
    }

    public void clearTableOfRecords(){
        for (Player p : players) {
            p.name = "Noname";
            p.score = 0;
            p.kills = 0;
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
