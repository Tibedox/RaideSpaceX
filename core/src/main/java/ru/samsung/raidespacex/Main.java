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

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    public ScreenMenu screenMenu;
    public ScreenGame screenGame;
    public ScreenSettings screenSettings;
    public ScreenLeaderBoard screenLeaderBoard;
    public ScreenAbout screenAbout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH,SCR_HEIGHT);
        touch = new Vector3();
        font = new BitmapFont(Gdx.files.internal("dscrystal100.fnt"));

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
        font.dispose();
    }
}
