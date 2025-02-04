package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.SCR_HEIGHT;
import static ru.samsung.spaceinvaders.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
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
        font = main.font;

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

        // события
        for(Space s: space) s.move();
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBG, s.x, s.y, s.width, s.height);
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
    }
}
