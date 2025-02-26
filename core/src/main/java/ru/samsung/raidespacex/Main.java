package ru.samsung.raidespacex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Game {
    public static final float SCR_WIDTH = 900;
    public static final float SCR_HEIGHT = 1600;
    public static final int SCREEN = 0, JOYSTICK = 1, ACCELEROMETER = 2;
    public static final boolean LEFT = false, RIGHT = true;
    public static int controls = SCREEN;
    public static boolean isSoundOn = true;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont fontLightGreen;
    public BitmapFont fontDarkGreen;
    public BitmapFont font50white;

    Joystick joystick;
    public ScreenMenu screenMenu;
    public ScreenGame screenGame;
    public ScreenSettings screenSettings;
    public ScreenLeaderBoard screenLeaderBoard;
    public ScreenAbout screenAbout;
    Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH,SCR_HEIGHT);
        touch = new Vector3();
        fontLightGreen = new BitmapFont(Gdx.files.internal("dscrystal100lightgreen.fnt"));
        fontDarkGreen = new BitmapFont(Gdx.files.internal("dscrystal100darkgreen.fnt"));
        font50white = new BitmapFont(Gdx.files.internal("dscrystal50white.fnt"));

        joystick = new Joystick(360, RIGHT);
        player = new Player();
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
        fontLightGreen.dispose();
        fontDarkGreen.dispose();
        font50white.dispose();
    }
}
