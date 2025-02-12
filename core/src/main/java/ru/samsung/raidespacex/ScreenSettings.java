package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {

    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont fontLightGreen;
    private BitmapFont fontDarkGreen;

    Texture imgBG;

    SpaceXButton btnControls;
    SpaceXButton btnScreen;
    SpaceXButton btnJoystick;
    SpaceXButton btnAccelerometer;
    SpaceXButton btnBack;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        fontLightGreen = main.fontLightGreen;
        fontDarkGreen = main.fontDarkGreen;

        imgBG = new Texture("bg3.jpg");
        btnControls = new SpaceXButton(fontLightGreen, "Controls", 100, 1200);
        btnScreen = new SpaceXButton(fontLightGreen, "Screen", 200, 1100);
        btnJoystick = new SpaceXButton(fontDarkGreen, "Joystick LEFT", 200, 1000);
        btnAccelerometer = new SpaceXButton(fontDarkGreen, "Accelerometr", 200, 900);
        btnBack = new SpaceXButton(fontLightGreen, "Back", 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnScreen.hit(touch)){
                btnScreen.setFont(fontLightGreen);
                btnJoystick.setFont(fontDarkGreen);
                btnAccelerometer.setFont(fontDarkGreen);
                controls = SCREEN;
            }
            if(btnJoystick.hit(touch)){
                btnScreen.setFont(fontDarkGreen);
                btnJoystick.setFont(fontLightGreen);
                btnAccelerometer.setFont(fontDarkGreen);
                if(controls == JOYSTICK) {
                    if(isJoystickLeft){
                        isJoystickLeft = false;
                        btnJoystick.setText("Joystick RIGHT");
                        joystickX = SCR_WIDTH-JOYSTICK_WIDTH/2;
                    } else {
                        isJoystickLeft = true;
                        btnJoystick.setText("Joystick LEFT");
                        joystickX = JOYSTICK_WIDTH/2;
                    }
                } else {
                    controls = JOYSTICK;
                }
            }
            if(btnAccelerometer.hit(touch)){
                btnScreen.setFont(fontDarkGreen);
                btnJoystick.setFont(fontDarkGreen);
                btnAccelerometer.setFont(fontLightGreen);
                controls = ACCELEROMETER;
            }
            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        fontLightGreen.draw(batch,"Settings", 0, 1500, SCR_WIDTH, Align.center, true);
        btnControls.font.draw(batch, btnControls.text, btnControls.x, btnControls.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
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

    }
}
