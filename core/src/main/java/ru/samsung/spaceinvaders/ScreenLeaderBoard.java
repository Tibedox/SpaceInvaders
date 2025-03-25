package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.SCR_HEIGHT;
import static ru.samsung.spaceinvaders.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenLeaderBoard implements Screen {
    Main main;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font90;
    public BitmapFont font50;

    Texture imgBG;

    SpaceButton btnSwitcher;
    SpaceButton btnClear;
    SpaceButton btnBack;
    Player[] players;
    private boolean isGlobal;

    public ScreenLeaderBoard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font90 = main.font90yellow;
        font50 = main.font50yellow;
        players = main.screenGame.players;

        imgBG = new Texture("bg4.jpg");

        btnSwitcher = new SpaceButton(font90, "Local", 1350);
        btnClear = new SpaceButton(font90, "Clear", 300);
        btnBack = new SpaceButton(font90, "Back", 150);
    }

    @Override
    public void show() {
        // Устанавливаем желаемую частоту кадров
        Gdx.graphics.setVSync(false); // Отключаем вертикальную синхронизацию
        Gdx.graphics.setForegroundFPS(10); // Например, ставим 30 FPS
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnSwitcher.hit(touch)){
                isGlobal = !isGlobal;
                if(isGlobal) {
                    //main.screenGame.loadTableFromDB();
                    btnSwitcher.setText("Global");
                } else {
                    btnSwitcher.setText("Local");
                }
            }
            if(btnClear.hit(touch)){
                main.screenGame.clearTableOfRecords();
                main.screenGame.saveTableOfRecords();
            }
            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font90.draw(batch, "Leader Board", 0, 1500, SCR_WIDTH, Align.center, true);
        btnSwitcher.font.draw(batch, btnSwitcher.text, btnSwitcher.x, btnSwitcher.y);
        font50.draw(batch, "score", 400, 1180, 200, Align.right, false);
        font50.draw(batch, "kills", 550, 1180, 200, Align.right, false);
        for (int i = 0; i < players.length; i++) {
            font50.draw(batch, players[i].name, 150, 1100 - 70*i);
            font50.draw(batch, ""+players[i].score, 400, 1100 - 70*i, 200, Align.right, false);
            font50.draw(batch, ""+players[i].kills, 550, 1100 - 70*i, 200, Align.right, false);
        }
        btnClear.font.draw(batch, btnClear.text, btnClear.x, btnClear.y);
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
        // Устанавливаем желаемую частоту кадров
        Gdx.graphics.setForegroundFPS(60); // Например, ставим 30 FPS
        Gdx.graphics.setVSync(true); // Включаем вертикальную синхронизацию
    }

    @Override
    public void dispose() {
        imgBG.dispose();
    }
}
