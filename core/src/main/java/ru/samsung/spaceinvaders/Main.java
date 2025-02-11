package ru.samsung.spaceinvaders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Game {
    public static final float SCR_WIDTH = 900;
    public static final float SCR_HEIGHT = 1600;
    public static final int SCREEN = 0, JOYSTICK = 1, ACCELEROMETER = 2, GYROSCOPE = 3;
    public static int controls = SCREEN;
    public static final float JOYSTICK_WIDTH = 360;
    public static final float JOYSTICK_HEIGHT = 360;
    public static float joystickX = SCR_WIDTH-JOYSTICK_WIDTH/2;
    public static float joystickY = JOYSTICK_HEIGHT/2;
    public static boolean isJoystickRight = true;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font90yellow, font90gray;

    public ScreenMenu screenMenu;
    public ScreenGame screenGame;
    public ScreenSettings screenSettings;
    public ScreenLeaderBoard screenLeaderBoard;
    public ScreenAbout screenAbout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        font90yellow = new BitmapFont(Gdx.files.internal("stylo90yellow.fnt"));
        font90gray = new BitmapFont(Gdx.files.internal("stylo90gray.fnt"));

        screenMenu = new ScreenMenu(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenLeaderBoard = new ScreenLeaderBoard(this);
        screenAbout = new ScreenAbout(this);
        setScreen(screenGame);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font90yellow.dispose();
        font90gray.dispose();
    }
}
