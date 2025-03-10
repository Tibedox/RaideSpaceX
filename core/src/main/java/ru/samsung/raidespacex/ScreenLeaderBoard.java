package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.SCR_HEIGHT;
import static ru.samsung.raidespacex.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenLeaderBoard implements Screen {

    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font100lightGreen;
    private BitmapFont font50white;

    Texture imgBG;

    SpaceXButton btnGlobal;
    SpaceXButton btnClear;
    SpaceXButton btnBack;

    public ScreenLeaderBoard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font100lightGreen = main.font100lightGreen;
        font50white = main.font50white;

        imgBG = new Texture("bg4.jpg");

        btnGlobal = new SpaceXButton(font100lightGreen, "Local", 1350);
        btnClear = new SpaceXButton(font100lightGreen, "Clear", 350);
        btnBack = new SpaceXButton(font100lightGreen, "Back", 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnClear.hit(touch)){
                main.screenGame.clearLeaderBoard();
                main.screenGame.saveLeaderBoard();
            }
            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for (int i = 0; i < main.screenGame.players.length; i++) {
            font50white.draw(batch, i+1+" ", 140, 1200-i*80);
            font50white.draw(batch, main.screenGame.players[i].name, 200, 1200-i*80);
            font50white.draw(batch, ""+main.screenGame.players[i].score, 450, 1200-i*80, 150, Align.right, false);
            font50white.draw(batch, ""+main.screenGame.players[i].kills, 600, 1200-i*80, 150, Align.right, false);
        }
        font100lightGreen.draw(batch,"Leader Board", 0, 1500, SCR_WIDTH, Align.center, true);
        btnGlobal.font.draw(batch, btnGlobal.text, btnGlobal.x, btnGlobal.y);
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

    }

    @Override
    public void dispose() {
        imgBG.dispose();
    }

}
